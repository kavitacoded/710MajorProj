package com.nt.service;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nt.entity.CourseDetails;
import com.nt.model.SearchInputs;
import com.nt.model.SearchResults;
import com.nt.repository.ICourseDetailsRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseMgmtServiceImpl implements ICourseMgmtService {

	@Autowired
	private ICourseDetailsRepository courseRepo;

	@Override
	public Set<String> showAllCourseCategories() {
		return courseRepo.getUniqueCourseCategories();
	}

	@Override
	public Set<String> showAllTrainingModes() {
		return courseRepo.getUniqueTrainingModes();
	}

	@Override
	public Set<String> showAllFaculties() {
		return courseRepo.getUniqueFacultyNames();
	}

	/*@Override
	public List<SearchResults> showCoursesByFilters(SearchInputs inputs) {
	CourseDetails entities=new CourseDetails();
	String category=inputs.getCourseCategory();
	if(category!=null && !category.equals("")&& category.length()!=0)
		entities.setCourseCategory(category);
	
	String facultyName=inputs.getFacultyName();
	if(facultyName!=null && !facultyName.equals("")&& facultyName.length()!=0)
	entities.setFacultyName(facultyName);
	
	String trainingMode=inputs.getTrainingMode();
	if(trainingMode!=null && !trainingMode.equals("")&& trainingMode.length()!=0)
	entities.setTraningMode(trainingMode);
	
	LocalDateTime startDate=inputs.getStartsOn();
	if(startDate!=null)
		entities.setStartDate(startDate);
	
	Example<CourseDetails> example=Example.of(entities);
	//performs search operation with filters data of Example Entity obj
	List<CourseDetails> listEntities=courseRepo.findAll(example);
	//convert List<EntityObj> to List<SearchResult> obj
	List<SearchResults> listResults=new ArrayList();
	listEntities.forEach(course ->{
		SearchResults result=new SearchResults();
		BeanUtils.copyProperties(course, result);
		listResults.add(result);
		
	});
		return listResults;
	}*/

	@Override
	public List<SearchResults> showCoursesByFilters(SearchInputs inputs) {
		// get NonNull and non empty String values from the inputs object and prepareEntity
		// obj having that non null data and also place that entity object inside
		// Example obj
		CourseDetails entities = new CourseDetails();
		
		String category = inputs.getCourseCategory();
		if (StringUtils.hasLength(category))
			entities.setCourseCategory(category);

		String facultyName = inputs.getFacultyName();
		if (StringUtils.hasLength(facultyName))
			entities.setFacultyName(facultyName);

		String trainingMode = inputs.getTrainingMode();
		if (StringUtils.hasLength(trainingMode))
			entities.setTrainingMode(trainingMode);

		LocalDateTime startDate = inputs.getStartsOn();
		if (!ObjectUtils.isEmpty(startDate))
			entities.setStartDate(startDate);

		Example<CourseDetails> example = Example.of(entities);
		// performs search operation with filters data of Example Entity obj
		List<CourseDetails> listEntities = courseRepo.findAll(example);
		// convert List<EntityObj> to List<SearchResult> obj
		List<SearchResults> listResults = new ArrayList<>();
		listEntities.forEach(course -> {
			SearchResults result = new SearchResults();
			BeanUtils.copyProperties(course, result);
			listResults.add(result);
		});
		return listResults;
	}


	@Override
	public void generatePdfReport(SearchInputs inputs, HttpServletResponse res) throws Exception {
		// get the search result
		List<SearchResults> listResults = showCoursesByFilters(inputs);
		// create document object(openPdf)
		Document document = new Document(PageSize.A4);
		// get pdf writer to write to the document and response obj
		PdfWriter.getInstance(document, res.getOutputStream());
		// open the document
		document.open();
		// define font for paragraph
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);
		font.setColor(Color.RED);

		// create the paragraph having content and above font style
		Paragraph para = new Paragraph("Search Report of Courses ", font);
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

		cell.setPhrase(new Phrase("courseId", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseName", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseCategory", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("facultyName", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("location", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fee", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("CourseStatus", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("TrainingMode", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("adminContact", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("startDate", cellFont));
		table.addCell(cell);

		// add data cells to pdf table
		listResults.forEach(result -> {
			table.addCell(String.valueOf(result.getCourseId()));
			table.addCell(result.getCourseName());
			table.addCell(result.getCourseCategory());
			table.addCell(result.getFacultyName());
			table.addCell(result.getLocation());
			table.addCell(String.valueOf(result.getFee()));
			table.addCell(result.getCourseStatus());
			table.addCell(result.getTrainingMode());
			table.addCell(String.valueOf(result.getAdminContact()));
			table.addCell(result.getStartDate().toString());

		});
		// add table to document
		document.add(table);
		// close the document
		document.close();
	}

	@Override
	public void generateExcelReport(SearchInputs inputs, HttpServletResponse res) throws Exception {
		// get the search results
		List<SearchResults> listResults = showCoursesByFilters(inputs);

		// create ExcelWorkBook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// create sheet in the workbook
		HSSFSheet sheet1 = workbook.createSheet("CourseDetails");
		HSSFRow headerRow = sheet1.createRow(0);
		headerRow.createCell(0).setCellValue("courseID");
		headerRow.createCell(1).setCellValue("courseName");
		headerRow.createCell(2).setCellValue("Location");
		headerRow.createCell(3).setCellValue("courseCategory");
		headerRow.createCell(4).setCellValue("facultyName");
		headerRow.createCell(5).setCellValue("fee");
		headerRow.createCell(6).setCellValue("AdminContact");
		headerRow.createCell(7).setCellValue("trainingMode");
		headerRow.createCell(8).setCellValue("startDate");
		headerRow.createCell(9).setCellValue("CourseStatus");

		// add data rows to the sheet
		int i = 1;
		for (SearchResults result : listResults) {

			HSSFRow datarow = sheet1.createRow(0);
			datarow.createCell(0).setCellValue(result.getCourseId());
			datarow.createCell(1).setCellValue(result.getCourseName());
			datarow.createCell(2).setCellValue(result.getLocation());
			datarow.createCell(3).setCellValue(result.getCourseCategory());
			datarow.createCell(4).setCellValue(result.getFacultyName());
			datarow.createCell(5).setCellValue(result.getFee());
			datarow.createCell(6).setCellValue(result.getAdminContact());
			datarow.createCell(7).setCellValue(result.getTrainingMode());
			datarow.createCell(8).setCellValue(result.getStartDate());
			datarow.createCell(9).setCellValue(result.getCourseStatus());
			i++;
		}
		// get output stream pointing to response obj
		ServletOutputStream outputStream = res.getOutputStream();
		// write Excel workbook data response object using the above stream
		workbook.write(outputStream);
		// close the stream
		outputStream.close();
		workbook.close();
	}

	@Override
	public void generateExcelReportAllData(HttpServletResponse res) throws Exception {
		// get all the records from Db table
		List<CourseDetails> list = courseRepo.findAll();
		// copy List<CourseDetails> to List<SearchResults>
		List<SearchResults> listResults = new ArrayList();
		list.forEach(course -> {
			SearchResults result = new SearchResults();
			BeanUtils.copyProperties(course, result);
			listResults.add(result);
		});
		// create ExcelWorkBook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// create sheet in the workbook
		HSSFSheet sheet1 = workbook.createSheet("CourseDetails");
		HSSFRow headerRow = sheet1.createRow(0);
		headerRow.createCell(0).setCellValue("courseID");
		headerRow.createCell(1).setCellValue("courseName");
		headerRow.createCell(2).setCellValue("Location");
		headerRow.createCell(3).setCellValue("courseCategory");
		headerRow.createCell(4).setCellValue("facultyName");
		headerRow.createCell(5).setCellValue("fee");
		headerRow.createCell(6).setCellValue("AdminContact");
		headerRow.createCell(7).setCellValue("trainingMode");
		headerRow.createCell(8).setCellValue("startDate");
		headerRow.createCell(9).setCellValue("CourseStatus");
		// add data rows to the sheet
		int i = 1;
		for (SearchResults result : listResults) {

			HSSFRow datarow = sheet1.createRow(0);
			datarow.createCell(0).setCellValue(result.getCourseId());
			datarow.createCell(1).setCellValue(result.getCourseName());
			datarow.createCell(2).setCellValue(result.getLocation());
			datarow.createCell(3).setCellValue(result.getCourseCategory());
			datarow.createCell(4).setCellValue(result.getFacultyName());
			datarow.createCell(5).setCellValue(result.getFee());
			datarow.createCell(6).setCellValue(result.getAdminContact());
			datarow.createCell(7).setCellValue(result.getTrainingMode());
			datarow.createCell(8).setCellValue(result.getStartDate());
			datarow.createCell(9).setCellValue(result.getCourseStatus());
			i++;
		}
		// get output stream pointing to response obj
		ServletOutputStream outputStream = res.getOutputStream();
		// write Excel workbook data response object using the above stream
		workbook.write(outputStream);
		// close the stream
		outputStream.close();
		workbook.close();

	}

	@Override
	public void generatePdfReportAllData(HttpServletResponse res) throws Exception {
		// get All the records from DB table
		List<CourseDetails> list = courseRepo.findAll();
		// copy List<CourseDetails> to List<SearchResults>
		List<SearchResults> listResults = new ArrayList();
		list.forEach(course -> {
			SearchResults result = new SearchResults();
			BeanUtils.copyProperties(course, result);
			listResults.add(result);
		});

		// create document object(openPdf)
		Document document = new Document(PageSize.A4);
		// get pdf writer to write to the document and response obj
		PdfWriter.getInstance(document, res.getOutputStream());
		// open the document
		document.open();
		// define font for paragraph
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);
		font.setColor(Color.RED);

		// create the paragraph having content and above font style
		Paragraph para = new Paragraph("Search Report of Courses ", font);
		para.setAlignment(Paragraph.ALIGN_CENTER);

		// add paragraph to document
		document.add(para);

		// display search results as the pdf tables
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f });
		table.setSpacingBefore(5.0f);

		// prepare heading row cells in the pdf table
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.gray);
		cell.setPadding(5);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		cellFont.setColor(Color.BLACK);

		cell.setPhrase(new Phrase("courseId", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseName", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseCategory", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("facultyName", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("location", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fee", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("CourseStatus", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("TrainingMode", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("adminContact", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("startDate", cellFont));
		table.addCell(cell);

		// add data cells to pdf table
		listResults.forEach(result -> {
			table.addCell(String.valueOf(result.getCourseId()));
			table.addCell(result.getCourseName());
			table.addCell(result.getCourseCategory());
			table.addCell(result.getFacultyName());
			table.addCell(result.getLocation());
			table.addCell(String.valueOf(result.getFee()));
			table.addCell(result.getCourseStatus());
			table.addCell(result.getTrainingMode());
			table.addCell(String.valueOf(result.getAdminContact()));
			table.addCell(result.getStartDate().toString());

		});
		// add table to document
		document.add(table);
		// close the document
		document.close();
	}

}// class
