package serializationTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Test1 {

	public static void main(String[] args) throws IOException {
		/*
		 * ����ȭ Object -> Binary ������ ����(0�̳� 1�� ������ ����)
		 * .bin, ser�� ���� ������ Binary�����̴�.
		 *  
		 * ������ȭ
		 * Binary ������ ���� -> Object�� ��ȯ
		 */		

		Student std = new Student(111, "�л�1", "��ǻ���а�1");
		
		System.out.println(
				std.getId()+"\n"+
				std.getName()+"\n"+
				std.getMajor()
				
				);
		
		/*
		 * ����ȭ 
		 * 1. FileOutputStream(���� ���� Ŭ����) 
		 * 2. ObjectOutputStream(���� ����)
		 * 
		 */
		
		/* FileOutputStream
		 * ������ ���ϰ�� ����(SerializationTest ���� ��ġ�� �����ϰ� �л�1.bin ������ 
                *  ������ ����)
		 * "C:\\Users\\OOO\\Documents\\"
				+ "SpringInAction\\SerializationTest\\�л�1.bin"
		 * 
		 *  ���� �̸�(�л�1.bin)�� ���� �ڹ� ���α׷��� �ִ� ���� �ȿ� �ڵ����� ������ش�.
		 * 
		 * 
		 * ObjectOutputStream
		 * 1. ObjectOutputStream �ν��Ͻ��� FileOutputStream�� ���ڷ� �ִ´�.
		 * 2. �� ���� �� ��ü�� �� Object������ �ִ´�.
		 * 3. ObjectOutputStream�� close�Ѵ�.
		 * 
		 */
		FileOutputStream fsOut = new FileOutputStream("�л�1.bin");
		ObjectOutputStream osOut = new ObjectOutputStream(fsOut);
		osOut.writeObject(std);
		osOut.close();//�޸� ����
	}
}
