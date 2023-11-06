package org.trananh.shoppingappbackend.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.trananh.shoppingappbackend.model.PromotionDetail;
import org.trananh.shoppingappbackend.model.PromotionHeader;
import org.trananh.shoppingappbackend.model.PromotionLine;
import org.trananh.shoppingappbackend.model.UnitOfMeasure;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.PromotionDetailRepository;
import org.trananh.shoppingappbackend.repository.PromotionHeaderRepository;
import org.trananh.shoppingappbackend.repository.PromotionLineRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.Constants;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponse;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;

@RestController
@RequestMapping("/promotion_header")
public class PromotionHeaderController {
	@Autowired(required = true)
	private PromotionHeaderRepository promotionHeaderRepository;
	@Autowired(required = true)
	private PromotionLineRepository promotionLineRepository;
	@Autowired(required = true)
	private PromotionDetailRepository promotionDetailRepository;
	@Autowired(required = true)
	private AuthService authService;
	
	@GetMapping("/")
	public Map<String, Object> getAll() {
		List<PromotionHeader> promotionHeaders = promotionHeaderRepository.findAll();
		
		return null;
			
	}
	
	@GetMapping("/{id}")
    public MyHttpResponse getById(@PathVariable(value = "id") String id)
        throws ResourceNotFoundException {
        PromotionHeader promotionHeader = promotionHeaderRepository.findById(id).orElse(null);
        if (promotionHeader == null) {
			return new MyHttpResponse(404, "không tìm thấy", null);
		}
        return new MyHttpResponse(200, "Tìm thành công", promotionHeader);
    }
	
	
	@PostMapping("/")
    public Map<String, Object> create(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
    	
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		updatePromotion();
		
    	PromotionHeader promotionHeader = new PromotionHeader();
    	promotionHeader.setName(map.get("name").toString());
    	promotionHeader.setDescription(map.get("description").toString());
    	promotionHeader.setStartDate(Date.valueOf(map.get("startDate").toString()));
    	promotionHeader.setEndDate(Date.valueOf(map.get("endDate").toString()));
    	promotionHeader.setCreateUser(user);
    	
    	if (promotionHeader.getStartDate() != null 
				&& promotionHeader.getEndDate() != null 
				&& Date.valueOf(LocalDate.now()).compareTo(promotionHeader.getStartDate()) >= 0 
				&& Date.valueOf(LocalDate.now()).compareTo(promotionHeader.getEndDate()) < 0 ) {
    		promotionHeader.setStatus(1);
		}else {
			promotionHeader.setStatus(0);
		}
    	
    	List<Map<String, Object>> mapLines = Constants.gson.fromJson(Constants.gson.toJson(map.get("promotionLines")), ArrayList.class);
    	if (mapLines == null) {
			return new ResponseMap(1, "Failed: Not Found Promotion Lines", null);
		}
    	
    	PromotionHeader promotionHeader1 = promotionHeaderRepository.save(promotionHeader);
    	
    	if (promotionHeader1 == null) {
    		return new ResponseMap(1, "Failed", null);
		}
    	
    	for(Map<String, Object> m : mapLines) {
    		PromotionLine line = new PromotionLine();
    		line.setType((int)Double.parseDouble(m.get("type").toString()));
    		line.setPromotionHeader(promotionHeader1);
    		
    		PromotionLine line1 = promotionLineRepository.save(line);
    		
    		if (line1 == null) {
    			return new ResponseMap(1, "Failed", null);
			}
    		
    		List<Map<String, Object>> mapDetails = Constants.gson.fromJson(Constants.gson.toJson(m.get("promotionDetails")), ArrayList.class);
    		if (line1.getType() == 0) {
				for(Map<String, Object> m1 : mapDetails) {
					PromotionDetail detail = new PromotionDetail();
					detail.setUnitOfMeasureId((int)Double.parseDouble(m1.get("unitOfMeasureId").toString()));
					detail.setPurchaseValue(Double.parseDouble(m1.get("purchaseValue").toString()));
					detail.setPromotionValue(Double.parseDouble(m1.get("promotionValue").toString()));
					detail.setMaxValue(Double.parseDouble(m1.get("maxValue").toString()));
					detail.setPromotionLine(line1);
					
					System.out.println(detail.toString());
					
					PromotionDetail detail1 = promotionDetailRepository.save(detail);
					
					if (detail1 == null) {
						return new ResponseMap(1, "Failed", null);
					}
					
				}
			}
    		if (line1.getType() == 1) {
    			for(Map<String, Object> m1 : mapDetails) {
					PromotionDetail detail = new PromotionDetail();
					detail.setPurchaseValue(Double.parseDouble(m1.get("purchaseValue").toString()));
					detail.setPromotionValue(Double.parseDouble(m1.get("promotionValue").toString()));
					detail.setMaxValue(Double.parseDouble(m1.get("maxValue").toString()));
					detail.setPromotionLine(line1);
					
					PromotionDetail detail1 = promotionDetailRepository.save(detail);
					
					if (detail1 == null) {
						return new ResponseMap(1, "Failed", null);
					}
					
				}
			}
    		
    	}
    	
    	return new ResponseMap(0, "Successfully", null);
    }
	
	private void updatePromotion() {
		
		List<PromotionHeader> list = promotionHeaderRepository.findAll();
		
		for(PromotionHeader header : list) {
			if (header.getStartDate() != null 
					&& header.getEndDate() != null 
					&& Date.valueOf(LocalDate.now()).compareTo(header.getStartDate()) >= 0 
					&& Date.valueOf(LocalDate.now()).compareTo(header.getEndDate()) < 0 ) {
				header.setStatus(1);
				promotionHeaderRepository.save(header);
			}else {
				header.setStatus(0);
				promotionHeaderRepository.save(header);
			}
		}
		
	}
	
	
}
