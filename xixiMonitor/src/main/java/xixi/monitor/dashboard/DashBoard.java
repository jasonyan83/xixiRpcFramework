package xixi.monitor.dashboard;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import xixi.monitor.statistics.StatisticsService;

public class DashBoard {

	private StatisticsService statService;

	public StatisticsService getStatService() {
		return statService;
	}

	public void setStatService(StatisticsService statService) {
		this.statService = statService;
	}

	public DashBoard() {
	}

	public ConcurrentHashMap<String, AtomicLong> getFailedTransactionMap() {
		return statService.getFailedTransactionMap();
	}

	public ConcurrentHashMap<String, AtomicLong> getSucceedTransactionMap() {
		return statService.getSucceedTransactionMap();
	}

	public ConcurrentHashMap<String, AtomicLong> getLastMinuteTransactionNumMap() {
		return statService.getLastMinuteTransactionNumMap();
	}

	public ConcurrentHashMap<String, AtomicLong> getLastMinuteAverageTransactionTimeMap() {
		return statService.getLastMinuteTransactionTime();
	}
}
