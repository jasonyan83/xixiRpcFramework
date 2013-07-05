package xixi.rpc.future;

import java.util.UUID;

import xixi.rpc.Callback;
import xixi.rpc.bean.RpcResponse;
import xixi.rpc.exception.RpcException;
import xixi.rpc.exception.TimeoutException;

public abstract class AbstractRpcFuture extends AbstractFuture implements RpcFuture{

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
		super.getValue(timeout);
		if(retValue!=null){
			if(retValue instanceof RpcResponse){
				RpcResponse resp = (RpcResponse)retValue;
				if(resp.getStatus()==RpcResponse.SUCCEED){
					return ((RpcResponse)retValue).getData();
				}
				if(resp.getStatus()==RpcResponse.ERROR){
					return ((RpcResponse)retValue).getData();
				}
				if(resp.getStatus()==RpcResponse.TIMEOUT){
					throw new TimeoutException("RPC Future timeout for Response");
				}
				return ((RpcResponse)retValue).getData();
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
