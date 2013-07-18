package xixi.rc.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.common.annotation.EventMethod;
import xixi.rc.iservice.RCModuleService;
import xixi.rc.register.RegisterListener;
import xixi.rc.register.Registry;

public class RcModuleServiceImpl implements RCModuleService {

	private static final Logger logger = LoggerFactory
			.getLogger(RcModuleServiceImpl.class);

	private Registry registry;

	private RegisterListener registerListener;

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	@EventMethod(name = "registerModule", filter = "rcRegisterFilter")
	public int registerModule(ModuleInfo moduleInfo) {
		boolean ret = false;
		try {
			ret = registry.register(moduleInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret) {
			registerListener.onRegistered(moduleInfo.getModuleId(), moduleInfo.getIp());
			return 0;
		} else {
			return -1;
		}
	}

	public RegisterListener getRegisterListener() {
		return registerListener;
	}

	public void setRegisterListener(RegisterListener registerListener) {
		this.registerListener = registerListener;
	}

	@Override
	@EventMethod(name = "unRegisterModule", filter = "rcRegisterFilter")
	public int unRegisterModule(short moduleId, String ipAddress) {
		boolean ret = registry.unRegister(moduleId, ipAddress);
		if (ret) {
			registerListener.onRegistered(moduleId, ipAddress);
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	@EventMethod(name = "getInstanceList")
	public List<ModuleInfo> getInstanceList(short srcModuleId,
			List<Short> destModuleIdList) {
		if (destModuleIdList == null || destModuleIdList.size() == 0) {
			logger.warn("The DestModuldIdList is empty");
			return null;
		}
		List<ModuleInfo> list = new ArrayList<ModuleInfo>();
		for (Short destModuleId : destModuleIdList) {
			list.addAll(registry.getModuleInstances(destModuleId));
			registry.buildModuleDependencyMap(srcModuleId, destModuleId);
		}
		return list;
	}

}
