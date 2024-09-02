package com.nt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class DcIncomeEntity {

	@Id
	private Integer id;
	private Integer caseNo;
	private Double empIncome;
	private Double propertyIncome;
}
