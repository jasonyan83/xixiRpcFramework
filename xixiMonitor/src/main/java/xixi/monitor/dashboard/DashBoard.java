package xixi.monitor.dashboard;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DashBoard {

	private ConcurrentHashMap<String,AtomicLong> averageTranactionTimeMap = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> transactionTimeMap = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> transactionNumMap = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> failedTransactionMap = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> succeedTransactionMap = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> lastMinuteTransactionTime = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> lastMinuteTransactionNumMap = new ConcurrentHashMap<String,AtomicLong>();
	
	private ConcurrentHashMap<String,AtomicLong> lastMinuteAverageTransactionNumMap = new ConcurrentHashMap<String,AtomicLong>();
	
	public void addTranactionTime(String service,long time){
		AtomicLong t = transactionTimeMap.get(service);
		if(t==null){
			t = new AtomicLong();
			t.addAndGet(time);
		}
		else{
			t.addAndGet(time);
		}
	}
	
	public void addFaildeTransaction(String service){
		AtomicLong t = failedTransactionMap.get(service);
		if(t==null){
			t = new AtomicLong();
			t.incrementAndGet();
		}
		else{
			t.incrementAndGet();
		}
	}
	
	public void addSucceedTransaction(String service){
		AtomicLong t = succeedTransactionMap.get(service);
		if(t==null){
			t = new AtomicLong();
			t.incrementAndGet();
		}
		else{
			t.incrementAndGet();
		}
	}
	
	private class DashBoardCountTask implements Runnable{

		@Override
		public void run() {
			for(Entry<String, AtomicLong> entry : transactionTimeMap.entrySet()){
				  String service = entry.getKey();
				  averageTranactionTimeMap.get(service);
			}
			
		}
		
	}
	public void init(){
		Thread thread = new Thread(new DashBoardCountTask(),"DashBoard count Thread");
		thread.setDaemon(true);
		thread.start();
	}
	
}
