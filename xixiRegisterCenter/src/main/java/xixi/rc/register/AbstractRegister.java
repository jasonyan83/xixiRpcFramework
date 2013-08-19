package xixi.rc.register;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.util.ModuleStringUtil;
import xixi.transport.channel.Channel;

public abstract class AbstractRegister implements Registry{

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractRegister.class);
	
	//ThreadSafe
		private final Map<String, Channel> instanceChannelMap = new HashMap<String, Channel>();
		
		//ThreadSafe
		private final Map<Channel, String> channelInstanceMap = new HashMap<Channel, String>();

		public Channel getChannelByInstance(String ipAddress) {
			return this.instanceChannelMap.get(ipAddress);
		}

		public String getInstanceIpByChannel(Channel channel) {
			return this.channelInstanceMap.get(channel);
		}
		
		public void removeInstance(short moduleId, String ipAddress){
			String moduleString = moduleId + "-" + ipAddress;
			Channel channel = this.instanceChannelMap.get(moduleString);
			if(channel!=null){
				this.channelInstanceMap.remove(channel);
			}
			this.instanceChannelMap.remove(moduleString);
		}
		
		@Override
		public void buildInstanceChannelMap(short moduleId, String ipAddress,
				Channel channel) {

			logger.debug("Build instance channle map for {}-{}", moduleId, ipAddress);
			String moduleString = channelInstanceMap.put(channel, moduleId + "-"
					+ ipAddress);
			if (moduleString != null) {
				logger.warn(moduleString + " already exsit in the map for channel:"
						+ channel);
			}

			Channel ch = instanceChannelMap.put(ipAddress, channel);
			if (ch != null) {
				logger.warn(channel + " already exsit in the map for instance:"
						+ moduleString);
			}
		}
		
		public void removeInstanceAndDeactive(Channel channel){
			String  moduleString = this.channelInstanceMap.get(channel);
			if(moduleString!=null){
				this.instanceChannelMap.remove(moduleString);
			}
			this.channelInstanceMap.remove(channel);
			this.deactiveInstance(Short.valueOf(ModuleStringUtil.getMoudleId(moduleString)),ModuleStringUtil.getIpAddress(moduleString));
		}
		
		public Map<String, String> getInstanceChannelMap() {
			Map<String,String> retMap = new HashMap<String,String>();
			for(String key:instanceChannelMap.keySet()){
				retMap.put(key, instanceChannelMap.get(key).toString());
			}
			return retMap;
		}

		public Map<String, String> getChannelInstanceMap() {
			Map<String,String> retMap = new HashMap<String,String>();
			for(Channel key:channelInstanceMap.keySet()){
				retMap.put(key.toString(), channelInstanceMap.get(key));
			}
			return retMap;
		}
		
		protected abstract void deactiveInstance(short moduleId, String ipAddress);
}
