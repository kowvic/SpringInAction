package com.subway.board.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Ingredient {
	private final String id;
	private final String name;
	private final Type type;
	
	public static enum Type {
		BREAD, PROTEIN, VEGGIES, CHEESE, SAUCE
	}
}
