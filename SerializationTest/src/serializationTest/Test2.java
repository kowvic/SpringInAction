package serializationTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Test2 {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//주소지정 안하면 해당 애플의 파일 안의 bin파일을 찾는다.
				FileInputStream fsIn = new FileInputStream("학생2.bin");
				ObjectInputStream osIn = new ObjectInputStream(fsIn);
				
				
				//학생1.bin을 object로 읽는다.
				//Student 타입으로 캐스팅해야 clone에서 받아들일 수 있다.
				Student clone = (Student)osIn.readObject();
				osIn.close();//메모리절약
				
				System.out.println(
						clone.getId()+"\n"+
						clone.getName()+"\n"+
						clone.getMajor()
						);
				
			}
}
