package xixi.rc.dispatcher;

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

public class RCRegisterDispatcher implements Dispatcher {
	private static final Logger logger = LoggerFactory
			.getLogger(RCRegisterDispatcher.class);

	private EventBus eventBus;

	@Override
	public void dispatcher(Object message) {

		if (message instanceof RpcRequest) {
			final RpcRequest rpcRequest = (RpcRequest) message;
			final RpcInvocation inv = new RpcInvocation(
					rpcRequest.getInterfaceName(), rpcRequest.getMethodName(),
					rpcRequest.getData());
			final Channel channel = (Channel) rpcRequest
					.getProperty("channel");
			
			inv.addProperty("channel", channel);
			
			final RpcResponse rpcResp = rpcRequest.copytoResponse();

			eventBus.fireEvent(inv);

			try {
				inv.future().addCallback(new Callback() {
					@Override
					public void invoke(Object resp) {
						rpcResp.setData(resp).setType((byte) 5).setStatus(0);
						
						channel.send(rpcResp);
					}
				});
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		} else if (message instanceof RpcResponse) {
			DefaultFuture.setResult(message);
		} else {
			throw new IllegalArgumentException("invalid message");
		}
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
