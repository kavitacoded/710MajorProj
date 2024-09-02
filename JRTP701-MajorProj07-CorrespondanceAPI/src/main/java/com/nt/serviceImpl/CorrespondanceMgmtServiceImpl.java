package com.nt.serviceImpl;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nt.bindings.COSummary;
import com.nt.entity.CitizenAppRegistraionEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.EligibilityDetailsEntity;
import com.nt.repository.ICitizenAppRegistrationRepository;
import com.nt.repository.ICoTriggerRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IEligibilityDetermineRepository;
import com.nt.service.ICorrespondanceMgmtService;
import com.nt.util.EmailUtils;


@Service
public class CorrespondanceMgmtServiceImpl implements ICorrespondanceMgmtService {
	@Autowired
	private ICoTriggerRepository triggerRepo;
	@Autowired
	private IEligibilityDetermineRepository eligibilityRepo;
	@Autowired
	private IDcCaseRepository caseRepo;
	@Autowired
	private ICitizenAppRegistrationRepository citizenRepo;
	
	@Autowired
	private EmailUtils mailUtils;
	
	@Override
	public COSummary processPendingTriggers() {
		CitizenAppRegistraionEntity citizenEntity=null;
		EligibilityDetailsEntity eligiEntity=null;
		int pendingTriggers=0;
		int successTriggers=0;
		//get all pending Triggers
		List<CoTriggersEntity> triggersList=triggerRepo.findByTriggerStatus("pending");
		//process each pending triggers
		for(CoTriggersEntity triggerEntity : triggersList) {
				//get Eligibility details base on caseNo
			eligiEntity= eligibilityRepo.findByCaseNo(triggerEntity.getCaseNo());
			//get appId based on caseNo
			Optional<DcCaseEntity> optCaseEntity=caseRepo.findById(triggerEntity.getCaseNo());
			if(optCaseEntity.isPresent()) {
				DcCaseEntity caseEntity=optCaseEntity.get();
				Integer appId=caseEntity.getAppId();
				Optional<CitizenAppRegistraionEntity> optCitizenEntity=citizenRepo.findById(appId);
				if(optCitizenEntity.isPresent()) {
					citizenEntity=optCitizenEntity.get();
				}
			}
	
	
		//get citizen Data base no caseNo
		//generate PDF Doc having eligibility details
		try {
			generatePdfAndSendMail(eligiEntity, citizenEntity);
			successTriggers ++;
		} catch (Exception e) {
			pendingTriggers ++;
			e.printStackTrace();
		}
	}	//for
		COSummary summary=new COSummary();
		summary.setTotalTriggers(triggersList.size());
		summary.setPendingTriggers(pendingTriggers);
		summary.setSuccessTriggers(successTriggers);
		//send pdF doc to citizen 
		//store pdF doc in CO_TRIGGERS db table and also update Triggers status to completed 
		return summary;
	}
	
	
	//helper method to generate the pdf doc
	private void generatePdfAndSendMail(EligibilityDetailsEntity eligiEntity,CitizenAppRegistraionEntity citizenEntity) throws Exception {
	
				// create document object(openPdf)
				Document document = new Document(PageSize.A4);
				//create pdf file to write the content for it
				File file=new File(eligiEntity.getCaseNo()+".pdf");
				FileOutputStream fos=new FileOutputStream(file);
				
				// get pdf writer to write to the document and response obj
				PdfWriter.getInstance(document, fos);
				// open the document
				document.open();
				// define font for paragraph
				Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
				font.setSize(30);
				font.setColor(Color.CYAN);

				// create the paragraph having content and above font style
				Paragraph para = new Paragraph("Plan Approval/Denial Communication ", font);
				para.setAlignment(Paragraph.ALIGN_CENTER);

				// add paragraph to document
				document.add(para);

				// display search results as the pdf tables
				PdfPTable table = new PdfPTable(10);
				table.setWidthPercentage(110);
				table.setWidths(new float[] { 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f });
				table.setSpacingBefore(5.0f);

				// prepare heading row cells in the pdf table
				PdfPCell cell = new PdfPCell();
				cell.setBackgroundColor(Color.gray);
				cell.setPadding(5);
				Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
				cellFont.setColor(Color.BLACK);

				cell.setPhrase(new Phrase("traceId", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("HolderName", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("CourseCategory", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("HolderSSN", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("PlanName", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("PnalStatus", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("PlanStartDate", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("PlanEndDate", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("BenifitAmt", cellFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("DenialReason", cellFont));
				table.addCell(cell);

				// add data cells to pdf table
			
					table.addCell(String.valueOf(eligiEntity.getEdTraceId()));
					table.addCell(String.valueOf(eligiEntity.getCaseNo()));
					table.addCell(String.valueOf(eligiEntity.getHolderName()));
					table.addCell(String.valueOf(eligiEntity.getHolderSSN()));
					table.addCell(eligiEntity.getPlanName());
					table.addCell(eligiEntity.getPlanStatus());
					table.addCell(String.valueOf(eligiEntity.getPlanStartDate()));
					table.addCell(String.valueOf(eligiEntity.getPlanEndDate()));
					table.addCell(String.valueOf(eligiEntity.getBenifitAmt()));
					table.addCell(eligiEntity.getDenialReason());

		
				// add table to document
				document.add(table);
				// close the document
				document.close();
				//send the generated pdf doc as the email message
				String subject= "Plan Approved/denial mail";
				String body="hello Mr/Miss/Mrs."+ citizenEntity.getFullName() +"This mail contains complete details plan approval or deniel";
				mailUtils.sendMail(citizenEntity.getEmail(),subject, body, file);
				//update Co_Trigger table
				updateCoTrigger(eligiEntity.getCaseNo(),file);
	}

	private void updateCoTrigger(Integer caseNo,File file)throws Exception {
		//check Trigger availability base on caseNo
		CoTriggersEntity triggerEntity= triggerRepo.findByCaseNo(caseNo);
		//get byte[] representing pdf doc content
		byte[] pdfContent=new byte[((int) file.length())];
		FileInputStream fis=new FileInputStream(file);
		fis.read(pdfContent);
		if(triggerEntity!=null) {
			triggerEntity.setCoNoticepdf(pdfContent);
			triggerEntity.setTriggerStatus("completed");
			triggerRepo.save(triggerEntity);
		}
		fis.close();
	}
}
