package xixi.transport.handler;

import xixi.transport.dispatcher.Dispatcher;

public class MessageDispatcherChannelHandler extends ExecutorChannelHandler{
	
	private Dispatcher messageDispatcher;

	@Override
	public void onMessageRecieved(final Object message) {
		this.getExecutor().submit(new Runnable(){
			@Override
			public void run() {	
				messageDispatcher.dispatcher(message);
			}
			
		});

	}

	public Dispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	public void setMessageDispatcher(Dispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}
}
