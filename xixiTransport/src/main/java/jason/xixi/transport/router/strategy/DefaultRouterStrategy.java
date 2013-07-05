/*package jason.xixi.transport.router.strategy;

import jason.xixi.common.bean.ModuleStatInfoToClient;
import jason.xixi.common.bean.ModuleStatToClientNotify;
import jason.xixi.common.util.collection.HashListMap;
import jason.xixi.transport.router.Router;
import jason.xixi.transport.router.RouterFactory;
import jason.xixi.transport.router.RouterRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRouterStrategy implements RouterStrategy{
	
	private static final Logger logger = LoggerFactory
	.getLogger(DefaultRouterStrategy.class);
	
	public DefaultRouterStrategy(RouterFactory routerFactory,
			RouterRepository repository) {
		this.repository = repository;
		this.routerFactory = routerFactory;
	}

	private RouterRepository repository;
	private RouterFactory routerFactory;
	
	public RouterRepository getRepository() {
		return repository;
	}

	public void setRepository(RouterRepository repository) {
		this.repository = repository;
	}

	public RouterFactory getRouterFactory() {
		return routerFactory;
	}

	public void setRouterFactory(RouterFactory routerFactory) {
		this.routerFactory = routerFactory;
	}

	public void processModuleStatChanged(ModuleStatToClientNotify notify){
		logger.debug("开始执行ModuleStatChanged");
		HashListMap<Integer,ModuleStatInfoToClient> latestModuleStatMap = new HashListMap<Integer,ModuleStatInfoToClient>();
		if(notify.getModuleStatInfoList()==null||notify.getModuleStatInfoList().isEmpty()){
			logger.debug("没有需要更新的服务模块状态信息，请检查是否服务全部宕机，或者是正常");
			return ;
		}
		for(ModuleStatInfoToClient info : notify.getModuleStatInfoList()){
			latestModuleStatMap.addValue(info.getModuleId(), info);
		}
		for(Integer key :latestModuleStatMap.keySet()){
			List<ModuleStatInfoToClient> moduleList = latestModuleStatMap.get(key);
			logger.debug("当前模块{}的实例个数为{}",key,moduleList.size());
			Router router = repository.getRouter(key);
			for(int i=0;i<moduleList.size();i++){
				if(i<router.getRouterNum()){
					logger.debug("更新router的可用实例列表，添加的ip为{}",moduleList.get(i).getIpAddress());
					router.addIntanceIp(moduleList.get(i).getIpAddress());
				}
			}
			repository.getCandidateModuleMap().put(key,moduleList.subList(router.getRouterNum()-1, moduleList.size()));
		}

	}
	
	
	public ModuleStatInfoToClient getNewModuleInfo(){
		return null;
	}
	
}
*/