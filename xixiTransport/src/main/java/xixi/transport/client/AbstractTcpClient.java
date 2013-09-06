package xixi.transport.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.bean.AbstractLBProperty;
import xixi.transport.channel.Channel;
import xixi.transport.handler.ChannelHandler;
import xixi.transport.listener.ConnectorListener;

public abstract class AbstractTcpClient extends AbstractLBProperty implements TcpClient {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractTcpClient.class);

	protected ScheduledExecutorService exec = Executors
			.newSingleThreadScheduledExecutor();

	private short moduleId;

	private String destIp = null;
	private int destPort = -1;
	private String name;

	protected ChannelHandler channelHandler;

	protected Channel channel = null;

	protected long retryTimeout = 10 * 1000;
	
	protected int maxRetryTimes = 3;
	
	public int getMaxRetryTimes() {
		return maxRetryTimes;
	}

	public void setMaxRetryTimes(int maxRetryTimes) {
		this.maxRetryTimes = maxRetryTimes;
	}
	
	protected AtomicInteger retryTimes = new AtomicInteger(1);

	public AbstractTcpClient(String destIp, int despPort, String name) {
		this.destIp = destIp;
		this.destPort = despPort;
		this.name = name;
	}

	public void start() {
		doConnect();
	}

	public void stop() {
		this.exec.shutdownNow();
		if (channel != null) {
			this.channel.close();
		}
	}

	public void send(Object msg) {
		if (channel!=null&&channel.isConnected()) {
			channel.send(msg);
		} else {
			logger.warn("ͨ��{} 通道已经断开", name());
		}
	}

	protected void onChannelDisconnected(Channel channel) {
		if (logger.isInfoEnabled()) {
			logger.info(name() + " channel : " + channel
					+ "closed, retry connect...");
		}
		
		exec.schedule(new Runnable() {

			public void run() {
				doConnect();
			}
		}, retryTimeout, TimeUnit.MILLISECONDS);
		
	}

	protected abstract void doConnect();

	public long getRetryTimeout() {
		return retryTimeout;
	}

	public void setRetryTimeout(long retryTimeout) {
		this.retryTimeout = retryTimeout;
	}

	public boolean isConnected() {
		return channel.isConnected();
	}

	public String name() {
		return this.name;
	}
	
	protected String destIp(){
		return this.destIp;
	}
	
	protected int destPort(){
		return this.destPort;
	}
	
	public ChannelHandler getChannelHandler() {
		return channelHandler;
	}

	public void setChannelHandler(ChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
	}
	
	@Override
	public void setDestIp(String ip) {
		this.destIp = ip;
	}

	@Override
	public void setDestPort(int port) {
		this.destPort = port;
	}
	
	@Override
	public String getDestIpAddress() {
		return this.destIp + ":" + this.destPort;
	}
	
	public void addConnectorListener(ConnectorListener connectorListener) {
		if(channelHandler!=null){
			channelHandler.addConnectorListener(connectorListener);
		}
		else{
			logger.error("ChannelHandler is NULL");
		}
	}
	
	public short getModuleId() {
		return moduleId;
	}

	public void setModuleId(short moduleId) {
		this.moduleId = moduleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destIp == null) ? 0 : destIp.hashCode());
		result = prime * result + destPort;
		result = prime * result + moduleId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTcpClient other = (AbstractTcpClient) obj;
		if (destIp == null) {
			if (other.destIp != null)
				return false;
		} else if (!destIp.equals(other.destIp))
			return false;
		if (destPort != other.destPort)
			return false;
		if (moduleId != other.moduleId)
			return false;
		return true;
	}
}
