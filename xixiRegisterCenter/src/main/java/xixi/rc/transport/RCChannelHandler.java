package xixi.rc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rc.register.Registry;
import xixi.transport.channel.Channel;
import xixi.transport.handler.MessageDispatcherChannelHandler;

public class RCChannelHandler extends MessageDispatcherChannelHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RCChannelHandler.class);
	
	private Registry registry;
	
	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	//once instance is down, update registry info,but do not need to send status update notify to dependent modules, 
	// module instance itself will handle the disconnect event.
	@Override
	public void onChannelDisconntected(Channel channel){
		logger.debug("Channel disconnect for channel {}", channel);
		registry.removeInstanceAndDeactive(channel);
	}
}
