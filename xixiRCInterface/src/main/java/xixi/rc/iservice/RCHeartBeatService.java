package xixi.rc.iservice;

import xix.rc.bean.HeartBeat;
import xixi.common.annotation.EventService;

@EventService(name="RCModuleService", moduleId=110, version = "1.0")
public interface RCHeartBeatService {

	public void heartBeat(HeartBeat heartBeat);
}
