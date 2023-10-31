package org.trananh.shoppingappbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trananh.shoppingappbackend.exception.ResourceNotFoundException;
import org.trananh.shoppingappbackend.model.Cart;
import org.trananh.shoppingappbackend.repository.CartRepository;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponse;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponseArray;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired(required = true)
	private CartRepository cartRepository;
	
	@GetMapping("/")
	public MyHttpResponseArray getAll() {
		List<Cart> carts = cartRepository.findAll();
		
		ArrayList<Object> objects = new ArrayList<Object>();
		for(int i = 0; i < carts.size(); i++) {
			objects.add(carts.get(i));
		}
		if (carts!= null && carts.size()>0) {
			return new MyHttpResponseArray(200, "Tìm thành công", objects);
		}
		return new MyHttpResponseArray(404, "Không tìm thấy", null);
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
	
	@PostMapping("/")
    public MyHttpResponse create(@Validated @RequestBody Cart cart) {
    	
		Cart cart2 = cartRepository.findByUnitAndUser(cart.getUnitOfMeasure().getId(), cart.getUser().getId());
		
		if (cart2 == null) {
			Cart cart1 = cartRepository.save(cart);
	    	
	    	if (cart1 == null) {
	    		return new MyHttpResponse(404, "Thêm không thành công", null);
			}
			
	        return new MyHttpResponse(200, "Thêm thành công" , cart1);
		}else {
			cart2.setQuantity(cart2.getQuantity() + 1);
			
			Cart cart1 = cartRepository.save(cart2);
	    	
	    	if (cart1 == null) {
	    		return new MyHttpResponse(404, "Thêm không thành công", null);
			}
			
	        return new MyHttpResponse(200, "Thêm thành công" , cart1);
		}
		
    }
	
	@PutMapping("/")
	public MyHttpResponse update(@Validated @RequestBody Cart cart) {
	Cart cart1 = cartRepository.save(cart);
    	
    	if (cart1 == null) {
    		return new MyHttpResponse(404, "Cập nhật không thành công", null);
		}
		
        return new MyHttpResponse(200, "Cập nhật thành công" , cart1);
	}
	
	@DeleteMapping("/{id}")
	public MyHttpResponse delete(@PathVariable(value = "id") int cartId) {
		Cart cart = cartRepository.findById(cartId).orElse(null);
		if (cart == null) {
			return new MyHttpResponse(404, "Xóa không thành công", null);
		}
		
		cartRepository.delete(cart);
		
		return new MyHttpResponse(200, "Xóa thành công" , null);
	}
}
