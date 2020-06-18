package streamIntroduce;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class IteratorVsStreamExample {

	public static void main(String[] args) {

		List<String> list = Arrays.asList("a", "b", "c");
		
		//자바 7 이전 코드
		Iterator<String> iterator = list.iterator();
		while(iterator.hasNext()) {
			String str = iterator.next();
			System.out.println(str); //a, b, c
		}
		
		//자바 8 이후 코드
		Stream<String> stream = list.stream();
		stream.forEach(x -> System.out.println(x));//a, b, c. x는 익명함수(람다식)
		
	}

}
