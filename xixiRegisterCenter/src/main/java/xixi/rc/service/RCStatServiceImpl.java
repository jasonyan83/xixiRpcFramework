package xixi.rc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleStatusInfo;
import xixi.common.annotation.EventMethod;
import xixi.rc.iservice.RCStatService;
import xixi.rc.register.Registry;

public class RCStatServiceImpl implements RCStatService {

	private static final Logger logger = LoggerFactory
			.getLogger(RCStatServiceImpl.class);
	
	private Registry registry;
	
	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	@EventMethod(name = "sendModuleStatInfo")
	public int sendModuleStatInfo(ModuleStatusInfo moduleStatusInfo) {
		logger.debug("Get moduleStatInfo " + moduleStatusInfo);
		boolean ret = registry.updateModuleStatusInfo(moduleStatusInfo);
		if(ret){
			return 0;
		}
		return -1;
	}

}