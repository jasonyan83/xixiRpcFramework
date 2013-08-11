package xixi.transport.handler;

import xixi.common.thread.XixiThreadPool;


public abstract class ExecutorChannelHandler extends AbstractChannelHandler{

	private XixiThreadPool threadPool;

	public XixiThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(XixiThreadPool threadPool) {
		this.threadPool = threadPool;
	}
	
}
