package com.subway.board.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.subway.User;
import com.subway.board.domain.Ingredient;
import com.subway.board.domain.SandWich;
import com.subway.data.IngredientRepository;
import com.subway.data.SandWichRepository;
import com.subway.data.UserRepository;
import com.subway.board.domain.Ingredient.Type;
import com.subway.board.domain.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j	//컴파일 시 lombok이 제공되며 이 클래스에 Logger를 생성한다.
@Controller	//컨트롤러
@RequestMapping("/design")	//다목적 요청을 처리하는 애노테이션. 클래스 수준에서 적용될 떄는 해당 컨트롤러가 처리하는 요청의 종류를 나타낸다.
@SessionAttributes("order")
public class DesignSandWichController {
	
	private final IngredientRepository ingredientRepo;
	private SandWichRepository sandRepo;
	private UserRepository userRepo;
	
	@Autowired
	public DesignSandWichController(IngredientRepository ingredientRepo, SandWichRepository sandRepo, UserRepository userRepo) {
		this.ingredientRepo = ingredientRepo;
		this.sandRepo = sandRepo;
		this.userRepo = userRepo;
	}
	
	
	@GetMapping
	public String showDesignForm(Model model, Principal principal) {
		List<Ingredient> ingredients = new ArrayList<>();
		 
		ingredientRepo.findAll().forEach(i->ingredients.add(i));
		
	
		Type[] types = Ingredient.Type.values();	
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),
					filterByType(ingredients, type));
		}
		
		String username = principal.getName();
		User user = userRepo.findByUsername(username);
		model.addAttribute("user", user);
		
		return "design";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {	
		return ingredients
				.stream()
				.filter(x->x.getType().equals(type)) 
				.collect(Collectors.toList());
		
	}
	
	@ModelAttribute(name="order")
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name="sandwich")
	public SandWich sandwich() {
		return new SandWich();
	}
	
	
	@PostMapping
	public String processDesign(@Valid SandWich design, Errors errors, @ModelAttribute Order order) {
		
		if(errors.hasErrors()) {
			return "design";
		}
		
		SandWich saved = sandRepo.save(design);
		order.addDesign(saved);
		return "redirect:/orders/current";
	}
}
