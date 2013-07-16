package xixi.router.rc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.HeartBeat;
import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;
import xixi.rc.iservice.RCHeartBeatService;
import xixi.rc.iservice.RCModuleService;
import xixi.router.direct.DirectRouterInitializer;

public class RCRouterInitializer extends DirectRouterInitializer {

	private static final Logger logger = LoggerFactory
			.getLogger(RCRouterInitializer.class);

	private RCModuleService rcModuleService;

	public RCModuleService getRcModuleService() {
		return rcModuleService;
	}

	public void setRcModuleService(RCModuleService rcModuleService) {
		this.rcModuleService = rcModuleService;
	}

	public RCRouterInitializer() {
		super.routerAddresses = ConfigUtils.getProperty(
				Constants.RC_ADDRESSES_KEY, "");
	}

	public void init() {
		logger.debug("Initializing DirectRouter, routerAddresses="
				+ routerAddresses);
		super.init();

		int weight = Integer.valueOf(ConfigUtils.getProperty(Constants.WEIGHT,
				"0"));
		String description = ConfigUtils.getProperty(Constants.DESCRIPTION, "");
		int result = rcModuleService.registerModule(Constants.SOURCE_MODULEID,
				Constants.LOCAL_IP + ":" + Constants.LOCAL_PORT, weight,
				description);
		if (result != 0) {
			logger.error("Register RC {0} Failed. PLEASE CHECK!",
					super.routerAddresses);
		} else {
			logger.info("Register RC {0} SUCCEED!", super.routerAddresses);
		}
	}

}
