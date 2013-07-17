package xixi.router.schedule;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRouterSchedule implements RouterSchedule {

	private final Map<Short, RouterSchedule> moduleScheduleMap = new HashMap<Short, RouterSchedule>();

	@Override
	public void setModuleScheduleType(Short moduleId, String type) {
		switch (type) {
		case "roundrobin":
			moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
		case "weight":
			moduleScheduleMap.put(moduleId, new WeightSelectSchedule());
		default:
			moduleScheduleMap.put(moduleId, new RoundRobinSchedule());
		}

	}
}
