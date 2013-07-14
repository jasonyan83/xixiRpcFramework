package xixi.rc.service;

import xix.rc.bean.HeartBeat;
import xixi.rc.iservice.RCHeartBeatService;
import xixi.transport.channel.Channel;

public class RCHeartBeatServiceImpl implements RCHeartBeatService{


	@Override
	public void heartBeat(Channel channel){
		HeartBeat hb = new HeartBeat(moduleId,ipAddress,interval);
		channel.send(hb);
	}
}
