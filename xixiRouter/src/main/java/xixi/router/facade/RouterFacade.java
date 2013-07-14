package xixi.router.facade;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.router.RouterInitializer;
import xixi.router.direct.DirectRouterInitializer;
import xixi.router.direct.RCRouterInitializer;
import xixi.router.multi.MutilRouterInitializer;

public class RouterFacade {

	public static void initRouter() {
		RouterInitializer routerInitializer = null;
		String routerName = ConfigUtils.getProperty(Constants.ROUTER_NAME);
		if (null == routerName) {
			return; // do nothing if the server do not need client
		} else {
			switch (routerName) {

			case Constants.RC_ROUTER:
				RouterInitializer rcRouterInitializer = new RCRouterInitializer();
				rcRouterInitializer.init();
				routerInitializer = new MutilRouterInitializer();
			default:
				routerInitializer = new DirectRouterInitializer();
			}
			routerInitializer.init();
		}

	}

}
