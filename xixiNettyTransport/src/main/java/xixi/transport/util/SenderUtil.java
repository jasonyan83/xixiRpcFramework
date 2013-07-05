package xixi.transport.util;

import xixi.rpc.bean.RpcMessage;
import xixi.transport.channel.Channel;

public class SenderUtil {

	public static void attachChannelToMsg(Object message, Channel channel){
		if(message instanceof RpcMessage){
			message = ((RpcMessage)message).setProperty("channel", channel);
		}
	}
}
