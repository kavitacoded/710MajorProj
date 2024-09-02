package com.nt.ms;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.repository.ICourseDetailsRepository;
import com.nt.service.ICourseDetailsService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@RestController
@RequestMapping("/report/api")
@OpenAPIDefinition (info =
@Info(
          title = "Reports",
          version = "0.0",
          description = "PDF EXCEL API",
          license = @License(name = "apple", url = "http://apple.org"),
          contact = @Contact(url = "http://gigantic-server.com", name = "Kavita", email = "Kavita@gmail.com")
  )
)
public class ReportsApiController {

	@Autowired
	private ICourseDetailsService courseService;
	
	@GetMapping("/report")
	public ResponseEntity<?> showReport(){
		return new ResponseEntity<String>("Report Generated",HttpStatus.OK);
	}
	
	@GetMapping("/categories")
	public ResponseEntity<?>fetchCourseCategories(){
		try {
			//use service
		Set<String> courseCategories=courseService.getCourseCategories();
		return new ResponseEntity<Set<String>>(courseCategories,HttpStatus.OK);
		} catch (Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
