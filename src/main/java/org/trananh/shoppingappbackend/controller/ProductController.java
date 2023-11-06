package org.trananh.shoppingappbackend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.trananh.shoppingappbackend.model.PriceDetail;
import org.trananh.shoppingappbackend.model.PriceHeader;
import org.trananh.shoppingappbackend.model.Product;
import org.trananh.shoppingappbackend.model.StructureValue;
import org.trananh.shoppingappbackend.model.UnitOfMeasure;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.PriceDetailRepository;
import org.trananh.shoppingappbackend.repository.PriceHeaderRepository;
import org.trananh.shoppingappbackend.repository.ProductRepository;
import org.trananh.shoppingappbackend.repository.StructureValueRepository;
import org.trananh.shoppingappbackend.repository.UnitOfMeasureRepository;
import org.trananh.shoppingappbackend.service.AuthService;
import org.trananh.shoppingappbackend.ultilities.Constants;
import org.trananh.shoppingappbackend.ultilities.MyHttpResponseArray;
import org.trananh.shoppingappbackend.ultilities.ResponseMap;
import org.trananh.shoppingappbackend.ultilities.ResponseMapArray;

import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired(required = true)
	private ProductRepository productRepository;
	
	@Autowired(required = true)
	private UnitOfMeasureRepository unitOfMeasureRepository;

	@Autowired(required = true)
	private StructureValueRepository mStructureValueRepository;
	
	@Autowired(required = true)
	private PriceHeaderRepository mPriceHeaderRepository;
	
	@Autowired(required = true)
	private PriceDetailRepository mPriceDetailRepository;
	
	@Autowired(required = true)
	private AuthService authService;
	
	@GetMapping("/")
	public ResponseMapArray getAllproduct() {
		
		List<Product> products = productRepository.findAll();
		
		List<Map<String, Object>> lstUnit = unitOfMeasureRepository.findAllProduct();
		
		List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
		
		
		if (products!= null) {
			
			List<Map<String , Object>> lstMap = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> mapUnit : lstUnit) {
				
				for(Product pro : products) {
					if (mapUnit.get("product_id").toString().trim().equals(pro.getId().trim())) {
						Map<String, Object> m = new HashMap<String, Object>();
						
						m.put("id", pro.getId().toString().trim());
						m.put("name", pro.getName().toString().trim());
						m.put("description", pro.getDescription().toString().trim());
						m.put("imageUrl", pro.getImageUrl().toString().trim());
						m.put("category", pro.getCategory().getValue().toString().trim());
						m.put("baseUnitOfMeasureId", mapUnit.get("base_unit_of_measure_id"));
						m.put("baseOfUnitMeasureName", mapUnit.get("value"));
						m.put("unitOfMeasureId", mapUnit.get("id"));
						m.put("baseUnitOfMeasureImageUrl", mapUnit.get("image_url"));
						m.put("categoryId", pro.getCategory().getId());
						
						m.put("price", getPrice(priceDetails, priceHeaders, Integer.parseInt(mapUnit.get("id").toString())));
						
						lstMap.add(m);
					}
				}
				
			}
			
			return new ResponseMapArray(0, "Successfully", lstMap);
			
		}
		
		return new ResponseMapArray(1, "Failed", null);
	}
	
	@GetMapping("/{id}")
    public ResponseMapArray getProductById(@RequestHeader("token") String token, @PathVariable(value = "id") String gradeId)
        throws ResourceNotFoundException {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
		
		if (token == null || token.equals("")) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
        Product pro = productRepository.findById(gradeId).orElse(null);
        if (pro == null) {
			return new ResponseMapArray(1, "Not found", null);
		}
        
        List<Map<String , Object>> lstMap = new ArrayList<Map<String,Object>>();
        
        List<Map<String, Object>> lstUnit = unitOfMeasureRepository.findAllProductById(pro.getId().trim());
        
        List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
        
        for(Map<String, Object> mapUnit : lstUnit) {
        	
        	Map<String, Object> map = new HashMap<String, Object>();
        	
            map.put("id", pro.getId().toString().trim());
    		map.put("name", pro.getName().toString().trim());
    		map.put("description", pro.getDescription().toString().trim());
    		map.put("imageUrl", pro.getImageUrl().toString().trim());
    		map.put("category", pro.getCategory().getValue().toString().trim());
    		map.put("baseUnitOfMeasure", mapUnit.get("base_unit_of_measure_id"));
    		map.put("baseOfUnitMeasureName", mapUnit.get("value"));
    		map.put("unitOfMeasureId", mapUnit.get("id"));
    		
    		map.put("price", getPrice(priceDetails, priceHeaders, Integer.parseInt(mapUnit.get("id").toString())));
    		
    		lstMap.add(map);
        }
        
        return new ResponseMapArray(0, "Successfully", lstMap);
    }
	
	@GetMapping("/detail/{id}")
    public ResponseMap getDetailById(@PathVariable(value = "id") String gradeId) {
		
		Product product = productRepository.findById(gradeId).orElse(null);
		
		if (product == null) {
			return new ResponseMap(1, "Not Found", null);
		}
		
		List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", product.getId().toString().trim());
		map.put("name", product.getName().toString().trim());
		map.put("description", product.getDescription().toString().trim());
		map.put("imageUrl", product.getImageUrl().toString().trim());
		
		Map<String, Object> mapCategory = new HashMap<String, Object>();
		mapCategory.put("id", product.getCategory().getId());
		mapCategory.put("value", product.getCategory().getValue());
		
		map.put("category", mapCategory);
		
		List<UnitOfMeasure> unitOfMeasures = unitOfMeasureRepository.findByProductId(product.getId().trim());
		
		if (unitOfMeasures == null) {
			unitOfMeasures = new ArrayList<UnitOfMeasure>();
		}
		
		List<Map<String, Object>> lstMap = new ArrayList<Map<String,Object>>();
		
		for(UnitOfMeasure u : unitOfMeasures) {
			
			Map<String, Object> map1 = new HashMap<String, Object>();
			
			map1.put("id", u.getId());
			map1.put("value", u.getValue());
			map1.put("imageUrl", u.getImageUrl());
			map1.put("quantity", u.getQuantity());
			map1.put("baseOfUnitMeasure", u.getBaseUnitOfMeasure().getId().trim());
			map1.put("price", getPrice(priceDetails, priceHeaders, u.getId()));
			
			lstMap.add(map1);
		}
		
		map.put("unitOfMeasures", lstMap);
		
		return new ResponseMap(0, "Successfully", map);
	}
	
	@GetMapping("/find/{id}")
    public ResponseMapArray getProduct(@RequestHeader("token") String token, @PathVariable(value = "id") String gradeId)
        throws ResourceNotFoundException {
		if (token == null || token.equals("")) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
        Product pro = productRepository.findById(gradeId).orElse(null);
        if (pro == null) {
			return new ResponseMapArray(1, "Not found", null);
		}
        
        List<Map<String , Object>> lstMap = new ArrayList<Map<String,Object>>();
        
        List<Map<String, Object>> lstUnit = unitOfMeasureRepository.findAllProductById(pro.getId().trim());
        
        for(Map<String, Object> mapUnit : lstUnit) {
        	
        	Map<String, Object> map = new HashMap<String, Object>();
        	
            map.put("id", pro.getId().toString().trim());
    		map.put("name", pro.getName().toString().trim());
    		map.put("description", pro.getDescription().toString().trim());
    		map.put("imageUrl", pro.getImageUrl().toString().trim());
    		map.put("category", pro.getCategory().getValue().toString().trim());
    		map.put("baseUnitOfMeasure", mapUnit.get("base_unit_of_measure_id"));
    		map.put("baseOfUnitMeasureName", mapUnit.get("value"));
    		map.put("unitOfMeasureId", mapUnit.get("id"));
    		
    		lstMap.add(map);
        }
        
        return new ResponseMapArray(0, "Successfully", lstMap);
    }
	
	@GetMapping("/search/{keySearch}")
	public Map<String, Object> search(@PathVariable(value = "keySearch") String keySearch){
		
		List<Product> products = productRepository.findAll();
		
		List<Map<String, Object>> lstUnit = productRepository.fullTextSearch(keySearch);
		
		List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
		
		if (products!= null) {
			
			List<Map<String , Object>> lstMap = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> mapUnit : lstUnit) {
				
				for(Product pro : products) {
					if (mapUnit.get("product_id").toString().trim().equals(pro.getId().trim())) {
						Map<String, Object> m = new HashMap<String, Object>();
						
						m.put("id", pro.getId().toString().trim());
						m.put("name", pro.getName().toString().trim());
						m.put("description", pro.getDescription().toString().trim());
						m.put("imageUrl", pro.getImageUrl().toString().trim());
						m.put("category", pro.getCategory().getValue().toString().trim());
						m.put("baseUnitOfMeasureId", mapUnit.get("base_unit_of_measure_id"));
						m.put("baseOfUnitMeasureName", mapUnit.get("value"));
						m.put("unitOfMeasureId", mapUnit.get("id"));
						m.put("baseUnitOfMeasureImageUrl", mapUnit.get("image_url"));
						m.put("categoryId", pro.getCategory().getId());
						
						m.put("price", getPrice(priceDetails, priceHeaders, Integer.parseInt(mapUnit.get("id").toString())));
						
						lstMap.add(m);
					}
				}
				
			}
			
			return new ResponseMapArray(0, "Successfully", lstMap);
			
		}
		
		return new ResponseMapArray(1, "Failed", null);
	}
	
	@GetMapping("/category/{category}")
	public Map<String, Object> getByCategory(@PathVariable(value = "category") String structureValueId) {
		List<StructureValue> allCategory = mStructureValueRepository.findAllByTypeDESCLevel(2);
		StructureValue structureValue = mStructureValueRepository.findById(structureValueId).orElse(null);
		if (structureValue == null) {
			return new ResponseMapArray(1, "Not Found Category", null);
		}
		
		List<StructureValue> categories = new ArrayList<StructureValue>();
		
		int level = structureValue.getLevel() + 1;
		List<String> lstParentId = new ArrayList<String>();
		lstParentId.add(structureValue.getId().trim());
		categories.add(structureValue);
		
		System.out.println(allCategory.get(0).getLevel());
		
		while (level <= allCategory.get(0).getLevel()) {
			
			for(String parentId : lstParentId) {
				for(StructureValue t : allCategory) {
					if (t.getLevel() == level && t.getParentId().equals(parentId)) {
						categories.add(t);
					}
				}
			}
			
			lstParentId = new ArrayList<String>();
			for(StructureValue t : categories) {
				if (t.getLevel() == level) {
					lstParentId.add(t.getId().trim());
				}
			}
			System.out.println(level);
			level++;
		}
		
		for(StructureValue t : categories) {
			System.out.println(t.toString());
		}
		
		List<Product> products = new ArrayList<Product>();
		
		for (StructureValue t : categories) {
			List<Product> lst = productRepository.findByStructurevalue(t.getId());
			if (lst != null && lst.size() > 0) {
				for(Product p : lst) {
					products.add(p);
				}
			}
		}
		
//		return null;
		
//		List<Product> products = productRepository.findByStructurevalue(structureValueId);
//		
		List<Map<String, Object>> lstUnit = unitOfMeasureRepository.findAllProduct();
		
		List<PriceHeader> priceHeaders = mPriceHeaderRepository.findAllStatusTrue();
		List<PriceDetail> priceDetails = mPriceDetailRepository.findAll();
		
		if (products!= null) {
			
			List<Map<String , Object>> lstMap = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> mapUnit : lstUnit) {
				
				for(Product pro : products) {
					if (mapUnit.get("product_id").toString().trim().equals(pro.getId().trim())) {
						Map<String, Object> m = new HashMap<String, Object>();
						
						m.put("id", pro.getId().toString().trim());
						m.put("name", pro.getName().toString().trim());
						m.put("description", pro.getDescription().toString().trim());
						m.put("imageUrl", pro.getImageUrl().toString().trim());
						m.put("category", pro.getCategory().getValue().toString().trim());
						m.put("categoryId", pro.getCategory().getId().toString().trim());
						m.put("baseUnitOfMeasureId", mapUnit.get("base_unit_of_measure_id"));
						m.put("baseOfUnitMeasureName", mapUnit.get("value"));
						m.put("unitOfMeasureId", mapUnit.get("id"));
						m.put("baseUnitOfMeasureImageUrl", mapUnit.get("image_url"));
						m.put("price", getPrice(priceDetails, priceHeaders, Integer.parseInt(mapUnit.get("id").toString())));
						
						lstMap.add(m);
					}
				}
				
			}
			
			return new ResponseMapArray(0, "Successfully", lstMap);
			
		}
		
		return new ResponseMapArray(1, "Failed", null);
	}
	
	@PostMapping("/")
    public Map<String, Object> createProduct(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
    	
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMapArray(401, "Authentication failed", null);
		}
		
    	Product product = new Product();
    	product.setId(map.get("id").toString().trim());
    	product.setName(map.get("name").toString().trim());
    	product.setDescription(map.get("description").toString().trim());
    	product.setImageUrl(map.get("imageUrl").toString().trim());
    	product.setCategory(new StructureValue(map.get("category").toString().trim()));
    	product.setCreateUser(user);
    	String json = Constants.gson.toJson(map.get("unitOfMeasures"));
    	List<UnitOfMeasure> unitOfMeasures = Constants.gson.fromJson(json, new TypeToken<List<UnitOfMeasure>>(){}.getType());
    	
