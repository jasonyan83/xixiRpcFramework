package xixi.rpc.dispatcher.xixi;

import xixi.rpc.bean.RpcNotify;
import xixi.rpc.bean.RpcResponse;
import xixi.rpc.future.DefaultFuture;
import xixi.transport.dispatcher.Dispatcher;

public class ClientMessageDispatcher implements Dispatcher {

	@Override
	public void dispatcher(Object message) {
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
