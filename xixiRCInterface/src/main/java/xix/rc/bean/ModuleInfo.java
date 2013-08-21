package xix.rc.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import xixi.common.annotation.XixiBean;
import xixi.common.util.ModuleStringUtil;

@XixiBean(id=300102)
public class ModuleInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4648995679231228617L;

	private short moduleId;

	private String ipAddress;

	private int weight;

	private String routerScheduleType="";

	private String description="";
	
	private boolean rcConnectLost = false;


	public String getDescription() {
		return description;
		
	}

	public boolean isRcConnectLost() {
		return rcConnectLost;
	}

	public void setRcConnectLost(boolean rcConnectLost) {
		this.rcConnectLost = rcConnectLost;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRouterScheduleType() {
		return routerScheduleType;
	}

	public void setRouterScheduleType(String routerScheduleType) {
		this.routerScheduleType = routerScheduleType;
	}

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

	public String getIp() {
		if (ipAddress != null && !"".equals(ipAddress)) {
			return ModuleStringUtil.getIp(ipAddress);
		} else {
			return null;
		}
	}

	public int getPort() {
		if (ipAddress != null && !"".equals(ipAddress)) {
			return ModuleStringUtil.getPort(ipAddress);
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
