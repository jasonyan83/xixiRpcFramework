package xix.rc.bean;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import xixi.common.annotation.XixiBean;

@XixiBean(id=300103)
public class ModuleStatusInfo {
	
	private short moduleId;

	private String ipAddress;

	private long totalTaskCount;

	private long lastMinuteTaskCount;

	private long averageTaskExecTime;
	
	private long lastMinuteTaskExecTime;

	private Date registerTime;
	
	private int weight;
	
	private boolean live;
	
	private Date lastHBTime;
	
	private long heartBeatInteval;
	
	private int heartBeatRetryTimes = 0;
	
	private String routerScheduleType;
	
	public String getRouterScheduleType() {
		return routerScheduleType;
	}

	public void setRouterScheduleType(String routerScheduleType) {
		this.routerScheduleType = routerScheduleType;
	}

	public int getHeartBeatRetryTimes() {
		return heartBeatRetryTimes;
	}

	public void setHeartBeatRetryTimes(int heartBeatRetryTimes) {
		this.heartBeatRetryTimes = heartBeatRetryTimes;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

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
	
	public long getLastMinuteTaskExecTime() {
		return lastMinuteTaskExecTime;
	}

	public void setLastMinuteTaskExecTime(long lastMinuteTaskExecTime) {
		this.lastMinuteTaskExecTime = lastMinuteTaskExecTime;
	}
	
	public ModuleStatusInfo buildModuleStatusInfo(ModuleInfo moduleInfo){
		this.setModuleId(moduleInfo.getModuleId());
		this.setIpAddress(moduleInfo.getIpAddress());
		this.setWeight(moduleInfo.getWeight());
		this.setRegisterTime(new Date());
		this.setLastHBTime(new Date());
		this.setLive(true);
		this.setRouterScheduleType(moduleInfo.getRouterScheduleType());	
		return this;
	}
	
	//The router schedule type will stay the same in the moduleStatusInfo update notify
	public ModuleStatusInfo updateModuleStatusInfo(ModuleStatusInfo moduleStatusInfo){
		this.setWeight(moduleStatusInfo.getWeight());
		this.setLastHBTime(new Date());
		this.setLive(true);
		this.setAverageTaskExecTime(moduleStatusInfo.getAverageTaskExecTime());
		this.setLastMinuteTaskCount(moduleStatusInfo.getLastMinuteTaskCount());
		this.setTotalTaskCount(moduleStatusInfo.getTotalTaskCount());
		this.setLastMinuteTaskExecTime(moduleStatusInfo.getLastMinuteTaskExecTime());
		return this;
	}

}
