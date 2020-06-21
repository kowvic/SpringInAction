package com.subway.data;

import com.subway.board.domain.Order;

public interface OrderRepository {
	Order save(Order order);
}
