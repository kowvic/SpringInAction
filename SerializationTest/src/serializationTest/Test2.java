package serializationTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Test2 {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//�ּ����� ���ϸ� �ش� ������ ���� ���� bin������ ã�´�.
				FileInputStream fsIn = new FileInputStream("�л�2.bin");
				ObjectInputStream osIn = new ObjectInputStream(fsIn);
				
				
				//�л�1.bin�� object�� �д´�.
				//Student Ÿ������ ĳ�����ؾ� clone���� �޾Ƶ��� �� �ִ�.
				Student clone = (Student)osIn.readObject();
				osIn.close();//�޸�����
				
				System.out.println(
						clone.getId()+"\n"+
						clone.getName()+"\n"+
						clone.getMajor()
						);
				
			}
}
