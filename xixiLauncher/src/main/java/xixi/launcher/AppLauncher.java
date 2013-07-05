package xixi.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.spring.BeanFactoryUtil;
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

		RouterFacade.initRouter();

	}
}
