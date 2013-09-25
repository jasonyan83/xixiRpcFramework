package xixi.monitor.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import xixi.common.annotation.EventMethod;
import xixi.monitor.api.InstanceStatisticsInfo;
import xixi.monitor.service.XixiMonitorService;

public class MethodReflactionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		XixiMonitorService service = new XixiMonitorService();
		Method[] methods = service.getClass().getMethods();
		for(Method m : methods){
			if(m.getAnnotation(EventMethod.class)!=null){
				System.out.println(m.getName());
				
				InstanceStatisticsInfo info = new InstanceStatisticsInfo();
				info.setModuleId(Short.parseShort("302"));
				info.setIpAddress("127.0.0.1:7010");
				info.setLastMinuteTaskATT(12);
				info.setLastMinuteTaskCount(99);
				info.setServiceName("jasontestservice");
				ArrayList<InstanceStatisticsInfo> list = new ArrayList<InstanceStatisticsInfo>();
				list.add(info);
				
				Object[] objs = {list};
				
				try {
					m.invoke(service, objs[0]);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
