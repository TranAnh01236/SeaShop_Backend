package org.trananh.shoppingappbackend.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trananh.shoppingappbackend.exception.ResourceNotFoundException;
import org.trananh.shoppingappbackend.model.BaseUnitOfMeasure;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.BaseUnitOfMeasureRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;
import org.trananh.shoppingappbackend.ultilities.ResponseMapArray;

@RestController
@RequestMapping("/base_unit_of_measure")
public class BaseUnitOfMeasureController {
	@Autowired(required = true)
	private AuthService authService;
	@Autowired(required = true)
	private BaseUnitOfMeasureRepository baseUnitOfMeasureRepository;
	
	@GetMapping("/")
	public ResponseMapArray getAll(@RequestHeader("token") String token) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
		
		List<BaseUnitOfMeasure> donvicoban = baseUnitOfMeasureRepository.findAll();
		
		if (donvicoban!= null) {
			
			ArrayList<Map<String, Object>> lstMap = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < donvicoban.size(); i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				
				m.put("id", donvicoban.get(i).getId().trim());
				m.put("value", donvicoban.get(i).getValue().trim());
				
				lstMap.add(m);
				
			}
			
			return new ResponseMapArray(0, "Successfully", lstMap);
		}
		return new ResponseMapArray(1, "Không tìm thấy", null);
	}
	
	@GetMapping("/{id}")
    public ResponseMap getById(@RequestHeader("token") String token, @PathVariable(value = "id") String id)
        throws ResourceNotFoundException {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
        BaseUnitOfMeasure donvicoban = baseUnitOfMeasureRepository.findById(id).orElse(null);
        if (donvicoban == null) {
			return new ResponseMap(1, "Not found", null);
		}
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", donvicoban.getId().trim());
        map.put("value", donvicoban.getValue().trim());
        
        return new ResponseMap(200, "Successfully", map);
    }
	
	@PostMapping("/")
    public ResponseMap create(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
    	
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure();
		
		baseUnitOfMeasure.setId(map.get("id").toString().trim());
		baseUnitOfMeasure.setValue(map.get("value").toString().trim());
		
		BaseUnitOfMeasure baseUnitOfMeasure2 = baseUnitOfMeasureRepository.findById(baseUnitOfMeasure.getId().trim()).orElse(null);
		
		if (baseUnitOfMeasure2 != null) {
			return new ResponseMap(2, "Id already in use", null);
		}
		
    	BaseUnitOfMeasure baseUnitOfMeasure1 = baseUnitOfMeasureRepository.save(baseUnitOfMeasure);
    	
    	if (baseUnitOfMeasure1 == null) {
    		return new ResponseMap(1, "Failed", null);
		}
		
    	Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", baseUnitOfMeasure1.getId().trim());
        map1.put("value", baseUnitOfMeasure1.getValue().trim());
    	
        return new ResponseMap(0, "Successfully" , map1);
    }
	
	@PutMapping("/")
	public ResponseMap update(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		

		BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure();
		
		baseUnitOfMeasure.setId(map.get("id").toString().trim());
		baseUnitOfMeasure.setValue(map.get("value").toString().trim());
		
		BaseUnitOfMeasure baseUnitOfMeasure1 = baseUnitOfMeasureRepository.save(baseUnitOfMeasure);
    	
    	if (baseUnitOfMeasure1 == null) {
    		return new ResponseMap(1, "Failed", null);
		}
		
    	Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", baseUnitOfMeasure1.getId().trim());
        map1.put("value", baseUnitOfMeasure1.getValue().trim());
    	
        return new ResponseMap(0, "Successfully" , map1);
	}
	
	@DeleteMapping("/{id}")
	public ResponseMap delete(@RequestHeader("token") String token, @PathVariable(value = "id") String id) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		BaseUnitOfMeasure baseUnitOfMeasure = baseUnitOfMeasureRepository.findById(id).orElse(null);
		if (baseUnitOfMeasure == null) {
			return new ResponseMap(1, "Not found id", null);
		}
		
		baseUnitOfMeasureRepository.delete(baseUnitOfMeasure);
		return new ResponseMap(0, "Successfully", null);
		
	}
}
