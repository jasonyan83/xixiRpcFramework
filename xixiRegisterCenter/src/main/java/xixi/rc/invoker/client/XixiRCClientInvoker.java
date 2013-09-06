package xixi.rc.invoker.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.common.constants.Constants;
import xixi.rc.register.Registry;
import xixi.rpc.bean.RpcNotify;
import xixi.rpc.client.invoker.AbstractClientInvoker;
import xixi.rpc.client.invoker.RpcInvocation;
import xixi.rpc.future.RpcFuture;
import xixi.transport.channel.Channel;

//a broad cast invoker, will send the message to all the module instance
public class XixiRCClientInvoker extends AbstractClientInvoker {

	private static final Logger logger = LoggerFactory
			.getLogger(XixiRCClientInvoker.class);
	
	private Registry registry;
	
	@Override
	protected RpcFuture invoke(RpcInvocation inv) {
		if(inv.getArgs()!=null&&inv.getArgs().length>0){
			short moduleId = (short)inv.getArgs()[0];
			
			List<ModuleInstanceInfo> instances =  registry.getModuleInstances(moduleId);
			
			RpcNotify notify = new RpcNotify();
			notify.setBasicVer((byte)1).setDstModule(inv.moduleId()).setSrcModule(Constants.SOURCE_MODULEID)
			.setInterfaceName(inv.rpcInterface()).setMethodName(inv.rpcMethod()).setType((byte)3).setData(inv.getArgs())
			.setTransactionId(inv.future().id());
			
		    notify.setData(inv.getArgs());	
		    
		    logger.debug("Building notify {}", notify);
		    
		    for(ModuleInstanceInfo m : instances){
		    	Channel channel = registry.getChannelByInstance(m.getIpAddress());
		    	logger.debug("sending notify by instance {} through channel {}", m.getIpAddress(), channel);
		    	channel.send(notify);
		    }
		}
		else{
			inv.future().fail();
			logger.error("Args is null or size is 0");
		}
		
		return inv.future();
	}
	
	public Registry getRegistry() {
		return registry;
	}
	
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

}
