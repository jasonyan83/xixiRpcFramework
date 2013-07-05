package xixi.rpc.client.invoker;

import java.lang.reflect.Method;
import java.util.UUID;

import xixi.common.constants.Constants;
import xixi.rpc.IRpcInvocation;
import xixi.rpc.future.DefaultFuture;
import xixi.rpc.future.Future;
import xixi.rpc.future.RpcFuture;

public class RpcInvocation implements IRpcInvocation {
	
	private RpcFuture future;

	private Object[] args;

	private String rpcInterface;

	private String rpcMethodName;

	private String version;

	private short moduleId;
	
	public RpcInvocation(Object target, Method method,Object... args) {
		this( target.getClass().getCanonicalName(), method.getName(), args);
	}

	public RpcInvocation(String rpcInterface, String rpcMethodName, Object... args) {
		this.args = args;
		this.rpcInterface = rpcInterface;
		this.rpcMethodName = rpcMethodName;
		this.future = DefaultFuture.getOrCreateFuture(UUID.randomUUID(),Constants.DEFAULT_FUTURE_TIMEOUT, null);
	}
	
	@Override
	public RpcFuture future() {
		return future;
	}

	@Override
	public Object[] getArgs() {
		return args;
	}

	@Override
	public String rpcInterface() {
		return rpcInterface;
	}

	@Override
	public String rpcMethod() {
		return rpcMethodName;
	}

	@Override
	public void setFuture(Future future) {
		this.future = (RpcFuture)future;
	}

	@Override
	public String version() {
		return version;
	}

	@Override
	public short moduleId() {
		return moduleId;
	}

	@Override
	public void setModuleId(short moduleId) {
		this.moduleId = moduleId;
	}

}
