package xixi.router.multi;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.router.AbstractRouter;
import xixi.router.schedule.RouterSchedule;
import xixi.rpc.bean.RpcMessage;
import xixi.transport.client.TcpClient;

public class MutilConnectRouter extends AbstractRouter{

	private static final Logger logger = 
	        	LoggerFactory.getLogger(MutilConnectRouter.class);
	   
	public static MutilConnectRouter getOrAddRouter(short moduleId) {
		if (moduleId < 0) {
			return null;
		}
		MutilConnectRouter router = (MutilConnectRouter) ROUTERMAP
				.get(moduleId);
		if (router == null) {
			MutilConnectRouter r = new MutilConnectRouter(moduleId);
			router = (MutilConnectRouter) ROUTERMAP.putIfAbsent(moduleId, r);
			if (router == null) {
				router = r;
			}
		}
		return router;
	}
	
	private final List<TcpClient> clientList = new ArrayList<TcpClient>();
	
	private RouterSchedule schedule;
	
	public MutilConnectRouter(int moduleId) {
		super(moduleId);
	}

	@Override
	public void addTcpClient(TcpClient client) {
		logger.debug("Add new client for mutilConnectRouter: " + client);
		clientList.add(client);
	}

	@Override
	public void router(RpcMessage message) {
		int index = schedule.schedule();
		TcpClient client = clientList.get(index);
		client.send(message);
	}

	@Override
	protected TcpClient getTcpClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTcpClient(String ipAddress) {
		for(TcpClient client : clientList){
			if(ipAddress.equals(client.getDestIpAddress())){
				
			}
		}
		
	}

}
