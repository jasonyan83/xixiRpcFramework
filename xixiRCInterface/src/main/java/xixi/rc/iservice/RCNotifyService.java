package xixi.rc.iservice;

import java.util.List;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.common.annotation.EventService;

@EventService(name="RCNotifyService", version = "1.0")
public interface RCNotifyService {
	
	public void updatedModuleInstances(short moduleId, List<ModuleInstanceInfo> instancesList);

}
