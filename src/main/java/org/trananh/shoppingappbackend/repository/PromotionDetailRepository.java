package org.trananh.shoppingappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trananh.shoppingappbackend.model.PromotionDetail;

public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, String>{

	//select details.id, pro.name, units.value, details.purchase_value, details.promotion_value, details.max_value, headers.status
//	from promotion_details details
//	inner join promotion_lines lines
//	on details.promotion_line_id = lines.id
//	inner join promotion_headers headers
//	on lines.promotion_header_id = headers.id
//	inner join unit_of_measures units
//	on details.unit_of_measure_id = units.id
//	inner join base_unit_of_measures baseUnits
//	on units.base_unit_of_measure_id = baseUnits.id
//	inner join products pro
//	on units.product_id = pro.id
//	where headers.status = 1 and pro.id = 'PEPSI'
	
}
