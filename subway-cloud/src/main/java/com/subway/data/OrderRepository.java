package com.subway.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.subway.board.domain.Order;

public interface OrderRepository 
	extends CrudRepository<Order, Long>{
	
	//스프링 데이터는 DSL(Domain Sepcific Language)을 정의한다.
	//특정 zip(우편번호)로 배달되는 모든 주문 데이터를 db에서 가져온다.
	List<Order> findByDeliveryZip(String deliveryZip);
	
	//지정된 일자 내에서 특정 zip코드로 배달된 주문을 찾는 경우
	List<Order>readOrdersByDeliveryZipAndPlacedAtBetween(
			String deliveryZip,
			Date startDate, 
			Date endDate);
	
	//배달된 도시들 중 '시에틀'에 배달된 모든 주문을 요청한다. 
	//에러표시, 왜?
//	@Query("Order o where o.deliveryCity='Seattle'")
//	List<Order> readOrdersByDeliveredInSeattle();
	
}
