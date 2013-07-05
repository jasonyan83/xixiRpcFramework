package jason.xixi.common.util.collection;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashListMap<K, V> extends AbstractListMap<K,V> implements ListMap<K,V>{
	public ConcurrentHashListMap(){
       	super.map =  new ConcurrentHashMap<K, List<V>>();	
}
}
