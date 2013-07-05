/*package xixi.transport.client;

import jason.xixi.common.bean.HeartbeatReq;
import jason.xixi.common.event.RegisterClientEvent;
import jason.xixi.common.util.XiXiConstantsConfig;
import jason.xixi.transport.connection.AbstractTcpClient;
import jason.xixi.transport.connection.RCConnector;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stc.skymobi.fsm.FiniteStateMachine;
import stc.skymobi.util.MutableIdentifyable;

		req.setIdentification( UUID.randomUUID());
		req.setIpAddress(getLocalAddressString());
		req.setModuleId(moduleId);
		req.setInterval(XiXiConstantsConfig.MODULE_HEARTBEAT_INTEVAL);
		logger.debug("发送心跳: {}", req);
		send(req);
	}
	
	protected class IOHandler extends SimpleChannelUpstreamHandler {
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			logger.info("RCConnector-messageReceived:{}", e);
			MutableIdentifyable msg =  (MutableIdentifyable)e.getMessage();
			msg.setIdentification(fsmContextId);
			fsm.acceptEvent(msg);
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			logger.info("RCConnector-channelConnected:{}", e);
			channel = e.getChannel();
			RegisterClientEvent event = new RegisterClientEvent();
			event.setIdentification(fsmContextId);
			event.setChannel(channel);
			fsm.acceptEvent(event);
			exec.scheduleWithFixedDelay(new Runnable(){
				public void run(){
					doSendHeartBeat();
				}
			}, 10*1000, XiXiConstantsConfig.MODULE_HEARTBEAT_INTEVAL, TimeUnit.MILLISECONDS);
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception {
			logger.info("RCConnector-channelDisconnected:{}", e);
			stop();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			logger.error("RCConnector-channelDisconnected for ERROR:{}", e.getCause());
			//stop();
		}
	}
	
	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	
	public FiniteStateMachine getFsm() {
		return fsm;
	}

	public void setFsm(FiniteStateMachine fsm) {
		this.fsm = fsm;
	}

	@Override
	protected SimpleChannelUpstreamHandler getCustomIOHandler() {
		return new IOHandler();
	}

	@Override
	protected void onConnectLost() {
		// do nothing
		
	}

	@Override
	protected void onConnectSuccess() {
		// do nothing
	}
}
*/