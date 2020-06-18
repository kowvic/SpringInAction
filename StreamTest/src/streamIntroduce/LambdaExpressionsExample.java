package streamIntroduce;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LambdaExpressionsExample {
	
	
	
	public static void main(String[] args) {
		List<Student> list = Arrays.asList(
				new Student("È«±æµ¿", 90),
				new Student("ÀÓ²©Á¤", 92));
		
		Stream<Student> stream = list.stream();
		stream.forEach(s->{
			String name = s.getName();
			int score = s.getScore();
			System.out.println(name+"-"+score);
			//È«±æµ¿-90
			//ÀÓ²©Á¤-92
		});
	}

}
