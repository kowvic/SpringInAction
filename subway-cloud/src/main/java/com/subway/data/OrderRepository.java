package com.subway.data;

import org.springframework.data.repository.CrudRepository;

import com.subway.board.domain.Order;

public interface OrderRepository 
	extends CrudRepository<Order, Long>{
//	Order save(Order order);
}
