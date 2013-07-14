package xix.rc.bean;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ModuleStatusInfo {
	
	private int moduleId;

	private String ipAddress;

	private long totalTaskCount;

	private long lastMinuteTaskCount;

	private long averageTaskExecTime;

	private Date registerTime;
	
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public long getTotalTaskCount() {
		return totalTaskCount;
	}

	public void setTotalTaskCount(long totalTaskCount) {
		this.totalTaskCount = totalTaskCount;
	}

	public long getLastMinuteTaskCount() {
		return lastMinuteTaskCount;
	}

	public void setLastMinuteTaskCount(long lastMinuteTaskCount) {
		this.lastMinuteTaskCount = lastMinuteTaskCount;
	}

	public long getAverageTaskExecTime() {
		return averageTaskExecTime;
	}

	public void setAverageTaskExecTime(long averageTaskExecTime) {
		this.averageTaskExecTime = averageTaskExecTime;
	}

	private boolean live;
	
	private Date lastHBTime;
	
	private long heartBeatInteval;
	
	public long getHeartBeatInteval() {
		return heartBeatInteval;
	}

	public void setHeartBeatInteval(long heartBeatInteval) {
		this.heartBeatInteval = heartBeatInteval;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Date getLastHBTime() {
		return lastHBTime;
	}

	public void setLastHBTime(Date lastHBTime) {
		this.lastHBTime = lastHBTime;
	}

	public String toString() {

		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



}
