package xixi.monitor.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticsCalculateJob {

	private static final Logger logger = LoggerFactory
			.getLogger(StatisticsCalculateJob.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	ScheduledExecutorService service = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					// TODO Auto-generated method stub
					return new Thread(r, "Statistics Draw Thread");
				}

			});

	private String chartsDirectory = "d://charts";
	private String statisticsDirectory = "statistics";

	public String getStatisticsDirectory() {
		return statisticsDirectory;
	}

	public void setStatisticsDirectory(String statisticsDirectory) {
		this.statisticsDirectory = statisticsDirectory;
	}

	private void draw() {
		File rootDir = new File(statisticsDirectory);
		if (!rootDir.exists()) {
			return;
		}
		File[] dateDirs = rootDir.listFiles();
		for (File dateDir : dateDirs) {
			File[] moduleDirs = dateDir.listFiles();
			for (File moduleDir : moduleDirs) {
				File[] ipDirs = moduleDir.listFiles();
				for (File ipDir : ipDirs) {

					File[] serviceFiles = ipDir.listFiles();
					for (File serviceFile : serviceFiles) {
						String serviceFileName = chartsDirectory + "/"
								+ dateDir.getName() + "/" + moduleDir.getName()
								+ "/" + ipDir.getName() + "/"
								+ serviceFile.getName();

						File transTimepicFile = new File(serviceFileName
								+ "-TT" + ".png");
						File transNumpicFile = new File(serviceFileName + "-TN"
								+ ".png");

						Map<String, Double> numData = new HashMap<String, Double>();
						Map<String, Double> avgTransTimeData = new HashMap<String, Double>();
						double[] numSummary = new double[2];
						double[] avgTransTimeSummary = new double[2];

						// because we need to calculate transaction number per
						// seconds, so the unit is 60
						// the data wrote into the file is number per minute
						appendData(serviceFile, numData, 1, numSummary, 60);
						// the data wrote into the file is already average
						// transaction time per minute
						appendData(serviceFile, avgTransTimeData, 2,
								avgTransTimeSummary, 1);

						createChart("t/s", serviceFile.getName(),
								dateDir.getName(), numData, numSummary,
								transNumpicFile);

						createChart("ms/t", serviceFile.getName(),
								dateDir.getName(), avgTransTimeData,
								avgTransTimeSummary, transTimepicFile);
					}
				}
			}
		}
	}

	private void appendData(File file, Map<String, Double> data, int type,
			double[] summary, int unit) {
		if (file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				try {
					String line;
					while ((line = reader.readLine()) != null) {
						// line=1051 18 50
						String[] strs = line.split(" ");
						if (strs.length >= 3) {
							String key = strs[0];
							double value = Double.valueOf(strs[type]) / unit;
							data.put(key, Double.valueOf(strs[type]));
							summary[0] = Math.max(summary[0], value);
							summary[1] = summary[1] == 0 ? value : Math.min(
									summary[1], value);
						}
					}
				} finally {
					reader.close();
				}
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	private static void createChart(String key, String service, String date,
			Map<String, Double> data, double[] summary, File chartFile) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		DecimalFormat numberFormat = new DecimalFormat("###,##0.##");
		TimeSeriesCollection xydataset = new TimeSeriesCollection();

		TimeSeries timeseries = new TimeSeries("Tranaction Data - " + key);
		for (Map.Entry<String, Double> entry : data.entrySet()) {

			try {
				timeseries.add(
						new Minute(dateFormat.parse(date + entry.getKey())),
						entry.getValue());
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		xydataset.addSeries(timeseries);
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("max: "
				+ numberFormat.format(summary[0])
				+ (summary[1] >= 0 ? " min: " + numberFormat.format(summary[1])
						: ""), service + "  " + toDisplayDate(date), key,
				xydataset, true, true, false);
		jfreechart.setBackgroundPaint(Color.WHITE);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(Color.GRAY);
		xyplot.setRangeGridlinePaint(Color.GRAY);
		xyplot.setDomainGridlinesVisible(true);
		xyplot.setRangeGridlinesVisible(true);
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		BufferedImage image = jfreechart.createBufferedImage(600, 300);
		try {
			if (logger.isInfoEnabled()) {
				logger.info("write chart: " + chartFile.getAbsolutePath());
			}
			File methodChartDir = chartFile.getParentFile();
			if (methodChartDir != null && !methodChartDir.exists()) {
				methodChartDir.mkdirs();
			}
			FileOutputStream output = new FileOutputStream(chartFile);
			try {
				ImageIO.write(image, "png", output);
				output.flush();
			} finally {
				output.close();
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}
	}

	private static String toDisplayDate(String date) {
		try {
			Date formatDate = new SimpleDateFormat("yyyyMMdd").parse(date);
			return new SimpleDateFormat("yyyy-MM-dd").format(formatDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void init() {
		service.scheduleWithFixedDelay(new StatisticsDrawTask(), 5 * 1000,
				60 * 1000, TimeUnit.MILLISECONDS);
	}

	private class StatisticsDrawTask implements Runnable {
		@Override
		public void run() {
			logger.info("Starting draw from ", statisticsDirectory);
			draw();
		}

	}

}
