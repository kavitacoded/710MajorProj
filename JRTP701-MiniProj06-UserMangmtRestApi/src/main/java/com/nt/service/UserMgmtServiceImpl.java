package com.nt.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.nt.bindings.ActivateUser;
import com.nt.bindings.LoginCredentials;
import com.nt.bindings.RecoverPassword;
import com.nt.bindings.UserAccount;
import com.nt.entity.UserMaster;
import com.nt.repository.IUserMasterRepository;
import com.nt.utils.EmailUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserMgmtServiceImpl implements IUserMgmtService {

	@Autowired
	private IUserMasterRepository userRepo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private Environment env;
	
	@Override
	public String registerUser(UserAccount user) throws Exception {
		// convert user account obj data to user master obj data
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(user, master);
		System.out.println(user);
		// set random string of 6 char as password
		String tempPwd=generateRandGeneratedPassword(6);
		master.setPassword(tempPwd);
		master.setActiveSW("InActive");
		// save object
		UserMaster savedMaster = userRepo.save(master);
		//perform sent mail operation
		String subject="User Registration Success";
		String body=readEmailMessageBody(env.getProperty("mailbody.registeruser.location"), user.getName(),tempPwd);
		emailUtils.sendEmailMessage(user.getEmail(), subject, body);
		
		// return message
		return savedMaster.getUserId()!=null?"User is registered with Id value :: " + savedMaster.getUserId()
				: "Problem is user registration";

	}
	
	/*@Override
	public String activateUserAccount(ActivateUser user) {
		//convert ActiveUserObj to EntityObj (User Master Obj) data
		UserMaster master=new UserMaster(); 
		master.setEmail((user.getEmail()));
		master.setPassword(user.getTempPassword());
		//chek the records available by using email and temp password
		Example<UserMaster> example=Example.of(master);
		List<UserMaster> list=userRepo.findAll(example);
		
		//if valid email and temp is given the enduser supplied real password to update the record
		if(list.size()!=0) {
			//get entity object
			UserMaster entity=list.get(0);
			//set password
			entity.setPassword(user.getConfirmPassword());
			//change user account status to active
			entity.setActive_sw("Active");
			//update the object
			UserMaster updateEntity=userRepo.save(entity);
			return " user is activated with new Password";
		}
		return "User is not fount for activation";
	}*/
	@Override
	public String activateUserAccount(ActivateUser user) {
		// use find by method
		UserMaster entity = userRepo.findByEmailAndPassword(user.getEmail(), user.getTempPassword());
		if (entity == null) {
			return "User is not found for activation";
		} else {
			// set password
			entity.setPassword(user.getConfirmPassword());
			// change the user account status to active
			entity.setActiveSW("Active");
			// update the obj
			UserMaster updatedEntity = userRepo.save(entity);
			return "User is Activated with new Password";
		}

	}

	@Override
	public String login(LoginCredentials credentials) {
		// convert login credentials obj to UserMaster obj (Entity obj)
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(credentials, master);
		// prepare Example obj
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> listEntities = userRepo.findAll(example);
		if (listEntities.isEmpty()) {
			return "Invalid Credentials";
		} else {
			// get entity obj
			UserMaster entity = listEntities.get(0);
			if (entity.getActiveSW().equalsIgnoreCase("Active")) {
				return "Valid Credentials and Login successfull";
			} else {
				return "User account is not active";
			}
		}

	}

	@Override
	public List<UserAccount> listUsers() {
		// Load all entities and convert to UserAccount obj
		return userRepo.findAll().stream().map(entity -> {
			UserAccount user = new UserAccount();
			BeanUtils.copyProperties(entity, user);
			return user;
		}).toList();
		
	}

	@Override
	public UserAccount showUserByUserId(Integer id) {
		// Load the user by user id
		Optional<UserMaster> opt = userRepo.findById(id);
		UserAccount account = null;
		if (opt.isPresent()) {
			account = new UserAccount();
			BeanUtils.copyProperties(opt.get(), account);
		}
		return account;
	}

	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		// use the custom findbyId() method
		UserMaster master = userRepo.findByEmailAndPassword(email, name);
		UserAccount account = null;
		if (master != null) {
			account = new UserAccount();
			BeanUtils.copyProperties(master, account);
		}
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		// Load the obj
		Optional<UserMaster> opt = userRepo.findById(user.getUserId());
		if (opt.isPresent()) {
			UserMaster master = opt.get();
			BeanUtils.copyProperties(user, master);
			userRepo.save(master);
			return "User Details are udpated";
		} else {
			return "User not found for updation";
		}
	}

	@Override
	public String deleteUserById(Integer id) {
		// Load the obj
		Optional<UserMaster> opt = userRepo.findById(id);
		if (opt.isPresent()) {
			userRepo.deleteById(id);
			return "User is deleted";
		}
		return "User not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		// Load the obj
		Optional<UserMaster> opt = userRepo.findById(id);
		if (opt.isPresent()) {
			// get Entity obj
			UserMaster master = opt.get();
			// change the status
			master.setActiveSW(status);
			// update the object
			userRepo.save(master);
			return "User Status is Changed";
		}
		return "user not found for changing the status";
	}

	@Override
	public String recoverPassword(RecoverPassword recover) throws Exception {
		// get user master (Entity obj) by name,email
		UserMaster master = userRepo.findByNameAndEmail(recover.getName(), recover.getEmail());
		if (master!= null) {
			String pwd = master.getPassword();
			// sent recover password  to Email account
			String subject="mail for password recovery";
			String mailBody=readEmailMessageBody(env.getProperty("mailbody.recoverpwd.location"), recover.getName(),pwd);
			emailUtils.sendEmailMessage(recover.getEmail(), subject,mailBody);
			return pwd;
		}
		return "User and email not found";
	}

