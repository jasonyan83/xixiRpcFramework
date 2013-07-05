package xixi.rpc.filter;


import java.util.Date;

import xixi.common.util.BizLogger;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;

public class BizLoggerFilter implements Filter {

	private static final BizLogger bizLogger = new BizLogger();

	private String name = "bizLogger";

	public BizLoggerFilter() {
	}
	
	public BizLoggerFilter(String filterName) {
		name = filterName;
	}

	@Override
	public String filterName() {
		return name;
	}

	@Override
	public void doFilter(Invoker service, Invocation inv) {
		bizLogger.log(new Date().toLocaleString(), service.toString());
		service.invoke(inv);
	}

}
