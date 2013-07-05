package jason.xixi.common.util.collection;

import java.util.HashMap;
import java.util.List;

public class HashListMap<K, V> extends AbstractListMap<K,V> implements ListMap<K,V>{

	public HashListMap(){
           	super.map =  new HashMap<K, List<V>>();	
	}
}
