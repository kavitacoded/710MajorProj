package com.nt.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligibilityDetailsOutput {

	private Integer caseNo;
	private String holderName;
	private String holderSSN;
	private String planName;
	private String planStatus;
	private Double benifitAmt;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private String denialReason;
	
}
