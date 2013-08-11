package xixi.rc.register;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xix.rc.bean.ModuleInfo;
import xix.rc.bean.ModuleStatusInfo;
import xixi.transport.channel.Channel;

//Could have default, redis,DB, file system Regisgry
public interface Registry {

	public boolean register(ModuleInfo moduleInfo) throws Exception ;
	
	public boolean unRegister(short moduleId, String ipAddress);
	
	public List<ModuleInfo> getModuleInstances(short moduleId);
	
	public void buildInstanceChannelMap(short moduleId,String ipAddress,Channel channel);
	
	public void buildModuleDependencyMap(short moduleId,short dependentModuleId);
	
	public List<Short> getDependentModuleIds(short moduleId);
	
	public Channel getChannelByInstance(String ipAddress);
	
	public String getInstanceIpByChannel(Channel channel);
	
	public ModuleStatusInfo getModuleStatusInfo(short moduleId, String ipAddress);
	
	public Map<Short, HashMap<String,  ModuleStatusInfo>> getModulesMap();
	
	public boolean updateModuleStatusInfo(ModuleStatusInfo moduleStatusInfo);
	
	public void removeInstance(short moduleId, String ipAddress);
	
	public void removeInstanceAndDeactive(Channel channel);
}
