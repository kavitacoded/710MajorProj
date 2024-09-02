package com.nt.service;

import com.nt.bindings.CitizenAppRegistrionInputs;
import com.nt.exception.InvalidSSNException;


public interface ICitizenApplicationRegistrionService {
	public Integer registerCitizenApplication(CitizenAppRegistrionInputs inputs)throws InvalidSSNException;


	
}
