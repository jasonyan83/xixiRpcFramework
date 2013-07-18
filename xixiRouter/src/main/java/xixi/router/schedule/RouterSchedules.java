package xixi.router.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xixi.transport.client.TcpClient;

//ThreadSafe, the setModuleScheduleType will only be invoked by main thread.
public class RouterSchedules {

	protected static final Map<Short, RouterSchedule> moduleScheduleMap = new HashMap<Short, RouterSchedule>();

	public static void setModuleScheduleType(Short moduleId, String type) {
		switch (type) {
		case "roundrobin":
			if (moduleScheduleMap.get(moduleId) == null) {
				moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
			}

		case "weight":
			if (moduleScheduleMap.get(moduleId) == null) {
				moduleScheduleMap.put(moduleId, new WeightSelectSchedule());
			}
		default:
			if (moduleScheduleMap.get(moduleId) == null) {

				moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
			}
		}
	}

	public static TcpClient schedule(Short moduleId, List<TcpClient> clientList) {
		RouterSchedule schedule = moduleScheduleMap.get(moduleId);
		return schedule.schedule(moduleId, clientList);
	}

}
