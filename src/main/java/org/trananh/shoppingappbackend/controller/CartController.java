package org.trananh.shoppingappbackend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import org.trananh.shoppingappbackend.model.Cart;
import org.trananh.shoppingappbackend.model.PriceDetail;
import org.trananh.shoppingappbackend.model.PriceHeader;
import org.trananh.shoppingappbackend.model.UnitOfMeasure;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.CartRepository;
import org.trananh.shoppingappbackend.repository.PriceDetailRepository;
import org.trananh.shoppingappbackend.repository.PriceHeaderRepository;
import org.trananh.shoppingappbackend.repository.UnitOfMeasureRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponse;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponseArray;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;
import org.trananh.shoppingappbackend.ultilities.ResponseMapArray;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired(required = true)
	private CartRepository cartRepository;
	@Autowired(required = true)
	private AuthService authService;
	@Autowired(required = true)
	private PriceHeaderRepository mPriceHeaderRepository;
	@Autowired(required = true)
	private PriceDetailRepository mPriceDetailRepository;
	@Autowired(required = true)
	private UnitOfMeasureRepository mUnitOfMeasureRepository;
	
	@GetMapping("/")
	public Map<String, Object> getAll(@RequestHeader("token") String token) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		List<Cart> carts = cartRepository.findByUserId(user.getId().trim());
		
		if (carts == null) {
			return new ResponseMapArray(1, "Failed", null);
		}
		
		List<Map<String, Object>> rsMap = new ArrayList<Map<String,Object>>();
		
		List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
		
		for(Cart cart : carts) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cartId", cart.getId());
			map.put("quantity", cart.getQuantity());
			map.put("unitOfMeasureId", cart.getUnitOfMeasure().getId());
			map.put("unitOfMeasureName", cart.getUnitOfMeasure().getValue());
			map.put("unitOfMeasureImageUrl", cart.getUnitOfMeasure().getImageUrl());
			map.put("baseUnitOfMeasureId", cart.getUnitOfMeasure().getBaseUnitOfMeasure().getId().trim());
			map.put("productId", cart.getUnitOfMeasure().getProduct().getId().trim());
			map.put("price", getPrice(priceDetails, priceHeaders, cart.getUnitOfMeasure().getId()));
			map.put("productName", cart.getUnitOfMeasure().getProduct().getName().trim());
			
			rsMap.add(map);
		}
		
		return new ResponseMapArray(0, "Successfully", rsMap);
		
	}
	
	@GetMapping("/{id}")
    public MyHttpResponse getById(@PathVariable(value = "id") int cartId)
        throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
			return new MyHttpResponse(404, "không tìm thấy", null);
		}
        return new MyHttpResponse(200, "Tìm thành công", cart);
    }
	
	@GetMapping("/user/{id}")
    public MyHttpResponseArray getByUserId(@PathVariable(value = "id") String userId)
        throws ResourceNotFoundException {
        List<Cart> carts = cartRepository.findByUserId(userId);
        ArrayList<Object> objects = new ArrayList<Object>();
		for(int i = 0; i < carts.size(); i++) {
			objects.add(carts.get(i));
		}
		if (carts!= null && carts.size()>0) {
			return new MyHttpResponseArray(200, "Tìm thành công", objects);
		}
		return new MyHttpResponseArray(404, "Không tìm thấy", null);
    }
	
	@PostMapping("/add/")
	public Map<String, Object> addQuantity(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map){
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		Cart cart = cartRepository.findById(Integer.parseInt(map.get("id").toString())).orElse(null);
		
		if (cart == null) {
			return new ResponseMap(1, "Not Found Id", null);
		}
		
		cart.setQuantity(cart.getQuantity() + 1);
		
		Cart cart1 = cartRepository.save(cart);
		
		if (cart1 == null) {
			return new ResponseMap(2, "Failed", null);
		}
		
		return getAll(token);
		
	}
	
	@PostMapping("/sub/")
	public Map<String, Object> subQuantity(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map){
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		Cart cart = cartRepository.findById(Integer.parseInt(map.get("id").toString())).orElse(null);
		
		if (cart == null) {
			return new ResponseMap(1, "Not Found Id", null);
		}
		
		if (cart.getQuantity() <= 1) {
			cartRepository.delete(cart);
		}else {
			cart.setQuantity(cart.getQuantity() - 1);
			Cart cart1 = cartRepository.save(cart);
			if (cart1 == null) {
				return new ResponseMap(2, "Failed", null);
			}
		}
		return getAll(token);
	}
	
	@PostMapping("/")
    public Map<String, Object> create(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
    	
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setUnitOfMeasure(new UnitOfMeasure(Integer.parseInt(map.get("unitOfMeasure").toString())));
		cart.setQuantity(1);
		
		Cart cart2 = new Cart();
		Cart cart1 = cartRepository.findByUnitAndUser(cart.getUnitOfMeasure().getId(), cart.getUser().getId());
		
		Map<String, Object> rsMap = new HashMap<String, Object>();
		
		if (cart1 != null) {
			cart1.setQuantity(cart1.getQuantity() + 1);
			
			cart2 = cartRepository.save(cart1);
		}else {
			cart2 = cartRepository.save(cart);
		}
		
		if (cart2 == null) {
			return new ResponseMap(1, "Failed", null);
		}
		
		List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
		
		UnitOfMeasure unit = mUnitOfMeasureRepository.findById(cart2.getUnitOfMeasure().getId()).orElse(null);
		
		rsMap.put("cartId", cart2.getId());
		rsMap.put("quantity", cart2.getQuantity());
		rsMap.put("unitOfMeasureId", unit.getId());
		rsMap.put("unitOfMeasureName", unit.getValue());
		rsMap.put("unitOfMeasureImageUrl", unit.getImageUrl());
		rsMap.put("baseUnitOfMeasureId", unit.getBaseUnitOfMeasure().getId().trim());
		rsMap.put("productId", unit.getProduct().getId().trim());
		rsMap.put("price", getPrice(priceDetails, priceHeaders, unit.getId()));
		rsMap.put("productName", unit.getProduct().getName().trim());
		
		return new ResponseMap(0, "Successfully", rsMap);
		
    }
	
	@DeleteMapping("/{id}")
	public Map<String, Object> delete(@RequestHeader("token") String token, @PathVariable(value = "id") int id){
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		Cart cart = cartRepository.findById(id).orElse(null);
		if (cart == null) {
			return new ResponseMap(1, "Not Found Id", null);
		}
		cartRepository.delete(cart);
		return new ResponseMap(0, "Successfully", null);
	}
	
	@DeleteMapping("/")
	public Map<String, Object> deleteAll(@RequestHeader("token") String token){
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		List<Cart> carts = cartRepository.findByUserId(user.getId().trim());
		if (carts == null) {
			return new ResponseMap(1, "Failed", null);
		}
		
		cartRepository.deleteAll(carts);
		
		return new ResponseMap(0, "Successfully", null);
	}
	
	private Double getPrice(List<PriceDetail> priceDetails, List<PriceHeader> priceHeaders, int unitId) {
		for(PriceHeader priceHeader: priceHeaders) {
			for(PriceDetail priceDetail : priceDetails) {
				if ( priceDetail.getPriceHeader().getId().trim().equals(priceHeader.getId()) 
						&& priceDetail.getUnitOfMeasure().getId() == unitId) {
					return priceDetail.getPrice();
				}
			}
		}
		return null;
	}
	
}
