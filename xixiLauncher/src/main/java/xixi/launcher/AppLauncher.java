package xixi.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.common.spring.BeanFactoryUtil;
import xixi.common.util.ConfigUtils;
import xixi.rc.service.RCServiceInitializer;
import xixi.router.facade.RouterFacade;
import xixi.transport.facade.TransportFacade;

public class AppLauncher {

	private static final Logger logger = LoggerFactory
			.getLogger(AppLauncher.class);


	public static void main(String[] args) {

		if(logger.isDebugEnabled()){
			logger.debug("APP Started~~~~~~~~~!!!");
		}

		BeanFactoryUtil.initSpringApplicationContext();
		
		TransportFacade.initServer();

		String routerName = ConfigUtils.getProperty(Constants.ROUTER_NAME);
		if(routerName!=null&&routerName.equals(Constants.RC_ROUTER)){
			RCServiceInitializer.init();
		}
		RouterFacade.initRouter();

	}
}
