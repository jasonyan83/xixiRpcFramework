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
}
