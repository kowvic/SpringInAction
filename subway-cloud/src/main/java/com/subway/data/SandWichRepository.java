package com.subway.data;

import org.springframework.data.repository.CrudRepository;

import com.subway.board.domain.SandWich;

public interface SandWichRepository 
	extends CrudRepository<SandWich, Long>{

//	SandWich save(SandWich design);
}
