package com.subway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.subway.board.domain.Ingredient;
import com.subway.board.domain.Ingredient.Type;
import com.subway.data.IngredientRepository;

@SpringBootApplication
public class SubwayCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayCloudApplication.class, args);
	}
	//애플리케이션이 시작될 때dataLOader() 메소드 실행 
	//ingredient 데이터를 db에 미리 저장하기 위해서
	@Bean
	public CommandLineRunner dataLoader(IngredientRepository repo) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
               repo.save(new Ingredient("HOOT", "Honey Oat", Type.BREAD));
               repo.save(new Ingredient("HEIT", "Hearty Italian", Type.BREAD));
               repo.save(new Ingredient("WHIT", "White Bread", Type.BREAD));
           	   repo.save(new Ingredient("MEAT", "Meat", Type.PROTEIN));
           	   repo.save(new Ingredient("BEEF", "Beef", Type.PROTEIN));
           	   repo.save(new Ingredient("CHIC", "Chicken", Type.PROTEIN));
           	   repo.save(new Ingredient("LETT", "Letuce", Type.VEGGIES));
           	   repo.save(new Ingredient("TOMA", "Honey Oat", Type.VEGGIES));
           	   repo.save(new Ingredient("CUCU", "Cucumbers", Type.VEGGIES));
           	   repo.save(new Ingredient("ONIO", "Onions", Type.VEGGIES));
           	   repo.save(new Ingredient("AMER", "American Cheese", Type.CHEESE));
           	   repo.save(new Ingredient("SHRE", "Shredded Cheese", Type.CHEESE));
           	   repo.save(new Ingredient("MOZZ", "Mozzarella Cheese", Type.CHEESE));
           	   repo.save(new Ingredient("SWON", "Sweet Onion", Type.SAUCE));
           	   repo.save(new Ingredient("HOMU", "Honey Mustard", Type.SAUCE));
           	   repo.save(new Ingredient("SWCH", "Sweet Chilli", Type.SAUCE));
				
			}
			
		};
	};
}
