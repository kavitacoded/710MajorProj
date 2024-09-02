package com.nt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="JRTP_DC_EDUCTION")
public class DcEducationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer educationId;
	private Long caseNo;
	private String highestQlfy;
	private Integer passOutYear;
	
}
