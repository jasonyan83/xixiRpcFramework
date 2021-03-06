/*package xixi.rc.service.jobs;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.HeartBeat;
import xixi.common.constants.Constants;
import xixi.common.util.ModuleStringUtil;
import xixi.monitor.api.InstanceStatisticsInfo;
import xixi.monitor.dashboard.DashBoard;
import xixi.monitor.dashboard.DashBoard.AbstractDashBoard;
import xixi.rc.iservice.RCHeartBeatService;
import xixi.rc.iservice.RCStatService;

public class RCServiceScheduledJob {

	private static final Logger logger = LoggerFactory
			.getLogger(RCServiceScheduledJob.class);

	private final ScheduledExecutorService hbScheduleService = Executors
			.newScheduledThreadPool(2, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "RC Scheduled Job Thread");
				}
			});

	private RCHeartBeatService hbService;

	private RCStatService statService;

	private DashBoard dashBoard;

	public RCStatService getStatService() {
		return statService;
	}

	public void setStatService(RCStatService statService) {
		this.statService = statService;
	}

	public RCHeartBeatService getHbService() {
		return hbService;
	}

	public DashBoard getDashBoard() {
		return dashBoard;
	}

	public void setDashBoard(DashBoard dashBoard) {
		this.dashBoard = dashBoard;
	}

	public ScheduledExecutorService getHbScheduleService() {
		return hbScheduleService;
	}

	public void setHbService(RCHeartBeatService hbService) {
		this.hbService = hbService;
	}

	public void start() {
		logger.info("HeartBeat Schedule Job started!");
		hbScheduleService.scheduleWithFixedDelay(new HeartBeatTask(), 10000,
				10000, TimeUnit.MILLISECONDS);

		logger.info("ModuleStatus Schedule Job started!");
		hbScheduleService.scheduleWithFixedDelay(new ModuleStatusTask(), 15*1000,
				300*1000, TimeUnit.MILLISECONDS);
	}

	private void sendModuleStatusInfo() {
		InstanceStatisticsInfo moduleInfo = new InstanceStatisticsInfo();
		AbstractDashBoard serverDashboard = dashBoard.getDashBoard("server");
		Long totalNum = 0l;
		Long lastMinuteNum = 0l;
		if (serverDashboard != null) {
			for (AtomicLong num : serverDashboard.getTransactionNumMap()
					.values()) {
				totalNum = totalNum + num.get();
			}
			for (AtomicLong num : serverDashboard
					.getLastMinuteTransactionNumMap().values()) {
				lastMinuteNum = lastMinuteNum + num.get();
			}
		}

		moduleInfo.setLastMinuteTaskCount(lastMinuteNum);
		moduleInfo.setTotalTaskCount(totalNum);
		// TODO: define the detail stat info in the next version
		
		 * moduleInfo.setAverageTaskExecTime(averageTaskExecTime);
		 * moduleInfo.setLastMinuteTaskExecTime(lastMinuteTaskExecTime)
		 
		moduleInfo.setLive(true);
		moduleInfo.setLastHBTime(new Date());
		moduleInfo.setModuleId(Constants.SOURCE_MODULEID);
		moduleInfo
				.setIpAddress(Constants.LOCAL_IP + ModuleStringUtil.IP_SEPERATE + Constants.LOCAL_PORT);

		logger.debug("Sending moduleStatusInfo " + moduleInfo);
		statService.sendModuleStatInfo(moduleInfo);

	}

	private void sendHeartBeat() {
		HeartBeat heartBeat = new HeartBeat(Constants.SOURCE_MODULEID,
				Constants.LOCAL_IP + ModuleStringUtil.IP_SEPERATE + Constants.LOCAL_PORT, 10000);
		logger.debug("Sending heartbeat " + heartBeat);
		hbService.heartBeat(heartBeat);
	}

	private class HeartBeatTask implements Runnable {
		@Override
		public void run() {
			sendHeartBeat();
		}
	}

	private class ModuleStatusTask implements Runnable {
		@Override
		public void run() {
			sendModuleStatusInfo();
		}
	}
}
*/