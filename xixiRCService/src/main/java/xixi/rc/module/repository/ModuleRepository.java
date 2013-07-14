package xixi.rc.module.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleRepository {

	private Map<Short, List<String>> instanceMap = new HashMap<Short, List<String>>();

	public void addNewInstance(short moduleId, String ipAddress) {
		List<String> ipList = instanceMap.get(moduleId);
		if (ipList != null) {
			if (ipList.contains(ipAddress)) {

			}
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
