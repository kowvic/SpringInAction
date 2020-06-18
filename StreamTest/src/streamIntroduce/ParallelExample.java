package streamIntroduce;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParallelExample {

	public static void main(String[] args) {
		List<String> list = Arrays.asList(	
				"갑", "을", "병", "정", "무"
				);

		//순차처리
		Stream<String> stream = list.stream();
//		stream.forEach(name->print(name));
		stream.forEach(ParallelExample::print);//메소드 참조
		//실행결과 = 갑:main 을:main 병:main 정:main 무:main
		
		//병렬처리
		Stream<String> parallelStream = list.parallelStream();
//		parallelStream.forEach(name->print(name));
		parallelStream.forEach(ParallelExample::print);//메소드 참조
		//실행결과: 갑을병정무가 먼저 실행되는 순서대로 생성.
		//		ex:갑:main 정:ForkJoinPool.commonPool-worker-2
		//		       정:ForkJoinPool.commonPool-worker-1 ...

	}
	
	public static void print(String str) {
		System.out.println(str+":"+Thread.currentThread().getName());
	}

}
