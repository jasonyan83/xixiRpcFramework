package xixi.router.direct;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.common.util.ModuleStringUtil;
import xixi.router.Router;
import xixi.router.RouterInitializer;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class DirectRouterInitializer implements RouterInitializer{

	public void init() {
		String routerAddresses = ConfigUtils.getProperty(
				Constants.DIRECT_ROUTER_ADDRESSES_KEY, "");
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
