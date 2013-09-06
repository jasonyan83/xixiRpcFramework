package xixi.rc.register;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.MBeanExporter;

import xix.rc.bean.ModuleInstanceInfo;
import xix.rc.bean.ModuleInstanceStatusInfo;
import xixi.transport.channel.Channel;

//If RC is down, defaultRegisgry will lose all the the registry information since it stores all the 
//information to memory. When the RC is up again, the module instance will do the registry process again
//and send all the module dependence relationship to rc to help to build the dependency
public class DefaultRegistry extends AbstractRegistry implements Registry {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultRegistry.class);

	//Thread Unsafe
	private final ConcurrentHashMap<Short, HashMap<String, ModuleInstanceStatusInfo>> modulesMap = new ConcurrentHashMap<Short, HashMap<String, ModuleInstanceStatusInfo>>();

	//Thread Unsafe
	private final ConcurrentHashMap<Short, List<Short>> moduleDependenceMap = new ConcurrentHashMap<Short, List<Short>>();

	private MBeanExporter jmxExporter;
	
	public MBeanExporter getJmxExporter() {
		return jmxExporter;
	}

	public void setJmxExporter(MBeanExporter jmxExporter) {
		this.jmxExporter = jmxExporter;
	}

	@Override
	public boolean register(ModuleInstanceInfo moduleInfo) throws Exception {
		logger.debug("Registering module :" + moduleInfo);
		boolean succeed = false;
		if (modulesMap.get(moduleInfo.getModuleId()) != null) {
			HashMap<String, ModuleInstanceStatusInfo> modulesInstanceMap = modulesMap
					.get(moduleInfo.getModuleId());
			ModuleInstanceStatusInfo module = modulesInstanceMap.get(moduleInfo
					.getIpAddress());
			if (module != null) {
				if(!moduleInfo.isRcConnectLost()){
					//RC stays live
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
				}
				else{
					//RC was down and reboot again.
					module.setLive(true);
					module.setRegisterTime(new Date());
					succeed = true;
					logger.info("Module {} registered again after RCreboot", moduleInfo);
				}

			} else {
				ModuleInstanceStatusInfo moduleStatusInfo = new ModuleInstanceStatusInfo()
						.buildModuleStatusInfo(moduleInfo);
				modulesInstanceMap.put(moduleInfo.getIpAddress(),
						moduleStatusInfo);
				succeed = true;
			}
		} else {
			HashMap<String, ModuleInstanceStatusInfo> modulesInstanceMap = new HashMap<String, ModuleInstanceStatusInfo>();
			ModuleInstanceStatusInfo moduleStatusInfo = new ModuleInstanceStatusInfo()
					.buildModuleStatusInfo(moduleInfo);
			modulesInstanceMap.put(moduleInfo.getIpAddress(), moduleStatusInfo);

			modulesMap.put(moduleInfo.getModuleId(), modulesInstanceMap);
			succeed = true;
		}
		logger.debug("The result of registry is:" + (succeed==true?"SUCCEED!":"FAILED"));
		return succeed;
	}

	@Override
	public boolean unRegister(short moduleId, String version, String ipAddress) {
		logger.debug("Unregistering module {}-{}" + moduleId, ipAddress);
		boolean succeed = false;
		HashMap<String, ModuleInstanceStatusInfo> modulesInstanceMap = modulesMap
				.get(moduleId);
		if (modulesInstanceMap == null
				|| modulesInstanceMap.get(ipAddress) == null) {
			logger.debug("The module " + moduleId + " with instance"
					+ ipAddress + "is not exsit");
		} else {
			ModuleInstanceStatusInfo m = modulesInstanceMap.remove(ipAddress);
			logger.debug("unRegistering Module " + moduleId + " with instance "
					+ ipAddress);
			succeed = true;
			this.removeInstance(moduleId, ipAddress);

		}
		return succeed;
	}

	@Override
	public void buildModuleDependencyMap(short moduleId,short dependentModuleId) {
		logger.debug(
				"build module dependency map for source module {}, depentdent module {}",
				moduleId, dependentModuleId);
			List<Short> list = moduleDependenceMap.get(moduleId);
			if (list == null) {
				list = new ArrayList<Short>();
				list.add(dependentModuleId);
				moduleDependenceMap.putIfAbsent(moduleId, list);
			}
			if(!list.contains(dependentModuleId)){
				list.add(dependentModuleId);
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
	public List<ModuleInstanceInfo> getModuleInstances(short moduleId) {
		logger.debug("Get module instance for moduleId {}", moduleId);
		Map<String, ModuleInstanceStatusInfo> map = modulesMap.get(moduleId);
		List<ModuleInstanceInfo> moduleInfoList = new ArrayList<ModuleInstanceInfo>();

		if (map!=null&&!map.isEmpty()) {
			for (ModuleInstanceStatusInfo moduleStatusInfo : map.values()) {
				ModuleInstanceInfo m = new ModuleInstanceInfo();
				m.setIpAddress(moduleStatusInfo.getIpAddress());
				m.setModuleId(moduleId);
				m.setWeight(moduleStatusInfo.getWeight());
				m.setRouterScheduleType(moduleStatusInfo
						.getRouterScheduleType());
				moduleInfoList.add(m);
			}
		}
		return moduleInfoList;
	}

	public ModuleInstanceStatusInfo getModuleStatusInfo(short moduleId, String ipAddress) {
		logger.debug("Get module status info for {}-{}", moduleId, ipAddress);
		HashMap<String, ModuleInstanceStatusInfo> instanceMap = modulesMap
				.get(moduleId);
		if (instanceMap != null) {
			return instanceMap.get(ipAddress);
		} else {
			return null;
		}
	}

	public boolean updateModuleStatusInfo(ModuleInstanceStatusInfo moduleStatusInfo) {
		boolean succeed = false;
		logger.debug("Update moduleStatusInfo for {}", moduleStatusInfo);
		if (modulesMap.get(moduleStatusInfo.getModuleId()) != null) {
			HashMap<String, ModuleInstanceStatusInfo> modulesInstanceMap = modulesMap
					.get(moduleStatusInfo.getModuleId());
			ModuleInstanceStatusInfo module = modulesInstanceMap.get(moduleStatusInfo
					.getIpAddress());
			if (module != null) {
				logger.debug("Current module is {}", module);
				if (!module.isLive()) {
					//if the service is down , it will lose all the stat infomation currently
					//and when it is up again, the register center will see the empty stat info.
					module = module.updateModuleStatusInfo(moduleStatusInfo);
					logger.warn("模块{}对应的ip{},恢复服务",
							moduleStatusInfo.getModuleId(),
							moduleStatusInfo.getIpAddress());
					succeed = true;

				}
				else{
	                module = module.updateModuleStatusInfo(moduleStatusInfo);
					modulesInstanceMap.put(moduleStatusInfo.getIpAddress(),
							module);
					succeed = true;
				}

			} else {
				logger.error("There is No exsit module instance");
				succeed = false;
			}
		} else {
			logger.error("There is NO exsit moduleInstanceMap");
			succeed = false;
		}
		return succeed;
	}
	
	public void init(){
		if ( null != jmxExporter ) {
			ObjectName objectName;
			try {
				objectName = new ObjectName("prefix:class=rc,group=registry,name=dashboard");
				jmxExporter.registerManagedResource(new RegistryDashBoard(), objectName);
			} catch (Exception e) {
				logger.error("registerMBean", e);
			}
		}
	}
	public class RegistryDashBoard{
		public Map<String,List<String>> getModulesMapInfo(){
			Map<String,List<String>> retMap = new HashMap<String,List<String>>();
			for(Short key : modulesMap.keySet()){
				if(retMap.get(key.toString())!=null){
					List<String> statusInfoList = retMap.get(key.toString());
					HashMap<String,ModuleInstanceStatusInfo> statusInfoMap = modulesMap.get(key);
				    for(ModuleInstanceStatusInfo statusInfo : statusInfoMap.values()){
				    	statusInfoList.add(statusInfo.toString());
				    }
				    retMap.put(key.toString(), statusInfoList);
				}
				else{
					List<String> statusInfoList = new ArrayList<String>();
					HashMap<String,ModuleInstanceStatusInfo> statusInfoMap = modulesMap.get(key);
					 for(ModuleInstanceStatusInfo statusInfo : statusInfoMap.values()){
					    	statusInfoList.add(statusInfo.toString());
					    }
					retMap.put(key.toString(), statusInfoList);
				}
			}
			return retMap;
		}
	}
	

	protected void deactiveInstance(short moduleId, String ipAddress){
		Map<String, ModuleInstanceStatusInfo> map = this.modulesMap.get(moduleId);
		if(map!=null){
			ModuleInstanceStatusInfo m = map.get(ipAddress);
			if(m!=null){
				m.setLive(false);
				logger.debug("Setting instance {}-{} to unactive", moduleId, ipAddress);
			}
			else{
				logger.debug("No Module Instance info for {}-{}", moduleId, ipAddress);
			}
		}
		else{
			logger.debug("No Module Info for module {}", moduleId);
		}
	}
	
	public Map<Short, List<Short>> getModuleDependenceMap() {
		return moduleDependenceMap;
	}

	@Override
	public List<ModuleInstanceStatusInfo> getAllModules() {
		List<ModuleInstanceStatusInfo> list = new ArrayList<ModuleInstanceStatusInfo>();
		for(Entry<Short, HashMap<String, ModuleInstanceStatusInfo>> entry: modulesMap.entrySet()){
			for(ModuleInstanceStatusInfo info :entry.getValue().values()){
				list.add(info);
			}
		}
		return list;
	}

}
