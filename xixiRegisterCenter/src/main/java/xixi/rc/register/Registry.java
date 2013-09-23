package xixi.rc.register;

import java.util.List;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.monitor.dashboard.ModuleInstanceStatusInfo;
import xixi.transport.channel.Channel;

//Could have default, redis,DB, file system Regisgry
public interface Registry {

	public boolean register(ModuleInstanceInfo moduleInfo) throws Exception ;
	
	public boolean unRegister(short moduleId, String version, String ipAddress);
	
	public List<ModuleInstanceInfo> getModuleInstances(short moduleId);
	
	public void buildInstanceChannelMap(short moduleId,String ipAddress,Channel channel);
	
	public void buildModuleDependencyMap(short moduleId,short dependentModuleId);
	
	public List<Short> getDependentModuleIds(short moduleId);
	
	public Channel getChannelByInstance(String ipAddress);
	
	public String getInstanceIpByChannel(Channel channel);
	
	public ModuleInstanceStatusInfo getModuleStatusInfo(short moduleId, String ipAddress);
	
	public List<ModuleInstanceStatusInfo> getAllModules();
	
	public boolean updateModuleStatusInfo(ModuleInstanceStatusInfo moduleStatusInfo);
	
	public void removeInstance(short moduleId, String ipAddress);
	
	public void removeInstanceAndDeactive(Channel channel);
}
