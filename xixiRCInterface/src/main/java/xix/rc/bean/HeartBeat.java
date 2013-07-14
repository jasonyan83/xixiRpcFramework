package xix.rc.bean;

public class HeartBeat {

	private final short moduleId;
	
	private final String ipAddress;
	
	private final int interval;

	public HeartBeat(short moduleId, String ipAddress, int interval) {
		this.moduleId = moduleId;
		this.ipAddress = ipAddress;
		this.interval = interval;
	}

	public short getModuleId() {
		return moduleId;
	}


	public String getIpAddress() {
		return ipAddress;
	}


	public int getInterval() {
		return interval;
	}

}
