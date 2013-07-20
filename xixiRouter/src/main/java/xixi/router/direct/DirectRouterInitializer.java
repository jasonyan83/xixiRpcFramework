package xixi.router.direct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.common.util.ModuleStringUtil;
import xixi.router.DefaultConnectRouter;
import xixi.router.Router;
import xixi.router.RouterInitializer;
import xixi.router.schedule.RouterSchedules;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class DirectRouterInitializer implements RouterInitializer{

	private static final Logger logger = 
        	LoggerFactory.getLogger(DirectRouterInitializer.class);
    
	
	protected String routerAddresses;
	
	protected String[] routerAddressArray = new String[]{};
	
	public DirectRouterInitializer(){
		routerAddresses = ConfigUtils.getProperty(
				Constants.DIRECT_ROUTER_ADDRESSES_KEY, "");
		if (routerAddresses != null && !"".equals(routerAddresses)) {
			routerAddressArray = routerAddresses.split(",");
		}
	}
	public void init() {
		logger.debug("Initializing DirectRouter, routerAddresses=" + routerAddresses);
			for (String addressConfig : routerAddressArray) {
				String[] strs =addressConfig.split("-");
				String moduleString = null;
				String routerScheduleType = null;
				if(strs.length==3){
					moduleString = addressConfig;
					routerScheduleType = Constants.DEFAULT_ROUTER_SCHEDULE;
				}
				else if(strs.length==2){
					moduleString = addressConfig;
				}
				else{
					logger.equals("Invalidate directRouterAddress property, Initialize DirectRouter FAILED!!");
					return;
				}
				String moduleId = ModuleStringUtil.getMoudleId(moduleString);
				String ipAddress = ModuleStringUtil.getIpAddress(moduleString);
				String ip = ModuleStringUtil.getIp(ipAddress);
				int port = ModuleStringUtil.getPort(ipAddress);
				Router r = DefaultConnectRouter.getOrAddRouter(Short.parseShort(moduleId));
				if(routerScheduleType!=null){
					//Current not support schedule type set for RegisterCenter, if the routerScheduleType
					//is not null, then it must be  directlyConnectRouter for biz module server
					RouterSchedules.setModuleScheduleType(Short.valueOf(moduleId), "default");
				}
			
				TcpClient client = TransportFacade.initClient(ip, port);
				r.addTcpClient(client);
			}
	}
}