//    	System.out.println(product.toString());
//    	
//    	for(UnitOfMeasure u : unitOfMeasures) {
//    		System.out.println(u.toString());
//    	}
    	
    	Product product1 = productRepository.findById(product.getId().trim()).orElse(null);
    	if (product1 != null) {
			return new ResponseMap(1, "Id is already in use", null);
		}
    	
    	Product product2 = productRepository.save(product);
    	if (product2 == null) {
    		return new ResponseMap(2, "Failed", null);
		}
    	
    	for(UnitOfMeasure u : unitOfMeasures) {
    		u.setProduct(new Product(product2.getId()));
    		unitOfMeasureRepository.save(u);
    	}
    	
    	Map<String, Object> mapp = new HashMap<String, Object>();
		
		mapp.put("id", product2.getId().toString().trim());
		mapp.put("name", product2.getName().toString().trim());
		mapp.put("description", product2.getDescription().toString().trim());
		mapp.put("imageUrl", product2.getImageUrl().toString().trim());
		
		Map<String, Object> mapCategory = new HashMap<String, Object>();
		mapCategory.put("id", product2.getCategory().getId());
		mapCategory.put("value", product2.getCategory().getValue());
		
		mapp.put("category", mapCategory);
		
		List<UnitOfMeasure> unitOfMeasuress = unitOfMeasureRepository.findByProductId(product2.getId().trim());
		
		if (unitOfMeasuress == null) {
			unitOfMeasuress = new ArrayList<UnitOfMeasure>();
		}
		
		List<Map<String, Object>> lstMap = new ArrayList<Map<String,Object>>();
		
		for(UnitOfMeasure u : unitOfMeasuress) {
			
			Map<String, Object> map1 = new HashMap<String, Object>();
			
			map1.put("id", u.getId());
			map1.put("value", u.getValue());
			map1.put("imageUrl", u.getImageUrl());
			map1.put("quantity", u.getQuantity());
			map1.put("baseOfUnitMeasure", u.getBaseUnitOfMeasure().getId().trim());
			
			lstMap.add(map1);
		}
		
		mapp.put("unitOfMeasures", lstMap);
    	
    	return new ResponseMap(0, "Successfully", mapp);
    	
    }
	
	@PutMapping("/")
	public Map<String, Object> update(@RequestHeader("token") String token, @Validated @RequestBody Map<String, Object> map) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
    	Product product = new Product();
    	product.setId(map.get("id").toString().trim());
    	product.setName(map.get("name").toString().trim());
    	product.setDescription(map.get("description").toString().trim());
    	product.setImageUrl(map.get("imageUrl").toString().trim());
    	product.setCategory(new StructureValue(map.get("category").toString().trim()));
    	product.setCreateUser(user);
    	String json = Constants.gson.toJson(map.get("unitOfMeasures"));
    	List<UnitOfMeasure> unitOfMeasures = Constants.gson.fromJson(json, new TypeToken<List<UnitOfMeasure>>(){}.getType());
    	
