package xixi.rpc.dispatcher.xixi;

import xixi.register.eventBus.EventBus;
import xixi.transport.dispatcher.Dispatcher;

public abstract class AbstractDispatcher implements Dispatcher{

	private EventBus eventBus;
	
	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
