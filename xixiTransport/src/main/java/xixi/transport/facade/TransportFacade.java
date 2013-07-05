package xixi.transport.facade;


import xixi.common.annotation.DefaultImplement;
import xixi.common.constants.Constants;
import xixi.common.spring.BeanFactoryUtil;
import xixi.common.util.ConfigUtils;
import xixi.transport.client.TcpClient;
import xixi.transport.server.Server;
public final class TransportFacade {

	public static TcpClient initClient(String destIp,int destPort){
		String clientType = ConfigUtils.getProperty(Constants.RPC_CLIENT_KEY);
		if(clientType==null&&"".equals(clientType)){
			clientType = TcpClient.class.getAnnotation(DefaultImplement.class).value();
		}
		
		TcpClient client = (TcpClient)BeanFactoryUtil.getBean(clientType);
		client.setDestIp(destIp);
		client.setDestPort(destPort);
		client.start();
		return client;
	}
	
	public static Server initServer(){
		String serverType = ConfigUtils.getProperty(Constants.RPC_SERVER_KEY);
		if(serverType==null&&"".equals(serverType)){
			serverType = Server.class.getAnnotation(DefaultImplement.class).value();
		}
		
		Server server = (Server)BeanFactoryUtil.getBean(serverType);
		
		server.start();
		return server;
	}
}
