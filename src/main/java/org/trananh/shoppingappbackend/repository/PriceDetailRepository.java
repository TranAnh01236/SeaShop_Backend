package org.trananh.shoppingappbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.trananh.shoppingappbackend.model.PriceDetail;

public interface PriceDetailRepository extends JpaRepository<PriceDetail, String>{
	@Query(value = "select * from price_details where price_header_id = ?1", nativeQuery=true)
	List<PriceDetail> findByHeaderId(String id);
}
