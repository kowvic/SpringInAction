package com.subway.board.domain;

import java.util.List;

import lombok.Data;

@Data
public class SandWich {
	private String name;
	private List<String> ingredients;
}
