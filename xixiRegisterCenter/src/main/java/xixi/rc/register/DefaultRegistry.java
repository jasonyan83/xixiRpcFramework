package xixi.rc.register;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xix.rc.bean.ModuleStatusInfo;
import xixi.transport.channel.Channel;

public class DefaultRegistry implements Registry {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultRegistry.class);

	private final Map<Short, HashMap<String, ModuleStatusInfo>> modulesMap = new HashMap<Short, HashMap<String, ModuleStatusInfo>>();

	private final Map<String, Channel> instanceChannelMap = new HashMap<String, Channel>();
	private final Map<Channel, String> channelInstanceMap = new HashMap<Channel, String>();

	private final Map<Short, List<Short>> moduleDependenceMap = new HashMap<Short, List<Short>>();

	@Override
	public boolean register(ModuleInfo moduleInfo) throws Exception {
		logger.debug("Registering module :" + moduleInfo);
		boolean succeed = false;
		if (modulesMap.get(moduleInfo.getModuleId()) != null) {
			HashMap<String, ModuleStatusInfo> modulesInstanceMap = modulesMap
					.get(moduleInfo.getModuleId());
			ModuleStatusInfo module = modulesInstanceMap.get(moduleInfo
					.getIpAddress());
			if (module != null) {
				if (!module.isLive()) {
					module.setLive(true);
					logger.warn("模块{}对应的ip{},恢复服务", moduleInfo.getModuleId(),
							moduleInfo.getIpAddress());
					succeed = true;
				} else {
					logger.warn("模块{}已经存在相应的IP地址{}，重复注册？",
							moduleInfo.getModuleId(), moduleInfo.getIpAddress());
					succeed = false;
				}

			} else {
				ModuleStatusInfo moduleStatusInfo = new ModuleStatusInfo();
				moduleStatusInfo.setModuleId(moduleInfo.getModuleId());
				moduleStatusInfo.setIpAddress(moduleInfo.getIpAddress());
				moduleStatusInfo.setWeight(moduleInfo.getWeight());
				moduleStatusInfo.setRegisterTime(new Date());
				moduleStatusInfo.setLastHBTime(new Date());
				moduleStatusInfo.setLive(true);
				moduleStatusInfo.setRouterScheduleType(moduleInfo.getRouterScheduleType());

				modulesInstanceMap.put(moduleInfo.getIpAddress(),
						moduleStatusInfo);

				succeed = true;
			}
		} else {
			HashMap<String, ModuleStatusInfo> modulesInstanceMap = new HashMap<String, ModuleStatusInfo>();
			ModuleStatusInfo moduleStatusInfo = new ModuleStatusInfo();
			moduleStatusInfo.setModuleId(moduleInfo.getModuleId());
			moduleStatusInfo.setIpAddress(moduleInfo.getIpAddress());
			moduleStatusInfo.setWeight(moduleInfo.getWeight());
			moduleStatusInfo.setRegisterTime(new Date());
			moduleStatusInfo.setLastHBTime(new Date());
			moduleStatusInfo.setLive(true);
			moduleStatusInfo.setRouterScheduleType(moduleInfo.getRouterScheduleType());
			modulesInstanceMap.put(moduleInfo.getIpAddress(), moduleStatusInfo);

			modulesMap.put(moduleInfo.getModuleId(), modulesInstanceMap);
			succeed = true;
		}

		return succeed;
	}

	@Override
	public boolean unRegister(short moduleId, String ipAddress) {
		logger.debug("Unregistering module {}-{}" + moduleId, ipAddress);
		boolean succeed = false;
		HashMap<String, ModuleStatusInfo> modulesInstanceMap = modulesMap
				.get(moduleId);
		if (modulesInstanceMap == null
				|| modulesInstanceMap.get(ipAddress) == null) {
			logger.debug("The module " + moduleId + " with instance"
					+ ipAddress + "is not exsit");
		} else {
			modulesInstanceMap.remove(ipAddress);
			logger.debug("unRegistering Module " + moduleId + " with instance "
					+ ipAddress);
			succeed = true;
		}
		return succeed;
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

	@Override
	public void buildModuleDependencyMap(short srcModuleId, short destModuleId) {
		synchronized (this) {
			List<Short> list = moduleDependenceMap.get(srcModuleId);
			if (list == null) {
				list = new ArrayList<Short>();
				list.add(destModuleId);
				moduleDependenceMap.put(srcModuleId, list);
			}
			list.add(destModuleId);
		}
	}

	@Override
	public List<Short> getDependentModuleIds(short moduleId) {
		logger.debug("Get dependent moduleId for moduleId {}", moduleId);
		if (moduleId <= 0) {
			logger.error("invalidate moduleId for getDependentModule, moduleId is "
					+ moduleId);
			return null;
		}
		return moduleDependenceMap.get(moduleId);
	}

	@Override
	public List<ModuleInfo> getModuleInstances(short moduleId) {
		logger.debug("Get module instance for moduleId {}", moduleId);
		Map<String, ModuleStatusInfo> map = modulesMap.get(moduleId);
		List<ModuleInfo> moduleInfoList = new ArrayList<ModuleInfo>();
		if (map!=null&&!map.isEmpty()) {
			for (ModuleStatusInfo moduleStatusInfo : map.values()) {
				ModuleInfo m = new ModuleInfo();
				m.setIpAddress(moduleStatusInfo.getIpAddress());
				m.setModuleId(moduleId);
				m.setWeight(moduleStatusInfo.getWeight());
				m.setRouterScheduleType(moduleStatusInfo.getRouterScheduleType());
				moduleInfoList.add(m);
			}
		}
		return moduleInfoList;
	}

	public ModuleStatusInfo getModuleStatusInfo(short moduleId, String ipAddress) {
		logger.debug("Get module status info for {}-{}", moduleId, ipAddress);
		HashMap<String, ModuleStatusInfo> instanceMap = modulesMap
				.get(moduleId);
		if (instanceMap != null) {
			return instanceMap.get(ipAddress);
		} else {
			return null;
		}
	}

	public boolean updateModuleStatusInfo(ModuleStatusInfo moduleStatusInfo) {
		boolean succeed = false;
		logger.debug("Update moduleStatusInfo for {}", moduleStatusInfo);
		if (modulesMap.get(moduleStatusInfo.getModuleId()) != null) {
			HashMap<String, ModuleStatusInfo> modulesInstanceMap = modulesMap
					.get(moduleStatusInfo.getModuleId());
			ModuleStatusInfo module = modulesInstanceMap.get(moduleStatusInfo
					.getIpAddress());
			if (module != null) {
				if (!module.isLive()) {
					//TODO: if the service is down , it will lose all the stat infomation currently
					//and when it is up again, the register center will see the empty stat info.
					module = moduleStatusInfo;
					logger.warn("模块{}对应的ip{},恢复服务",
							moduleStatusInfo.getModuleId(),
							moduleStatusInfo.getIpAddress());
					modulesInstanceMap.put(moduleStatusInfo.getIpAddress(),
							moduleStatusInfo);
					succeed = true;
				}
				else{
					modulesInstanceMap.put(moduleStatusInfo.getIpAddress(),
							moduleStatusInfo);
					succeed = true;
				}

			} else {
				modulesInstanceMap.put(moduleStatusInfo.getIpAddress(),
						moduleStatusInfo);
				succeed =true;
			}
		} else {
			logger.error("There is NO exsit moduleInstanceMap");
			succeed =false;
		}
		return succeed;
	}

	public Map<Short, HashMap<String, ModuleStatusInfo>> getModulesMap() {
		return this.modulesMap;
	}

	public Channel getChannelByInstance(String ipAddress) {
		return this.instanceChannelMap.get(ipAddress);
	}

	public String getInstanceIpByChannel(Channel channel) {
		return this.channelInstanceMap.get(channel);
	}
}
