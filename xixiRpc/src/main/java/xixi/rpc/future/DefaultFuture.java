package xixi.rpc.future;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.constants.Constants;
import xixi.rpc.Callback;
import xixi.rpc.bean.RpcResponse;

public class DefaultFuture  extends AbstractRpcFuture{

	private static final Logger logger = LoggerFactory.getLogger(DefaultFuture.class);
	
	private static final Map<UUID,DefaultFuture> FUTURES = new ConcurrentHashMap<UUID,DefaultFuture>();
	
	private int timeout;
	
	private DefaultFuture(UUID id, int timeout, Callback callback){
		super(id,callback);
		this.setStartTime();
		if(timeout<=0){
			this.timeout = Constants.DEFAULT_FUTURE_TIMEOUT;
		}
	}
	
	private DefaultFuture(UUID id){
		this(id,0,null);
	}
	
	public static DefaultFuture getOrCreateFuture(UUID id, int timeout, Callback callbacks){
		DefaultFuture future = FUTURES.get(id);
		if(future!=null){
			return future;
		}
		else{
			future =  new DefaultFuture(id,timeout,callbacks);
			FUTURES.put(id, future);
			return future;
		}
	}
	
	public static DefaultFuture getOrCreateFuture(UUID id){
		DefaultFuture future = FUTURES.get(id);
		if(future!=null){
			return future;
		}
		else{
			future =  new DefaultFuture(id);
			FUTURES.put(id, future);
			return future;
		}
	}
	
	public static void setResult(Object retValue){
		RpcResponse response = (RpcResponse)retValue;
		DefaultFuture future = FUTURES.get(response.getTransactionId());
		if(future!=null){
			future.setValue(response);
		}
		else{
			//TODO: more well defined message
			logger.warn("Received Timeout Message" + retValue);
		}
		
	}

	private static class FutureTimeoutScanJob implements Runnable{

		@Override
		public void run() {
			for(DefaultFuture f : FUTURES.values()){
				if((System.currentTimeMillis() - f.futureStartTime())>= f.timeout()){
					//if timeout, invoke the setValue immiditely, so that the get() operationi will not be blocked.
					RpcResponse response = new RpcResponse();
					response.setTransactionId(f.id()).setStatus(RpcResponse.TIMEOUT);
					response.setStatus(RpcResponse.TIMEOUT);
					DefaultFuture.setResult(response);
					FUTURES.remove(f.id());
				}
			}
		}
	}
	
	static{
		/*Thread thread = new Thread(new FutureTimeoutScanJob());
		thread.setDaemon(true);
		thread.setName("FutureTimoutScanThread");
		thread.start();*/
	}

	public int timeout() {
		return timeout;
	}


}
