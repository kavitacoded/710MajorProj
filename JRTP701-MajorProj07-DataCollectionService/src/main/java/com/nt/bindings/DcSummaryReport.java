package com.nt.bindings;

import java.util.List;

import lombok.Data;

@Data
public class DcSummaryReport {

	private PlanSelectionInputs planDetails;
	private List<ChildInputs> childDetails;
	private EducationInputs educationDetails;
	private IncomeInputs incomeDetails;
	private CitizenAppRegistraionInputs citizenDetails;
	private String planName;
}
