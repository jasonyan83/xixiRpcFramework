package xixi.transport.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class ExecutorChannelHandler extends AbstractChannelHandler{

	private ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory(){
		@Override
		public Thread newThread(Runnable r) {
			//TODO: specific ThreadFactory
			return new Thread(r,"ShardThreadPool");
		}
	});

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		if(executor==null){
			throw new IllegalArgumentException();
		}
		this.executor = executor;
	}
}
