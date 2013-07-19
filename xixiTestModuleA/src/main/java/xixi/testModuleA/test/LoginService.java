package xixi.testModuleA.test;

import xixi.testModuleB.test.Account;
import xixi.testModuleB.test.IAccountService;

public class LoginService {

	private IAccountService accountService;
	
	public void login(){
		Thread thread = new Thread(new Runnable(){

			public void run() {
				while(true){
					Account account = accountService.getAccountById();
					System.out.println(account.getName() + "-" + account.getBalance());
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		thread.setDaemon(true);
		thread.start();
		
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
}
