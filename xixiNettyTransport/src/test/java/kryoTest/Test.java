package kryoTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kryo kryo = new Kryo();
		
		kryo.register(People.class);
		kryo.register(Student.class);
		
		List<String>  names = new ArrayList<String>();
		names.add("kan");
		names.add("bob");
		
		People p = new People(19,"Jason",names);
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Output output = new Output(outStream);
		kryo.writeClassAndObject(output, p);
		output.flush();
		byte[] result = outStream.toByteArray();
		System.out.println("size of the serialize result is " + result.length);
		
		System.out.println("size of the serialize result is " + result.length);
		
		Input input = new Input(new ByteArrayInputStream(result));
		People ret2 =(People)kryo.readClassAndObject(input);
		
		System.out.println(ret2.getName() + "-" + ret2.getAge());
		
		for(String s : ret2.getFriends()){
			System.out.println("friends name is" + "-" + s);
		}
		
		

	}

}
