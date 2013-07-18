package xixi.rc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.spring.BeanFactoryUtil;

public class RCLauncher {
	private static final Logger logger = LoggerFactory
			.getLogger(RCLauncher.class);


	public static void main(String[] args) {

		if(logger.isDebugEnabled()){
			logger.debug("Register Center Started~~~~~~~~~!!!");
		}

		BeanFactoryUtil.initSpringApplicationContext();

	}
}
