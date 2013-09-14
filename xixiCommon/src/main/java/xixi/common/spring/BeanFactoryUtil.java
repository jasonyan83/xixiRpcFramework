package xixi.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryUtil implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory
			.getLogger(BeanFactoryUtil.class);

	private static ApplicationContext context;

	public static ApplicationContext initSpringApplicationContext() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					"classpath*:/spring/applicationContext.xml");
		}
		return context;
	}

	public static Object getBean(String beanName) {
		if (context == null) {
			logger.error("Spring context is null");
			return null;
		}
		return context.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// synchronized(this){
		logger.info("Loading Spring context for BeanFactoryUtil");
		BeanFactoryUtil.context = applicationContext;
		// }
	}

}
