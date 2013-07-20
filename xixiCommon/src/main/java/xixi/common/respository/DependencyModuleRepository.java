package xixi.common.respository;

import java.util.ArrayList;
import java.util.List;

public class DependencyModuleRepository {

	public static final List<Short> dependentModuleIdList = new ArrayList<Short>();
	
	public static void addDepedentModuleId(Short moduleId){
		if(dependentModuleIdList.contains(moduleId)){
			return ;
		}
		dependentModuleIdList.add(moduleId);
	}
	
	public static List<Short> getDepentModuleIdList(){
		dependentModuleIdList.remove(new Short((short) 101));
		return dependentModuleIdList;
	}
}
