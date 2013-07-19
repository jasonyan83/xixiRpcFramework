package xixi.rpc;

import xixi.rpc.future.RpcFuture;

public interface IRpcInvocation extends Invocation {
	
	String version();
	
	short moduleId();
	
	@Override
	RpcFuture future();
	
	void setModuleId(short moduleId);
	
}
