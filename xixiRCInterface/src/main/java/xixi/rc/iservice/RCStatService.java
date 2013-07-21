package xixi.rc.iservice;

import xix.rc.bean.ModuleStatusInfo;
import xixi.common.annotation.EventService;

@EventService(name="RCStatService", moduleId=101, version = "1.0")
public interface RCStatService {
	public int sendModuleStatInfo(ModuleStatusInfo info);
}
