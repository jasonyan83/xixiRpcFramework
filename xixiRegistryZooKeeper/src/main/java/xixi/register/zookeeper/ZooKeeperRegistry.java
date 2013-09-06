package xixi.register.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import xix.rc.bean.ModuleInstanceInfo;
import xix.rc.bean.RegistryNotify;
import xixi.common.bean.ModuleInfo;
import xixi.common.constants.Constants;
import xixi.rc.iservice.RegistryService;
import xixi.register.client.AbstractZookeeperClient;
import xixi.register.client.ChildListener;
import xixi.register.client.ZooKeeperClient;

public class ZooKeeperRegistry implements RegistryService{

	private static final Logger logger = LoggerFactory.getLogger(AbstractZookeeperClient.class);

	
	private ZooKeeperClient zkClient;

	public final String prefix = "/xixi/";
	@Override
	public int registerModule(ModuleInstanceInfo moduleInfo) {
		String path = prefix +  moduleInfo.getModuleId() + Constants.PATH_SEPARATOR + moduleInfo.getVersion() + Constants.PATH_SEPARATOR + moduleInfo.getIpAddress();
		byte[] data = SerializationUtils.serialize(moduleInfo);
		zkClient.create(path, true, data);
		
		return 0;
	}

	@Override
	public int unRegisterModule(short moduleId, String version, String ipAddress) {
		String path = prefix +  moduleId + Constants.PATH_SEPARATOR + version + Constants.PATH_SEPARATOR + ipAddress ;
		zkClient.delete(path);
		return 0;
	}

	public void subscribe(ModuleInfo module, final RegistryNotify notify){
		String path =  prefix + module.getModuleId() + Constants.PATH_SEPARATOR + module.getVersion();
		zkClient.addChildListener(path, new ChildListener(){
			@Override
			public void childChanged(String path, List<String> children) {
				 List<ModuleInstanceInfo> instanceList = new ArrayList<ModuleInstanceInfo>();
			   for(String childPath : children){
				   byte[] data = zkClient.getData(path + Constants.PATH_SEPARATOR + childPath);  
				   ModuleInstanceInfo m = (ModuleInstanceInfo)SerializationUtils.deserialize(data);
				   instanceList.add(m); 
			   }
			   notify.onModuleRouterChanged(instanceList);
			}

			@Override
			public void nodeDataChanged(String path, byte[] data) {
				 ModuleInstanceInfo m = (ModuleInstanceInfo)SerializationUtils.deserialize(data);
				notify.onModuleInstanceChanged(m);
			}
		});
	}
		
	public List<ModuleInstanceInfo> getModuleInstances(short moduleId) {
		// will get the lasted version
		return getModuleInstanceList(moduleId, "-1");
	}

	private List<ModuleInstanceInfo> getModuleInstanceList(short moduleId,String version) {
		String path = prefix +  moduleId + Constants.PATH_SEPARATOR + version;
		List<String> childpaths = zkClient.getChildren(path);
		 List<ModuleInstanceInfo> moduleList = new ArrayList<ModuleInstanceInfo>();
		for(String childpath: childpaths){
			byte[] data = zkClient.getData(path + Constants.PATH_SEPARATOR + childpath);
			if(data!=null){
				ModuleInstanceInfo m = (ModuleInstanceInfo)SerializationUtils.deserialize(data);
				moduleList.add(m); 
			}
			else{
				logger.warn("No Data for path: {}", path + Constants.PATH_SEPARATOR + childpath);
			}
			
		}
		return moduleList;
	}

	private List<ModuleInstanceInfo> getModuleInstanceList(List<ModuleInfo> destModuleInfoList){
		 List<ModuleInstanceInfo> moduleList = new ArrayList<ModuleInstanceInfo>();
		for(ModuleInfo m : destModuleInfoList){
			List<ModuleInstanceInfo> list = getModuleInstanceList(m.getModuleId(),m.getVersion());
			moduleList.addAll(list);
		}
		return moduleList;
	}
	
	@Override
	public List<ModuleInstanceInfo> getInstanceList(short srcModuleId,
			List<ModuleInfo> destModuleList) {
		return getModuleInstanceList(destModuleList);
	}

	@Override
	public void unSubscribe(ModuleInfo module, RegistryNotify notify) {
		// TODO Auto-generated method stub
		
	}
	
	public ZooKeeperClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZooKeeperClient zkClient) {
		this.zkClient = zkClient;
	}
}
