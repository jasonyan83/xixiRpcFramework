package xixi.router.facade;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.router.RouterInitializer;
import xixi.router.direct.DirectRouterInitializer;

public class RouterFacade {


	public static void initRouter(){
		RouterInitializer routerInitializer = null;
		String routerName =  ConfigUtils.getProperty(Constants.ROUTER_NAME);
		if(null==routerName){
			return; //do nothing if the server do not need client
		}
		else{
			if(routerName.equals(Constants.DEFAULT_ROUTER)){
			    routerInitializer = new DirectRouterInitializer();
			}
			routerInitializer.init();
		}

	}
	
}
