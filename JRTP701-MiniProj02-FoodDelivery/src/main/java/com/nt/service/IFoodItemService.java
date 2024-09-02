package com.nt.service;

import java.util.List;
import java.util.Map;

import com.nt.entity.FoodItem;



public interface IFoodItemService {

	public String registerFoodItem(FoodItem item);
	public Map<Integer, String> getFoodItemCategories();//for select
	public List<FoodItem> showAllFoodItems();
	public FoodItem showFoodItemById(Integer itemId);
	public String updateFoodItem(FoodItem item);
	public String deleteFoodItem(Integer foodId);
	
	
}
