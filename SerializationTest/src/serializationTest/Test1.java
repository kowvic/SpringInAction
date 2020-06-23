package serializationTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Test1 {

	public static void main(String[] args) throws IOException {
		/*
		 * 직렬화 Object -> Binary 형태의 파일(0이나 1로 구성된 형태)
		 * .bin, ser이 붙은 파일은 Binary파일이다.
		 *  
		 * 역직렬화
		 * Binary 형태의 파일 -> Object로 변환
		 */		

		Student std = new Student(111, "학생1", "컴퓨터학과1");
		
		System.out.println(
				std.getId()+"\n"+
				std.getName()+"\n"+
				std.getMajor()
				
				);
		
		/*
		 * 직렬화 
		 * 1. FileOutputStream(파일 선택 클래스) 
		 * 2. ObjectOutputStream(직접 저장)
		 * 
		 */
		
		/* FileOutputStream
		 * 임의의 파일경로 지정(SerializationTest 파일 위치로 지정하고 학생1.bin 파일을 
                *  생성할 것임)
		 * "C:\\Users\\OOO\\Documents\\"
				+ "SpringInAction\\SerializationTest\\학생1.bin"
		 * 
		 *  파일 이름(학생1.bin)만 쓰면 자바 프로그램이 있는 폴더 안에 자동으로 만들어준다.
		 * 
		 * 
		 * ObjectOutputStream
		 * 1. ObjectOutputStream 인스턴스에 FileOutputStream을 인자로 넣는다.
		 * 2. 그 다음 이 객체에 들어갈 Object파일을 넣는다.
		 * 3. ObjectOutputStream을 close한다.
		 * 
		 */
		FileOutputStream fsOut = new FileOutputStream("학생1.bin");
		ObjectOutputStream osOut = new ObjectOutputStream(fsOut);
		osOut.writeObject(std);
		osOut.close();//메모리 절약
	}
}
