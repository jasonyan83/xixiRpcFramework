/**
 * 
 */
package xixi.register.eventBus;

import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.future.Future;




/**
 * @author jason.yan
 *
 */
//TODO: the name eventBus needs to be changed, it is not a eventBus pattern
public interface EventBus {
    
	public Future fireEvent(Invocation inv);
	
	public void registerObserver(String event, Invoker invoker);
	
	public void destory();
}

