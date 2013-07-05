package xixi.transport.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DefaultServerSocketChannelConfig;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.transport.handler.ChannelHandler;
import xixi.transport.netty.channel.NettyChannel;
import xixi.transport.netty.pipeline.TcpServerPipelineFactory;
import xixi.transport.server.AbstractServer;
import xixi.transport.util.SenderUtil;

public class NettyTcpServer extends AbstractServer {
	private static final Logger logger = LoggerFactory
			.getLogger(NettyTcpServer.class);
	
	private TcpServerPipelineFactory pipelineFactory;
	
	ServerBootstrap bootstrap = new ServerBootstrap(
			new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool()));
	
	private Channel channel;

	private ChannelHandler channelHandler;
	
	public NettyTcpServer(){
	}
	
	private class IOHandler extends SimpleChannelUpstreamHandler {
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			logger.debug("NettyTCPServer-channelConnected:{}", e);
			NettyChannel channel = NettyChannel.getOrCreateChannel(e.getChannel(),channelHandler);
			channel.onChannelConntected();
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			logger.debug("channelDisconntected:{}", e);
			NettyChannel channel = NettyChannel.removeChannel(e.getChannel());
		   if(channel!=null){
			   channel.onChannelDisconntected();
		   }
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			logger.debug("收到服务端模块消息:{}", e);
			Object message = e.getMessage();
			NettyChannel channel = (NettyChannel)NettyChannel.getChannel(e.getChannel());
			SenderUtil.attachChannelToMsg(message, channel);
			channel.onMessageReceived(message);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			logger.error("通道有异常发生{}。原因{}", e.getChannel(), e.getCause()
					.getMessage());
			NettyChannel channel = (NettyChannel)NettyChannel.getChannel(e.getChannel());
			channel.onExceptionCaught(e.getCause());
		}
	}

	@Override
	protected void doStart() {
		 try {
				pipelineFactory.setTcpRequestHandler(new IOHandler());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	        bootstrap.setPipelineFactory(pipelineFactory);
	        
	        int retryCount = 0;
	        boolean binded = false;

	        do {
	            try {
	                channel = bootstrap.bind(new InetSocketAddress(Constants.LOCAL_IP, Constants.LOCAL_PORT));
	     
	                binded = true;
	            } catch (ChannelException e) {
	                logger.warn("start failed : " + e.getMessage() + ", and retry...");

	                //  对绑定异常再次进行尝�??
	                retryCount++;
	                if (retryCount >= MAX_RETRY) {
	                    //  超过�??大尝试次�??
	                    throw e;
	                }
	                try {
	                    Thread.sleep(RETRY_TIMEOUT);
	                } catch (InterruptedException e1) {
	                }
	            }
	        } while (!binded);
	        
	        logger.info("start succeed in " + Constants.LOCAL_IP + ":" + Constants.LOCAL_PORT);

	        DefaultServerSocketChannelConfig config = (DefaultServerSocketChannelConfig) (channel.getConfig());
	        config.setBacklog(10240);
	        config.setReuseAddress(true);
	        config.setReceiveBufferSize(1024*8);
	        bootstrap.setOption("child.keepAlive", true);
	        bootstrap.setOption("child.tcpNoDelay", true);
	        bootstrap.setOption("child.soLinger", -1);
	        bootstrap.setOption("child.sendBufferSize", -1);
	}

	@Override
	protected void doStop() {
      if (null != channel) {
            channel.unbind();
            channel.getFactory().releaseExternalResources();
            channel = null;
        }
	}

	@Override
	protected void releaseResource() {
		// TODO Auto-generated method stub

	}

	public TcpServerPipelineFactory getPipelineFactory() {
		return pipelineFactory;
	}

	public void setPipelineFactory(TcpServerPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}

	public ChannelHandler getChannelHandler() {
		return channelHandler;
	}

	public void setChannelHandler(ChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
	}

}
