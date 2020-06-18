package streamIntroduce;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class IteratorVsStreamExample {

	public static void main(String[] args) {

		List<String> list = Arrays.asList("a", "b", "c");
		
		//�ڹ� 7 ���� �ڵ�
		Iterator<String> iterator = list.iterator();
		while(iterator.hasNext()) {
			String str = iterator.next();
			System.out.println(str); //a, b, c
		}
		
		//�ڹ� 8 ���� �ڵ�
		Stream<String> stream = list.stream();
		stream.forEach(x -> System.out.println(x));//a, b, c. x�� �͸��Լ�(���ٽ�)
		
	}

}
