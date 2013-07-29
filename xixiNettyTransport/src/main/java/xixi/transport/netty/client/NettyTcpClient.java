package xixi.transport.netty.client;

import static xixi.router.Router.ROUTERMAP;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.transport.client.AbstractTcpClient;
import xixi.transport.handler.ChannelHandler;
import xixi.transport.netty.channel.NettyChannel;
import xixi.transport.netty.pipeline.TcpClientPipelineFactory;
import xixi.transport.util.SenderUtil;

public class NettyTcpClient extends AbstractTcpClient {
	private static final Logger logger = LoggerFactory
			.getLogger(NettyTcpClient.class);

	private short moduleId;
	private TcpClientPipelineFactory pipelineFactory;
	private ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool()));
	
	private ChannelHandler channelHandler;

	public NettyTcpClient(String destIp,int destPort) {
		super(destIp,destPort,Constants.LOCAL_IP,Constants.LOCAL_PORT,"NettyTcpClient");
	}

	public NettyTcpClient() {
		super(null,0,Constants.LOCAL_IP,Constants.LOCAL_PORT,"NettyTcpClient");
	}
	
	protected class IOHandler extends SimpleChannelUpstreamHandler {
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			logger.info("收到消息{}", e.getMessage());
			Object message = e.getMessage();
			SenderUtil.attachChannelToMsg(message, channel);
			if(channel!=null){
				channel.onMessageReceived(message);
			}
			else{
				logger.error("Channel is null");
			}
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			logger.info("NettyTcpClient-channelConnected:{}", e);
			channel = NettyChannel.getOrCreateChannel(e
					.getChannel(),channelHandler);
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception {
			logger.info("XixiTcpClient-channelDisconnected:{}", e);
			channel = NettyChannel.removeChannel(e.getChannel());
			if (channel != null) {
				channel.onChannelDisconntected();
			}
			ROUTERMAP.get(moduleId).removeTcpClient(NettyTcpClient.this);
			//the stop() will be called from router clean task
			//stop();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			logger.error("XixiTcpClient-channelDisconnected for ERROR:{}", e
					.getCause());
			// stop();
		}
	}

	protected ChannelFuture doConnect() {
		if (null == name() || destIp().equals("")) {
			logger.warn(name() + " destIp is null, disable this connector.");
			return null;
		}

		bootstrap.setOption("remoteAddress", new InetSocketAddress(destIp(),
				destPort()));
		 bootstrap.setPipelineFactory(pipelineFactory);
		// bootstrap.setOption("localAddress", new
		// InetSocketAddress(localAddress, localPort));
		List<org.jboss.netty.channel.ChannelHandler> handlers = new ArrayList<org.jboss.netty.channel.ChannelHandler>();
		handlers.add(new IOHandler());
		this.pipelineFactory.setOtherHandlers(handlers);
		ChannelFuture future = bootstrap.connect();
		retryTimes.incrementAndGet();
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(final ChannelFuture future)
					throws Exception {
				exec.submit(new Runnable() {
					public void run() {
						onConnectComplete(future);
					}
				});
			}
		});
		return future;
	}

	public short getModuleId() {
		return moduleId;
	}

	public void setModuleId(short moduleId) {
		this.moduleId = moduleId;
	}

	public ChannelHandler getChannelHandler() {
		return channelHandler;
	}

	public void setChannelHandler(ChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
	}

	public TcpClientPipelineFactory getPipelineFactory() {
		return pipelineFactory;
	}

	public void setPipelineFactory(TcpClientPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}



}
