package xixi.testModuleA.test;

import xixi.rpc.exception.TimeoutException;
import xixi.testModuleB.test.Account;
import xixi.testModuleB.test.IAccountService;

public class LoginService {

	private IAccountService accountService;
	
	public void login(){
		Thread thread = new Thread(new Runnable(){

			public void run() {
				while(true){
					
					try {
						Account account = accountService.getAccountById();
						if(account!=null){
							System.out.println(account.getName() + "-" + account.getBalance());
						}
						else{
							System.out.println("NULL VALUE");
						}

						Thread.sleep(3000);
					} catch (InterruptedException e) {
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
