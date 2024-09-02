package com.nt.ms;

import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.model.SearchInputs;
import com.nt.model.SearchResults;
import com.nt.service.ICourseMgmtService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/reporting/api")
@OpenAPIDefinition(info = @Info(title = "Report API Generation", version = "1.0", description = "My Reports Generation API", license = @License(name = "Apache 2.0", url = "http://foo.bar"), contact = @Contact(url = "http://gigantic-server.com", name = "jani", email = "jani@gmail.com")))
public class ReportingOperationsController {

	@Autowired
	private ICourseMgmtService courseService;

	@GetMapping("/courses")
	@Operation(summary = "Get Courses", description = "Get list of courses")
	public ResponseEntity<?> fetchCourseCategories() {
		try {
			// use service
			Set<String> coursesInfo = courseService.showAllCourseCategories();
			return new ResponseEntity<Set<String>>(coursesInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/faculties")
	@Operation(summary = "Get faculties", description = "Get list of faculties")
	public ResponseEntity<?> fetchFaculties() {
		try {
			// use service
			Set<String> facultiesInfo = courseService.showAllFaculties();
			return new ResponseEntity<Set<String>>(facultiesInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/training-modes")
	public ResponseEntity<?> fetchTrainingModes() {
		try {
			// use service
			Set<String> trainingInfo = courseService.showAllTrainingModes();
			return new ResponseEntity<Set<String>>(trainingInfo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("excel-report")
	public void showExcelReport(@RequestBody SearchInputs inputs, HttpServletResponse res) {
		try {
			// set the response content type
			res.setContentType("application/vnd.ms-excel");
			// set the content disposition header to response content going to browser as
			// downloadable file
			res.setHeader("Content-Disposition", "attachment;fileName=courses.xls");
			// use service
			courseService.generateExcelReport(inputs, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/pdf-report")
	public void showPdfReport(@RequestBody SearchInputs inputs, HttpServletResponse res) {
		try {
			// set the response content type
			res.setContentType("application/pdf");
			// set the content disposition header to response content going to browser as
			// downloadable file
			res.setHeader("Content-Disposition", "attachment;fileName=courses.pdf");
			// use service
			courseService.generatePdfReport(inputs, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/search")
	public ResponseEntity<?> fetchCoursesByFilter(@RequestBody SearchInputs input) {
		try {
			// use service
			List<SearchResults> list = courseService.showCoursesByFilters(input);
			return new ResponseEntity<List<SearchResults>>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all-excel-report")
	public void showExcelReportAllData(HttpServletResponse res) {
		try {
			//set the response content type
			res.setContentType("application/vnd.ms-excel");
			//set the content disposition header to response content going to browser as downloadable file
			res.setHeader("content-Disposition", "attachment;fileName=courses.xls");
			//use service
			courseService.generateExcelReportAllData(res);
		} catch (Exception e) {
		e.printStackTrace();
		}//catch
	}//method
	
	@GetMapping("/all-pdf-report")
	public void showPdfReportAllData(HttpServletResponse res) {
		try {
			//set the response content
			res.setContentType("application/pdf");
			//set the content disposition header to response content going to browser as downloadable file
			res.setHeader("content-Disposition", "attachment;fileName=courses.pdf");
			//use service
			courseService.generatePdfReportAllData(res);
			
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	
	

}// class
