package com.nt.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.FoodCategory;
import com.nt.entity.FoodItem;
import com.nt.repository.IFoodCategoryRepository;
import com.nt.repository.IFoodItemRepository;

@Service
public class FoodItemServiceImpl implements IFoodItemService {

	@Autowired
	private IFoodItemRepository foodItemRepo;

	@Autowired
	private IFoodCategoryRepository foodCategoryRepo;

	@Override
	public String registerFoodItem(FoodItem item) {
//		FoodItem items=foodItemRepo.save(item);
//		return items.getFId()!=null ? "Food Item is Saved with Id" +items.getFId() : "Problem in Saving FoodItem";
		return Optional.ofNullable(foodItemRepo.save(item))
				.map(savedItem -> "Food Item is Saved with Id " + savedItem.getItemId())
				.orElse("Problem in Saving FoodItem");
	}

	@Override
	public Map<Integer, String> getFoodItemCategories() {
		// get food item categories
		return foodCategoryRepo.findAll().stream()
				.collect(Collectors.toMap(FoodCategory::getFcategory_Id, FoodCategory::getFcategory_Name));

	}

	@Override
	public List<FoodItem> showAllFoodItems() {
		return foodItemRepo.findAll();
	}

	@Override
	public FoodItem showFoodItemById(Integer itemId) {
		return foodItemRepo.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Food Item is not Found"));
	}

	@Override
	public String updateFoodItem(FoodItem item) {
		return foodItemRepo.findById(item.getItemId())
				.map(existingItem -> {
					foodItemRepo.save(item);
					return item + "Food Item is updated";
				})
				 .orElseGet(() -> item.getItemId() + " Travel Plan is not found");
	}

	@Override
	public String deleteFoodItem(Integer foodId) {
		return foodItemRepo.findById(foodId)
				.map(existingItem -> {
					foodItemRepo.deleteById(foodId);
					return foodId +" Food Item is Deleted ";
				})
				.orElse(foodId  + " Travel Plan is not Found");
	}

}
