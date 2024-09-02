package com.nt.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="JRTP_DC_CHILDREN")
public class DcChildrenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer caseNo;
	private LocalDate  childDOB;
	private Long childSSN;
	
}
