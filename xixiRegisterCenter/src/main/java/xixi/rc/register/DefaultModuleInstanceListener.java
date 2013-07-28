package xixi.rc.register;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.rc.iservice.RCNotifyService;

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
		logger.debug("On module registered: {}-{}", moduleId, ipAddress);
		List<Short> moduleIdList = registry.getDependentModuleIds(moduleId);
		if (moduleIdList != null && !moduleIdList.isEmpty()) {
			logger.debug("Module {} has following dependecny module {}", moduleId, moduleIdList);
			List<ModuleInfo> selfInstanceList = registry
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
		logger.debug("on module unregistered: {}-{}", moduleId, ipAddress);
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
		}
	}
}
