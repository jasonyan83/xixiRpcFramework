package xixi.rc.register;

import java.util.List;

import xix.rc.bean.ModuleInfo;
import xixi.transport.channel.Channel;

public interface Registry {

	public boolean register(short moduleId, String ipAddress,int weight,String description) throws Exception ;
	
	public boolean unRegister(short moduleId, String ipAddress);
	
	public List<ModuleInfo> getModuleInstances(short moduleId);
	
	public void buildInstanceChannelMap(short moduleId,String ipAddress,Channel channel);
	
	public void buildModuleDependencyMap(short srcModuleId, short destModuleId);
	
	public List<Short> getDependentModuleIds(short moduleId);
	
	public Channel getChannelByInstance(String ipAddress);
	
	public String getInstanceIpByChannel(Channel channel);
}