//    	System.out.println(product.toString());
//    	
//    	for(UnitOfMeasure u : unitOfMeasures) {
//    		System.out.println(u.toString());
//    	}
    	
    	Product product1 = productRepository.findById(product.getId().trim()).orElse(null);
    	if (product1 == null) {
			return new ResponseMap(1, "Not Found Product Id", null);
		}
    	
    	List<UnitOfMeasure> lstUnit = unitOfMeasureRepository.findByProductId(product1.getId().trim());
    	if (lstUnit == null) {
			lstUnit = new ArrayList<UnitOfMeasure>();
		}
    	
    	for(UnitOfMeasure u : lstUnit) {
    		
    		for(int i = 0; i < unitOfMeasures.size(); i++) {
    			if (unitOfMeasures.get(i).getId() == u.getId()) {
					
				}
    		}
    		
    	}
    	
//    	for(UnitOfMeasure u : unitOfMeasures) {
//    		u.setProduct(new Product(product2.getId()));
//    		unitOfMeasureRepository.sa
//    	}
    			
		Product product2 = productRepository.save(product);
    	if (product2 == null) {
    		return new ResponseMap(2, "Failed", null);
		}		
    	
    	Map<String, Object> mapp = new HashMap<String, Object>();
		
		mapp.put("id", product2.getId().toString().trim());
		mapp.put("name", product2.getName().toString().trim());
		mapp.put("description", product2.getDescription().toString().trim());
		mapp.put("imageUrl", product2.getImageUrl().toString().trim());
		
		Map<String, Object> mapCategory = new HashMap<String, Object>();
		mapCategory.put("id", product2.getCategory().getId());
		mapCategory.put("value", product2.getCategory().getValue());
		
		mapp.put("category", mapCategory);
		
		List<UnitOfMeasure> unitOfMeasuress = unitOfMeasureRepository.findByProductId(product2.getId().trim());
		
		if (unitOfMeasuress == null) {
			unitOfMeasuress = new ArrayList<UnitOfMeasure>();
		}
		
		List<Map<String, Object>> lstMap = new ArrayList<Map<String,Object>>();
		
		for(UnitOfMeasure u : unitOfMeasuress) {
			
			Map<String, Object> map1 = new HashMap<String, Object>();
			
			map1.put("id", u.getId());
			map1.put("value", u.getValue());
			map1.put("imageUrl", u.getImageUrl());
			map1.put("quantity", u.getQuantity());
			map1.put("baseOfUnitMeasure", u.getBaseUnitOfMeasure().getId().trim());
			
			lstMap.add(map1);
		}
		
		mapp.put("unitOfMeasures", lstMap);
    	
    	return new ResponseMap(0, "Update Successfully", mapp);
    	
	}
	
	@DeleteMapping("/{id}")
	public ResponseMap delete(@RequestHeader("token") String token, @PathVariable(value = "id") String id) {
		
		User user = authService.verifyToken(token);
		if (user == null) {
			return new ResponseMap(401, "Authentication failed", null);
		}
		
		
		Product product = productRepository.findById(id).orElse(null);
		
		if (product == null) {
			return new ResponseMap(1, "Not found id", null);
		}
		
		List<UnitOfMeasure> lstUnit = unitOfMeasureRepository.findByProductId(product.getId().trim());
		
		for(UnitOfMeasure u : lstUnit) {
			unitOfMeasureRepository.delete(u);
		}
		
		productRepository.delete(product);
		
		return new ResponseMap(0, "Delete Successfully", null);
		
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
