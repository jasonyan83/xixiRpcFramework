package xixi.rc.register;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.monitor.dashboard.ModuleInstanceStatusInfo;
import xixi.transport.channel.Channel;

public class FileRegistry implements Registry{

	@Override
	public boolean register(ModuleInstanceInfo moduleInfo) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unRegister(short moduleId, String version, String ipAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ModuleInstanceInfo> getModuleInstances(short moduleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildInstanceChannelMap(short moduleId, String ipAddress,
			Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildModuleDependencyMap(short moduleId, short dependentModuleId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Short> getDependentModuleIds(short moduleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel getChannelByInstance(String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstanceIpByChannel(Channel channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleInstanceStatusInfo getModuleStatusInfo(short moduleId, String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean updateModuleStatusInfo(ModuleInstanceStatusInfo moduleStatusInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeInstance(short moduleId, String ipAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeInstanceAndDeactive(Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ModuleInstanceStatusInfo> getAllModules() {
		// TODO Auto-generated method stub
		return null;
	}
}
