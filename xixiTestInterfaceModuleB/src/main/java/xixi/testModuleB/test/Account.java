package xixi.testModuleB.test;

import xixi.common.annotation.XixiBean;

@XixiBean(id=302001)
public class Account {

	private Integer id;
	
	private String name;
	
	private long balance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}
}
