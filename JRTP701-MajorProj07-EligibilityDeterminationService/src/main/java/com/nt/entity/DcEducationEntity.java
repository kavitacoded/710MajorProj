package com.nt.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class DcEducationEntity {

	@Id
	private Integer eId;
	private Integer caseNo;
	private Integer passOutYear;
}
