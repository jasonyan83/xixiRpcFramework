package xixi.transport.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.transport.channel.Channel;
import xixi.transport.listener.ConnectorListener;
public abstract class AbstractChannelHandler implements ChannelHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractChannelHandler.class);

	private Channel channel;

	private List<ConnectorListener> connectorListenerList = new ArrayList<ConnectorListener>();;
	
	//If the channel is already connected, invoke the listener immediately
	public void addConnectorListener(ConnectorListener listener){
		logger.debug("entering addConnectorListener");
		synchronized(this){
			connectorListenerList.add(listener);
			//logger.debug("current channel connect status is " + channel.isConnected());
			if(channel!=null&&channel.isConnected()){
				logger.debug("Invoking connectorListener from addConnectorListener " + listener);
				listener.onConnected();
			}
			if(channel!=null&&channel.isClosed()){
				listener.onDisConnected();
			}
		}
		logger.debug("leaving addConnectorListener");
	}
	
	
	public void onMessageRecieved(Object message){
		
	}
	
	public void onChannelConntected(Channel channel){
		logger.debug("entering onChannelConntected");
		this.channel = channel;
		synchronized(this){
			for(ConnectorListener listener: connectorListenerList){
				logger.debug("Invoking connectorListener from onChannelConntected " + listener);
				listener.onConnected();
			}
		}
		logger.debug("leaving onChannelConntected");
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
