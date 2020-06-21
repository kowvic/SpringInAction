package com.subway.board.controller;

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

import com.subway.board.domain.Ingredient;
import com.subway.board.domain.SandWich;
import com.subway.data.IngredientRepository;
import com.subway.data.SandWichRepository;
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
	
	@Autowired
	public DesignSandWichController(IngredientRepository ingredientRepo, SandWichRepository sandRepo) {
		this.ingredientRepo = ingredientRepo;
		this.sandRepo = sandRepo;
	}
	
	@GetMapping	// HTTP GET 요청을 처리한다. "/design"의 요청을 수신할 때 그 요청을 처리하기 위해 매소드를 호출
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		
		//findAll()로 찾은 Iterable<Ingredient> 컬렉션 안의 값들(ingredient들)을 하나씩 ingredients에 추가한다. 
		ingredientRepo.findAll().forEach(i->ingredients.add(i));
		
		//열거된 모든 원소를 배열에 담아 순서대로 반환(BREAD, PROTEIN, VEGGIES, CHEESE, SAUCE)
		Type[] types = Ingredient.Type.values();	
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),	//model 객체에 속성 추가(Type값, ingredients값)
					filterByType(ingredients, type));	//식자제의 유형(Type.ooo)을 List에서 필터링하면
		}
		
		model.addAttribute("sandwich", new SandWich());
		
		return "design";	//모델 데이터를 브라우저에 나타내는 뷰의 논리적인 이름
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {	
		return ingredients
				.stream()//스트림으로 배열의 값을 필터링 할 것이다.
				.filter(x->x.getType().equals(type))//람다표현식. ingredients에서 가져온 ingredient들의 type이 매개변수 type과 같은 경우를 찾는다. 
				.collect(Collectors.toList());//리스트로 결과를 가져온다.
		
	}
	
	@ModelAttribute(name="order")
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name="sandwich")
	public SandWich sandwich() {
		return new SandWich();
	}
	
	
	@PostMapping	//design.html의 form에서 action이 없이 post 요청을 시도하면 기존의 get요청과 같은 경로로(/design) post요청을 전송한다.
	public String processDesign(@Valid SandWich design, Errors errors, @ModelAttribute Order order) {
		
		if(errors.hasErrors()) {
			return "design";
		}
		
		SandWich saved = sandRepo.save(design);
		order.addDesign(saved);
		return "redirect:/orders/current";
	}
}
