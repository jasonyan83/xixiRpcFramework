package xixi.monitor.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.spring.BeanFactoryUtil;
import xixi.router.facade.RouterFacade;
import xixi.transport.facade.TransportFacade;

public class DefaultMonitorLauncher {
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultMonitorLauncher.class);


	public static void main(String[] args) {

		logger.debug("DefaultMonitorLauncher Started~~~~~~~~~!!!");

		BeanFactoryUtil.initSpringApplicationContext();

		TransportFacade.initServer();

		RouterFacade.initRouter();
	}
}
