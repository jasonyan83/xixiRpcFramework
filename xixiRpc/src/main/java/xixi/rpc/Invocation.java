package xixi.rpc;

import xixi.rpc.future.Future;

public interface Invocation{
	
	Object[] getArgs();
	
	Future future();
	
	void setFuture(Future future);
	
	String rpcInterface();
	
	String rpcMethod();


	
}
