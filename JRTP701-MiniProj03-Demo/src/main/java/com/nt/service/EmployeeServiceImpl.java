package com.nt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.Employee;
import com.nt.repo.IEmployeeRepo;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private IEmployeeRepo repo;

	@Override
	public String registerEmployee(Employee emp) {
		return Optional.ofNullable(repo.save(emp))
				.map(savedItem -> "emp is Saved with Id " + savedItem.getId())
				.orElse("Problem in Saving emp");
	}

}
