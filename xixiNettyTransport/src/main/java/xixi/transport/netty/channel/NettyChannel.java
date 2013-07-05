package xixi.transport.netty.channel;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import xixi.transport.channel.AbstractChannel;
import xixi.transport.handler.ChannelHandler;

public class NettyChannel extends AbstractChannel{

	private final static ConcurrentMap<org.jboss.netty.channel.Channel,NettyChannel> CHANNELS = new ConcurrentHashMap<org.jboss.netty.channel.Channel,NettyChannel>();
	
	private final org.jboss.netty.channel.Channel channel ;
	
	private NettyChannel(org.jboss.netty.channel.Channel ch,ChannelHandler handler){
		super(handler);
		channel = ch;
	}
	
	public static NettyChannel getOrCreateChannel(org.jboss.netty.channel.Channel ch, ChannelHandler handler){
		if(ch==null){
			return null;
		}
		NettyChannel channel = CHANNELS.get(ch);
		if(channel==null){
			if(ch.isConnected()){
				NettyChannel nc =  new NettyChannel(ch, handler);
				channel = CHANNELS.putIfAbsent(ch, nc);
				if(channel==null){
					channel = nc;
				}
			}
		}
		return channel;
	}

	public static NettyChannel getChannel(org.jboss.netty.channel.Channel ch){
		if(ch==null){
			return null;
		}
		return CHANNELS.get(ch);
	}
	
	public static NettyChannel removeChannel(org.jboss.netty.channel.Channel ch){
		if(ch!=null&&!ch.isConnected()){
			return CHANNELS.remove(ch);
		}
		return null;
	}
	
	@Override
	protected void doSend(Object message) {
		channel.write(message);
	}

	@Override
	public void close() {
		channel.close();
	}

	@Override
	public boolean isClosed() {
		return !channel.isConnected();
	}

	@Override
	public boolean isConnected() {
		return channel.isConnected();
	}
}
