package xixi.common.constants;


import java.util.ArrayList;
import java.util.List;

import xixi.common.util.ConfigUtils;

public class Constants {

	   public static final String  DEFAULT_PROPERTIES  = "app.properties";

	   public static final String  PROPERTIES_KEY  = "xixi.properties.file";
	   
	   public static final String  RPC_SERVER_KEY  = "rpc.server";
	   
	   public static final String  RPC_CLIENT_KEY  = "rpc.client";
	   
	   public static final String  RPC_HANDLER  = "rpc.channel.handler";
	   
	   public static final String  RPC_CLIENT_FILTER  = "rpc.client.filter";
	   
	   public static final String  RPC_BEAN_PACKAGE = "rpc.bean.package";
	   
	   public static final int RPC_FIXED_HEAD_SIZE = 32;
	   
	   public static final List<String> RPC_CLINET_FILTER_DEFAULT = new ArrayList<String>();
	   
	   public static final String ROUTER_NAME = "router.name";
	   
	   public static final String DEFAULT_ROUTER = "direct";
	   
	   public static final String RC_ROUTER = "direct-rc";
	   
	   public static final String RC_MODULE_ID = "rc.module.id";
	   
	   public static final String DISPATCHER_KEY = "dispatcher.name";
	   
	   public static final String DEFAULT_DISPATCHER = "invokerBusDispatcher";
	   
	   public static final String LOCAL_IP_KEY = "local.ip";
	   public static String LOCAL_IP;
	   
	   public static final String LOCAL_PORT_KEY ="local.prot";;
	   public static int LOCAL_PORT;
	   
	   public static final String DESCRIPTION = "description";
	   public static final String WEIGHT = "server.weight";
	   
	   //router.addresses=100-127.0.0.1:8080-roundrobin,100-127.0.0.1:8090-weight

	   //router.addresses=100-127.0.0.1:8080,100-127.0.0.1:8090
	   public static final String DIRECT_ROUTER_ADDRESSES_KEY = "direct.router.addresses";
	   public static final String MUTIL_RC_ROUTER_ADDRESSES_KEY = "mutil.router.addresses";
	   public static final String RC_ADDRESSES_KEY = "rc.addresses";
	   public static final String ROUTER_SCHEDULE_TYPE_KEY="router.schedule.type";
	   public static final String DEFAULT_ROUTER_SCHEDULE = "roundrobin";
	   
	   public static final int DEFAULT_FUTURE_TIMEOUT = 3*1000;
	   
	   public static final String SOURCE_MODULEID_KEY = "source.moduleId";
	   public static short SOURCE_MODULEID;
	   static{
		   //TODO：add system default value here
		   RPC_CLINET_FILTER_DEFAULT.add("logger");
		   RPC_CLINET_FILTER_DEFAULT.add("");
		   
		   LOCAL_IP = ConfigUtils.getProperty(LOCAL_IP_KEY);
		   LOCAL_PORT = Integer.parseInt(ConfigUtils.getProperty(LOCAL_PORT_KEY));
		   
		   SOURCE_MODULEID = Short.parseShort(ConfigUtils.getProperty(SOURCE_MODULEID_KEY));
		   
	   }
	   
}
