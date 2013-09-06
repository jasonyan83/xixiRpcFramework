package xixi.rpc.invoker.xixi;

import static xixi.router.Router.ROUTERMAP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.router.Router;
import xixi.rpc.InvocationException;
import xixi.rpc.bean.RpcRequest;
import xixi.rpc.client.invoker.AbstractClientInvoker;
import xixi.rpc.client.invoker.RpcInvocation;
import xixi.rpc.future.RpcFuture;

public class XixiClientInvoker extends AbstractClientInvoker {

    private static final Logger logger = 
        	LoggerFactory.getLogger(XixiClientInvoker.class);
    
	@Override
	protected RpcFuture invoke(RpcInvocation inv) {
		RpcRequest request = new RpcRequest();
		request.setBasicVer((byte)1).setDstModule(inv.moduleId()).setSrcModule(Constants.SOURCE_MODULEID)
		.setInterfaceName(inv.rpcInterface()).setMethodName(inv.rpcMethod()).setType((byte)1).setData(inv.getArgs())
		.setTransactionId(inv.future().id());
		
		if(inv.getArgs()!=null&&inv.getArgs().length>0){
		     request.setData(inv.getArgs());	
		}
		
		Router router = ROUTERMAP.get(inv.moduleId());
		if(router!=null){
			router.router(request);
		}
		else{
			logger.error("Can not find router for module {}", inv.moduleId());
			return inv.future();
		}
		return inv.future();
	}

}
