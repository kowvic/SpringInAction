package streamIntroduce;

import java.util.Arrays;
import java.util.List;

public class MapAndReduceExample {

	public static void main(String[] args) {
		List<Student> list = Arrays.asList(
				new Student("홍길동", 90),
				new Student("임꺽정", 92),
				new Student("김두환", 40)
				);
		
		//list.stream().mapToInt(s->s.getScore());
		double avg = list.stream() //오리지널 스트림
				.mapToInt(Student::getScore)//점수가 요소인 스트림 중간처리 스트림
				.average()//최종처리 스트림
				.getAsDouble();
		
		System.out.println("평균점수"+avg);
	}

}
