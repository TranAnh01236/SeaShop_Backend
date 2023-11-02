package org.trananh.shoppingappbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.trananh.shoppingappbackend.model.PriceHeader;

public interface PriceHeaderRepository extends JpaRepository<PriceHeader, String>{
	@Query(value = "select * from price_headers where status = 1 order by start_date DESC", nativeQuery=true)
	List<PriceHeader> findAllStatusTrue();
}
