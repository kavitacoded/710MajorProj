package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.CitizenAppRegistrionInputs;
import com.nt.exception.InvalidSSNException;
import com.nt.service.ICitizenApplicationRegistrionService;

@RestController
@RequestMapping("/Citizen-app")
public class CitizenApplicationRegistraionOperationsController {

	@Autowired
	private ICitizenApplicationRegistrionService citizenService;

	/*@PostMapping("/save")
	public ResponseEntity<String> saveCitizenApplication(@RequestBody CitizenAppRegistrionInputs inputs)throws InvalidSSNException {
		try {
			// use service
			int appId = citizenService.registerCitizenApplication(inputs);
			if (appId > 0)
				return new ResponseEntity<String>("Citizen Application is Registered with ID " + appId,
						HttpStatus.CREATED);
			else
				return new ResponseEntity<String>("Invalid SSN or Citizen must belong to California State ",
						HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	@PostMapping("/save")
	public ResponseEntity<String> saveCitizenApplication(@RequestBody CitizenAppRegistrionInputs inputs)throws Exception {
			// use service
			int appId = citizenService.registerCitizenApplication(inputs);
			return new ResponseEntity<String>("Citizen Application is Registered with ID " + appId,HttpStatus.CREATED);
	}
}
