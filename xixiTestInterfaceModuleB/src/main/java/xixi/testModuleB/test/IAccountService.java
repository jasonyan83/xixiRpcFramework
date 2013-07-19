package xixi.testModuleB.test;

import xixi.common.annotation.EventService;

@EventService(name = "accountService", moduleId = 302, version = "1")
public interface IAccountService {

	public Account getAccountById();
}
