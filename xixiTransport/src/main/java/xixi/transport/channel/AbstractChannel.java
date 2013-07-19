package xixi.transport.channel;

import xixi.transport.channel.Channel;
import xixi.transport.handler.ChannelHandler;
public abstract class AbstractChannel implements Channel {

	private ChannelHandler handler;
	
	public AbstractChannel(ChannelHandler handler){
		this.handler = handler;
	}
	public void send(Object message){
		if(isConnected()){
			doSend(message);
		}
	}
	
	protected abstract void doSend(Object message);

	@Override
	public void onExceptionCaught(Throwable cause) {
		handler.exceptionCaught(cause);
	}

	@Override
	public void onMessageReceived(Object message) {
		handler.onMessageRecieved(message);
	}
	
	@Override
	public void onChannelConntected(){
		handler.onChannelConntected(this);	
	}
	
	@Override
	public void onChannelDisconntected(){
		handler.onChannelDisconntected(this);
	}
}
