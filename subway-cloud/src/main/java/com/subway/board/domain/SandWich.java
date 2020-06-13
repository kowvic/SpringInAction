package com.subway.board.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SandWich {
	
	@NotNull
	@Size(min=5, message="name must be at least 5 charcters long")
	private String name;
	
	@NotNull
	@Size(min=5, message="you must choose at least 1 ingredient")
	private List<String> ingredients;
}
