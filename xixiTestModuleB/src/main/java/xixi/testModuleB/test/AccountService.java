package xixi.testModuleB.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.annotation.EventMethod;
import xixi.transport.netty.client.NettyTcpClient;

public class AccountService implements IAccountService {

	private static final Logger logger = LoggerFactory
	.getLogger(AccountService.class);

	
	public AccountService(){
		
	}
	
	@EventMethod(name = "getAccountById", filter = "serverStatInfoFilter")
	public Account getAccountById() {
		logger.debug("Invoking getAccountById");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Account account = new Account();
		account.setId(new Integer(1010));
		account.setName("jason");
		account.setBalance(System.currentTimeMillis());
		return account;
	}

}
