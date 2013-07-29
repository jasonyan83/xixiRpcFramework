package xixi.rpc.dispatcher.xixi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.bean.RpcNotify;
import xixi.rpc.bean.RpcResponse;
import xixi.rpc.client.invoker.RpcInvocation;
import xixi.rpc.future.DefaultFuture;

public class ClientMessageDispatcher extends AbstractDispatcher {

	private static final Logger logger = LoggerFactory
			.getLogger(ClientMessageDispatcher.class);


	@Override
	public void dispatcher(Object message) {
		logger.debug("Dispatchering message {}", message);
		if(message instanceof RpcResponse){
			DefaultFuture.setResult(message);
		}
		if(message instanceof RpcNotify){
			RpcNotify notify = (RpcNotify)message;
			final RpcInvocation inv = new RpcInvocation(notify.getInterfaceName(),notify.getMethodName(),notify.getData());
			this.getEventBus().fireEvent(inv);
		}
		else{
			throw new IllegalArgumentException("message is not RpcResponse");
		}
		
	}

}
