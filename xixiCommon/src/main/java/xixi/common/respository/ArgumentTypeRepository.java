package xixi.common.respository;

import java.util.HashMap;
import java.util.Map;

public class ArgumentTypeRepository {

	private static final Map<String, Class<?>> argumentes = new HashMap<String, Class<?>>();
	
	public static void addArgumentType(String serviceName, Class<?> clz){
		argumentes.put(serviceName, clz);
	}
	
	public static Class<?> getArgumentType(String serviceName){
		return argumentes.get(serviceName);
	}
	
	public static Map<String, Class<?>> getAllArgumentType(){
		Map<String, Class<?>> ret = new HashMap<String, Class<?>>();
		for(Map.Entry<String, Class<?>> entry : argumentes.entrySet()){
			ret.put(entry.getKey(), entry.getValue());
		}
		return ret;
	}
}
