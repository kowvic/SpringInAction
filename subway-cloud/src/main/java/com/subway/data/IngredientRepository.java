package com.subway.data;

import com.subway.board.domain.Ingredient;

public interface IngredientRepository {
	Iterable<Ingredient> findAll();
	Ingredient findById(String id);
	Ingredient save(Ingredient ingredient);
}
