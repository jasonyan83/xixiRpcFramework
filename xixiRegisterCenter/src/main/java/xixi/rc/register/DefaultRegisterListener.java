package xixi.rc.register;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.rc.iservice.RCNotifyService;
import xixi.transport.channel.Channel;

public class DefaultRegisterListener implements RegisterListener {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultRegisterListener.class);
	
	private Registry registry;
	private RCNotifyService notifyService;
	
	@Override
	public void onRegistered(short moduleId, String ipAddress) {
		List<Short> moduleIdList = registry.getDependentModuleIds(moduleId);
		if (moduleIdList != null && !moduleIdList.isEmpty()) {

			List<ModuleInfo> selfInstanceList = registry
					.getModuleInstances(moduleId);

			notifyService.updatedModuleInstances(moduleId, selfInstanceList);
		}
		else{
			logger.debug("No module depend on Module {0}-{1}", moduleId, ipAddress);
		}
	}

	@Override
	public void onUnRegistered(short moduleId, String ipAddress) {
		List<Short> moduleIdList = registry.getDependentModuleIds(moduleId);
		if (moduleIdList != null && !moduleIdList.isEmpty()) {

			List<ModuleInfo> selfInstanceList = registry
					.getModuleInstances(moduleId);

			notifyService.updatedModuleInstances(moduleId, selfInstanceList);
		}
		else{
			logger.debug("No module depend on Module {0}-{1}", moduleId, ipAddress);
		}
	}
}
