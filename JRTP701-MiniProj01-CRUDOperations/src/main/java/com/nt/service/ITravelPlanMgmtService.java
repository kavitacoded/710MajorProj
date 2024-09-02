package com.nt.service;

import java.util.List;
import java.util.Map;

import com.nt.entity.TravelPlan;

public interface ITravelPlanMgmtService {

	
	public String registerTravelPlan(TravelPlan plan);
	public Map<Integer, String> getTravelPlanCategories();//for select
	public List<TravelPlan> showAllTravelPlans(); //for select
	public TravelPlan showTravelPlanById(Integer planId);
	public String updateTravelPlan(TravelPlan plan);
	public String deleteTravelPlan(Integer planId);
	public String changeTravelPlanStatus(Integer planId,String status);
}
