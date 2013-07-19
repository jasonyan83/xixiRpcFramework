package xixi.common.util;

import org.apache.commons.lang.StringUtils;

public class ModuleStringUtil {

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
			return  ipAddress.split(":")[0];
			}
		return null;
	}
	
	public static int getPort(String ipAddress){
		if (StringUtils.isNotEmpty(ipAddress)) {
			return  Integer.parseInt(ipAddress.split(":")[1]);
			}
		return 0;
	}
}
