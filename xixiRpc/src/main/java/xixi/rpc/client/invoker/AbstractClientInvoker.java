package xixi.rpc.client.invoker;

import xixi.rpc.Invocation;
import xixi.rpc.RpcInvoker;
import xixi.rpc.future.RpcFuture;

public abstract class AbstractClientInvoker implements RpcInvoker{
	
	@Override
	public RpcFuture invoke(Invocation inv){
		return invoke((RpcInvocation)inv);
	}
	
	@Override
	public void destory() {
		//the client invoker will never distory
	}
	
	protected abstract RpcFuture invoke(RpcInvocation inv);
}
