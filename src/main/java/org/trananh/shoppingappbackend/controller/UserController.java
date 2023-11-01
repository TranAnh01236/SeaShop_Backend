package org.trananh.shoppingappbackend.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trananh.shoppingappbackend.exception.ResourceNotFoundException;
import org.trananh.shoppingappbackend.model.StructureValue;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.StructureValueRepository;
import org.trananh.shoppingappbackend.repository.UserRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;
import org.trananh.shoppingappbackend.ultilities.ResponseMapArray;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired(required = true)
	private UserRepository userRepository;
	@Autowired(required = true)
	private AuthService authService;
	
	@Autowired(required = true)
	private StructureValueRepository structureValueRepository;
	
	@GetMapping("/")
	public Map<String, Object> getAll(@RequestHeader("token") String token) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
		
		List<User> users = userRepository.findAll();
		
		if (users == null) {
			users = new ArrayList<User>();
		}
		
//		List<StructureValue> lst1 = structureValueRepository.findByTypeAndLevel(1, 1);
//		List<StructureValue> lst2 = structureValueRepository.findByTypeAndLevel(1, 2);
//		List<StructureValue> lst3 = structureValueRepository.findByTypeAndLevel(1, 3);
		
		List<Map<String, Object>> lstMap = new ArrayList<Map<String,Object>>();
		
		for(User u : users) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", u.getId().trim());
			map.put("loginName", u.getLoginName());
			map.put("firstName", u.getFirstName());
			map.put("lastName", u.getLastName());
			map.put("email", u.getEmail().trim());
			map.put("phoneNumber", u.getPhoneNumber());
			map.put("dayOfBirth", u.getDayOfBirth());
			map.put("gender", u.getGender());
			
			Map<String, Object> address3 = new HashMap<String, Object>();
			address3.put("id", u.getAddress().getId().trim());
			address3.put("value", u.getAddress().getValue().trim());
			address3.put("level", u.getAddress().getLevel());
			address3.put("parentId", u.getAddress().getParentId().trim());
			
			map.put("address", address3);
			
			map.put("addressDetail", u.getAddressDetail());
			map.put("type", u.getType());
			map.put("createAt", u.getCreateAt());
			map.put("updateAt", u.getUpdateAt());
			
			lstMap.add(map);
		}
		
		return new ResponseMapArray(0, "Successfully", lstMap);
		
	}
	
	@GetMapping("/{id}")
    public Map<String, Object> getUserById(@RequestHeader("token") String token, @PathVariable(value = "id") String userId)
        throws ResourceNotFoundException {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
		
        User u = userRepository.findById(userId).orElse(null);
        
        if (u == null) {
			return new ResponseMap(1, "Not Found", null);
		}
        
        Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", u.getId().trim());
		map.put("loginName", u.getLoginName());
		map.put("firstName", u.getFirstName());
		map.put("lastName", u.getLastName());
		map.put("email", u.getEmail().trim());
		map.put("phoneNumber", u.getPhoneNumber());
		map.put("dayOfBirth", u.getDayOfBirth());
		map.put("gender", u.getGender());
		
		Map<String, Object> address3 = new HashMap<String, Object>();
		address3.put("id", u.getAddress().getId().trim());
		address3.put("value", u.getAddress().getValue().trim());
		address3.put("level", u.getAddress().getLevel());
		address3.put("parentId", u.getAddress().getParentId().trim());
		
		map.put("address", address3);
		
		map.put("addressDetail", u.getAddressDetail());
		map.put("type", u.getType());
		map.put("createAt", u.getCreateAt());
		map.put("updateAt", u.getUpdateAt());
        return new ResponseMap(0, "Successfully", map);
		
		
    }
	
//	@GetMapping("/login_name/{login_name}")
//	public MyHttpResponse getUserByLoginName(@RequestHeader("token") String token, @PathVariable(value = "login_name") String loginName)
//        throws ResourceNotFoundException {
//		
//		User user = authService.verifyToken(token);
//		if (user == null) {
//			return new ResponseMapArray(401, "Authentication failed", null);
//		}
//		
//        User user = userRepository.findByLoginName(loginName);
//        
//        if (user == null) {
//			return new MyHttpResponse(404, "không tìm thấy", null);
//		}
//        return new MyHttpResponse(200, "Tìm thành công", user);
//    }
//	
//	@GetMapping("/phoneNumber/{phone_number}")
//	public MyHttpResponse getUserByPhoneNumber(@RequestHeader("token") String token, @PathVariable(value = "phone_number") String phoneNumber)
//        throws ResourceNotFoundException {
//		
//		User user = authService.verifyToken(token);
//		if (user == null) {
//			return new ResponseMapArray(401, "Authentication failed", null);
//		}
//		
//        User user = userRepository.findByPhoneNumber(phoneNumber);
//        if (user == null) {
//			return new MyHttpResponse(404, "không tìm thấy", null);
//		}
//        return new MyHttpResponse(200, "Tìm thành công", user);
//	}
	
	@PutMapping("/")
    public Map<String, Object> update(@RequestHeader("token") String token, @Validated @RequestBody Map<String, String> info) {
    	
		User user1 = authService.verifyToken(token);
		if (user1 == null) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
		
		User user = new User();
        user.setId(info.get("id").toString());
        user.setFirstName(info.get("firstName").toString());
        user.setLoginName(info.get("loginName").toString());
        user.setLastName(info.get("lastName").toString());
        user.setPassword(info.get("password").toString());
        user.setPhoneNumber(info.get("phoneNumber").toString());
        user.setAddressDetail(info.get("addressDetail").toString());
        user.setType(Integer.parseInt(info.get("type").toString()));
        user.setDayOfBirth(Date.valueOf(info.get("dayOfBirth").toString()));
        user.setEmail(info.get("email").toString());
        user.setGender(Integer.parseInt(info.get("gender").toString()));
        user.setAddress(new StructureValue(info.get("address").toString()));
        
        User user2 = userRepository.findById(user.getId().trim()).orElse(null);
        
        if (user2 == null) {
        	return new ResponseMap(1, "Not Found User Id", null);
		}
        
        user2 = userRepository.findByLoginName(user.getLoginName().trim());
        if (user2 == null) {
			return new ResponseMap(2, "Not Found User Login Name", null);
		}
        user2 = userRepository.findByPhoneNumber(user.getPhoneNumber().trim());
        if (user2 == null) {
			return new ResponseMap(3, "Not Found Phone Number", null);
		}
        
		user2 = userRepository.save(user);
		
		if (user2 == null) {
    		return new ResponseMap(4, "Update failed", null);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", user2.getId().trim());
		map.put("loginName", user2.getLoginName());
		map.put("firstName", user2.getFirstName());
		map.put("lastName", user2.getLastName());
		map.put("email", user2.getEmail().trim());
		map.put("phoneNumber", user2.getPhoneNumber());
		map.put("dayOfBirth", user2.getDayOfBirth());
		map.put("gender", user2.getGender());
		
		Map<String, Object> address3 = new HashMap<String, Object>();
		address3.put("id", user2.getAddress().getId().trim());
		address3.put("value", user2.getAddress().getValue().trim());
		address3.put("level", user2.getAddress().getLevel());
		address3.put("parentId", user2.getAddress().getParentId().trim());
		
		map.put("address", address3);
		
		map.put("addressDetail", user2.getAddressDetail());
		map.put("type", user2.getType());
		map.put("createAt", user2.getCreateAt());
		map.put("updateAt", user2.getUpdateAt());
		
        return new ResponseMap(0, "Update Successfully", map);
    }
	
}
