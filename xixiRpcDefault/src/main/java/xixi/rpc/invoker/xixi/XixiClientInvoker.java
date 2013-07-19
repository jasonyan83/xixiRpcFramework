package xixi.rpc.invoker.xixi;

import static xixi.router.Router.ROUTERMAP;
import xixi.common.constants.Constants;
import xixi.rpc.bean.RpcRequest;
import xixi.rpc.client.invoker.AbstractClientInvoker;
import xixi.rpc.client.invoker.RpcInvocation;
import xixi.rpc.future.RpcFuture;

public class XixiClientInvoker extends AbstractClientInvoker {

	@Override
	protected RpcFuture invoke(RpcInvocation inv) {
		RpcRequest request = new RpcRequest();
		request.setBasicVer((byte)1).setDstModule(inv.moduleId()).setSrcModule(Constants.SOURCE_MODULEID)
		.setInterfaceName(inv.rpcInterface()).setMethodName(inv.rpcMethod()).setType((byte)1).setData(inv.getArgs())
		.setTransactionId(inv.future().id());
		
		if(inv.getArgs()!=null&&inv.getArgs().length>0){
		     request.setData(inv.getArgs());	
		}
		
		ROUTERMAP.get(inv.moduleId()).router(request);
		return inv.future();
	}

}
