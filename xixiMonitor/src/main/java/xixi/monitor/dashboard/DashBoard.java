package xixi.monitor.dashboard;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DashBoard {

	private ConcurrentHashMap<String, AtomicLong> averageTranactionTimeMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> transactionTimeMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> transactionNumMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> failedTransactionMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> succeedTransactionMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> lastMinuteTransactionTime = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> lastMinuteTransactionNumMap = new ConcurrentHashMap<String, AtomicLong>();

	private ConcurrentHashMap<String, AtomicLong> lastMinuteAverageTransactionTimeMap = new ConcurrentHashMap<String, AtomicLong>();

	private AtomicLong lastMinuteStartTime = new AtomicLong(System.currentTimeMillis());

	public void addTranactionTime(String service, long time) {
		
		addTransactionTime(service, time, transactionTimeMap);

		addTransactionNum(service,transactionNumMap);
		
		if (System.currentTimeMillis() - 1000 * 60 <= lastMinuteStartTime.get()) {
			addLastMinuteStat(service,time);
		}
		else{
			lastMinuteStartTime.set(System.currentTimeMillis());
			addLastMinuteStat(service,time);
		}
	}

	private void addLastMinuteStat(String service, long time){
		AtomicLong lmtt = lastMinuteTransactionTime.get(service);
		if (lmtt == null) {
			lmtt = new AtomicLong();
			lmtt.addAndGet(time);
		} else {
			lmtt.addAndGet(time);
		}

		addTransactionNum(service,lastMinuteTransactionNumMap);
	}
	
	private void addTransactionNum(String service,ConcurrentHashMap<String, AtomicLong> map){
		AtomicLong n = map.get(service);
		if (n == null) {
			n = new AtomicLong();
			n.incrementAndGet();
		} else {
			n.incrementAndGet();
		}
	}
	
	private void addTransactionTime(String service, long time, ConcurrentHashMap<String, AtomicLong> map){
		AtomicLong t = map.get(service);
		if (t == null) {
			t = new AtomicLong();
			t.addAndGet(time);
		} else {
			t.addAndGet(time);
		}
	}
	
	public void addFaildeTransaction(String service) {
		addTransactionNum(service,failedTransactionMap);
	}

	public void addSucceedTransaction(String service) {
		addTransactionNum(service,succeedTransactionMap);
	}

	private class DashBoardCountTask implements Runnable {

		@Override
		public void run() {
			while(true){
			    //count total transaction number and average time
				countAverageTranactionTime(transactionTimeMap,transactionNumMap,averageTranactionTimeMap);
				
				//count last minute transaction number and average time
				countAverageTranactionTime(lastMinuteTransactionTime,lastMinuteTransactionNumMap,lastMinuteAverageTransactionTimeMap);
				
				try {
					Thread.sleep(60*1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}


		}
		
		private void countAverageTranactionTime(ConcurrentHashMap<String, AtomicLong> trannsactionTimeMap,
				ConcurrentHashMap<String, AtomicLong> transactionNumMap,
				ConcurrentHashMap<String, AtomicLong> averageTransactionTimeMap){
			for (Entry<String, AtomicLong> entry : trannsactionTimeMap
					.entrySet()) {
				String service = entry.getKey();
				AtomicLong totalTime = entry.getValue();
				
				AtomicLong totalNumber = transactionNumMap.get(service);

				AtomicLong att = averageTransactionTimeMap.get(service);
				if (att != null) {
					att.set(totalTime.get() / totalNumber.get());
				} else {
					att = new AtomicLong();
					att.set(totalTime.get() / totalNumber.get());
				}
			}
		}
	}

	public void init() {
		Thread thread = new Thread(new DashBoardCountTask(),
				"DashBoard count Thread");
		thread.setDaemon(true);
		thread.start();
	}


	public ConcurrentHashMap<String, AtomicLong> getAverageTranactionTimeMap() {
		return averageTranactionTimeMap;
	}

	public ConcurrentHashMap<String, AtomicLong> getTransactionTimeMap() {
		return transactionTimeMap;
	}

	public ConcurrentHashMap<String, AtomicLong> getTransactionNumMap() {
		return transactionNumMap;
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

}
