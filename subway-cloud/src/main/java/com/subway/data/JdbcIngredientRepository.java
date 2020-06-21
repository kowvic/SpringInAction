package com.subway.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.subway.board.domain.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository{
	private JdbcTemplate jdbc;
	
	@Autowired
	public JdbcIngredientRepository(JdbcTemplate jdbc) {
		this.jdbc=jdbc;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		//query(sql명령어, RowMapper 인터페이스를 구현한 mapRowToIngredient메소드)
		return jdbc.query("select id, name, type from Ingredient",
				this::mapRowToIngredient);
	}

	@Override
	public Ingredient findById(String id) {
		//query(sql명령어, RowMapper 인터페이스를 구현한 mapRowToIngredient메소드, 쿼리에서 요구하는 매개변수(where id)
		return jdbc.queryForObject("select id, name, type from Ingredient where id=?",
				this::mapRowToIngredient, id);
	}
	

	//spring의 RowMapper 인터페이스를 구현한 메소드
	//쿼리로 생성된 ResultSet 객체의 행 개수만큼 호출되며
	//ResultSet의 모든 행을 각각 Ingredient객체 id, name, type으로 생성하고 List에 저장 후 반환한다.)
	private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException{
		return new Ingredient(rs.getString("id"), rs.getString("name"),
				Ingredient.Type.valueOf(rs.getString("type")));
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbc.update("insert into Ingredient(id, name, type) values(?, ?, ?)",
				ingredient.getId(),
				ingredient.getName(),
				ingredient.getType().toString());
		return ingredient;
		
	}
}
