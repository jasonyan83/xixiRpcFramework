package xixi.monitor.api;

import java.util.List;

public interface MonitorService {

	void collectStatistics(List<InstanceStatisticsInfo> staList);
}
