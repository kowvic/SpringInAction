package streamIntroduce;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParallelExample {

	public static void main(String[] args) {
		List<String> list = Arrays.asList(	
				"��", "��", "��", "��", "��"
				);

		//����ó��
		Stream<String> stream = list.stream();
//		stream.forEach(name->print(name));
		stream.forEach(ParallelExample::print);//�޼ҵ� ����
		//������ = ��:main ��:main ��:main ��:main ��:main
		
		//����ó��
		Stream<String> parallelStream = list.parallelStream();
//		parallelStream.forEach(name->print(name));
		parallelStream.forEach(ParallelExample::print);//�޼ҵ� ����
		//������: ������������ ���� ����Ǵ� ������� ����.
		//		ex:��:main ��:ForkJoinPool.commonPool-worker-2
		//		       ��:ForkJoinPool.commonPool-worker-1 ...

	}
	
	public static void print(String str) {
		System.out.println(str+":"+Thread.currentThread().getName());
	}

}
