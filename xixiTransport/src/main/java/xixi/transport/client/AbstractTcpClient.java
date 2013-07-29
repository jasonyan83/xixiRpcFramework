package xixi.transport.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.bean.AbstractLBProperty;
import xixi.transport.channel.Channel;

public abstract class AbstractTcpClient extends AbstractLBProperty implements TcpClient {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractTcpClient.class);

	protected ScheduledExecutorService exec = Executors
			.newSingleThreadScheduledExecutor();

	private String destIp = null;
	private int destPort = -1;
	private String localIp = "0.0.0.0";
	private int localPort;
	private String name;

	protected Channel channel = null;

	protected long retryTimeout = 10 * 1000;
	protected static final int MAX_RETRY_TIMES = 3;
	protected AtomicInteger retryTimes = new AtomicInteger();

	public AbstractTcpClient(String destIp, int despPort, String localIp,
			int localPort, String name) {
		this.destIp = destIp;
		this.destPort = despPort;
		this.localIp = localIp;
		this.localPort = localPort;
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

	protected void onConnectComplete(ChannelFuture future) {
		if (!future.isSuccess()) {
			if (retryTimes.get() > MAX_RETRY_TIMES) {
				logger.info(name() + " connect [" + this.destIp + ":"
						+ this.destPort + "] 超出了最大尝试此次，不再尝试连接");
				return;
			}

			if (logger.isInfoEnabled()) {
				logger.info(name() + " connect [" + this.destIp + ":"
						+ this.destPort + "] failed, retry ["
						+ retryTimes.get() + "] times");
			}
			exec.schedule(new Runnable() {

				public void run() {
					doConnect();
				}
			}, retryTimeout, TimeUnit.MILLISECONDS);
		} else {
			// onConnectSuccess();
		}
	}

	private void onChannelDisconnected(Channel channel) {
		if (logger.isInfoEnabled()) {
			logger.info(name() + " channel : " + channel
					+ "closed, retry connect...");
		}
		
		if (channel != null) {
			channel.onChannelDisconntected();
		}
		
		exec.schedule(new Runnable() {

			public void run() {
				doConnect();
			}
		}, retryTimeout, TimeUnit.MILLISECONDS);
		
		
	}

	protected abstract ChannelFuture doConnect();

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
}
