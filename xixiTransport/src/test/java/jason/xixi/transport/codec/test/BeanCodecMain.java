/*package jason.xixi.transport.codec.test;

import jason.xixi.annotation.XixiSignal;
import jason.xixi.common.bean.ModuleRegisterReq;
import jason.xixi.common.bean.TLVSignal;
import jason.xixi.common.bean.XixiHeader;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import stc.skymobi.bean.bytebean.core.BeanFieldCodec;
import stc.skymobi.bean.tlv.encode.TLVEncodeContext;
import stc.skymobi.bean.tlv.encode.TLVEncoderOfBean;
import stc.skymobi.transport.protocol.xip.XipHeader;
import stc.skymobi.util.ByteUtils;

public class BeanCodecMain {

	*//**
	 * @param args
	 *//*
	
	private static 	BeanFieldCodec xixiHeaderCodec;
	private static TLVEncoderOfBean tlvBeanEncoder;
	public static void main(String[] args) {
		  AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("xixiBeanCodec.xml");
		 xixiHeaderCodec = (BeanFieldCodec)ctx.getBean("xixiBeanCodec");
		 tlvBeanEncoder = (TLVEncoderOfBean)ctx.getBean("beanTlvEncoder");
		 byte[] ret =null;
		 ModuleRegisterReq req = new ModuleRegisterReq();
			req.setModuleId(120);
			req.setIpAddress("127.0.0.1");
			req.setDesciption("moduleAzzzz");
			try {
				ret = encodeTLV(req);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] headerBytes = new byte[XixiHeader.XIXI_HEADER_LENGTH];
			headerBytes = ArrayUtils.subarray(ret, 0, 45);
			XixiHeader header = (XixiHeader) xixiHeaderCodec.decode(
					xixiHeaderCodec.getDecContextFactory().createDecContext(
							headerBytes, XixiHeader.class, null, null)).getValue();
			System.out.println(header);
         
	}
	
	private static decode(){
		byte[] headerBytes = new byte[XixiHeader.XIXI_HEADER_LENGTH];
		buffer.readBytes(headerBytes);

		if (logger.isTraceEnabled() && isDebugEnabled) {
			logger.trace("header raw bytes \r\n{}", ByteUtils.bytesAsHexString(
					headerBytes, dumpBytes));
		}

		XixiHeader header = (XixiHeader) xixiHeaderCodec.decode(
				xixiHeaderCodec.getDecContextFactory().createDecContext(
						headerBytes, XixiHeader.class, null, null)).getValue();

		if (logger.isDebugEnabled() && isDebugEnabled) {
			logger.debug("header-->" + header);
		}

		if (null == header) {
			logger.error(" Can't decode Xixi's fixed header");
			channel.close();
			return null;
		}

		byte[] bytes = new byte[packageLength - XixiHeader.XIXI_HEADER_LENGTH];
		buffer.readBytes(bytes);

		if (logger.isTraceEnabled() && isDebugEnabled) {
			logger.trace("body raw bytes \r\n{}", ByteUtils.bytesAsHexString(
					bytes, dumpBytes));
		}

		Class<?> type = xixiTypeMetainfo.find(header.getMessageCode());
		if (null == type) {
			throw new RuntimeException("unknow messageCode:"
					+ header.getMessageCode());
		}
		MutableIdentifyable identifyable = null;

		identifyable = (MutableIdentifyable) tlvBeanDecoder.decode(
				bytes.length, bytes, tlvBeanDecoder.getDecodeContextFactory()
						.createDecodeContext(type, null));

		if (null != identifyable) {
			// identifyable.setIdentification(header.getTransactionAsUUID());
			//identifyable.setIdentification(channelCache.get(channel));
			identifyable.setIdentification(header.getTransactionAsUUID());
			if (logger.isDebugEnabled() && isDebugEnabled) {
				logger.debug("channel {} \r\nrecv signal {}", channel,
						identifyable);
			}
			return identifyable;
		} else {
			if (logger.isTraceEnabled() && isDebugEnabled) {
				logger.trace("无法解码信令body");
			}
			return null;
		}
	}
	private static byte[] encodeTLV(TLVSignal signal) throws Exception {

		TLVEncodeContext ctx = tlvBeanEncoder.getEncodeContextFactory()
				.createEncodeContext(signal.getClass(), null);
		List<byte[]> byteList = tlvBeanEncoder.encode(signal, ctx);

		XixiSignal attr = signal.getClass().getAnnotation(XixiSignal.class);
		if (null == attr) {
			throw new RuntimeException(
					"invalid top tlv object, missing @TLVAttribute.");
		}

		byte[] bytesBody = ByteUtils.union(byteList);
		XixiHeader header = createHeader((byte) 1, signal.getIdentification(),
				attr.msgCode(), bytesBody.length);

		System.out.println("Header为:" + header);
		System.out.println("Body字节大小:" + bytesBody.length);
		
		byte[] bytes = ArrayUtils.addAll(xixiHeaderCodec.encode(xixiHeaderCodec
				.getEncContextFactory().createEncContext(header,
						XipHeader.class, null)), bytesBody);

		
			System.out.println("encode TLV:" + signal);
			System.out.println("and TLV raw bytes -->");
			System.out.println(ByteUtils.bytesAsHexString(bytes, 256));
			
			System.out.println("编码后的字节大小:" + bytes.length);

		return bytes;
	}
	
	private static XixiHeader createHeader(byte basicVer, UUID id, int messageCode,
			int messageLen) {

		XixiHeader header = new XixiHeader();

		header.setTransaction(id);

		int headerSize = xixiHeaderCodec.getStaticByteSize(XixiHeader.class);

		header.setLength(headerSize + messageLen);
		header.setMessageLength(messageLen);
		header.setMessageCode(messageCode);
		header.setBasicVer(basicVer);

		return header;
	}


}
*/