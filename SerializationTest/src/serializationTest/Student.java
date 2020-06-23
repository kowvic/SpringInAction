package serializationTest;

import java.io.Serializable;

//Serializable인터페이스가 있어야 오브젝트를 직렬화 할 수 있다.
public class Student implements Serializable{
	
	private static final long serialVersionUID = 3613874238496093371L;
	private transient int id;
	private String name;
	private String major;
	
	public Student(int id, String name, String major) {
		super();
		this.id = id;
		this.name = name;
		this.major = major;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMajor() {
		return major;
	}	
}


