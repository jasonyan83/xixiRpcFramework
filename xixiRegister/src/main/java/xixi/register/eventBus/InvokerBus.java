/**
 * 
 */
package xixi.register.eventBus;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.future.Future;
/**
 * @author jason.yan
 * 
 */
public class InvokerBus implements EventBus{

	//TODO: using a single thread to dispatch the event;
	private static final Logger logger = LoggerFactory.getLogger(InvokerBus.class);

	private static final Map<String, Invoker> invokerSet = new HashMap<String, Invoker>();

	public Future fireEvent(Invocation inv){
		String key = inv.rpcInterface() + "-" + inv.rpcMethod();
		Invoker invoker = invokerSet.get(key);
		return invoker.invoke(inv);
	}

	@Override
	public void registerObserver(String event,
			final Invoker service) {
		Invoker ser = invokerSet.get(event);
		if (ser == null) {
			invokerSet.put(event, service);
		} else {
			logger.error("duplicated register event:{}", event);
		}
	}


	@Override
	public void destory() {
		//TODO: need sync code here and wait for the execution to be done before destory.
		for(Invoker invoker : invokerSet.values()){
			invoker.destory();
		}
		invokerSet.clear();
	}
}
