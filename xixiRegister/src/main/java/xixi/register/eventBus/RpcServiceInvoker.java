package xixi.register.eventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.bean.NullValue;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.future.Future;

public class RpcServiceInvoker implements Invoker {

	private static final Logger logger = LoggerFactory
			.getLogger(RpcServiceInvoker.class);

	private final Object target;

	private final Method method;

	private boolean canceled = false;

	private Predicate predicate;
	
	public RpcServiceInvoker(Object target, Method method) {
		this.target = target;
		this.method = method;
	}

	@Override
	public Future invoke(Invocation inv) {
		logger.debug("Invoking with Invocation {}" , inv);
		if(canceled){
			//TODO:more logic code here
			logger.warn("sever stop");
		}
		Future f = inv.future();
		if(this.predicate!=null&&!this.predicate.evaluate(inv.getArgs())){
			//logger.warn("task [" + task + "] not acceptable, just ignore");
			//TODO:后期加failed reason
			f.fail();
			return f;
		}

		try {
			Object result;
			if(inv.getArgs()==null||inv.getArgs()[0]==null){
				 result = method.invoke(target);
			}
			else{
				 result = method.invoke(target,inv.getArgs());
			}
			
			if(result==null){
				f.setValue(new NullValue());
			}
			else{
				f.setValue(result);
			}
			//TODO: more test here
			/*if(result!=null&&!result.getClass().equals(Void.class)){

			}*/
			f.done();

		} catch (IllegalArgumentException e) {
			logger.error("invoker fail for IllegalArgumentException, exception is " + e.getMessage());
			f.fail();
		} catch (IllegalAccessException e) {
			logger.error("invoker fail for IllegalAccessException, exception is " + e.getMessage());
			f.fail();
		} catch (InvocationTargetException e) {
			logger.error("invoker fail for InvocationTargetException, exception is " + e.getMessage());
			f.fail();
		}
		return f;
	}

	@Override
	public String toString() {
		return target.getClass().getName() + "-" + method.getName();
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public void destory() {
		canceled = true;
		
	}
	
	/*	public RpcServiceInvoker(Object target, String methodName) {
	this.target = target;
	if (null == this.target) {
		throw new RuntimeException(" target is null.");
	}

	Method[] methods = null;
	Class<?> itr = target.getClass();
	while (!itr.equals(Object.class)) {
		methods = (Method[]) ArrayUtils.addAll(itr.getDeclaredMethods(),
				methods);
		itr = itr.getSuperclass();
	}
	for (Method methodItr : methods) {
		if (methodItr.getName().equals(methodName)) {
			methodItr.setAccessible(true);
			this.method = methodItr;
		}
	}
	if (null == this.method) {
		throw new RuntimeException("method [" + target.getClass() + "."
				+ methodName + "] !NOT! exist.");
	}
}*/
}
