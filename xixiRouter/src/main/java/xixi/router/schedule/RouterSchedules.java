package xixi.router.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xixi.transport.client.TcpClient;

public class RouterSchedules{

	protected static final Map<Short, RouterSchedule> moduleScheduleMap = new HashMap<Short, RouterSchedule>();

	public static void setModuleScheduleType(Short moduleId, String type) {
		switch (type) {
		case "roundrobin":
			moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
		case "weight":
			moduleScheduleMap.put(moduleId, new WeightSelectSchedule());
		default:
			moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
		}
	}

	public static TcpClient schedule(Short moduleId, List<TcpClient> clientList) {
		RouterSchedule schedule = moduleScheduleMap.get(moduleId);
		return schedule.schedule(moduleId, clientList);
	}
	
}
