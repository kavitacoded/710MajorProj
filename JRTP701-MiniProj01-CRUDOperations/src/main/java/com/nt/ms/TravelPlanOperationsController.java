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

import com.nt.entity.TravelPlan;
import com.nt.service.ITravelPlanMgmtService;



@RestController
@RequestMapping("/travelplan/api")
public class TravelPlanOperationsController {

	@Autowired
	private ITravelPlanMgmtService planService;
	
	@GetMapping("/categories")
	public ResponseEntity<?> showTravelplanCategories(){
		//invoke service
		try {
			Map<Integer, String> mapCategories=planService.getTravelPlanCategories();
			return new ResponseEntity<Map<Integer,String>>(mapCategories,HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> saveTravelPlan(@RequestBody TravelPlan travelPlan){
		//use service
		try {
			String msg= planService.registerTravelPlan(travelPlan);
			return new ResponseEntity<>(msg,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllTravelPlans(){
		//use service
		try {
			List<TravelPlan> list=planService.showAllTravelPlans();
			return new ResponseEntity<List<TravelPlan>>(list,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/find/{planId}")
	public ResponseEntity<?> getTravelPlanById(@PathVariable Integer planId ){
		//use service
		try {
			TravelPlan plan=planService.showTravelPlanById(planId);
			return new ResponseEntity<TravelPlan>(plan,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateTravelPlan(@RequestBody TravelPlan travelPlan){
		//use service
		try {
			String msg=planService.updateTravelPlan(travelPlan);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{planId}")
	public ResponseEntity<?> removeTravelPlanByPlanId(@PathVariable Integer planId ){
		//use service
		try {
			String msg=planService.deleteTravelPlan(planId);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/status-change/{planId}/{status}")
	public ResponseEntity<?> removeTravelPlanByPlanId(@PathVariable Integer planId,
																									@PathVariable String status){
		try {
			String msg= planService.changeTravelPlanStatus(planId, status);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
