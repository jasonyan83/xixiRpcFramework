package xixi.monitor.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.monitor.api.InstanceStatisticsInfo;
import xixi.monitor.api.MonitorService;

public class XixiMonitorService implements MonitorService {

	public String getStatisticsDirectory() {
		return statisticsDirectory;
	}

	public void setStatisticsDirectory(String statisticsDirectory) {
		this.statisticsDirectory = statisticsDirectory;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(XixiMonitorService.class);

	private String statisticsDirectory = "statistics";

	@Override
	public void collectStatistics(List<InstanceStatisticsInfo> statList) {

		logger.info("Starting write {} statistics into disk", statList.size());

		for (InstanceStatisticsInfo stat : statList) {

			String day = new SimpleDateFormat("yyyyMMdd")
					.format(stat.getDate());
			String minute = new SimpleDateFormat("MMdd").format(stat.getDate());

			String filename = statisticsDirectory + "/" + day + "/"
					+ stat.getModuleId() + "/" + stat.getIpAddress() + "/"
					+ stat.getServiceName();

			File file = new File(filename);
			File dir = file.getParentFile();
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}

			try {
				FileWriter writer = new FileWriter(filename, true);
				writer.append(minute + " " + stat.getLastMinuteTaskCount()
						+ " " + stat.getLastMinuteTaskATT());
				writer.flush();
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		logger.debug("Done writing statistics");

	}

}