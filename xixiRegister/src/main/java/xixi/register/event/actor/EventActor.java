package xixi.register.event.actor;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import xixi.common.annotation.EventService;
import xixi.register.eventBus.InvokerRegister;

public class EventActor implements BeanPostProcessor{

	private static final Logger logger = LoggerFactory
	.getLogger(EventActor.class);
	
	private InvokerRegister register;

	public EventActor(InvokerRegister register){
		this.register = register;
	}
	public EventActor(){
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		logger.info(">>>entering postProcessBeforeInitialization bean name is {}", beanName);
/*		if(beanName.equals("invokerRegister")){
			logger.info("setting invokerRegister");
			register =(InvokerRegister)bean;
		}*/
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		logger.info("entering postProcessAfterInitialization bean name is {}", beanName);
		if(isEventTemplate(bean)){
			logger.info("register bean {} to event bus", beanName);
			register.registerService(bean);
		}
		return bean;
	}

	private boolean isEventTemplate(Object bean){
		   boolean flag = false;
	        Class<?>[] interfaces = bean.getClass().getInterfaces();
	        for (Class<?> c : interfaces) {
	            Annotation a = c.getAnnotation(EventService.class);
	            if (a == null) {
	                continue;
	            }
	            flag = true;
	            break;
	        }
	        return flag;
	}
	public InvokerRegister getRegister() {
		return register;
	}
	public void setRegister(InvokerRegister register) {
		this.register = register;
	}
}
