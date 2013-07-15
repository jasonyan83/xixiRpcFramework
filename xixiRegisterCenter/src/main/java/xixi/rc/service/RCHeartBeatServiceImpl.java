package xixi.rc.service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.HeartBeat;
import xix.rc.bean.ModuleStatusInfo;
import xixi.common.annotation.EventMethod;
import xixi.rc.iservice.RCHeartBeatService;
import xixi.rc.register.Registry;

public class RCHeartBeatServiceImpl implements RCHeartBeatService {

	private static final Logger logger = LoggerFactory
			.getLogger(RCHeartBeatServiceImpl.class);
	
	private Registry registry;
	
	@Override
	@EventMethod(name = "rcheartbeat")
	public void heartBeat(HeartBeat heartbeat) {
		logger.debug("Recieved HeartBeat " + heartbeat);
		short moduleId = heartbeat.getModuleId();
		String ipAddress= heartbeat.getIpAddress();
		ModuleStatusInfo m =registry.getModuleStatusInfo(moduleId, ipAddress);
		m.setLastHBTime(new Date());
		m.setHeartBeatInteval(heartbeat.getInterval());	
	}
	
	public Registry getRegistry() {
		return registry;
	}
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	
	private static class HeartBeatCheckTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	static{

		ScheduledExecutorService exe = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "HeartBeat check Thread");
					}

				});
		
		exe.scheduleWithFixedDelay(new HeartBeatCheckTask(), 5000,5000, TimeUnit.MILLISECONDS);
	}
}
