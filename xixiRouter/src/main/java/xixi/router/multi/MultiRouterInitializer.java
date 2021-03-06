package xixi.router.multi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.common.bean.ModuleInfo;
import xixi.common.constants.Constants;
import xixi.common.respository.DependencyModuleRepository;
import xixi.common.spring.BeanFactoryUtil;
import xixi.common.thread.MainThreadLock;
import xixi.rc.iservice.RCModuleService;
import xixi.router.AbstractRouterInitializer;
import xixi.router.DefaultConnectRouter;
import xixi.router.Router;
import xixi.router.schedule.RouterSchedules;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class MultiRouterInitializer extends AbstractRouterInitializer {

	private static final Logger logger = LoggerFactory
			.getLogger(MultiRouterInitializer.class);

	private RCModuleService moduleService;

	public MultiRouterInitializer(String registryType) {
		moduleService = (RCModuleService) BeanFactoryUtil
				.getBean(registryType);
		if (moduleService == null) {
			logger.error("Get RCModuleService Failed");
		}
	}

	@Override
	public void init() {
		logger.debug("Initializing Mutil Router");
		List<ModuleInfo> destMoudleList = DependencyModuleRepository
				.getDepentModuleIdList();
		logger.debug("Get Dependency moduleId list " + destMoudleList);

		if (!MainThreadLock.lock.get()) {
			try {
				logger.debug("Wait for RC Router configured!");

				synchronized(MainThreadLock.lock){
					MainThreadLock.lock.wait();
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (destMoudleList != null && destMoudleList.size() == 0) {
			logger.info("Do not have ANY dependency module");
			return;
		}
		
		List<ModuleInstanceInfo> modulesInfo = moduleService.getInstanceList(
				Constants.SOURCE_MODULEID, destMoudleList);

		logger.debug("Get Dependency ModuleInfo list from RC: " + modulesInfo);

		if (modulesInfo != null && modulesInfo.size() > 0) {
			for (ModuleInstanceInfo m : modulesInfo) {
				Router r = DefaultConnectRouter.getOrAddRouter(m
						.getModuleId());
				TcpClient client = TransportFacade.initClient(m.getIp(),
						m.getPort());
				client.setWeight(m.getWeight());
				client.setModuleId(m.getModuleId());
				RouterSchedules.setModuleScheduleType(
						Short.valueOf(m.getModuleId()),
						m.getRouterScheduleType());
				addListener(client);
			}
		}
	}
}
