package com.nt.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.EligibilityDetailsOutput;
import com.nt.service.IEligibilityDeterminationMgmtService;

@RestController
@RequestMapping("/ed-api")
public class EligibilityDeterminationController {

	
	@Autowired
	private IEligibilityDeterminationMgmtService service;
	
	@GetMapping("/determine/{caseNo}")
	public ResponseEntity<EligibilityDetailsOutput> checkPlanEligibility(@PathVariable Integer caseNo){
		//use service
		System.out.println("123");
		EligibilityDetailsOutput output=service.determineEligibility(caseNo);
		return new ResponseEntity<EligibilityDetailsOutput>(output,HttpStatus.CREATED);
	}
}//class
