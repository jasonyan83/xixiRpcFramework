package xixi.rc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.spring.BeanFactoryUtil;

public class RCServiceInitializer {

	private static final Logger logger = LoggerFactory
			.getLogger(RCServiceInitializer.class);
	
	public static void init(){
		logger.info("Initialzing RC related service and job");
		//RCServiceScheduledJob job = (RCServiceScheduledJob)BeanFactoryUtil.getBean("rcServiceScheduleJob");
		RCNotifyServiceImpl rcNotifyService =(RCNotifyServiceImpl)BeanFactoryUtil.getBean("rcNotifyService");
	}
}
