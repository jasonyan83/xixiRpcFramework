package xixi.rpc.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import xixi.common.constants.Constants;
import xixi.rpc.Callback;
import xixi.rpc.exception.TimeoutException;

public abstract class AbstractFuture implements Future{

	protected boolean done;
	
	protected boolean fail;
	
	protected Object retValue;
	
	private final Lock lock = new ReentrantLock();
	
	private final Condition condition = lock.newCondition();
	
	private Callback callback;
	
	public AbstractFuture(Callback callback){
		this.callback = callback;
	}
	
	public AbstractFuture(){
	}
	
	@Override
	public void done() {
		done = true;
	}

	@Override
	public void fail() {
		done = true;
		fail = true;
	}

	protected boolean isDone(){
		return done;
	}
	
	protected boolean isSuccess(){
		return done=true&&retValue!=null;
	}
	
	@Override
	public Object getValue() throws TimeoutException{
		return getValue(Constants.DEFAULT_FUTURE_TIMEOUT);
	}
	
	
	@Override
	public Object getValue(int timeout) throws TimeoutException{
		if(timeout<=0){
			timeout = Constants.DEFAULT_FUTURE_TIMEOUT;
		}
		lock.lock();
		try{
			while(!isSuccess()){
				long startTime = System.currentTimeMillis();
				try {
					condition.await(timeout, TimeUnit.MILLISECONDS);
					if(isSuccess()||(System.currentTimeMillis()-startTime)>timeout){
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		finally{
			lock.unlock();
		}
		if(retValue==null){
			throw new TimeoutException("getValue timeout for future");
		}
		
		return retValue;
	}
	
	public void setValue(Object retValue){
		lock.lock();
		try{
			this.retValue = retValue;
			done=true;
			if(condition!=null){
				this.condition.signal();
			}
		}
		finally{
			lock.unlock();
		}
		
		if(callback!=null){
			callback.invoke(this.retValue);

		}
	}
	

	public void addCallback(Callback callback) {
		if(isSuccess()){
			this.callback = callback;
			callback.invoke(this.retValue);
		}
		else{
			boolean isDone = false;
			lock.lock();
			try{
				if(!isSuccess()){
					this.callback = callback;
				}
				else{
					isDone = true;
				}
			}
			finally{
				lock.unlock();
				if(isDone){
					callback.invoke(this.retValue);
				}
			}
		}
	}
}
