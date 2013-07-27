package xixi.transport.netty.coder;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.codec.api.Coder;
import xixi.common.constants.Constants;
import xixi.common.util.ByteUtils;
import xixi.rpc.bean.RpcMessage;

public class XixiNettyEncoder extends OneToOneEncoder{

	private static final Logger logger = LoggerFactory
	.getLogger(XixiNettyEncoder.class);
	
	private Coder coder;
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		logger.debug("The encoded message is {}", msg);
		if(msg instanceof RpcMessage){
			int msgSize = Constants.RPC_FIXED_HEAD_SIZE;
			
			RpcMessage message = (RpcMessage)msg;
	    	
	    	byte[] msgBody = coder.encoder(message.getData());
	    	
	    	logger.debug("MSG BODY is " +  ByteUtils.bytesAsHexString(msgBody, msgBody.length));
	    	String interfaceName = message.getInterfaceName();
	    	String methodName = message.getMethodName();
	    	byte[] interfaceNameByte = interfaceName.getBytes("utf-8");
	    	byte[] methodNameByte = methodName.getBytes("utf-8");
	    	
	    	msgSize = msgSize + interfaceNameByte.length + methodNameByte.length + msgBody.length;
	    	
	    	ChannelBuffer buffer = ChannelBuffers.buffer(msgSize);
	    	buffer.writeByte(message.getBasicVer());
	    	buffer.writeInt(msgSize);
	    	buffer.writeShort(message.getSrcModule());
	    	buffer.writeShort(message.getDstModule());
	    	buffer.writeLong(message.getFirstTransaction());
	    	buffer.writeLong(message.getSecondTransaction());
	    	buffer.writeByte(message.getType());
	    	
	    	buffer.writeByte(interfaceNameByte.length);
	    	buffer.writeBytes(interfaceNameByte);
	    	buffer.writeByte(methodNameByte.length);
	    	buffer.writeBytes(methodNameByte);
	    	
	    	buffer.writeInt(msgBody.length);
	    	buffer.writeBytes(msgBody);
	    	
	    	return ChannelBuffers.wrappedBuffer(buffer);
	    	
		}
		
		logger.error("This message is not RpcMessage. It is " + msg.getClass());
		
		return null;
	}

	public Coder getCoder() {
		return coder;
	}

	public void setCoder(Coder coder) {
		this.coder = coder;
	}
}
