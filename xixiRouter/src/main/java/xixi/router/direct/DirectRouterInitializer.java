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
			for (String moduleString : routerAddressArray) {
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
