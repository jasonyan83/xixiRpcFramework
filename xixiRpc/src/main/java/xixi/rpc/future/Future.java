package xixi.rpc.future;

import xixi.rpc.Callback;
import xixi.rpc.exception.TimeoutException;

public interface Future {

	void done();
	
	void fail(); 
    
	void setValue(Object result);
	
	Object getValue() throws TimeoutException;
	
    Object getValue(int timeout) throws TimeoutException;
	
    void addCallback(Callback callback) throws TimeoutException;

}
