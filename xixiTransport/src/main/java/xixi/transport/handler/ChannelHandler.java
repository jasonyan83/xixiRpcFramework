package xixi.transport.handler;

import xixi.transport.channel.Channel;
import xixi.transport.listener.ConnectorListener;
public interface ChannelHandler {

	void onMessageRecieved(Object message);
	
	void onChannelConntected(Channel channel);
	
	void onChannelDisconntected(Channel channel);
	
	void exceptionCaught(Throwable cause);
	
	void onSent(Object message);
	
	void addConnectorListener(ConnectorListener listener);

}
