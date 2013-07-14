package xix.rc.bean;

import xixi.common.util.ModuleStringUtil;

public class ModuleInfo {

	private short moduleId;
	
	private String ipAddress;

	private int weight;
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

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
	
	public String getIp(){
		if(ipAddress!=null&&!"".equals(ipAddress)){
			return ModuleStringUtil.getIp(ipAddress);
		}
		else{
			return null;
		}
	}
	
	public int getPort(){
		if(ipAddress!=null&&!"".equals(ipAddress)){
			return ModuleStringUtil.getPort(ipAddress);
		}
		else{
			return -1;
		}
	}
}
