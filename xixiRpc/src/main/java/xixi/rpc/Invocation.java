package xixi.rpc;

import xixi.common.bean.Property;
import xixi.rpc.future.Future;

public interface Invocation extends Property{
	
	Object[] getArgs();
	
	Future future();
	
	void setFuture(Future future);
	
	String rpcInterface();
	
	String rpcMethod();


	
}
