package xixi.router.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.transport.client.TcpClient;

//ThreadSafe, the setModuleScheduleType will only be invoked by main thread.
public class RouterSchedules {

	private static final Logger logger = 
        	LoggerFactory.getLogger(RouterSchedules.class);
    
	
	protected static final Map<Short, RouterSchedule> moduleScheduleMap = new HashMap<Short, RouterSchedule>();
	
	public static void setModuleScheduleType(Short moduleId, String type) {
		switch (type) {
		case "roundrobin":
			if (moduleScheduleMap.get(moduleId) == null) {
				moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
			}
			break;
		case "weight":
			if (moduleScheduleMap.get(moduleId) == null) {
				moduleScheduleMap.put(moduleId, new WeightSelectSchedule());
			}
			break;
		default:
			if (moduleScheduleMap.get(moduleId) == null) {
				moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
			}
		}
	}

	public static TcpClient schedule(Short moduleId, List<TcpClient> clientList) {
		if(clientList!=null&&clientList.size()>0){
			RouterSchedule schedule = moduleScheduleMap.get(moduleId);
			if(schedule==null){
				logger.warn("No matched ScheduleType for module " + moduleId + ". Will use the first client in the list");
				
				return clientList.get(0);
			}
			return schedule.schedule(moduleId, clientList);
		}
		else{
			return null;
		}
	
	}

}
