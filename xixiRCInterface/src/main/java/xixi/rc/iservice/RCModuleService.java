package xixi.rc.iservice;

import java.util.List;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.common.annotation.EventService;
import xixi.common.bean.ModuleInfo;

@EventService(name="RCModuleService", moduleId=101, version = "1.0")
public interface RCModuleService {

	public int registerModule(ModuleInstanceInfo moduleInfo);
	public int unRegisterModule(short moduleId, String version, String ipAddress);	
	public List<ModuleInstanceInfo> getInstanceList(short srcModuleId, List<ModuleInfo> destModuleList); 
}
