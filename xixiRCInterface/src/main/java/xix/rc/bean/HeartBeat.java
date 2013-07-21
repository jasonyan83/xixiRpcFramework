package xix.rc.bean;

public class HeartBeat {

	private short moduleId;
	
	private String ipAddress;
	
	private int interval;

	public HeartBeat(){
	}
	
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
	
	public String toString(){
		return this.moduleId + "-" + this.ipAddress + "-" + this.interval;
	}

}
