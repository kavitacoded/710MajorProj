package com.nt.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class EligibilityDetailsEntity {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer edTraceId;
	private Integer caseNo;
	@Column(length = 30)
	private String holderName;
	private Long holderSSN;
	@Column(length = 30)
	private String planName;
	@Column(length = 30)
	private String planStatus;
	private Double benifitAmt;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	@Column(length = 30)
	private String denialStatus;
}
