package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.entity.Employee;
import com.nt.service.IEmployeeService;

@RestController
@RequestMapping("/employee/api")
public class EmployeeController {

	@Autowired
	private IEmployeeService service;
	
	@PostMapping("/register")
	public ResponseEntity<?> saveEmployee(@RequestBody Employee emp){
		String msg=service.registerEmployee(emp);
		return new ResponseEntity<String>(msg,HttpStatus.CREATED);
	}
}
