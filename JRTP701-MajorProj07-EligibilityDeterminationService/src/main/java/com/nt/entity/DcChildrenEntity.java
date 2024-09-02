package com.nt.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class DcChildrenEntity {

	@Id
	private Integer cId;
	private Integer caseNo;
	private LocalDate childDOB;
}
