package xix.rc.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import xixi.common.annotation.XixiBean;

@XixiBean(id=300101)
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
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
