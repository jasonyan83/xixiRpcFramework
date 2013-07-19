package xixi.transport.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer implements Server{

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractServer.class);

	protected static final int MAX_RETRY = 20;
	protected static final long RETRY_TIMEOUT = 30 * 1000; // 30s
	
	protected String hostIp;
	
	protected Integer hostPort;

	public void start() {
		logger.info("Server is starting");
		doStart();
	}
	
	public void stop() {
		logger.info("Server is stoping");
		doStop();
	}
	
	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public Integer getHostPort() {
		return hostPort;
	}

	public void setHostPort(Integer hostPort) {
		this.hostPort = hostPort;
	}
	
	protected abstract void doStart();

	protected abstract void doStop();
	
	protected abstract void releaseResource();
}
