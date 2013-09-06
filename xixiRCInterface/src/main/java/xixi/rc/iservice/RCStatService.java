package xixi.rc.iservice;

import xix.rc.bean.ModuleInstanceStatusInfo;
import xixi.common.annotation.EventService;

@EventService(name="RCStatService", moduleId=101, version = "1.0")
public interface RCStatService {
	public int sendModuleStatInfo(ModuleInstanceStatusInfo info);
}
