package xixi.rc.iservice;

import xixi.common.annotation.EventService;
import xixi.monitor.dashboard.ModuleInstanceStatusInfo;

@EventService(name="RCStatService", moduleId=101, version = "1.0")
public interface RCStatService {
	public int sendModuleStatInfo(ModuleInstanceStatusInfo info);
}
