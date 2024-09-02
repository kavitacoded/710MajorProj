package com.nt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="FOOD_CATEGORY")
public class FoodCategory {

	@Id
	@SequenceGenerator(name="gen1",sequenceName = "fcategory_seq",initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "gen1",strategy = GenerationType.SEQUENCE)
	private Integer fcategory_Id;
	
	@Column(name="FCATEGORY_NAME")
	private String fcategory_Name;
	
	
}
