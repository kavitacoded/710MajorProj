package com.nt.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.bindings.EligibilityDetailsOutput;
import com.nt.entity.CitizenAppRegistraionEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.EligibilityDetailsEntity;
import com.nt.entity.PlanEntity;
import com.nt.repository.ICitizenAppRegistrationRepository;
import com.nt.repository.ICoTriggerRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IDcChildrenRepository;
import com.nt.repository.IDcEducationRepository;
import com.nt.repository.IDcIncomRepository;
import com.nt.repository.IEligibilityDetermineRepository;
import com.nt.repository.IPlanRepository;

@Service
public class EligibilityDeterminationMgmtServiceImpl implements IEligibilityDeterminationMgmtService {

	@Autowired
	private IEligibilityDetermineRepository eligibilityRepo;
	@Autowired
	private IDcCaseRepository caseRepo;
	@Autowired
	private IPlanRepository planRepo;
	@Autowired
	private IDcIncomRepository incomeRepo;
	@Autowired
	private IDcChildrenRepository childrenRepo;
	@Autowired
	private ICitizenAppRegistrationRepository citizenRepo;
	@Autowired
	private IDcEducationRepository educationRepo;
	@Autowired
	private ICoTriggerRepository triggerRepo;
	
	@Override
	public EligibilityDetailsOutput determineEligibility(int caseNo) {
		Integer planId = null;
		Integer appId = null;
		long citizenSSN=0L;
		// get plan id and appId based on caseNo
		Optional<DcCaseEntity> optCaseEntity = caseRepo.findById(caseNo);
		if (optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity = optCaseEntity.get();
			planId = caseEntity.getPlanId();
			appId = caseEntity.getAppId();
		}
		// get plan Name
		String planName = null;
		Optional<PlanEntity> optPlanEntity = planRepo.findById(planId);
		if (optPlanEntity.isPresent()) {
			PlanEntity planEntity = optPlanEntity.get();
			planName = planEntity.getPlanName();
		}
		//calculate citizen age by getting citizen DOB through appId
		Optional<CitizenAppRegistraionEntity> optCitizenEntity=citizenRepo.findById(appId);
		int  citizenAge=0;
		String citizenName=null;
		if(optCaseEntity.isPresent()) {
			CitizenAppRegistraionEntity citizenEntity=optCitizenEntity.get();
			LocalDate  citizenDOB=citizenEntity.getDob();
			citizenName =citizenEntity.getFullName();
			LocalDate sysDate=LocalDate.now();
			citizenAge=Period.between(citizenDOB, sysDate).getYears();
			citizenSSN=citizenEntity.getSsn();
		}
		//call helper method to plan conditions
		EligibilityDetailsOutput  eligibilityOutput=applyPlanConditions(caseNo, planName,citizenAge);
		
		//set citizen name
		eligibilityOutput.setHolderName(citizenName);
		//save Eligibility entity object
		EligibilityDetailsEntity eligibEntity=new EligibilityDetailsEntity();
		BeanUtils.copyProperties(eligibilityOutput, eligibEntity);
		eligibEntity.setCaseNo(caseNo);
		eligibEntity.setHolderSSN(citizenSSN);
		eligibilityRepo.save(eligibEntity);
		//save CoTriggers object
		CoTriggersEntity triggersEntity=new CoTriggersEntity();
		triggersEntity.setCaseNo(caseNo);
		triggersEntity.setTriggerStatus("Pending");
		triggerRepo.save(triggersEntity);
		return eligibilityOutput;
	}

	private EligibilityDetailsOutput applyPlanConditions(Integer caseNo, String planName,Integer citizenAge) {
		EligibilityDetailsOutput eligibleOutput = new EligibilityDetailsOutput();
		eligibleOutput.setPlanName(planName);

		// get income details of the citizen
		DcIncomeEntity IncomeEntity = incomeRepo.findByCaseNo(caseNo);
		// DcIncomeEntity incomeEntity = optIncomeEntity.get();
		double empIncome = IncomeEntity.getPropertyIncome();
		double propertyIncome = IncomeEntity.getEmpIncome();

		// for snap
		if (planName.equalsIgnoreCase("SNAP")) {
			if (empIncome <= 300) {
				eligibleOutput.setPlanStatus("Approved");
				eligibleOutput.setBenifitAmt(200.0);
			} else {
				eligibleOutput.setPlanStatus(planName);
				eligibleOutput.setDenialReason("High Income");
			}
		} else if (planName.equalsIgnoreCase("CCAP")) {
			boolean kidsCountCondition = false;
			boolean kidsAgeCondition = false;

			List<DcChildrenEntity> listChilds = childrenRepo.findByCaseNo(caseNo);
			if (!listChilds.isEmpty()) {
				kidsCountCondition = true;
				for (DcChildrenEntity child : listChilds) {
					int kidAge = Period.between(child.getChildDOB(), LocalDate.now()).getYears();
					if (kidAge > 16) {
						kidsAgeCondition = false;
						break;
					} // if
				} // for
			} // if
			if (empIncome <= 300 && kidsCountCondition && kidsAgeCondition) {
				eligibleOutput.setPlanStatus("Approved");
				eligibleOutput.setBenifitAmt(300.0);
			} else {
				eligibleOutput.setPlanStatus("Denied");
				eligibleOutput.setDenialReason("CCAP rules is not satisfied");
			}

		} else if (planName.equalsIgnoreCase("MEDCARE")) {
			if(citizenAge >=65) {
				eligibleOutput.setPlanStatus("Approved");
				eligibleOutput.setBenifitAmt(350.0);
			}else {
				eligibleOutput.setPlanStatus("Denied");
				eligibleOutput.setDenialReason("Medicare rules is not satisfied");
				
			}
		} else if (planName.equalsIgnoreCase("MEDAID")) {
			if (empIncome <= 300 && propertyIncome == 0) {
				eligibleOutput.setPlanStatus("Approved");
				eligibleOutput.setBenifitAmt(200.0);
			} else {
				eligibleOutput.setPlanStatus("Denied");
				eligibleOutput.setDenialReason("MEMAID rules are not satisfied");
			}
		} else if (planName.equalsIgnoreCase("CAJW")) {
					DcEducationEntity educationEntity=educationRepo.findByCaseNo(caseNo);
					int passOutYear=educationEntity.getPassOutYear();
					if(empIncome ==0  && passOutYear < LocalDate.now().getYear()) {
						eligibleOutput.setPlanStatus("Approved");
						eligibleOutput.setBenifitAmt(300.0);
					}else {
						eligibleOutput.setPlanStatus("Denied");
						eligibleOutput.setDenialReason("CAJW rules are not satisfied");
					}
					
		}
		else if(planName .equalsIgnoreCase("QHP")) {
			if(citizenAge >=1) {
				eligibleOutput.setPlanStatus("Approved");
			}else{
				eligibleOutput.setPlanStatus("Denied");
				eligibleOutput.setDenialReason("QHP rules are not satisfied");
			}
		}

		// set the common properties for elibibleOutput object
		eligibleOutput.setPlanStartDate(LocalDate.now());
		eligibleOutput.setPlanEndDate(LocalDate.now().plusYears(2));
		return eligibleOutput;

	}

}// class
