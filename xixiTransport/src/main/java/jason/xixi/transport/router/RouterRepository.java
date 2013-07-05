package jason.xixi.transport.router;

import jason.xixi.common.bean.ModuleStatInfoToClient;
import jason.xixi.common.util.collection.ConcurrentHashListMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterRepository {

	
	private Map<Integer,Integer> routerNumMap = new ConcurrentHashMap<Integer,Integer>();
	
	private int routerNum;
	
	private Map<Integer,Router> routerMap = new ConcurrentHashMap<Integer,Router>();
	
	private ConcurrentHashListMap<Integer, ModuleStatInfoToClient> candidateModuleMap = new ConcurrentHashListMap<Integer, ModuleStatInfoToClient>();
	
	
	public void addRouter(Router router){
		routerMap.put(router.getModuleId(), router);
	}
	
	public boolean hasRouter(Integer moduleId){
		return routerMap.containsKey(moduleId);
	}
	
	public void addToCandiModuleMap(ModuleStatInfoToClient moduleInfo){
		candidateModuleMap.addValue(moduleInfo.getModuleId(), moduleInfo);
	}
	
	public ModuleStatInfoToClient getCandiModule(Integer moduleId){
		List<ModuleStatInfoToClient> list = this.candidateModuleMap.get(moduleId);
		if(list!=null&&!list.isEmpty()){
			return list.remove(0);
		}
		return null;
	}
	
	public Map<Integer, Router> getRouterMap() {
		return routerMap;
	}

	public void setRouterMap(Map<Integer, Router> routerMap) {
		this.routerMap = routerMap;
	}

	public Router getRouter(Integer moduleId){
		return routerMap.get(moduleId);
	}


	public ConcurrentHashListMap<Integer, ModuleStatInfoToClient> getCandidateModuleMap() {
		return candidateModuleMap;
	}

	public void setCandidateModuleMap(
			ConcurrentHashListMap<Integer, ModuleStatInfoToClient> candidateModuleMap) {
		this.candidateModuleMap = candidateModuleMap;
	}

	public Map<Integer, Integer> getRouterNumMap() {
		return routerNumMap;
	}

	public void setRouterNumMap(Map<Integer, Integer> routerNumMap) {
		this.routerNumMap = routerNumMap;
	}

	public int getRouterNum() {
		return routerNum;
	}

	public void setRouterNum(int routerNum) {
		this.routerNum = routerNum;
	}

}
