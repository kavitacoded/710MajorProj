package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.EligibilityDetailsEntity;

public interface IEligibilityDetermineRepository extends JpaRepository<EligibilityDetailsEntity, Integer> {

	public EligibilityDetailsEntity  findByCaseNo(int caseNo);
	
}
