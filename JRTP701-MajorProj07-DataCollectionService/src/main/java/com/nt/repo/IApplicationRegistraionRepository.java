package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.CitizenAppRegistraionEntity;


public interface IApplicationRegistraionRepository  extends JpaRepository<CitizenAppRegistraionEntity, Integer>{

}
