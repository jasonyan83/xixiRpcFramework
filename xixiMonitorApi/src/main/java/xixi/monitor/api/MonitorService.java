package xixi.monitor.api;

import java.util.List;

import xixi.common.annotation.EventService;

@EventService(name = "monitorService", moduleId = 150, version = "1.0")
public interface MonitorService {
	
	void collectStatistics(List<StatisticsInfoMinute> staList);
}
