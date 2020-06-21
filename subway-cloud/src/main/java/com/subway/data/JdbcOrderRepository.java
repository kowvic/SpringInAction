package com.subway.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subway.board.domain.Order;
import com.subway.board.domain.SandWich;

@Repository
public class JdbcOrderRepository implements OrderRepository{
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderSandWichInserter;
	private ObjectMapper objectMapper;

	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbc) {
		this.orderInserter = new SimpleJdbcInsert(jdbc)
				.withTableName("SandWich_Order")
				.usingGeneratedKeyColumns("id");
		
		this.orderSandWichInserter = new SimpleJdbcInsert(jdbc)
				.withTableName("SandWich_Order_SandWiches");
		
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Order save(Order order) {
		order.setPlacedAt(new Date());
		long orderId = saveOrderDetails(order);
		order.setId(orderId);
		List<SandWich> sandwiches = order.getSandwiches();
		
		for(SandWich sandwich : sandwiches) {
			saveSandWichToOrder(sandwich, orderId);
		}
		return order;
	}

	private long saveOrderDetails(Order order) {
		@SuppressWarnings("unchecked")
		Map<String, Object> values = 
			objectMapper.convertValue(order,  Map.class);
		values.put("placedAt", order.getPlacedAt());
		
		long orderId = 
				orderInserter
				.executeAndReturnKey(values)
				.longValue();
		return orderId;
	}

	private void saveSandWichToOrder(SandWich sandwich, long orderId) {
		Map<String, Object> values = new HashMap<>();
		values.put("sandOrder", orderId);
		values.put("sand", sandwich.getId());
		orderSandWichInserter.execute(values);
		
	}
}
