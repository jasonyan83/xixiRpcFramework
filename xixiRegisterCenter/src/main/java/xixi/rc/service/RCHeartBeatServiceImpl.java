package xixi.rc.service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.HeartBeat;
import xix.rc.bean.ModuleInstanceStatusInfo;
import xixi.common.annotation.EventMethod;
import xixi.rc.iservice.RCHeartBeatService;
import xixi.rc.register.Registry;

public class RCHeartBeatServiceImpl implements RCHeartBeatService {

	private static final Logger logger = LoggerFactory
			.getLogger(RCHeartBeatServiceImpl.class);

	private Registry registry;

	private boolean firstCheck = true;
	@Override
	@EventMethod(name = "heartBeat")
	public void heartBeat(HeartBeat heartbeat) {
		logger.debug("Recieved HeartBeat " + heartbeat);
		short moduleId = heartbeat.getModuleId();
		String ipAddress = heartbeat.getIpAddress();
		ModuleInstanceStatusInfo m = registry.getModuleStatusInfo(moduleId, ipAddress);
		m.setLastHBTime(new Date());
		m.setHeartBeatInteval(heartbeat.getInterval());
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public void init() {
		ScheduledExecutorService exe = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "HeartBeat check Thread");
					}
				});

		exe.scheduleWithFixedDelay(new HeartBeatCheckTask(), 5000, 5000,
				TimeUnit.MILLISECONDS);
	}

	private class HeartBeatCheckTask implements Runnable {

		@Override
		public void run() {
			for (ModuleInstanceStatusInfo moduleInfo : registry.getAllModules()) {
					if (!moduleInfo.isLive()) {
/*						logger.debug(
								"Instance {} is down. No not need to check the heartbeat",
								moduleInfo.getModuleId() + "-"
										+ moduleInfo.getIpAddress());*/
						continue;
					}
					if (isHeartbeatTimeout(moduleInfo)) {
						logger.debug(
								"Module {} heartbeat TIMEOUT for the {} times!",
								moduleInfo,
								moduleInfo.getHeartBeatRetryTimes() + 1);
						if (moduleInfo.getHeartBeatRetryTimes() < 3) {
							// if the retry time doesn't exceed the system
							// config times, then wait for retry
							moduleInfo.setHeartBeatRetryTimes(moduleInfo
									.getHeartBeatRetryTimes() + 1);
						} else {
							logger.debug("Set the instane to unactive");
							moduleInfo.setLive(false);
							moduleInfo.setHeartBeatRetryTimes(0);
							registry.removeInstance(moduleInfo.getModuleId(),moduleInfo.getIpAddress());
						}
					} else {
						moduleInfo.setHeartBeatRetryTimes(0);
					}
				}
			}

		}

		private boolean isHeartbeatTimeout(ModuleInstanceStatusInfo moduleInfo) {
			long duration = System.currentTimeMillis()
					- moduleInfo.getLastHBTime().getTime();
			return (duration >= moduleInfo.getHeartBeatInteval());
		}
	
}
