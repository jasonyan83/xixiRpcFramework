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
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.xml.ParseException;
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

	private String chartsDirectory = "charts";
	private String statisticsDirectory = "statistics";
	
	 private void draw() {
	        File rootDir = new File(statisticsDirectory);
	        if (! rootDir.exists()) {
	            return;
	        }
	        File[] dateDirs = rootDir.listFiles();
	        for (File dateDir : dateDirs) {
	            File[] moduleDirs = dateDir.listFiles();
	            for (File moduleDir : moduleDirs) {
	                File[] ipDirs = moduleDir.listFiles();
	                for (File ipDir : ipDirs) {
	                	
	                	File[] serviceFiles = ipDir.listFiles();
	                	for(File serviceFile : serviceFiles){
	                		 String serviceFileName = chartsDirectory + "/" + dateDir.getName() + "/" + moduleDir.getName() + "/" + ipDir.getName()+"/"+serviceFile;
	                		 
	                		File picFile = new File(serviceFileName + ".png");
	  	                    long picModified = picFile.lastModified();
	  	                    boolean changed = false;
	  	                    Map<String, long[]> data = new HashMap<String, long[]>();
	  	                    double[] dataSummary = new double[4];
	  	                    
	  	                    
	  	                  appendData(new File[] {consumerSuccessFile, providerSuccessFile}, data, dataSummary);
	  	                    
	  	                    
	  	                    
	  	                  if (changed) {
		                        divData(successData, 60);
		                        successSummary[0] = successSummary[0] / 60;
		                        successSummary[1] = successSummary[1] / 60;
		                        successSummary[2] = successSummary[2] / 60;
		                        createChart("t/s", moduleDir.getName(), ipDir.getName(), dateDir.getName(), new String[] {CONSUMER, PROVIDER}, successData, successSummary, successFile.getAbsolutePath());
		                    }
	                	}
	                    String methodUri = chartsDirectory + "/" + dateDir.getName() + "/" + moduleDir.getName() + "/" + ipDir.getName();
	                           
	                    File[] consumerDirs = ipDir.listFiles();
	                    for (File consumerDir : consumerDirs) {
	                        File[] providerDirs = consumerDir.listFiles();
	                        for (File providerDir : providerDirs) {
	                            File consumerSuccessFile = new File(providerDir, CONSUMER + "." + SUCCESS);
	                            File providerSuccessFile = new File(providerDir, PROVIDER + "." + SUCCESS);
	                            appendData(new File[] {consumerSuccessFile, providerSuccessFile}, successData, successSummary);
	                            if (consumerSuccessFile.lastModified() > successModified 
	                                    || providerSuccessFile.lastModified() > successModified) {
	                                successChanged = true;
	                            }
	                            
	                            File consumerElapsedFile = new File(providerDir, CONSUMER + "." + ELAPSED);
	                            File providerElapsedFile = new File(providerDir, PROVIDER + "." + ELAPSED);
	                            appendData(new File[] {consumerElapsedFile, providerElapsedFile}, elapsedData, elapsedSummary);
	                            elapsedMax = Math.max(elapsedMax, CountUtils.max(new File(providerDir, CONSUMER + "." + MAX_ELAPSED)));
	                            elapsedMax = Math.max(elapsedMax, CountUtils.max(new File(providerDir, PROVIDER + "." + MAX_ELAPSED)));
	                            if (consumerElapsedFile.lastModified() > elapsedModified 
	                                    || providerElapsedFile.lastModified() > elapsedModified) {
	                                elapsedChanged = true;
	                            }
	                        }
	                    }
	                    if (elapsedChanged) {
	                        divData(elapsedData, successData);
	                        elapsedSummary[0] = elapsedMax;
	                        elapsedSummary[1] = -1;
	                        elapsedSummary[2] = successSummary[3] == 0 ? 0 : elapsedSummary[3] / successSummary[3];
	                        elapsedSummary[3] = -1;
	                        createChart("ms/t", moduleDir.getName(), ipDir.getName(), dateDir.getName(), new String[] {CONSUMER, PROVIDER}, elapsedData, elapsedSummary, elapsedFile.getAbsolutePath());
	                    }
	                    if (successChanged) {
	                        divData(successData, 60);
	                        successSummary[0] = successSummary[0] / 60;
	                        successSummary[1] = successSummary[1] / 60;
	                        successSummary[2] = successSummary[2] / 60;
	                        createChart("t/s", moduleDir.getName(), ipDir.getName(), dateDir.getName(), new String[] {CONSUMER, PROVIDER}, successData, successSummary, successFile.getAbsolutePath());
	                    }
	                }
	            }
	        }
	    }
	 
	    private void appendData(File file, Map<String,Long> data, double[] summary) {
	    	if (file.exists()){
	            try {
	                BufferedReader reader = new BufferedReader(new FileReader(file));
	                try {
	                    int sum = 0;
	                    int cnt = 0;
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        int index = line.indexOf(" ");
	                        if (index > 0) {
	                            String key = line.substring(0, index).trim();
	                            long value = Long.parseLong(line.substring(index + 1).trim());
	                            data.put(key, value);
	  
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
	private static void createChart(String key, String service, String method, String date, String[] types, Map<String, long[]> data, double[] summary, String path) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        DecimalFormat numberFormat = new DecimalFormat("###,##0.##");
        TimeSeriesCollection xydataset = new TimeSeriesCollection();
        for (int i = 0; i < types.length; i ++) {
            String type = types[i];
            TimeSeries timeseries = new TimeSeries(type);
            for (Map.Entry<String, long[]> entry : data.entrySet()) {
                try {
                    timeseries.add(new Minute(dateFormat.parse(date + entry.getKey())), entry.getValue()[i]);
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            xydataset.addSeries(timeseries);
        }
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
                "max: " + numberFormat.format(summary[0]) + (summary[1] >=0 ? " min: " + numberFormat.format(summary[1]) : "") 
                + " avg: " + numberFormat.format(summary[2]) + (summary[3] >=0 ? " sum: " + numberFormat.format(summary[3]) : ""), 
                toDisplayService(service) + "  " + method + "  " + toDisplayDate(date), key, xydataset, true, true, false);
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
                logger.info("write chart: " + path);
            }
            File methodChartFile = new File(path);
            File methodChartDir = methodChartFile.getParentFile();
            if (methodChartDir != null && ! methodChartDir.exists()) {
                methodChartDir.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(methodChartFile);
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
	
    private static String toDisplayService(String service) {
        int i = service.lastIndexOf('.');
        if (i >= 0) {
            return service.substring(i + 1);
        }
        return service;
    }
    
    private static String toDisplayDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(date));
    }
    
}
