package xixi.router;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import xixi.common.annotation.DefaultImplement;
import xixi.rpc.bean.RpcMessage;
import xixi.transport.client.TcpClient;

@DefaultImplement(value="direct")
public interface Router{
	
	public static final ConcurrentMap<Short, Router> ROUTERMAP = new ConcurrentHashMap<Short, Router>();
	
	void addTcpClient(TcpClient client);
	
	void removeTcpClient(String ipAddress);
	
	void removeTcpClient(TcpClient client);
	
	TcpClient getTcpClient(String ipAddress);
	
	short moduleId();
	
	public void router(RpcMessage message);
	
	public void destory();
	
}
