package org.trananh.shoppingappbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.trananh.shoppingappbackend.model.Cart;
import org.trananh.shoppingappbackend.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer>{
	@Query(value = "select top 1 *  from carts where unit_of_measure_id = ?1 and user_id = ?2", nativeQuery=true)
	Cart findByUnitAndUser(int unitMeasureId, String userId);
	
	@Query(value = "select * from carts where user_id = ?1", nativeQuery=true)
	List<Cart> findByUserId(String userId);
	
	@Query(value = "select MAX(u.id) as max from unit_of_measures u", nativeQuery = true)
	int getMaxId();
}
