package com.nt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.bindings.ChildInputs;
import com.nt.bindings.CitizenAppRegistraionInputs;
import com.nt.bindings.DcSummaryReport;
import com.nt.bindings.EducationInputs;
import com.nt.bindings.IncomeInputs;
import com.nt.bindings.PlanSelectionInputs;
import com.nt.entity.CitizenAppRegistraionEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.PlanEntity;
import com.nt.repo.IApplicationRegistraionRepository;
import com.nt.repo.IChildrenRepository;
import com.nt.repo.IDcCaseRepository;
import com.nt.repo.IDcEducationRepository;
import com.nt.repo.IDcIncomeRepository;
import com.nt.repo.IPlanRepository;

@Service
public class DcMgmtServiceImpl implements IDcMgmtService {

	@Autowired
	private IDcCaseRepository caseRepository;
	@Autowired
	private IApplicationRegistraionRepository citizenAppRepo;

	@Autowired
	private IPlanRepository planRepo;
	@Autowired
	private IDcIncomeRepository incomeRepo;

	@Autowired
	private IDcEducationRepository educationRepo;

	@Autowired
	private IChildrenRepository childrenRepo;

	@Override
	public Integer genereateCaseNo(Integer appId) {
		// Load Citizen Data
		Optional<CitizenAppRegistraionEntity> appCitizen = citizenAppRepo.findById(appId);
		if (appCitizen.isPresent()) {
			DcCaseEntity caseEntity = new DcCaseEntity();
			caseEntity.setAppId(appId);
			return caseRepository.save(caseEntity).getCaseNo();

		}
		return 0;
	}

	@Override
	public List<String> showAllPlanNames() {
		List<PlanEntity> planList = planRepo.findAll();
		// get only plan names
		List<String> planNamesList = planList.stream().map(plan -> plan.getPlanName()).toList();
		return planNamesList;
	}

	@Override
	public Integer savePlanSelection(PlanSelectionInputs plan) {
		// Load DCaseEntity
		Optional<DcCaseEntity> opt = caseRepository.findById(plan.getCaseNo());
		if (opt.isPresent()) {
			DcCaseEntity caseEntity = opt.get();
			caseEntity.setPlanId(plan.getPlanId());
			// update the DcCaseEntity with plain Id
			caseRepository.save(caseEntity);
			return caseEntity.getCaseNo();
		}
		return null;
	}

	@Override
	public Integer saveIncomeDetails(IncomeInputs income) {
		// convert binding obj data to Entity class obj data
		DcIncomeEntity incomeEntity = new DcIncomeEntity();
		BeanUtils.copyProperties(income, incomeEntity);
		// save the income details
		incomeRepo.save(incomeEntity);
		// return caseNo
		return income.getCaseNo();
	}

	@Override
	public Integer saveEducationDetails(EducationInputs education) {
		// convert Binding object to Entity Object
		DcEducationEntity educationEntity = new DcEducationEntity();
		BeanUtils.copyProperties(education, educationEntity);
		// save the obj
		educationRepo.save(educationEntity);
		// return the caseNumber
		return education.getCaseNo();
	}

	@Override
	public Integer childrenDetails(List<ChildInputs> children) {
		// Convert each Binding class obj to each Entity class obj
		children.forEach(child -> {
			DcChildrenEntity childEntity = new DcChildrenEntity();
			BeanUtils.copyProperties(child, childEntity);
			// save each entity object
			childrenRepo.save(childEntity);
		});
		return children.get(0).getCaseNo();
	}

	@Override
	public DcSummaryReport showDCSummary(Integer caseNo) {
		//get multiple entity objs based on caseNo
		DcIncomeEntity incomeEntity =incomeRepo.findByCaseNo(caseNo);
		DcEducationEntity educationEntity=educationRepo.findByCaseNo(caseNo);
		List<DcChildrenEntity> childsEntityList=childrenRepo.findByCaseNo(caseNo);
		Optional<DcCaseEntity> optCaseEntity=caseRepository.findById(caseNo);
		//get PlanName
		String planName=null;
		Integer appId=null;
		if(optCaseEntity.isPresent()) {
		DcCaseEntity caseEntity=optCaseEntity.get();
		Integer planId=caseEntity.getPlanId();
		appId=caseEntity.getAppId();
		Optional<PlanEntity> optPlanEntity=planRepo.findById(planId);
		if(optPlanEntity.isPresent()) {
			planName=optPlanEntity.get().getPlanName();
		}
		}
		Optional<CitizenAppRegistraionEntity> optCitizenEntity=citizenAppRepo.findById(appId);
		CitizenAppRegistraionEntity citizenEntity=null;
		if(optCitizenEntity.isPresent())
			citizenEntity=optCitizenEntity.get();
		//convert Entity objs to Binding objs
		IncomeInputs income=new IncomeInputs();
		BeanUtils.copyProperties(incomeEntity, income);
		EducationInputs education= new EducationInputs();
		BeanUtils.copyProperties(educationEntity, education);
		List<ChildInputs> listChilds=new ArrayList();
		childsEntityList.forEach(childsEntity ->{
			ChildInputs child=new ChildInputs();
			BeanUtils.copyProperties(childsEntity, child);
			listChilds.add(child);
		});
		CitizenAppRegistraionInputs citizen=new CitizenAppRegistraionInputs();
		BeanUtils.copyProperties(citizenEntity, citizen);
		
		//prepare DcsummaryReport obj
		DcSummaryReport report=new DcSummaryReport();
		report.setPlanName(planName);
		report.setIncomeDetails(income);
		report.setEducationDetails(education);
		report.setCitizenDetails(citizen);
		report.setChildDetails(listChilds);
		return report;
	}

}
