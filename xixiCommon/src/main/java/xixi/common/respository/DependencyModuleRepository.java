package xixi.common.respository;

import java.util.ArrayList;
import java.util.List;

public class DependencyModuleRepository {

	public static final List<Short> dependentModuleIdList = new ArrayList<Short>();
	
	public static void addDepedentModuleId(Short moduleId){
		dependentModuleIdList.add(moduleId);
	}
	
	public static List<Short> getDepentModuleIdList(){
		return dependentModuleIdList;
	}
}
