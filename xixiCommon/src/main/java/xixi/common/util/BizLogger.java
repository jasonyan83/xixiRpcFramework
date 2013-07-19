package xixi.common.util;

import xixi.common.util.BizLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.util.BizLogger;

public class BizLogger {
    private Logger logger = LoggerFactory.getLogger(BizLogger.class);;

    public BizLogger() {
   }

    public void log(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            String value = String.valueOf(arg);
            value = escape(value);
            builder.append(value).append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        logger.info(builder.toString());
    }

    public String escape(String val) {
        if(val==null){
            return "";
        }
        return val.replace(",", "\\,");
    }
}