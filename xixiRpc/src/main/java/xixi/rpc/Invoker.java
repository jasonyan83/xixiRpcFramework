package xixi.rpc;

import xixi.rpc.future.Future;

public interface Invoker {

	public Future invoke(Invocation inv) throws InvocationException;

	public void destory();
}
