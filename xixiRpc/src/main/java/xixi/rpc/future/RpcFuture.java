package xixi.rpc.future;

import java.util.UUID;

public interface RpcFuture extends Future{
    
	void setStartTime();
	
    UUID id();
	
}
