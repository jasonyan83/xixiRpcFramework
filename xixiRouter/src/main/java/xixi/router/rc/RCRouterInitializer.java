package xixi.router.rc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.common.constants.Constants;
import xixi.common.spring.BeanFactoryUtil;
import xixi.common.thread.MainThreadLock;
import xixi.common.util.ConfigUtils;
import xixi.common.util.ModuleStringUtil;
import xixi.rc.iservice.RCModuleService;
import xixi.router.direct.DirectRouterInitializer;
import xixi.transport.client.TcpClient;
import xixi.transport.listener.ConnectorListener;

public class RCRouterInitializer extends DirectRouterInitializer {

	private static final Logger logger = LoggerFactory
			.getLogger(RCRouterInitializer.class);

	private RCModuleService rcModuleService;

	public RCRouterInitializer() {
		routerAddresses = ConfigUtils.getProperty(Constants.RC_ADDRESSES_KEY,
				"");
		if (routerAddresses != null && !"".equals(routerAddresses)) {
			routerAddressArray = routerAddresses.split(",");
		}
		rcModuleService = (RCModuleService) BeanFactoryUtil
				.getBean("rcModuleService");
		if (rcModuleService == null) {
			logger.error("Get RCModuleService Failed");
		}
	}

	public void init() {
		logger.debug("Initializing RCDirectRouter, routerAddresses="
				+ routerAddresses);
		super.init();
	}

	@Override
	protected void addListener(TcpClient client) {
		super.addListener(client);
		client.addConnectorListener(new ConnectorListener() {
			@Override
			public void onConnected() {
				synchronized (MainThreadLock.lock) {
					logger.debug("On client connected!");
					int weight = Integer.valueOf(ConfigUtils.getProperty(
							Constants.WEIGHT, "0"));
					String description = ConfigUtils.getProperty(
							Constants.DESCRIPTION, "");
					String routerScheduleType = ConfigUtils.getProperty(
							Constants.ROUTER_SCHEDULE_TYPE_KEY,
							Constants.DEFAULT_ROUTER_SCHEDULE);
					ModuleInfo moduleInfo = new ModuleInfo();

					moduleInfo.setModuleId(Constants.SOURCE_MODULEID);
					moduleInfo.setWeight(weight);
					moduleInfo.setDescription(description);
					moduleInfo.setIpAddress(Constants.LOCAL_IP
							+ ModuleStringUtil.IP_SEPERATE
							+ Constants.LOCAL_PORT);
					moduleInfo.setRouterScheduleType(routerScheduleType);

					int result = rcModuleService.registerModule(moduleInfo);
					if (result != 0) {
						logger.error("Register RC {} Failed. PLEASE CHECK!",
								RCRouterInitializer.this.routerAddresses);
					} else {
						logger.info("Register RC {} SUCCEED!", routerAddresses);
					}
					// notify
					MainThreadLock.lock.set(true);
					MainThreadLock.lock.notify();
				}
			}

			@Override
			public void onDisConnected() {
				// TODO Auto-generated method stub

			}

		});

	}
}
