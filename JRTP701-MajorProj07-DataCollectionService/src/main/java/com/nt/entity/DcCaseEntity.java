package com.nt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="JRTP_DC_CASESS")
public class DcCaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private  Integer caseNo;
	private Integer PlanId;
	private Integer appId;
	
	
	
}
