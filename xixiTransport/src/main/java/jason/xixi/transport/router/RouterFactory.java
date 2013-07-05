package jason.xixi.transport.router;

import jason.xixi.common.util.ModuleStringUtil;
import jason.xixi.transport.connection.NettyTcpClienttt;
import jason.xixi.transport.connection.XixiTcpClientFactorty;
import jason.xixi.transport.lister.RouterListener;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterFactory {

	private static final Logger logger = LoggerFactory
	.getLogger(ModuleRouter.class);
	
	private RouterRepository repository;
	private XixiTcpClientFactorty clientFactory;
	private ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	private ScheduledExecutorService routerDestoryExec = Executors.newScheduledThreadPool(1);
	
	public void start(){
		exec.scheduleWithFixedDelay(new Runnable(){
			public void run(){
				checkRouterChange();
			}
			
		}, 5*1000, 60*1000, TimeUnit.MILLISECONDS);
	}
	
	private void checkRouterChange(){
		logger.debug("开始检查是否有服务模块通道信息变化");
		for(Router router:repository.getRouterMap().values()){
			List<String> list = router.getRouterChangeList();
			if(list.isEmpty()){
				logger.debug("模块{}没有发现需要变化的router", router.getModuleId());
				router.clearInstanceIp();
				continue;
			}
			else{
				logger.debug("发现需要变化的router: {}，变化的channel个数为:{}", router,list.size());
				for(String ipAddress:list){
					this.createRouter(router.getModuleId(), ipAddress);
				}
				router.clearInstanceIp();
			}
		}
	}

	public void createRouter(Integer moduleId,String address){
		logger.debug("正在创建router，模块号为: {}，ip地址为:{}", moduleId, address);
		String ip = ModuleStringUtil.getIp(address);
		Integer port = ModuleStringUtil.getPort(address);
		clientFactory.buildTcpClient(ip, port, moduleId, String.valueOf(moduleId), new RouterListener(){
			@Override
			public void onClientClosNettyTcpClientntnt tcpClient) {
				int moduleId = tcpClient.getModuleId();
				if(repository.hasRouter(moduleId)){
					Router router = repository.getRouter(moduleId);
					router.removeTcpClient(tcpClient);
				}
			}

			@Override
			public void onClientConnecNettyTcpCliententent tcpClient) {
				int moduleId = tcpClient.getModuleId();
				if(repository.hasRouter(moduleId)){
					Router router = repository.getRouter(moduleId);
					List<String> removedConnList= router.getRouterRemovedList();
					if(!removedConnList.isEmpty()){
						String ipAddress = removedConnList.remove(0);
						logger.debug("移除即将不参与服务的router，模块Id是{}，ipAddress为{}", moduleId,ipAddress);
						router.removeTcpClient(ipAddress);

					}
					router.addTcpClient(tcpClient);
				}
				else{
					ModuleRouter router = new ModuleRouter(moduleId,routerDestoryExec);
					router.addTcpClient(tcpClient);
					repository.addRouter(router);
				}
			}
		});
	}
	public RouterRepository getRepository() {
		return repository;
	}

	public void setRepository(RouterRepository repository) {
		this.repository = repository;
	}

	public XixiTcpClientFactorty getClientFactory() {
		return clientFactory;
	}

	public void setClientFactory(XixiTcpClientFactorty clientFactory) {
		this.clientFactory = clientFactory;
	}
}
