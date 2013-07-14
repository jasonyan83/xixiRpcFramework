package xixi.rc.module.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleRepository {

	private static final Logger logger = LoggerFactory
			.getLogger(ModuleRepository.class);
	
	private final Map<Short, List<String>> instanceMap = new HashMap<Short, List<String>>();

	public void addNewInstance(short moduleId, String ipAddress) {
		logger.debug("Adding new instance {0} to module {1}", ipAddress,moduleId);
		List<String> ipList = instanceMap.get(moduleId);
		if (ipList != null) {
			if (ipList.contains(ipAddress)) {
				logger.warn("Duplicate instance, please double check");
			}
			else{
				ipList.add(ipAddress);
			}
		}
		else{
			ipList = new ArrayList<String>();
			ipList.add(ipAddress);
			instanceMap.put(moduleId, ipList);
		}
	}

	public void removeInstance(short moduleId, String ipAddress){
		logger.debug("Removing instance {0} from module {1}", ipAddress,moduleId);
		List<String> ipList = instanceMap.get(moduleId);
		if (ipList != null) {
			if (ipList.contains(ipAddress)) {
				ipList.remove(ipAddress);
			}
			else{
				logger.error("The instance is not in the active instance list currently");
			}
		}
		else{
			logger.error("Empty instance list");
		}
	}
	
	public List<String> getAddedInstanceList(short moduleId,
			List<String> newInstanceList) {
		List<String> changedList = new ArrayList<String>();

		List<String> ipList = instanceMap.get(moduleId);

		if (ipList != null || ipList.size() > 0) {
			for (String ip : newInstanceList) {
				if (!ipList.contains(ip)) {
					changedList.add(ip);
				}
			}
			return changedList;
		} else {
			return newInstanceList;
		}
	}

	public List<String> getRemovedInstanceList(short moduleId,
			List<String> newInstanceList) {
		List<String> removedList = new ArrayList<String>();

		List<String> ipList = instanceMap.get(moduleId);

		if (ipList != null || ipList.size() > 0) {
			for (String ip : ipList) {
				if (!newInstanceList.contains(ip)) {
					removedList.add(ip);
				}
			}
			return removedList;
		} else {
			return ipList;
		}
	}
}
