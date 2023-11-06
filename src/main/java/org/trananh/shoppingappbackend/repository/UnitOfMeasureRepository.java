package org.trananh.shoppingappbackend.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.trananh.shoppingappbackend.model.UnitOfMeasure;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Integer>{
	@Query(value = "select top 1 * from unit_of_measures where product_id = ?1 and quantity = 1", nativeQuery=true)
	UnitOfMeasure findLowestByProductId(String productId);
	
	@Query(value = "select * from unit_of_measures where product_id = ?1", nativeQuery=true)
	List<UnitOfMeasure> findByProductId(String productId);
	
	@Query(value = "select u.id, u.quantity, u.product_id, u.base_unit_of_measure_id, b.value, u.image_url from unit_of_measures u inner join base_unit_of_measures b on u.base_unit_of_measure_id = b.id where u.quantity = 1", nativeQuery=true)
	List<Map<String, Object>> findAllProduct();
	
	@Query(value = "select u.id, u.product_id, u.base_unit_of_measure_id, b.value, u.image_url from unit_of_measures u inner join base_unit_of_measures b on u.base_unit_of_measure_id = b.id where u.quantity = 1 and u.product_id = ?1", nativeQuery=true)
	List<Map<String, Object>> findAllProductById(String productId);
}
