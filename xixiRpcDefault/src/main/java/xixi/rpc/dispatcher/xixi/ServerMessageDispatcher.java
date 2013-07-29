/**
 * 
 */
package xixi.rpc.dispatcher.xixi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.register.eventBus.EventBus;
import xixi.rpc.Callback;
import xixi.rpc.bean.RpcRequest;
import xixi.rpc.bean.RpcResponse;
import xixi.rpc.client.invoker.RpcInvocation;
import xixi.rpc.exception.TimeoutException;
import xixi.rpc.future.DefaultFuture;
import xixi.transport.channel.Channel;
import xixi.transport.dispatcher.Dispatcher;


/**
 * @author hp
 *
 */
public class ServerMessageDispatcher extends AbstractDispatcher{

    private static final Logger logger = 
    	LoggerFactory.getLogger(ServerMessageDispatcher.class);
    
	@Override
	public void dispatcher(Object message) {
		logger.debug("Dispatchering message {}", message);
		if(message instanceof RpcRequest){
			final RpcRequest rpcRequest =(RpcRequest)message;
			final RpcInvocation inv = new RpcInvocation(rpcRequest.getInterfaceName(),rpcRequest.getMethodName(),rpcRequest.getData());
			
			final RpcResponse rpcResp = rpcRequest.copytoResponse();
			
			this.getEventBus().fireEvent(inv);
			
			try {
				inv.future().addCallback(new Callback(){
					@Override
					public void invoke(Object resp) {
						rpcResp.setData(new Object[]{resp}).setType((byte)2).setStatus(0);
						Channel channel = (Channel)rpcRequest.getProperty("channel");
						channel.send(rpcResp);
					}
				});
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
		else if(message instanceof RpcResponse){
			DefaultFuture.setResult(message);
		}
		else{
			throw new IllegalArgumentException("invalid message");
		}
	}

}
