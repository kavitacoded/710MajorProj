package com.nt.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="JRTP701_TRAVEL_PLAN")
@Data
public class TravelPlan {

	@Id
	@Column(name="PLAN_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer planId;
	
	@Column(name="PLAN_NAME",length=30)
	private String planname;
	
	@Column(name="PLAN_MIN_BUDGET")
	private Double planMinBudget;
	
	@Column(name="PLAN_DESCRIPTION",length=50)
	private String planDescription;
	
	@Column(name="PLAN_CATEGORY_ID")
	private Integer planCategoryId;
	
	@Column(name="CREATED_DATE",updatable=false,insertable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	@Column(name="UPDATED_DATE",length=20)
	private LocalDateTime updateDate;
	
	
	@Column(name="CREATED_BY",length=20)
	private String createdBy;
	
	@Column(name="UPDATED_BY", length=20)
	private String updatedBy;
	
	@Column(name="ACTIVE_SW",length=20)
	private String activateSW="active";
	
	
	
}
