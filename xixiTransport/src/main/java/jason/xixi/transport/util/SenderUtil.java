package jason.xixi.transport.util;

lePropertyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stc.skymobi.util.MutablePropertyable;
import xixi.transport.channel.Channel;

public class SenderUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(SenderUtil.class);

	private static final String TRANSPORT_SENDER = "TRANSPORT_SENDER";

	public static void attachChannelToMsg(Object message, Channel channel) {
		if (message instanceof MutablePropertyable) {
			((MutablePropertyable) message).setProperty(TRANSPORT_SENDER,
					channel);
		} else {
			logger.error("消息:{} 不是有效的MutablePropertyable消息,无法设置channel属性",
					message);
		}
	}

}
