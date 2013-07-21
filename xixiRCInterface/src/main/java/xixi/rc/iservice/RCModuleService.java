package xixi.rc.iservice;

import java.util.List;

import xix.rc.bean.ModuleInfo;
import xixi.common.annotation.EventService;

@EventService(name="RCModuleService", moduleId=101, version = "1.0")
public interface RCModuleService {

	public int registerModule(ModuleInfo moduleInfo);
	public int unRegisterModule(short moduleId, String ipAddress);	
	public List<ModuleInfo> getInstanceList(short srcModuleId, List<Short> destModuleIdList); 
}
