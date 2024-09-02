package com.nt.ms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.entity.FoodItem;
import com.nt.service.IFoodItemService;


@RestController
@RequestMapping("/foodItem/api")
public class FoodItemController {

	@Autowired
	private IFoodItemService foodItemService;

	@PostMapping("/register")
	public ResponseEntity<?> saveFoodItem(@RequestBody FoodItem foodItem) {
		try {
			String msg = foodItemService.registerFoodItem(foodItem);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/categories")
	public ResponseEntity<?> showFoodITemCategories() {
		try {
			Map<Integer, String> foodCategories = foodItemService.getFoodItemCategories();
			return new ResponseEntity<Map<Integer, String>>(foodCategories, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllFoodItems() {
		try {
			List<FoodItem> foodItem = foodItemService.showAllFoodItems();
			return new ResponseEntity<List<FoodItem>>(foodItem, HttpStatus.OK);
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateFoodItem(@RequestBody FoodItem foodItem){
		try {
			String updateItems=foodItemService.updateFoodItem(foodItem);
			return new ResponseEntity<String>(updateItems,HttpStatus.OK);
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.OK);
		}
	}
	
	@GetMapping("/find/{itemId}")
	public ResponseEntity<?> getFoodItemById(@PathVariable Integer itemId){
		try {
			FoodItem item=foodItemService.showFoodItemById(itemId);
			return new ResponseEntity<FoodItem> (item,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/delete/{itemId}")
	public ResponseEntity<?> deleteFoodItem(@PathVariable Integer id){
		try {
			String deleteFoodItem=foodItemService.deleteFoodItem(id);
			return new ResponseEntity<String>(deleteFoodItem,HttpStatus.OK);
		} catch (Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
