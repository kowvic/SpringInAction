package com.subway.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.subway.board.domain.Ingredient;
import com.subway.data.IngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient>{
	private IngredientRepository ingredientRepo;
	
	@Autowired
	public IngredientByIdConverter(IngredientRepository ingredientRepo) {
		this.ingredientRepo = ingredientRepo;
	}
	
	@Override
	public Ingredient convert(String id) {
//		return ingredientRepo.findById(id);
		//<>안에 들어오는 값이 있는지 참거짓을 판별하는 Optional
		//isPresent()가 참이면 <>안에 있는 객체를 가져오고 거짓이면 null을 가져온다.
		Optional<Ingredient> optionalIngredient = ingredientRepo.findById(id);
		return optionalIngredient.isPresent()?
				optionalIngredient.get() : null;
	}
	
}
