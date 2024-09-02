package com.nt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "FOOD_ITEM")
public class FoodItem {

	@Id
	@Column(name = "FOOD_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer itemId;

	@Column(name = "FOOD_NAME")
	private String food_Name;

	@Column(name = "FOOD_DESCRIPTION")
	private String food_Description;
	
	@Column(name = "FOOD_PRICE")
	private Double food_Price;

	@Column(name = "CATEGORY_ID")
	private Integer category_Id;

}
