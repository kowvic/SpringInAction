package com.subway.board.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.subway.board.domain.Ingredient;
import com.subway.board.domain.SandWich;
import com.subway.board.domain.Ingredient.Type;

import lombok.extern.slf4j.Slf4j;

@Slf4j	//컴파일 시 lombok이 제공되며 이 클래스에 Logger를 생성한다.
@Controller	//컨트롤러
@RequestMapping("/design")	//다목적 요청을 처리하는 애노테이션. 클래스 수준에서 적용될 떄는 해당 컨트롤러가 처리하는 요청의 종류를 나타낸다.
public class DesignSandWichController {
	
	@GetMapping	// HTTP GET 요청을 처리한다. "/design"의 요청을 수신할 때 그 요청을 처리하기 위해 매소드를 호출
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = Arrays.asList(	//배열을 List처럼 사용, private 타입의 ArrayList리턴, 원소 추가 안됨(add()없음)
				new Ingredient("HOOT", "Honey Oat", Type.BREAD),
				new Ingredient("HEIT", "Hearty Italian", Type.BREAD),
				new Ingredient("WHIT", "White Bread", Type.BREAD),
				new Ingredient("MEAT", "Meat", Type.PROTEIN),
				new Ingredient("BEEF", "Beef", Type.PROTEIN),
				new Ingredient("CHIC", "Chicken", Type.PROTEIN),
				new Ingredient("LETT", "Letuce", Type.VEGGIES),
				new Ingredient("TOMA", "Honey Oat", Type.VEGGIES),
				new Ingredient("CUCU", "Cucumbers", Type.VEGGIES),
				new Ingredient("ONIO", "Onions", Type.VEGGIES),
				new Ingredient("AMER", "American Cheese", Type.CHEESE),
				new Ingredient("SHRE", "Shredded Cheese", Type.CHEESE),
				new Ingredient("MOZZ", "Mozzarella Cheese", Type.CHEESE),
				new Ingredient("SWON", "Sweet Onion", Type.SAUCE),
				new Ingredient("HOMU", "Honey Mustard", Type.SAUCE),
				new Ingredient("SWCH", "Sweet Chilli", Type.SAUCE)
				);
		
		Type[] types = Ingredient.Type.values();	//열거된 모든 원소를 배열에 담아 순서대로 반환(BREAD, PROTEIN, VEGGIES, CHEESE, SAUCE)
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),	//model 객체에 속성 추가(toString값, filterByType값)
					filterByType(ingredients, type));	//식자제의 유형(Type.ooo)을 List에서 필터링하면
		}
		
		model.addAttribute("sandwich", new SandWich());
		
		return "design";	//모델 데이터를 브라우저에 나타내는 뷰의 논리적인 이름
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {	
		return ingredients
				.stream()
				.filter(x->x.getType().equals(type))
				.collect(Collectors.toList());
	}
	
	@PostMapping	//design.html의 form에서 action이 없이 post 요청을 시도하면 기존의 get요청과 같은 경로로(/design) post요청을 전송한다.
	public String processDesign(SandWich design) {
		//샌드위치 디자인(재료선택내역)을 저장한다.
		//3장에서 할 것이다.
		log.info("processing design: "+design);
		return "redirect:/orders/current";
	}
}
