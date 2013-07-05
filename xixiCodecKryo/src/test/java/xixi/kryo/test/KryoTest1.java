package xixi.kryo.test;

import xixi.codec.kryo.KryoCoder;
import xixi.testModuleB.test.Account;

public class KryoTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KryoCoder coder = new KryoCoder();
		coder.registerClass(Student.class);
		coder.registerClass(Account.class);
		
		Student s = new Student();
		s.setAge(20);
		s.setName("Jason");
		s.setHometown("ruian");
		
		
		byte[]  ret = coder.encoder(s);
		
		System.out.println("Length after encoder is :" + ret.length);
		
		Object o =  coder.decode(ret);
		
		Account account = new Account();
		account.setId(new Integer(1010));
		account.setName("jason");
		account.setBalance(System.currentTimeMillis());
		
		byte[] accountbyte = coder.encoder(account);
		
		Object a = coder.decode(accountbyte);
		
		System.out.println(a.toString());
	}

}
