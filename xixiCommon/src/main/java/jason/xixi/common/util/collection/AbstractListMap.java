package jason.xixi.common.util.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractListMap<K,V> implements ListMap<K,V>{
	protected Map<K, List<V>> map;

	public Map<K, List<V>> getMap() {
		return map;
	}

	public void setMap(Map<K, List<V>> map){
		this.map = map;
	}
	
	public List<V> get(K key){
		return map.get(key);
	}
	
	public List<V> put(K key, List<V> value){
		return map.put(key, value);
	}
	
	public Set<K> keySet(){
		return this.map.keySet();
	}
	
	public void addValue(K k, V v) {
		List<V> list = map.get(k);
		if (list == null || list.isEmpty()) {
			list = new ArrayList<V>();
			list.add(v);
			map.put(k, list);
		}
	}
	
	public boolean isMapEmpty(){
		return map.isEmpty();
	}
	
	public boolean isValueEmpty(K k){
		List<V> list = map.get(k);
		return list.isEmpty();
	}
}
