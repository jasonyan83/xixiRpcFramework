package xixi.common.util;

import org.apache.commons.lang.StringUtils;

public class ModuleStringUtil {

	public final static String IP_SEPERATE = ":";
	
	public static String getMoudleId(String moduleString){
		if (StringUtils.isNotEmpty(moduleString)) {
			return  moduleString.split("-")[0];
			}
		return "0";
	}
	
	public static String getIpAddress(String moduleString){
		if (StringUtils.isNotEmpty(moduleString)) {
			return  moduleString.split("-")[1];
			}
		return null;
	}
	
	public static String getIp(String ipAddress){
		if (StringUtils.isNotEmpty(ipAddress)) {
			return  ipAddress.split(IP_SEPERATE)[0];
			}
		return null;
	}
	
	public static int getPort(String ipAddress){
		if (StringUtils.isNotEmpty(ipAddress)) {
			return  Integer.parseInt(ipAddress.split(IP_SEPERATE)[1]);
			}
		return 0;
	}
}
