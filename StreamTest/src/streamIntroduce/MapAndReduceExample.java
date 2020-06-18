package streamIntroduce;

import java.util.Arrays;
import java.util.List;

public class MapAndReduceExample {

	public static void main(String[] args) {
		List<Student> list = Arrays.asList(
				new Student("ȫ�浿", 90),
				new Student("�Ӳ���", 92),
				new Student("���ȯ", 40)
				);
		
		//list.stream().mapToInt(s->s.getScore());
		double avg = list.stream() //�������� ��Ʈ��
				.mapToInt(Student::getScore)//������ ����� ��Ʈ�� �߰�ó�� ��Ʈ��
				.average()//����ó�� ��Ʈ��
				.getAsDouble();
		
		System.out.println("�������"+avg);
	}

}
