package xixi.rc.service;

import xix.rc.bean.HeartBeat;
import xixi.transport.channel.Channel;

public class HeartBeatService {

	private short moduleId;
	
	private String ipAddress;
	
	private int interval;
	
	public short getModuleId() {
		return moduleId;
	}

	public void setModuleId(short moduleId) {
		this.moduleId = moduleId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void sendHeartBeat(Channel channel){
		HeartBeat hb = new HeartBeat(moduleId,ipAddress,interval);
		channel.send(hb);
	}
}
