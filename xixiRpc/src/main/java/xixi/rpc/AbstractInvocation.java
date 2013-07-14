package xixi.rpc;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInvocation implements Invocation{

	private Map<String, Object> properties = new HashMap<String, Object>();
	
	@Override
	public void addProperty(String key, Object value) {
        properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return  properties.get(key);
    }
}
