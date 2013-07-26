package xixi.rpc.dispatcher.xixi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.bean.RpcNotify;
import xixi.rpc.bean.RpcResponse;
import xixi.rpc.future.DefaultFuture;
import xixi.transport.dispatcher.Dispatcher;

public class ClientMessageDispatcher implements Dispatcher {

	private static final Logger logger = LoggerFactory
			.getLogger(ClientMessageDispatcher.class);
	
	@Override
	public void dispatcher(Object message) {
		logger.debug("Dispatchering message {}", message);
		if(message instanceof RpcResponse){
			DefaultFuture.setResult(message);
		}
		if(message instanceof RpcNotify){
			DefaultFuture.setResult(message);
		}
		else{
			throw new IllegalArgumentException("message is not RpcResponse");
		}
		
	}

}
