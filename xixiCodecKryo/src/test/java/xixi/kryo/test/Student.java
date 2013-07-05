package xixi.kryo.test;

public class Student {

	private String name;
	
	private int age;
	
	private String hometown;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	
	public String toString(){
		return this.name + " is "+ this.age + ". And comes from "+ this.hometown;
	}
}
