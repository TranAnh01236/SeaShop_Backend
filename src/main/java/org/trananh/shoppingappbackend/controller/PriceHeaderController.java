package org.trananh.shoppingappbackend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trananh.shoppingappbackend.exception.ResourceNotFoundException;
import org.trananh.shoppingappbackend.model.PriceDetail;
import org.trananh.shoppingappbackend.model.PriceHeader;
import org.trananh.shoppingappbackend.model.UnitOfMeasure;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.PriceDetailRepository;
import org.trananh.shoppingappbackend.repository.PriceHeaderRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.Constants;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponse;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;
import org.trananh.shoppingappbackend.ultilities.ResponseMapArray;

import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/price_header")
public class PriceHeaderController {
	@Autowired(required = true)
	private PriceHeaderRepository priceHeaderRepository;
	
	@Autowired(required = true)
	private PriceDetailRepository priceDetailRepository;
	
	@Autowired
	private AuthService authService;
	
	@GetMapping("/")
	public Map<String, Object> getAll() {
		List<PriceHeader> priceHeaders = priceHeaderRepository.findAll();
		if (priceHeaders == null) {
			return new ResponseMapArray(1, "Failed", null);
		}
			
		List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		
		for(PriceHeader p : priceHeaders) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", p.getId().trim());
			map.put("name", p.getName().trim());
			map.put("startDate", p.getStartDate());
			map.put("endDate", p.getEndDate());
			map.put("status", String.valueOf(p.getStatus()));
			map.put("description", p.getDescription().trim());
			
			maps.add(map);
		}
		
		return new ResponseMapArray(0, "Successfully", maps);
	}
	
	@GetMapping("/{id}")
    public MyHttpResponse getById(@PathVariable(value = "id") String id)
        throws ResourceNotFoundException {
        PriceHeader priceHeader = priceHeaderRepository.findById(id).orElse(null);
        if (priceHeader == null) {
			return new MyHttpResponse(404, "không tìm thấy", null);
		}
        return new MyHttpResponse(200, "Tìm thành công", priceHeader);
    }
	
	
	@PostMapping("/")
    public ResponseMap create(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		PriceHeader priceHeader = new PriceHeader();
		priceHeader.setName(map.get("name").toString().trim());
		priceHeader.setDescription(map.get("description").toString().trim());
		priceHeader.setCreateUser(new User(user.getId().trim()));
		
		String json = Constants.gson.toJson(map.get("priceDetails"));
    	List<PriceDetail> priceDetails = Constants.gson.fromJson(json, new TypeToken<List<PriceDetail>>(){}.getType());
		
		System.out.println(priceHeader.toString());
		
		PriceHeader priceHeader1 = priceHeaderRepository.save(priceHeader);
		if (priceHeader1 == null) {
			return new ResponseMap(1, "Failed", null);
		}
		
		System.out.println(priceHeader1.getId().trim());
		
		for(PriceDetail p : priceDetails) {
			
			p.setPriceHeader(new PriceHeader(priceHeader1.getId().trim()));
			
			PriceDetail pd = priceDetailRepository.save(p);
			
			if (p == null) {
				return new ResponseMap(1, "Failed", null);
			}
			
		}
		
		return null;
		
    }
}
