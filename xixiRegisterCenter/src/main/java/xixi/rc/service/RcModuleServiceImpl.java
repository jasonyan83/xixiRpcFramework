package xixi.rc.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.common.annotation.EventMethod;
import xixi.common.bean.ModuleInfo;
import xixi.rc.iservice.RCModuleService;
import xixi.rc.register.ModuleInstanceListener;
import xixi.rc.register.Registry;

public class RcModuleServiceImpl implements RCModuleService {

	private static final Logger logger = LoggerFactory
			.getLogger(RcModuleServiceImpl.class);

	private Registry registry;

	private ModuleInstanceListener registerListener;

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public ModuleInstanceListener getRegisterListener() {
		return registerListener;
	}

	public void setRegisterListener(ModuleInstanceListener registerListener) {
		this.registerListener = registerListener;
	}
	
	//TODO:when the server down and then up immediately, the RC might not recognize the same server, it is still wait for the hb to retry
	@Override
	@EventMethod(name = "registerModule", filter = "rcRegisterFilter")
	public int registerModule(ModuleInstanceInfo moduleInfo) {
		boolean ret = false;
		try {
			ret = registry.register(moduleInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret) {
			registerListener.onRegistered(moduleInfo.getModuleId(),
					moduleInfo.getIp());
			return 0;
		} else {
			return -1;
		}
	}


	@Override
	@EventMethod(name = "unRegisterModule", filter = "rcRegisterFilter")
	public int unRegisterModule(short moduleId, String version, String ipAddress) {
		boolean ret = registry.unRegister(moduleId, null, ipAddress);
		if (ret) {
			registerListener.onUnRegistered(moduleId, ipAddress);
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	@EventMethod(name = "getInstanceList")
	public List<ModuleInstanceInfo> getInstanceList(short srcModuleId,
			List<ModuleInfo> destModuleList) {
		if (destModuleList == null || destModuleList.size() == 0) {
			logger.warn("The DestModuldIdList is empty");
			return null;
		}
		List<ModuleInstanceInfo> list = new ArrayList<ModuleInstanceInfo>();
		for (ModuleInfo m : destModuleList) {
			list.addAll(registry.getModuleInstances(m.getModuleId()));
			registry.buildModuleDependencyMap(m.getModuleId(), srcModuleId);
		}
		return list;
	}

}
