package xixi.router.facade;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.router.RouterInitializer;
import xixi.router.direct.DirectRouterInitializer;
import xixi.router.multi.MultiRouterInitializer;
import xixi.router.rc.RCRouterInitializer;
import xixi.router.rc.ZKRouterInitializer;

public class RouterFacade {

	public static void initRouter() {
		RouterInitializer routerInitializer = null;
		String routerName = ConfigUtils.getProperty(Constants.ROUTER_NAME);
		String registryType = ConfigUtils.getProperty(Constants.REGISTRY_TYPE,"rcModuleService");
		if (null == routerName) {
			return; // do nothing if the server do not need client
		} else {
			switch (routerName) {

			case Constants.RC_ROUTER:
				RouterInitializer rcRouterInitializer = new RCRouterInitializer();
				rcRouterInitializer.init();
				routerInitializer = new MultiRouterInitializer(registryType);
				break;
				
			case Constants.ZOOKEEPER_ROUTER:
				ZKRouterInitializer zooKeeperInitializer = new ZKRouterInitializer();
				zooKeeperInitializer.init();
				routerInitializer = new MultiRouterInitializer(registryType);
				break;
			default:
				routerInitializer = new DirectRouterInitializer();
			}
			routerInitializer.init();
		}

	}

}
