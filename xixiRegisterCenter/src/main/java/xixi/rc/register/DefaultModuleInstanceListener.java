package xixi.rc.register;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.rc.iservice.RCNotifyService;
import xixi.transport.channel.Channel;

public class DefaultModuleInstanceListener implements ModuleInstanceListener {

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public RCNotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(RCNotifyService notifyService) {
		this.notifyService = notifyService;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultModuleInstanceListener.class);
	
	private Registry registry;
	private RCNotifyService notifyService;
	
	@Override
	public void onRegistered(short moduleId, String ipAddress) {
		logger.debug("On instance registered: {}-{}", moduleId, ipAddress);
		List<Short> moduleIdList = registry.getDependentModuleIds(moduleId);
		if (moduleIdList != null && !moduleIdList.isEmpty()) {
			logger.debug("Module {} has following dependent modules {}", moduleId, moduleIdList);
			List<ModuleInstanceInfo> selfInstanceList = registry
					.getModuleInstances(moduleId);
			for(Short id : moduleIdList){
				logger.debug("Send module instance registered notify to module {}", id);
				notifyService.updatedModuleInstances(id, selfInstanceList);
			}
		}
		else{
			logger.debug("No module depend on Module {}-{}", moduleId, ipAddress);
		}
	}

	@Override
	public void onUnRegistered(short moduleId, String ipAddress) {
		Channel channel = registry.getChannelByInstance(ipAddress);
		if(channel.isConnected()){
			logger.debug("Closing the channel {}", channel);
			channel.close();
		}
	/*	logger.debug("on instance unregistered: {}-{}", moduleId, ipAddress);
		List<Short> moduleIdList = registry.getDependentModuleIds(moduleId);
		if (moduleIdList != null && !moduleIdList.isEmpty()) {
			if (moduleIdList != null && !moduleIdList.isEmpty()) {
				logger.debug("Module {} has following dependecny module", moduleId, moduleIdList);
				List<ModuleInfo> selfInstanceList = registry
						.getModuleInstances(moduleId);
				for(Short id : moduleIdList){
					logger.debug("Send module instance unregistered notify to module {}", id);
					notifyService.updatedModuleInstances(id, selfInstanceList);
				}
			}
		}
		else{
			logger.debug("No module depend on Module {}-{}", moduleId, ipAddress);
		}*/
	}
}
