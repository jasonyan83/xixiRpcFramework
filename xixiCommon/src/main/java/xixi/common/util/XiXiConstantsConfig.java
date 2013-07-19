package xixi.common.util;


public class XiXiConstantsConfig {
	
	  public static int MODULE_HEARTBEAT_INTEVAL = 10*1000;
	  
	  public static int REGISTER_BORADCAST_INTEVAL = 60*1000;
	  
	  public void setHeartbeatInteval(String heartbeatInteval) {
		  MODULE_HEARTBEAT_INTEVAL = Integer.decode(heartbeatInteval);
	    }

	    public void setRegisterBroadcastInteval(String registerBroadcastInteval) {
	    	REGISTER_BORADCAST_INTEVAL =  Integer.decode(registerBroadcastInteval);
	    }
}
