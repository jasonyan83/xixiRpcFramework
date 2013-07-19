package xixi.register.eventBus;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.annotation.EventService;
import xixi.common.annotation.EventMethod;
import xixi.common.respository.ArgumentTypeRepository;
import xixi.common.spring.BeanFactoryUtil;
import xixi.register.util.PackageUtils;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.filter.Filter;
import xixi.rpc.future.Future;

public final class InvokerRegister {

	private static final Logger logger = LoggerFactory
			.getLogger(InvokerRegister.class);

	private EventBus invokerBus;
	
	public InvokerRegister(){
	
	}
	
	
	public void registerService(Object service) {
		Class<?> cls = service.getClass();
		if (isEventTemplate(cls)) {
			Method[] methods = cls.getMethods();
			for (Method method : methods) {
				if (isTemplateService(method)) {
					registerServiceMethod(service, method);
				}
			}
		} else {
			logger.info("NOT EventTemplate,Object is {}", service);
		}
	}

	public void registerService(Class<?> cls){
		if(isEventTemplate(cls)){
			Method[] methods = cls.getMethods();
			for (Method method : methods) {
				if (isTemplateService(method)) {
					try {
						Object instance = cls.newInstance();
						registerServiceMethod(instance, method);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			logger.info("NOT EventTemplate,Class is {}", cls.getName());
		}
	}
	
	public void registerService(Collection<String> packages) {
		if (null != packages) {
			for (String pkgName : packages) {
				try {
					String[] clsNames = PackageUtils.findClassesInPackage(
							pkgName, null, null);
					for (String clsName : clsNames) {
						try {
							ClassLoader cl = Thread.currentThread()
									.getContextClassLoader();
							if (logger.isDebugEnabled()) {
								logger
										.debug(
												"using ClassLoader {} to load Class {}",
												cl, clsName);
							}
							Class<?> cls = cl.loadClass(clsName);
							
							registerService(cls);

						} catch (ClassNotFoundException e) {
							logger.error("createTypeMetainfo: ", e);
						}
					}
				} catch (IOException e) {
					logger.error("createTypeMetainfo: ", e);
				}
			}
		}
	}


	private void registerServiceMethod(Object instance, Method method) {
		RpcServiceInvoker service = new RpcServiceInvoker(instance, method);
		
		Invoker serviceWrapper = buildFilterChain(service,method);
		
		invokerBus.registerObserver(instance.getClass().getInterfaces()[0].getCanonicalName() + "-"
				+ method.getName(), serviceWrapper);
		logger.info("Registering EventTemplate,event is {}", instance.getClass().getSimpleName() + "-"
				+ method.getName());
		
		Class<?>[] clzs = method.getParameterTypes();

		if(method.getReturnType() != void.class){
			ArgumentTypeRepository.addArgumentType(method.getReturnType().getCanonicalName(), method.getReturnType());
		}
		
		for(Class<?> clz : clzs){
			if(clz!=void.class){
				ArgumentTypeRepository.addArgumentType(instance.getClass().getSimpleName() + "-"
						+ method.getName() ,clz);
			}
		}
	}

	private boolean isEventTemplate(Class<?> cls) {
		 boolean flag = false;
	        Class<?>[] interfaces =cls.getInterfaces();
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


	private boolean isTemplateService(Method method) {
		return null != method.getAnnotation(EventMethod.class);
	}

	static public Method[] getAllMethodsOfClass(Class<?> cls) {
		Method[] methods = new Method[0];

		Class<?> itr = cls;
		while ((null != itr) && !itr.equals(Object.class)) {
			methods = (Method[]) ArrayUtils.addAll(itr.getDeclaredMethods(),
					methods);
			itr = itr.getSuperclass();
		}

		return methods;
	}

	private Invoker buildFilterChain(Invoker service, Method method){
		Invoker serviceWrapper = service;
		EventMethod annotation = method.getAnnotation(EventMethod.class);
		if(annotation!=null&&!annotation.filter().equals("")){
			String[] filterNames  = annotation.filter().split(",");
			for(String filterName : filterNames){
				final Filter filter = (Filter)BeanFactoryUtil.getBean(filterName);
				if(filter!=null){
					final Invoker nextService = serviceWrapper;
					serviceWrapper = new Invoker(){
						@Override
						public Future invoke(Invocation inv) {
							 filter.doFilter(nextService,inv);
							 return inv.future();
						}

						@Override
						public void destory() {
							nextService.destory();		
						}
					};
				}
			}
			
		}
		
		return serviceWrapper;
	}


	public EventBus getInvokerBus() {
		return invokerBus;
	}


	public void setInvokerBus(EventBus invokerBus) {
		this.invokerBus = invokerBus;
	}

}
