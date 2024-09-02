package com.nt.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.nt.bindings.CitizenAppRegistrionInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.exception.InvalidSSNException;
import com.nt.repository.IAppRegistraionRepository;
import com.nt.service.ICitizenApplicationRegistrionService;

import reactor.core.publisher.Mono;

@Service
public class CitizenApplicaitonRegistraionService implements ICitizenApplicationRegistrionService{
	@Autowired
	private IAppRegistraionRepository citizenRepo;
	
	@Autowired
	private RestTemplate template;
	
	@Autowired
	private WebClient client;
	
	@Value("${ar.ssa-web.url}")
	private String endpointUrl;
	
	@Value("${ar.state}")
	private String targetState;
	
	/*@Override
	public Integer registerCitizenApplication(CitizenAppRegistrionInputs inputs) {
		//perform WebService call to check weather SSN is valid or not and to get the state name
		ResponseEntity<String> response=template.exchange(endpointUrl,HttpMethod.GET,null,String.class,inputs.getSsn());
		//get state name
//		Mono<ResponseEntity<String>> response=
//				client.get()
//				.uri(endpointUrl,inputs.getSsn())
//				.exchange()
//				.flatMap(ClientResponse ->
//				ClientResponse.bodyToMono(String.class).block());
				
		//String stateName=response.getBody();
		//register citizen if he belongs to California state (CA)
		if(stateName.equalsIgnoreCase(targetState)) {
			//prepare the Entity Object
			CitizenAppRegistrationEntity entity=new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setCreatedBy(stateName);
			//save the object
			int appId=citizenRepo.save(entity).getAppId();
			return appId;
		}	
	return 0;
	}*/

	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrionInputs inputs)throws InvalidSSNException {
	 
	        // Perform WebService call to check SSN validity and get the state name
	    	Mono<String> response=
				client.get()
			.uri(endpointUrl,inputs.getSsn())
			.retrieve()
			.onStatus(HttpStatus.BAD_REQUEST :: equals, res -> res.bodyToMono(String.class) 
			.map(ex-> new InvalidSSNException("Invalid ssn"))).bodyToMono(String.class);
	    	String stateName=response.block();
	        // Register citizen if they belong to California (CA)
	        if (stateName != null && stateName.equalsIgnoreCase(targetState)) {
	            // Prepare the Entity Object
	            CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
	            BeanUtils.copyProperties(inputs, entity);
	            entity.setCreatedBy(stateName);
	            
	            // Save the object and return the application ID
	            return citizenRepo.save(entity).getAppId();
	        }//if
	        throw new InvalidSSNException("Invalid SSN");
	    }
}