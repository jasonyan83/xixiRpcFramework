package xixi.rpc;

import xixi.rpc.future.RpcFuture;


public interface RpcInvoker extends Invoker{

	@Override
	public RpcFuture invoke(Invocation inv) throws InvocationException;
	

}
