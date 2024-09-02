package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.FoodCategory;

public interface IFoodCategoryRepository extends JpaRepository<FoodCategory, Integer> {

}
