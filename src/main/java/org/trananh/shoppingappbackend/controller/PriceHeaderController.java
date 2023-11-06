package org.trananh.shoppingappbackend.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
		
		updatePrice();
		
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
	
	@GetMapping("/detail/{id}")
    public Map<String, Object> getDetailById(@PathVariable(value = "id") String id)
        throws ResourceNotFoundException {
		
		updatePrice();
		
        PriceHeader priceHeader = priceHeaderRepository.findById(id).orElse(null);
        if (priceHeader == null) {
			new ResponseMap(1, "Not Found Id", null);
		}
        
        List<PriceDetail> lstPriceDetails = priceDetailRepository.findByHeaderId(id);
        
        if (lstPriceDetails == null) {
        	lstPriceDetails = new ArrayList<PriceDetail>();
        }
        
        Map<String, Object> rsMap = new HashMap<String, Object>();
        
        rsMap.put("id", priceHeader.getId().trim());
        rsMap.put("name", priceHeader.getName().trim());
        rsMap.put("startDate", priceHeader.getStartDate());
        rsMap.put("endDate", priceHeader.getEndDate());
		rsMap.put("status", String.valueOf(priceHeader.getStatus()));
		rsMap.put("description", priceHeader.getDescription().trim());
		
		List<Map<String, Object>> lstMapDetails = new ArrayList<Map<String,Object>>();
		
		for(PriceDetail p : lstPriceDetails) {
			
			Map<String, Object> m = new HashMap<String, Object>();
			
			m.put("priceHeaderId", p.getPriceHeader().getId().trim());
			m.put("unitOfMeasureId", p.getUnitOfMeasure().getId());
			m.put("price", p.getPrice());
			
			lstMapDetails.add(m);
			
		}
		
		rsMap.put("priceDetails", lstMapDetails);
		
		return new ResponseMap(0, "Successfully", rsMap);
        
    }
	
	@PostMapping("/")
    public Map<String, Object> create(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		PriceHeader header = new PriceHeader();
		header.setName(map.get("name").toString().trim());
		header.setDescription(map.get("description").toString().trim());
		header.setStartDate(Date.valueOf(map.get("startDate").toString()));
		header.setEndDate(Date.valueOf(map.get("endDate").toString()));
		header.setCreateUser(user);
		
		List<PriceDetail> priceDetails = new ArrayList<PriceDetail>();
		String json = Constants.gson.toJson(map.get("priceDetails"));
		List<Map<String, Object>> lstDetailsMap = Constants.gson.fromJson(json, ArrayList.class);
		for(Map<String, Object> m : lstDetailsMap) {
			
			PriceDetail p = new PriceDetail();
			p.setPrice(Double.parseDouble(m.get("price").toString()));
			p.setUnitOfMeasure(new UnitOfMeasure((int)Double.parseDouble(m.get("unitOfMeasureId").toString())));
			p.setPriceHeader(header);
			
			priceDetails.add(p);
		}
		
		if (header.getStartDate() != null 
				&& header.getEndDate() != null 
				&& Date.valueOf(LocalDate.now()).compareTo(header.getStartDate()) >= 0 
				&& Date.valueOf(LocalDate.now()).compareTo(header.getEndDate()) < 0 ) {
			header.setStatus(1);
		}else {
			header.setStatus(0);
		}
		
		PriceHeader priceHeader = priceHeaderRepository.save(header);
		if (priceHeader == null) {
			return new ResponseMap(1, "Failed", null);
		}
		for(PriceDetail detail : priceDetails) {
			priceDetailRepository.save(detail);
		}
		
        
        List<PriceDetail> lstPriceDetails = priceDetailRepository.findByHeaderId(priceHeader.getId().trim());
        
        if (lstPriceDetails == null) {
        	lstPriceDetails = new ArrayList<PriceDetail>();
        }
        
        Map<String, Object> rsMap = new HashMap<String, Object>();
        
        rsMap.put("id", priceHeader.getId().trim());
        rsMap.put("name", priceHeader.getName().trim());
        rsMap.put("startDate", priceHeader.getStartDate());
        rsMap.put("endDate", priceHeader.getEndDate());
		rsMap.put("status", String.valueOf(priceHeader.getStatus()));
		rsMap.put("description", priceHeader.getDescription().trim());
		
		List<Map<String, Object>> lstMapDetails = new ArrayList<Map<String,Object>>();
		
		for(PriceDetail p : lstPriceDetails) {
			
			Map<String, Object> m = new HashMap<String, Object>();
			
			m.put("priceHeaderId", p.getPriceHeader().getId().trim());
			m.put("unitOfMeasureId", p.getUnitOfMeasure().getId());
			m.put("price", p.getPrice());
			
			lstMapDetails.add(m);
			
		}
		
		rsMap.put("priceDetails", lstMapDetails);
		
		return new ResponseMap(0, "Successfully", rsMap);
		
    }
	
	private void updatePrice() {
		
		List<PriceHeader> list = priceHeaderRepository.findAll();
		
		for(PriceHeader header : list) {
			if (header.getStartDate() != null 
					&& header.getEndDate() != null 
					&& Date.valueOf(LocalDate.now()).compareTo(header.getStartDate()) >= 0 
					&& Date.valueOf(LocalDate.now()).compareTo(header.getEndDate()) < 0 ) {
				header.setStatus(1);
				priceHeaderRepository.save(header);
			}else {
				header.setStatus(0);
				priceHeaderRepository.save(header);
			}
		}
		
	}
	
}
