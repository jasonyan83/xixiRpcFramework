package xixi.router.multi;

import static xixi.router.Router.ROUTERMAP;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.common.constants.Constants;
import xixi.common.respository.DependencyModuleRepository;
import xixi.common.util.ConfigUtils;
import xixi.rc.iservice.RCModuleService;
import xixi.router.Router;
import xixi.router.RouterInitializer;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class MutilRouterInitializer implements RouterInitializer{

    private static final Logger logger = 
        	LoggerFactory.getLogger(MutilRouterInitializer.class);
    
	private RCModuleService moduleService;
	
	public RCModuleService getModuleService() {
		return moduleService;
	}
	public void setModuleService(RCModuleService moduleService) {
		this.moduleService = moduleService;
	}
	
	@Override
	public void init() {
		logger.debug("Initializing Mutil Router");
		
		List<Short> destMoudleIdList = DependencyModuleRepository.getDepentModuleIdList();
		logger.debug("Get Dependency moduleId list " + destMoudleIdList);
		
		List<ModuleInfo> modulesInfo = moduleService.getInstanceList(Constants.SOURCE_MODULEID, destMoudleIdList);
		
		logger.debug("Get Dependency ModuleInfo list " + modulesInfo);
		
		if(modulesInfo!=null&&modulesInfo.size()>0){
			for(ModuleInfo m : modulesInfo){
				Router r = MutilConnectRouter.getOrAddRouter(m.getModuleId());
				TcpClient client = TransportFacade.initClient(m.getIp(), m.getPort());
				r.addTcpClient(client);
			}
		}
	}

	private static class RouterCleanTask implements Runnable{

		@Override
		public void run() {
			Set<Entry<Short, Router>> entrySet = ROUTERMAP.entrySet();
			for(Entry<Short, Router> entry : entrySet){
				if(entry.getKey()!=Short.valueOf(ConfigUtils.getProperty(Constants.RC_MODULE_ID, "201"))){
					Router router = entry.getValue();
				}
				
			}
			
		}
		
	}
	static{
		Thread thread = new Thread(new RouterCleanTask());
		thread.setDaemon(true);
		thread.start();
	}
}
