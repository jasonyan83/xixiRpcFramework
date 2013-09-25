package xixi.monitor.statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import xixi.common.constants.Constants;
import xixi.monitor.api.InstanceStatisticsInfo;
import xixi.monitor.api.MonitorService;

public class StatisticsService {

	private ConcurrentHashMap<String, AtomicLong> failedTransactionMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> succeedTransactionMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> lastMinuteTransactionTime = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> lastMinuteTransactionNumMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> lastMinuteAverageTransactionTimeMap = new ConcurrentHashMap<String, AtomicLong>();

	private  SimpleDateFormat format = new SimpleDateFormat("HHmm");
	
	private volatile String currentMinute = format.format(new Date()); 
	
	private BlockingQueue<InstanceStatisticsInfo> queue = new LinkedBlockingQueue<InstanceStatisticsInfo>();
	
	private Lock lock = new ReentrantLock();
	
	private MonitorService monitorService;

	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory(){
		@Override
		public Thread newThread(Runnable r) {
			// TODO Auto-generated method stub
			return new Thread(r, "Statistics Collect Thread");
		}
		
	});
	
	public void addTranactionTime(String service
			, long time) {
		
		long currentTime = System.currentTimeMillis();
		
		if (currentMinute.equals(format.format(currentTime))) {
			addLastMinuteStat(service,time);
		}
		else{
			try{
				lock.lock();
				if (!currentMinute.equals(format.format(currentTime))) {
					snapshotLastMinuteStatus(currentTime - 60000);
				}
				addLastMinuteStat(service,time);
			}
			finally{
				lock.unlock();
			}
		}
	}

	private void snapshotLastMinuteStatus(long previousTime){
		
		HashMap<String, AtomicLong> transactionTime = new HashMap<String, AtomicLong>();
		transactionTime.putAll(lastMinuteTransactionTime);
		
		HashMap<String, AtomicLong> transactionNum = new HashMap<String, AtomicLong>();
		transactionNum.putAll(lastMinuteTransactionNumMap);
		
		lastMinuteTransactionTime.clear();
		lastMinuteTransactionNumMap.clear();
		
		//reset the current minute
		currentMinute=format.format(System.currentTimeMillis());
		
		for (Entry<String, AtomicLong> entry : transactionTime
				.entrySet()) {
			String service = entry.getKey();
			AtomicLong totalTime = entry.getValue();
			
			AtomicLong totalNumber = transactionNum.get(service);
			
			Long att = totalTime.get() / totalNumber.get();
			
			AtomicLong lmatt = lastMinuteAverageTransactionTimeMap.get(service);
			if (lmatt == null) {
				lmatt = new AtomicLong();
				lmatt.set(att);
			} else {
				lmatt.set(att);
			}

			InstanceStatisticsInfo statisticsInfo = new InstanceStatisticsInfo();
			statisticsInfo.setModuleId(Constants.SOURCE_MODULEID);
			statisticsInfo.setIpAddress("TODO");
			statisticsInfo.setServiceName(service);
			statisticsInfo.setLastMinuteTaskCount(totalNumber.get());
			statisticsInfo.setLastMinuteTaskATT(att);
			statisticsInfo.setDate(new Date(previousTime));
			
			queue.add(statisticsInfo);
			//monitorService.collectStatistics(staList)
		}
	}
	
	private void addLastMinuteStat(String service, long time){
		addTransactionTime(service, time, lastMinuteTransactionTime);
		
		addTransactionNum(service,lastMinuteTransactionNumMap);
	}
	
	private void addTransactionTime(String service,long time,ConcurrentHashMap<String, AtomicLong> map){
		AtomicLong lmtt = map.get(service);
		if (lmtt == null) {
			lmtt = new AtomicLong();
			lmtt.addAndGet(time);
			map.put(service, lmtt);
		} else {
			lmtt.addAndGet(time);
		}
	}
	
	private void addTransactionNum(String service,ConcurrentHashMap<String, AtomicLong> map){
		AtomicLong n = map.get(service);
		if (n == null) {
			n = new AtomicLong();
			n.incrementAndGet();
			map.put(service, n);
		} else {
			n.incrementAndGet();
		}
	}
	
	
	public void addFaildeTransaction(String service) {
		addTransactionNum(service,failedTransactionMap);
	}

	public void addSucceedTransaction(String service) {
		addTransactionNum(service,succeedTransactionMap);
	}

	private void countAverageTranactionTime(ConcurrentHashMap<String, AtomicLong> trannsactionTimeMap,
			ConcurrentHashMap<String, AtomicLong> transactionNumMap,
			ConcurrentHashMap<String, AtomicLong> averageTransactionTimeMap){
		for (Entry<String, AtomicLong> entry : trannsactionTimeMap
				.entrySet()) {
			String service = entry.getKey();
			AtomicLong totalTime = entry.getValue();
			
			AtomicLong totalNumber = transactionNumMap.get(service);
			
			Long att = totalTime.get() / totalNumber.get();
			
			AtomicLong lmatt = averageTransactionTimeMap.get(service);
			if (lmatt == null) {
				lmatt = new AtomicLong();
				lmatt.set(att);
				averageTransactionTimeMap.put(service, lmatt);
			} else {
				lmatt.set(att);
			}
		}
	}

	public void init(){
		service.scheduleWithFixedDelay(new StatisticsTask(), 2*1000, 60*1000, TimeUnit.MILLISECONDS);
	}
	
	private class StatisticsTask implements Runnable{
		@Override
		public void run() {
			List<InstanceStatisticsInfo> staList = new ArrayList<InstanceStatisticsInfo>();
			queue.drainTo(staList);
			monitorService.collectStatistics(staList);
		}
		
	}
	
	public ConcurrentHashMap<String, AtomicLong> getFailedTransactionMap() {
		return failedTransactionMap;
	}

	public ConcurrentHashMap<String, AtomicLong> getSucceedTransactionMap() {
		return succeedTransactionMap;
	}

	public ConcurrentHashMap<String, AtomicLong> getLastMinuteTransactionTime() {
		return lastMinuteTransactionTime;
	}

	public ConcurrentHashMap<String, AtomicLong> getLastMinuteTransactionNumMap() {
		return lastMinuteTransactionNumMap;
	}

	public ConcurrentHashMap<String, AtomicLong> getLastMinuteAverageTransactionTimeMap() {
		return lastMinuteAverageTransactionTimeMap;
	}
	
	public MonitorService getMonitorService() {
		return monitorService;
	}

	public void setMonitorService(MonitorService monitorService) {
		this.monitorService = monitorService;
	}


}
