package com.subway.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.subway.board.domain.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orderForm";		//3장에서 주문한 샌드위치 객체들을 db에 저장하도록 변경할 것이다.
	}
	
	@PostMapping
	public String processOrder(Order order) {
		log.info("Order submited:" + order);
		return "redirect:/";
	}
}
