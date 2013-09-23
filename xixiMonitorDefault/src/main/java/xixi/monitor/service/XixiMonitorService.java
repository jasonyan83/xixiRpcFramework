package xixi.monitor.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.monitor.api.InstanceStatisticsInfo;
import xixi.monitor.api.MonitorService;

public class XixiMonitorService implements MonitorService{

	private static final Logger logger = LoggerFactory
			.getLogger(XixiMonitorService.class);
	
	private String statisticsDirectory = "statistics";
	 
	@Override
	public void collectStatistics(List<InstanceStatisticsInfo> statList) {

		logger.debug("Starting write statistics into disk");
		
		
		for(InstanceStatisticsInfo stat: statList){
		
			  String day = new SimpleDateFormat("yyyyMMdd").format(stat.getDate());
			  String minute = new SimpleDateFormat("MMdd").format(stat.getDate());
		 
        String filename = statisticsDirectory 
                + "/" + day 
                + "/" + stat.getModuleId() 
                + "/" + stat.getServiceName();
			  
		}
		
	}

}
