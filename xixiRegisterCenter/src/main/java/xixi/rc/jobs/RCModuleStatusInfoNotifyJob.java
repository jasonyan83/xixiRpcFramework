package xixi.rc.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xix.rc.bean.ModuleStatusInfo;
import xixi.rc.iservice.RCNotifyService;
import xixi.rc.register.Registry;

//The notify job is not used by default. RC will only send moduleStatus updated notify 
//when a new instance register to the RC
public class RCModuleStatusInfoNotifyJob {

	private static final Logger logger = LoggerFactory
			.getLogger(RCModuleStatusInfoNotifyJob.class);
	private Registry registry;
	
	private RCNotifyService notifyService;
	
	public void init() {
		ScheduledExecutorService exe = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "ModuleStatusInfo notify Thread");
					}
				});

		exe.scheduleWithFixedDelay(new ModuleStatusInfoNotifyTaskk(), 10*1000, 10*1000,
				TimeUnit.MILLISECONDS);
	}
	
	private void sendModuleStatusInfoNotify(){
	    Map<Short, HashMap<String, ModuleStatusInfo>> fullCopyMap = new HashMap<Short, HashMap<String, ModuleStatusInfo>>();
	   // fullCopyMap.putAll(registry.getModulesMap());
	    
	    for(Entry<Short, HashMap<String, ModuleStatusInfo>> entry : fullCopyMap.entrySet()){
	    	Short moduleId = entry.getKey();
	    	
	    	List<Short> moduleIdList = registry.getDependentModuleIds(moduleId);
			if (moduleIdList != null && !moduleIdList.isEmpty()) {
				logger.debug(" Module {} has following dependecny module {}", moduleId, moduleIdList);
				List<ModuleInfo> selfInstanceList = registry
						.getModuleInstances(moduleId);
				for(Short id : moduleIdList){
					logger.debug("Send module instance registered notify to module {}", id);
					notifyService.updatedModuleInstances(id, selfInstanceList);
				}
			}
			else{
				logger.debug("No module depend on Module {}", moduleId);
			}
	    }
	}
	
	private class ModuleStatusInfoNotifyTaskk implements Runnable {
		@Override
		public void run() {
			sendModuleStatusInfoNotify();
		}
	}
	
	public RCNotifyService getNotifyService() {
		return notifyService;
	}
	public void setNotifyService(RCNotifyService notifyService) {
		this.notifyService = notifyService;
	}
	public Registry getRegistry() {
		return registry;
	}
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}
