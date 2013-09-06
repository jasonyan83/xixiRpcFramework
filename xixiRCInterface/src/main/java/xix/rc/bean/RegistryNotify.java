package xix.rc.bean;

import java.util.List;


public interface RegistryNotify {

	void onModuleInstanceChanged(ModuleInstanceInfo m);
	void onModuleRouterChanged(List<ModuleInstanceInfo> moduleInstanceList);
}