//	private String generateRandGeneratedPassword(int length) {
//		// a list of characters to choose from in form of a string
//		String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
//		// creating a StringBuffer size of AlphaNumericStr
//		StringBuilder randomWord = new StringBuilder(length);
//		int i;
//		for (i = 0; i < length; i++) {
//			SecureRandom random=new SecureRandom();
//			float randVal=random.nextInt();
//			// generating a random number using math.random()
//			int ch = (int) (alphaNumericString.length() * randVal);
//			// adding Random character one by one at the end of s
//			randomWord.append(alphaNumericString.charAt(ch));
//		}
//		return randomWord.toString();
//	}
	private String generateRandGeneratedPassword(int length) {
		// a list of characters to choose from in form of a string
		 String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		 // creating a StringBuffer size of AlphaNumericStr
		 StringBuilder randomWord = new StringBuilder(length);
		 int i;
		 for ( i=0; i<length; i++) {
		   //generating a random number using math.random()
		   int ch = (int)(AlphaNumericStr.length() * Math.random());
		   //adding Random character one by one at the end of s
		   randomWord.append(AlphaNumericStr.charAt(ch));
		 }
		    return randomWord.toString();
	}
	private String readEmailMessageBody(String fileName, String fullName, String pwd) throws Exception {
		String mailBody = null;
		String url = "http://localhost:4043/activate";
		try (FileReader reader = new FileReader(fileName); 
				BufferedReader br = new BufferedReader(reader)) {
			// read file content to String buffer object line by line
			StringBuilder buffer = new StringBuilder();
			String line = null;
			do {
				line =br.readLine();
				if(line!=null)
				buffer.append(line);
			} while (line!=null);

			mailBody= buffer.toString();
			mailBody= mailBody.replace("{FULL-NAME}", fullName);
			mailBody= mailBody.replace("{PWD}", pwd);
			mailBody= mailBody.replace("{URL}", url);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mailBody;
	}

	

}
