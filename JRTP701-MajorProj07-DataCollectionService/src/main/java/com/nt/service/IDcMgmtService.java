package com.nt.service;

import java.util.List;

import com.nt.bindings.ChildInputs;
import com.nt.bindings.DcSummaryReport;
import com.nt.bindings.EducationInputs;
import com.nt.bindings.IncomeInputs;
import com.nt.bindings.PlanSelectionInputs;

public interface IDcMgmtService {

	public Integer genereateCaseNo(Integer appId);
	public List<String> showAllPlanNames();
	public Integer savePlanSelection(PlanSelectionInputs  plan);
	public Integer saveIncomeDetails(IncomeInputs income);
	public Integer saveEducationDetails(EducationInputs education);
	public Integer childrenDetails(List<ChildInputs> children);
	public DcSummaryReport showDCSummary(Integer caseNo);
}
