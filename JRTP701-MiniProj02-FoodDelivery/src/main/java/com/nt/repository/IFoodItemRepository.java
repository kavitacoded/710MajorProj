package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.FoodItem;

public interface IFoodItemRepository extends JpaRepository<FoodItem, Integer> {

}
