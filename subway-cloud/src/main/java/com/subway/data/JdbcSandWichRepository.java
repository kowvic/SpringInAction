package com.subway.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.subway.board.domain.Ingredient;
import com.subway.board.domain.SandWich;

@Repository
public class JdbcSandWichRepository implements SandWichRepository {

	private JdbcTemplate jdbc;
	
	public JdbcSandWichRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}


	@Override
	public SandWich save(SandWich sandwich) {
		//SandWich의 id와 createdAt을 생성하여 저장한다.
		long sandwichId = saveSandWichInfo(sandwich);
		sandwich.setId(sandwichId);
		
		//design.html에서 받아온 sandwich의 ingredients 안의 ingredient값을
		//id와 묶어서 저장한다.
		for(Ingredient ingredient : sandwich.getIngredients()) {
			saveIngredientToSandWich(ingredient, sandwichId);
		}
		
		return sandwich;
	}
	
	private long saveSandWichInfo(SandWich sandwich) {
		//createdAt 생성
		sandwich.setCreatedAt(new Date());
		
		//insert문 생성, design.html에서 받아온 name과 createdAt을 입력한다.
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"insert into SandWich(name, createdAt) values(?, ?)",
				Types.VARCHAR, Types.TIMESTAMP
				).newPreparedStatementCreator(
						Arrays.asList(
								sandwich.getName(),
								//new date로 저장한 값을 가져와 (년월일시분초) 밀리세컨드초로 변경한 다음(12345678초)
								//Timestamp 형식으로 변환
								new Timestamp(sandwich.getCreatedAt().getTime())));
		
		//keyholer인터페이스. sql 삽입문에 실행될 때 자동으로 생성되는 키를 담고 있는 인터페이스이다.
		 
		KeyHolder keyholder = new GeneratedKeyHolder();
		jdbc.update(psc, keyholder);
		
		//keyholder의 키값을 가져와 정수타입으로 변환한 값이 id가 된다.
		return keyholder.getKey().longValue();
		
	}

	private void saveIngredientToSandWich(Ingredient ingredient, long sandwichId) {
		jdbc.update("insert into SandWich_Ingredients(sandwich, ingredient)"+
				"values(?,?)", sandwichId, ingredient.getId());

	}



}
