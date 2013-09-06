package xixi.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.annotation.EventService;
import xixi.common.constants.Constants;
import xixi.common.respository.ArgumentTypeRepository;
import xixi.common.respository.DependencyModuleRepository;
import xixi.common.spring.BeanFactoryUtil;
import xixi.common.util.ConfigUtils;
import xixi.rpc.RpcInvoker;
import xixi.rpc.client.invoker.AbstractClientInvoker;
import xixi.rpc.client.invoker.RpcInvocation;
import xixi.rpc.exception.TimeoutException;
import xixi.rpc.filter.Filter;
import xixi.rpc.future.Future;
import xixi.rpc.future.RpcFuture;

public class ProxyFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(ProxyFactory.class);
	
	private final Map<Class<?>, Object> proxyRepository = new HashMap<Class<?>, Object>();
	// TODO: the invoker is singltion this time ,to make sure the thread safe
	private RpcInvoker invoker;

	private final List<String> filterList = new ArrayList<String>();

	public void init() {
		String filterString = ConfigUtils.getProperty(
				Constants.RPC_CLIENT_FILTER, "default");
		if ("default".equals(filterString)) {
			filterList.addAll(Constants.RPC_CLINET_FILTER_DEFAULT);
		} else {
			String[] filters = filterString.split(",");
			filterList.addAll(Arrays.asList(filters));
		}
		invoker = this.buildInvokeChain();
	}

	private RpcInvoker buildInvokeChain() {
		RpcInvoker invoker = this.invoker;

		for (String filterName : filterList) {
			final Filter filter = (Filter) BeanFactoryUtil.getBean(filterName);
			final RpcInvoker i = invoker;
			invoker = new AbstractClientInvoker() {
				@Override
				public RpcFuture invoke(RpcInvocation inv) {
					filter.doFilter(i, inv);
					return inv.future();
				}

				@Override
				public void destory() {
					i.destory();
				}
			};
		}

		return invoker;
	}

	public Object createProxy(Class<?> serviceInterface) {
		Object proxy = Proxy.newProxyInstance(
				serviceInterface.getClassLoader(),
				new Class<?>[] { serviceInterface },
				new DefaultInvocationHandler(invoker));
		proxyRepository.put(serviceInterface, proxy);

		EventService annotation = serviceInterface
				.getAnnotation(EventService.class);
		if (annotation != null) {
			DependencyModuleRepository.addDepedentModuleId(annotation);
		}

		for (Method m : serviceInterface.getDeclaredMethods()) {
			for (Class<?> clz : m.getParameterTypes()) {
				ArgumentTypeRepository.addArgumentType(
						serviceInterface.getCanonicalName() + "-" + m.getName()
								+ "-" + clz.getCanonicalName(), clz);
			}
		}

		return proxy;
	}

	public <T> T getProxy(Class<T> serviceInterface) {
		return (T) proxyRepository.get(serviceInterface);
	}

	private class DefaultInvocationHandler implements InvocationHandler {

		private RpcInvoker invoker;

		private DefaultInvocationHandler(RpcInvoker invoker) {
			this.invoker = invoker;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			if (method.getName().equals("toString")
					|| method.getName().equals("hashCode")) {
				method.invoke(proxy);
			}

			RpcInvocation inv = new RpcInvocation(method.getDeclaringClass()
					.getCanonicalName(), method.getName(), args);

			EventService annotation = method.getDeclaringClass().getAnnotation(
					EventService.class);

			if (annotation != null) {
				inv.setModuleId(annotation.moduleId());
			} else {
				inv.setModuleId((short) 100);// TODO: set a configurable default
												// dest moduleId
			}

			if (method.getReturnType() == Future.class) {
				return invoker.invoke(inv);
			} else {
				// sync invoke
				try{
					return invoker.invoke(inv).getValue();
				}
				catch(TimeoutException e){
					logger.error("Invoke TIMEOUT for {}", method.getName());
					return null;
				}

			}
		}
	}

	public RpcInvoker getInvoker() {
		return invoker;
	}

	public void setInvoker(RpcInvoker invoker) {
		this.invoker = invoker;
	}
}
