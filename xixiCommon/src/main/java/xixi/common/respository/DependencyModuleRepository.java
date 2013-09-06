package xixi.common.respository;

import java.util.ArrayList;
import java.util.List;

import xixi.common.annotation.EventService;
import xixi.common.bean.ModuleInfo;

public class DependencyModuleRepository {

	public static final List<ModuleInfo> modules = new ArrayList<ModuleInfo>();
	
	public static void addDepedentModuleId(EventService a){
		if(a!=null&&a.moduleId()!=101){
			ModuleInfo m = new ModuleInfo();
			m.setModuleId(a.moduleId());
			m.setVersion(a.version());
			if(modules.contains(m)){
				return ;
			}
			modules.add(m);
		}
		
		
	}
	
	public static List<ModuleInfo> getDepentModuleIdList(){
		return modules;
	}
}
