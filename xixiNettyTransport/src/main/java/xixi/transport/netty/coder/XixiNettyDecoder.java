package xixi.transport.netty.coder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.codec.api.Coder;
import xixi.rpc.bean.RpcNotify;
import xixi.rpc.bean.RpcRequest;
import xixi.rpc.bean.RpcResponse;

public class XixiNettyDecoder extends FrameDecoder {

	private static final Logger logger = LoggerFactory
			.getLogger(XixiNettyDecoder.class);

	private int maxMessageLength = 1024 * 1024;

	private final int FIXED_HEAD_SIZE = 32;

	private Coder coder;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {

		if (buffer.readableBytes() < FIXED_HEAD_SIZE) {
			logger.debug(
					"readable byte size  {} is less than fixed Head Size{}",
					buffer.readableBytes(), FIXED_HEAD_SIZE);
			return null;
		}

		// ��2-5�ֽ�Ϊ���
		int packageLength = buffer.getInt(buffer.readerIndex() + 1);

		if (packageLength > maxMessageLength) {
			byte[] headerBytes = new byte[FIXED_HEAD_SIZE];
			buffer.readBytes(headerBytes);
			logger.error("header.length {} exceed maxMessageLength {}, "
					+ " so drop this connection.\r\ndump bytes received:\r\n",
					packageLength, maxMessageLength);
			channel.close();
			return null;
		}
		if (buffer.readableBytes() < packageLength) {
			return null;
		}

		logger.debug("parse header... try parse...");

		byte version = buffer.getByte(buffer.readerIndex());
		short srcModule = buffer.getShort(buffer.readerIndex() + 5);
		short dstModule = buffer.getShort(buffer.readerIndex() + 7);
		long firstTransactionId = buffer.getLong(buffer.readerIndex() + 9);
		long secondTransactionId = buffer.getLong(buffer.readerIndex() + 17);
		byte messageType = buffer.getByte(buffer.readerIndex() + 25);

		byte interfaceNameLength = buffer.getByte(26);
		buffer.readerIndex(27);
		byte[] interfaceNameByte = new byte[interfaceNameLength];
		buffer.readBytes(interfaceNameByte);
		String interfaceName = new String(interfaceNameByte, "utf-8");

		byte methodNameLength = buffer.getByte(buffer.readerIndex());
		buffer.readerIndex(buffer.readerIndex() + 1);
		byte[] methodNameByte = new byte[methodNameLength];
		buffer.readBytes(methodNameByte);
		String methodName = new String(methodNameByte, "utf-8");

		int msgLength = buffer.getInt(buffer.readerIndex());

		buffer.readerIndex(buffer.readerIndex() + 4);

		byte[] msgBody = new byte[msgLength];
		buffer.readBytes(msgBody);

		if (messageType == 1) {
			RpcRequest request = new RpcRequest();
			request.setBasicVer(version).setLength(packageLength)
					.setSrcModule(srcModule).setDstModule(dstModule)
					.setFirstTransaction(firstTransactionId)
					.setSecondTransaction(secondTransactionId)
					.setMessageLength(msgLength)
					.setInterfaceName(interfaceName).setMethodName(methodName);

			request.setData(coder.decode(msgBody));
			logger.debug("The requst is " + request);
			return request;
		} else if (messageType == 2) {
			RpcResponse response = new RpcResponse();

			response.setBasicVer(version).setLength(packageLength)
					.setSrcModule(srcModule).setDstModule(dstModule)
					.setFirstTransaction(firstTransactionId)
					.setSecondTransaction(secondTransactionId)
					.setMessageLength(msgLength)
					.setInterfaceName(interfaceName).setMethodName(methodName);
			response.setData(coder.decode(msgBody));
			logger.debug("The response is " + response);
			return response;
		} else if (messageType == 3) {
			RpcNotify notify = new RpcNotify();

			notify.setBasicVer(version).setLength(packageLength)
					.setSrcModule(srcModule).setDstModule(dstModule)
					.setFirstTransaction(firstTransactionId)
					.setSecondTransaction(secondTransactionId)
					.setMessageLength(msgLength)
					.setInterfaceName(interfaceName).setMethodName(methodName);
			notify.setData(coder.decode(msgBody));
			logger.debug("The notify is " + notify);
			return notify;
		}

		else {
			logger.warn("Unsupport message type");
			return null;
		}
	}

	public Coder getCoder() {
		return coder;
	}

	public void setCoder(Coder coder) {
		this.coder = coder;
	}

}
