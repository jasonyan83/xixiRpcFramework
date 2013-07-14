package xixi.router.direct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.common.util.ModuleStringUtil;
import xixi.router.Router;
import xixi.router.RouterInitializer;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class DirectRouterInitializer implements RouterInitializer{

	private static final Logger logger = 
        	LoggerFactory.getLogger(DirectRouterInitializer.class);
    
	
	protected String routerAddresses;
	
	public DirectRouterInitializer(){
		routerAddresses = ConfigUtils.getProperty(
				Constants.DIRECT_ROUTER_ADDRESSES_KEY, "");
	}
	public void init() {
		logger.debug("Initializing DirectRouter, routerAddresses=" + routerAddresses);
		if (routerAddresses != null && !"".equals(routerAddresses)) {
			String[] routerAddress = routerAddresses.split(",");
			for (String moduleString : routerAddress) {
				String moduleId = ModuleStringUtil.getMoudleId(moduleString);
				String ipAddress = ModuleStringUtil.getIpAddress(moduleString);
				String ip = ModuleStringUtil.getIp(ipAddress);
				int port = ModuleStringUtil.getPort(ipAddress);
				Router r = DirectlyConnectRouter.getOrAddRouter(Short.parseShort(moduleId));
				TcpClient client = TransportFacade.initClient(ip, port);
				r.addTcpClient(client);
			}
		}
	}
}
