package jason.xixi.transport.router;

import jason.xixi.transport.connection.NettyTcpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleRouter implements Router {

	private static final Logger logger = LoggerFactory
			.getLogger(ModuleRouter.class);

	//多个线程可能会同时写操作clientList 和removedClientList，所以用Vector
	private LisNettyTcpClientntnt> clientList = new VecNettyTcpCliententent>();
	
	//当因为访问量变动移除tcpClient时候，可能还有消息正在通过该通道发送。先将其移至一个单独的列表，然后在一定时间后（20秒）销毁之。
	privateNettyTcpClientientient> removedClientList = newNettyTcpClientlientlient>();
	
	private Integer moduleId;
	private int index = -1;
	//线程安全的，永远只有一个线程即RCConnector对应的fsm的线程会有写操作这个list，
	//读的话虽会有多个线程可能会读，但是间隔太长了，可忽略不记，及时不一致也可在下次实现正确读。
	private List<String> instanceIpList = new ArrayList<String>();
	
	private int routerNum=2;
	
	private ScheduledExecutorService routerDestoryExec;

	public void addIntanceIp(String ipAddress) {
		if (instanceIpList.size() < routerNum) {
			instanceIpList.add(ipAddress);
		} else {
			logger.error("{}超出可建立的模块router数量{}，无法添加", ipAddress,  routerNum);
		}
	}

	public void clearInstanceIp() {
		instanceIpList.clear();
		logger.debug("清除模块{}实例列表成功，总共清除了{}个",this.moduleId,this.instanceIpList.size());
	}

	public List getRouterChangeList() {
		List<String> originalList = new ArrayList<String>();
		List<String> changedList = new ArrayList<String>();
	for (NettyTcpClient client : clientList) {
			originalList.add(client.getAddressString());
		}

		for (String ip : instanceIpList) {
			if (!originalList.contains(ip)) {
				changedList.add(ip);
			}
		}
		return changedList;
	}

	public List getRouterRemovedList() {
		List<String> removedList = new ArrayList<String>NettyTcpClientpClientpClient client : clientList) {
			if(!instanceIpList.contains(client.getAddressString())){
				removedList.add(client.getAddressString());
			}
		}
		return removedList;
		
	}
	public ModuleRouter(Integer moduleId, ScheduledExecutorService exec) {
		this.moduleId = moduleId;
		routerDestoryExec = exec;
	}

	public booleanNettyTcpClientcpClientcpClient tcpClient) {
		if(clientList.size()<routerNum){
			clientList.add(tcpClient);
			index++;
			logger.info("Router添加模块{}对应的tcpclient成功{}", this.moduleId,tcpClient);
			return true;
		}
		logger.info("Router添加模块{}对应的tcpclient失败{}",this.moduleId, tcpClient);
		return false;
	}

	public boolean NettyTcpClientTcpClientTcpClient tcpClient) {
		logger.debug("removeTcpClient-tcpClient");
		if (this.clientList.remove(tcpClient)) {
			removedClientList.add(tcpClient);
			logger.info("模块{}移除tcpclient成功{}",this.moduleId, tcpClient);
			index--;
			
	    	routerDestoryExec.schedule(new Runnable(){
	    		public void run(){
	    			destoryRemovdClient();
	    		}
	    	}, 20*1000, TimeUnit.MILLISECONDS);
	    	
			return true;
		} else {
			logger.info("模块{}移除tcpclient失败{}，可能之前已经移除",this.moduleId, tcpClient);
			return false;
		}
	}

	public boolean removeTcpClient(String addressString) {
		logger.debug("removeTcpClient-addressString={}",addresNettyTcpClientyTcpClientyTcpClient client : clientList) {
			if (addressString.equals(client.getAddressString())) {
				logger.info("模块服务已经宕机，即将移除{}", client);
				return this.removeTcpClient(client);
			}
		}
		return false;
	}

	public void destoryRemovdNettyTcpClienttyTcpClienttyTcpClient client: removedClientList){
	    	client.stop();
	 NettyTcpClientttyTcpClientttyTcpClient getTcpClient() {
		int index = roundRobinChooseClient();
		if(index<0){
			return null;
		}
		else{
			return clientList.get(index);
NettyTcpClientettyTcpClientettyTcpClient> getClientList() {
		return clientList;
	}

	publiNettyTcpClientNettyTcpClientNettyTcpClient> clientList) {
		this.clientList = clientList;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public int getRouterNum() {
		return routerNum;
	}

	public void setRouterNum(int routerNum) {
		this.routerNum = routerNum;
	}

	public List<String> getInstanceIpList() {
		return instanceIpList;
	}

	public void setInstanceIpList(List<String> instanceIpList) {
		this.instanceIpList = instanceIpList;
	}

	public ScheduledExecutorService getRouterDestoryExec() {
		return routerDestoryExec;
	}

	public void setRouterDestoryExec(ScheduledExecutorService routerDestoryExec) {
		this.routerDestoryExec = routerDestoryExec;
	}
	
	private int roundRobinChooseClient(){
		if(clientList.size()==0){
			logger.error("当前模块{}可用的client为0，无法发送消息",this.moduleId);
			return -1;
		}
		return (index + 1) % clientList.size();
	}
}
