package xixi.transport.handler;

import xixi.transport.channel.Channel;
public interface ChannelHandler {

	void onMessageRecieved(Object message);
	
	void onChannelConntected(Channel channel);
	
	void onChannelDisconntected(Channel channel);
	
	void exceptionCaught(Throwable cause);
	
	void onSent(Object message);
}
