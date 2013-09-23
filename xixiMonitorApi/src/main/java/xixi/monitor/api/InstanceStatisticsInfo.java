package xixi.monitor.api;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import xixi.common.annotation.XixiBean;

@XixiBean(id=300103)
public class InstanceStatisticsInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3010603398012468028L;

	private short moduleId;

	private String ipAddress;

	private long lastMinuteTaskCount;
	
	private long lastMinuteTaskATT;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	private String serviceName;
	
	private Date date;
	
	public long getLastMinuteTaskCount() {
		return lastMinuteTaskCount;
	}

	public void setLastMinuteTaskCount(long lastMinuteTaskCount) {
		this.lastMinuteTaskCount = lastMinuteTaskCount;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public long getLastMinuteTaskATT() {
		return lastMinuteTaskATT;
	}

	public void setLastMinuteTaskATT(long lastMinuteTaskATT) {
		this.lastMinuteTaskATT = lastMinuteTaskATT;
	}


}
