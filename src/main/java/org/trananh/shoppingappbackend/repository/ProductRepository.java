package org.trananh.shoppingappbackend.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.trananh.shoppingappbackend.model.Product;

public interface ProductRepository extends JpaRepository<Product, String>{
	@Query(value = "select * from products where category_id = ?1", nativeQuery=true)
	List<Product> findByStructurevalue(String structureValueId);
	
	@Query(value = "select u.id, u.product_id, u.base_unit_of_measure_id, b.value, u.image_url, u.quantity "
			+ "from unit_of_measures u "
			+ "inner join base_unit_of_measures b "
			+ "on u.base_unit_of_measure_id = b.id "
			+ "inner join products p "
			+ "on u.product_id = p.id "
			+ "inner join structure_values s "
			+ "on p.category_id = s.id "
			+ "where u.quantity = 1 and "
			+ "(s.value like %:keySearch% or p.name like %:keySearch% or b.value like %:keySearch%)", nativeQuery = true)
	List<Map<String, Object>> fullTextSearch(String keySearch);
}
