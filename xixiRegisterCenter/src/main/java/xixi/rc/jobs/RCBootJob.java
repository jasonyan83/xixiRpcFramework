package xixi.rc.jobs;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.monitor.dashboard.ModuleInstanceStatusInfo;
import xixi.rc.register.Registry;

public class RCBootJob {

	private static final Logger logger = LoggerFactory
			.getLogger(RCBootJob.class);

	ScheduledExecutorService exe = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "Registry check Thread");
				}
			});

	private Registry registry;

	private int checkTimes = 0;

	private int maxCheckTimes = 2;
	private Date startTime = new Date();;

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public void init() {

		exe.schedule(new RegistryCheckTask(), 60 * 1000, TimeUnit.MILLISECONDS);

	}

	private class RegistryCheckTask implements Runnable {

		@Override
		public void run() {
			for (ModuleInstanceStatusInfo moduleInfo : registry.getAllModules()) {

				if (!moduleInfo.isLive()) {
					continue;
				}
				if (!isNewRegistry(moduleInfo)) {
					if (checkTimes >= maxCheckTimes) {
						moduleInfo.setLive(false);
					} else {
						logger.debug("Wait for Module {} registoring to RC!",
								moduleInfo);
					}

				}
			}
			checkTimes++;
			if (checkTimes < maxCheckTimes) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					exe.submit(new RegistryCheckTask());
				}
			} else {
				exe.shutdown();
			}
		}

		private boolean isNewRegistry(ModuleInstanceStatusInfo moduleInfo) {
			return (moduleInfo.getRegisterTime().getTime()
					- startTime.getTime() > 0);
		}
	}
}
