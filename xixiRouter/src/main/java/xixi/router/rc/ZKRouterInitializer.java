package xixi.router.rc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInstanceInfo;
import xix.rc.bean.RegistryNotify;
import xixi.common.bean.ModuleInfo;
import xixi.common.constants.Constants;
import xixi.common.respository.DependencyModuleRepository;
import xixi.common.spring.BeanFactoryUtil;
import xixi.common.thread.MainThreadLock;
import xixi.common.util.ConfigUtils;
import xixi.common.util.ModuleStringUtil;
import xixi.rc.iservice.RegistryService;
import xixi.router.DefaultConnectRouter;
import xixi.router.Router;
import xixi.router.direct.DirectRouterInitializer;
import xixi.router.multi.ModuleRepository;
import xixi.router.schedule.RouterSchedules;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class ZKRouterInitializer extends DirectRouterInitializer {

	private static final Logger logger = LoggerFactory
			.getLogger(ZKRouterInitializer.class);

	private final RegistryService registerService;

	private ModuleRepository moduleRepository;

	public ZKRouterInitializer() {
		String registryName = ConfigUtils.getProperty(Constants.REGISTRY_TYPE);
		registerService = (RegistryService) BeanFactoryUtil
				.getBean(registryName);
		
		moduleRepository = (ModuleRepository) BeanFactoryUtil
				.getBean("moduleRepository");
	}

	public void init() {
		logger.debug("Initializing ZKRouterInitializer...");
		int weight = Integer.valueOf(ConfigUtils.getProperty(Constants.WEIGHT,
				"0"));
		
		String version =  ConfigUtils.getProperty(Constants.MODULE_VERSION, "1.0");
		String description = ConfigUtils.getProperty(Constants.DESCRIPTION, "");
		String routerScheduleType = ConfigUtils.getProperty(
				Constants.ROUTER_SCHEDULE_TYPE_KEY,
				Constants.DEFAULT_ROUTER_SCHEDULE);
		ModuleInstanceInfo moduleInfo = new ModuleInstanceInfo();

		moduleInfo.setModuleId(Constants.SOURCE_MODULEID);
		moduleInfo.setWeight(weight);
		moduleInfo.setDescription(description);
		moduleInfo.setIpAddress(Constants.LOCAL_IP
				+ ModuleStringUtil.IP_SEPERATE + Constants.LOCAL_PORT);
		moduleInfo.setRouterScheduleType(routerScheduleType);
		moduleInfo.setVersion(version);

		int result = registerService.registerModule(moduleInfo);
		if (result != 0) {
			logger.error("Register ZooKeeper FAILED. PLEASE CHECK!");
		} else {
			logger.info("Register ZooKeeper SUCCEED!");
			MainThreadLock.lock.set(true);
		}

		List<ModuleInfo> destMoudleList = DependencyModuleRepository
				.getDepentModuleIdList();

		for (ModuleInfo m : destMoudleList) {
			registerService.subscribe(m, new RegistryNotify() {
				@Override
				public void onModuleInstanceChanged(ModuleInstanceInfo m) {
					Router r = DefaultConnectRouter.getOrAddRouter(m
							.getModuleId());
					if (r != null) {
						TcpClient client = r.getTcpClient(m.getIpAddress());
						if(client!=null){
							client.setWeight(m.getWeight());
							logger.debug("Change Client {} config SUCCEED!", client);
						}
						else{
							logger.error("Client is NULL for instance {}", m);
						}
					} else {
						logger.error("Router is NULL for instance {}", m);
					}
				}

				@Override
				public void onModuleRouterChanged(
						List<ModuleInstanceInfo> instanceList) {

					if (instanceList != null && !instanceList.isEmpty()) {
						short moduleId;
						List<String> ipAddressList = new ArrayList<String>();
						moduleId = instanceList.get(0).getModuleId();

						for (ModuleInstanceInfo m : instanceList) {
							ipAddressList.add(m.getIpAddress());
						}

						List<ModuleInstanceInfo> addedInstanceList = moduleRepository
								.getAddedInstanceList(moduleId, instanceList);
						for (ModuleInstanceInfo m : addedInstanceList) {
							TcpClient client = TransportFacade.initClient(
									m.getIp(), m.getPort());
							client.setWeight(m.getWeight());
							client.setModuleId(m.getModuleId());
							RouterSchedules.setModuleScheduleType(
									Short.valueOf(m.getModuleId()),
									m.getRouterScheduleType());
							addListener(client);
						}

						List<String> removedInstanceList = moduleRepository
								.getRemovedInstanceList(moduleId, ipAddressList);
						for (String ipAddress : removedInstanceList) {
							Router r = DefaultConnectRouter
									.getOrAddRouter(moduleId);
							r.removeTcpClient(ipAddress);
						}
					} else {
						logger.warn("chanaged instance list is EMPTY!");
					}

				}

			});
		}

	}
}
