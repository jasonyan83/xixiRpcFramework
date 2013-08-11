package xixi.transport.handler;

import java.util.ArrayList;
import java.util.List;

import xixi.transport.channel.Channel;
import xixi.transport.listener.ConnectorListener;
public abstract class AbstractChannelHandler implements ChannelHandler {

	private Channel channel;
	private List<ConnectorListener> connectorListenerList = new ArrayList<ConnectorListener>();;
	
	//If the channel is already connected, invoke the listener immediately
	public void addConnectorListener(ConnectorListener listener){
		synchronized(this){
			connectorListenerList.add(listener);
			if(channel!=null&&channel.isConnected()){
				listener.onConnected();
			}
			if(channel!=null&&channel.isClosed()){
				listener.onDisConnected();
			}
		}

	}
	
	
	public void onMessageRecieved(Object message){
		
	}
	
	public void onChannelConntected(Channel channel){
		synchronized(this){
			for(ConnectorListener listener: connectorListenerList){
				listener.onConnected();
			}
		}
	}
	
	public void onChannelDisconntected(Channel channel){
		synchronized(this){
			for(ConnectorListener listener: connectorListenerList){
				listener.onDisConnected();
			}
		}
	}
	
	public void exceptionCaught(Throwable cause){
		
	}
	
	public void onSent(Object message){
		
	}
}
