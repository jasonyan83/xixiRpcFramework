package xixi.transport.handler;


import xixi.transport.channel.Channel;
public abstract class AbstractChannelHandler implements ChannelHandler {

	public void onMessageRecieved(Object message){
		
	}
	
	public void onChannelConntected(Channel channel){
		
	}
	
	public void onChannelDisconntected(Channel channel){
		
	}
	
	public void exceptionCaught(Throwable cause){
		
	}
	
	public void onSent(Object message){
		
	}
}
