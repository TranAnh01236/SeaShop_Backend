package org.trananh.shoppingappbackend.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trananh.shoppingappbackend.model.StructureValue;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.StructureValueRepository;
import org.trananh.shoppingappbackend.repository.UserRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponse;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired(required = true)
	private UserRepository userRepository;
	
	@Autowired(required = true)
	private AuthService authService;
	
	@Autowired(required = true)
	private StructureValueRepository structureValueRepository;
	
	@PostMapping("/login")
	public Map<String, Object> login(@Validated @RequestBody Map<String, String> info) {
        User user = userRepository.findByLoginName(info.get("account"));
        if (user == null) {
        	user = userRepository.findByPhoneNumber(info.get("account"));
        	if (user == null) {
        		return new ResponseMap(1, "Login name or phone number is invalid", null);
			}
		}
    	if(user.getPassword().trim().equals(info.get("password"))) {
    		
    		String token = authService.generateToken(user.getId().trim());
    		
    		return new ResponseMap(0, "Loggin successfully", convertUserForLoginAndRegister(user, token));
    		
    	}
    	return new ResponseMap(2, "Password is invalid", null);
    }
	
	@PostMapping("/register")
	public Map<String, Object> register(@Validated @RequestBody Map<String, String> info) {
        
        User user = new User();
        user.setId(info.get("id").toString());
        user.setFirstName(info.get("firstName").toString());
        user.setLoginName(info.get("loginName").toString());
        user.setLastName(info.get("lastName").toString());
        user.setPassword(info.get("password").toString());
        user.setPhoneNumber(info.get("phoneNumber").toString());
        user.setAddressDetail(info.get("addressDetail").toString());
        user.setType(Integer.parseInt(info.get("type")));
        user.setDayOfBirth(Date.valueOf(info.get("dayOfBirth").toString()));
        user.setEmail(info.get("email").toString());
        user.setGender(Integer.parseInt(info.get("gender").toString()));
        user.setAddress(new StructureValue(info.get("address").toString()));
        System.out.println(user.toString());
        
        User user1 = userRepository.findById(user.getId().trim()).orElse(null);
        if (user1 != null) {
        	return new ResponseMap(1, "Id is already in use", null);
		}
        user1 = userRepository.findByLoginName(user.getLoginName().trim());
        if (user1 != null) {
			return new ResponseMap(2, "Login name is already in use", null);
		}
        user1 = userRepository.findByPhoneNumber(user.getPhoneNumber().trim());
        if (user1 != null) {
			return new ResponseMap(3, "Phone number is already in use", null);
		}
        
		user1 = userRepository.save(user);
    	
    	if (user1 == null) {
    		return new ResponseMap(4, "Register failed", null);
		}
		
    	String token = authService.generateToken(user.getId().trim());
    	
        return new ResponseMap(0, "Register Successfully" , convertUserForLoginAndRegister(user1, token));
        
	}
	
	public Map<String, Object> convertUserForLoginAndRegister(User user, String token){
		String address = "";
		StructureValue ad1 = user.getAddress();
		address += ad1.getValue();
		while(ad1.getLevel() > 1) {
			StructureValue ad2 = ad1;
			ad1 = structureValueRepository.findById(ad2.getParentId()).orElse(null);
			if (ad1 != null) {
				String str = address;
				address = ad1.getValue() + ", " + str;
			}else {
				break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.getId());
		map.put("firstName", user.getFirstName());
		map.put("lastName", user.getLastName());
		map.put("loginName", user.getLoginName());
		map.put("phoneNumber", user.getPhoneNumber());
		map.put("email", user.getEmail());
		map.put("gender", user.getGender());
		map.put("type", user.getType());
		map.put("createAt", user.getCreateAt());
		map.put("updateAt", user.getUpdateAt());
		map.put("dayOfBirth", user.getDayOfBirth().toString());
		map.put("addressDetail", user.getAddressDetail());
		map.put("address", address);
		map.put("token", token);
		
		return map;
	}
	
	@PostMapping("/login_mobile")
	public MyHttpResponse loginMobile(@Validated @RequestBody Map<String, String> info) {
        User user = userRepository.findByLoginName(info.get("account"));
        if (user == null) {
        	user = userRepository.findByPhoneNumber(info.get("account"));
        	if (user == null) {
        		return new MyHttpResponse(1, "Login name or phone number is invalid", null);
			}
		}
    	if(user.getPassword().trim().equals(info.get("password"))) {
    		
    		return new MyHttpResponse(0, "Loggin successfully", user);
    		
    	}
    	return new MyHttpResponse(2, "Password is invalid", null);
    }
}
