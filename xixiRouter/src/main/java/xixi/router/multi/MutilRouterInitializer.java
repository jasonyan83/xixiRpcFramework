package xixi.router.multi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.common.constants.Constants;
import xixi.common.respository.DependencyModuleRepository;
import xixi.rc.iservice.RCModuleService;
import xixi.router.DefaultMutilConnectRouter;
import xixi.router.Router;
import xixi.router.RouterInitializer;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class MutilRouterInitializer implements RouterInitializer {

	private static final Logger logger = LoggerFactory
			.getLogger(MutilRouterInitializer.class);

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

		List<Short> destMoudleIdList = DependencyModuleRepository
				.getDepentModuleIdList();
		logger.debug("Get Dependency moduleId list " + destMoudleIdList);

		List<ModuleInfo> modulesInfo = moduleService.getInstanceList(
				Constants.SOURCE_MODULEID, destMoudleIdList);

		logger.debug("Get Dependency ModuleInfo list from RC: " + modulesInfo);

		if (modulesInfo != null && modulesInfo.size() > 0) {
			for (ModuleInfo m : modulesInfo) {
				Router r = DefaultMutilConnectRouter.getOrAddRouter(m.getModuleId());
				TcpClient client = TransportFacade.initClient(m.getIp(),
						m.getPort());
				client.setWeight(m.getWeight());
				r.addTcpClient(client);
			}
		}
	}
}
