package com.subway.data;

import org.springframework.data.repository.CrudRepository;

import com.subway.board.domain.Ingredient;

public interface IngredientRepository 
	extends CrudRepository<Ingredient, String>{//객체 타입, 객체 Id속성
//	Iterable<Ingredient> findAll();
//	Ingredient findById(String id);
//	Ingredient save(Ingredient ingredient);
}
