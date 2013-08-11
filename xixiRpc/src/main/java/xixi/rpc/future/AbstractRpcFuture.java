package xixi.rpc.future;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.Callback;
import xixi.rpc.bean.RpcNotify;
import xixi.rpc.bean.RpcResponse;
import xixi.rpc.exception.RpcException;
import xixi.rpc.exception.TimeoutException;

public abstract class AbstractRpcFuture extends AbstractFuture implements RpcFuture{

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractRpcFuture.class);

	private final UUID id;
	
	private long futureStartTime;
	
	public AbstractRpcFuture(UUID uuid,Callback callback){
		super(callback);
		this.id = uuid;
	}
	
	public UUID id(){
		return id;
	}
	
	@Override
	public void setStartTime() {
		this.futureStartTime = System.currentTimeMillis();
	}
	
	public long futureStartTime() {
		return futureStartTime;
	}

	@Override
	public Object getValue(int timeout) throws TimeoutException, RpcException{
		logger.debug("Trying to get value in {}ms", timeout);
		super.getValue(timeout);
		if(retValue!=null){
			if(retValue instanceof RpcResponse){
				RpcResponse resp = (RpcResponse)retValue;
			    if(resp.getData().length==1){
			    	if(resp.getStatus()==RpcResponse.SUCCEED){
						return resp.getData()[0];
					}
					if(resp.getStatus()==RpcResponse.ERROR){
						return resp.getData()[0];
					}
					if(resp.getStatus()==RpcResponse.TIMEOUT){
						logger.error("PC Future timeout for Response: {}", resp);
						throw new TimeoutException("RPC Future timeout for Response "+ resp);
					}
				   return resp.getData()[0];
			    }
			    else{
			    	throw new RpcException("Invaliate return value, the length for rpcReponse.date is " + resp.getData().length);
			    }
			}
			else if(retValue instanceof RpcNotify){
				RpcNotify notify = (RpcNotify)retValue;
				if(notify.getData()!=null&&notify.getData().length==1){
					return notify.getData()[0];
				}
				else{
					throw new RpcException("Invaliate RPC Notify value, the length for rpcNotify.date is " + notify.getData().length);
				}
			}
			else{
				throw new RpcException("Not valid response, except RpcRespose but is " + retValue.getClass().toString() + " actually");
			}
		}
		else{
			throw new RpcException("response is null");
		}
	}
}
