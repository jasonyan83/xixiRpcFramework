package xixi.rc.iservice;

import xix.rc.bean.RegistryNotify;
import xixi.common.bean.ModuleInfo;

public interface RegistryService extends RCModuleService{

	public void subscribe(ModuleInfo module, RegistryNotify notify);
	
	public void unSubscribe(ModuleInfo module, RegistryNotify notify);
}
