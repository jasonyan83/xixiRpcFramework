package xixi.rc.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInfo;
import xixi.common.annotation.EventMethod;
import xixi.common.util.ModuleStringUtil;
import xixi.rc.iservice.RCNotifyService;
import xixi.router.DefaultConnectRouter;
import xixi.router.Router;
import xixi.router.multi.ModuleRepository;
import xixi.transport.client.TcpClient;
import xixi.transport.facade.TransportFacade;

public class RCNotifyServiceImpl implements RCNotifyService {

	private static final Logger logger = LoggerFactory
			.getLogger(RCNotifyServiceImpl.class);

	private ModuleRepository repository;

	@Override
	@EventMethod(name = "updatedModuleInstances", filter = "bizLogger")
	public void updatedModuleInstances(short moduleId,
			List<ModuleInfo> instancesList) {
		List<String> newInstanceList = new ArrayList<String>();
		if(instancesList!=null&&!instancesList.isEmpty()){
			short destModuleId = instancesList.get(0).getModuleId();
			logger.debug("Updating Module {} instances", destModuleId);
			for (ModuleInfo m : instancesList) {
				newInstanceList.add(m.getIpAddress());
			}
			
			List<String> addedInstanceList = repository.getAddedInstanceList(
					destModuleId, newInstanceList);
			List<String> removedInstanceList = repository.getRemovedInstanceList(
					destModuleId, newInstanceList);

			if (addedInstanceList != null && !addedInstanceList.isEmpty()) {
				for (String ipAddress : addedInstanceList) {
					logger.debug("Module {} got new service instance: {}", destModuleId,ipAddress);
					for(ModuleInfo m : instancesList){
						if(m.getIpAddress().equals(ipAddress)){
							Router r = DefaultConnectRouter.getOrAddRouter(destModuleId);
							TcpClient client = TransportFacade.initClient(
									ModuleStringUtil.getIp(ipAddress),
									ModuleStringUtil.getPort(ipAddress));
							client.setWeight(m.getWeight());
							logger.debug("Adding new client{} to Router {}", client,r);
							r.addTcpClient(client);
						}
					}
				}
			}

			if (removedInstanceList != null && !removedInstanceList.isEmpty()) {
				for (String ipAddress : removedInstanceList) {
					logger.debug("Module {} has instance to remove: {}", destModuleId, ipAddress);
					Router r = DefaultConnectRouter.getOrAddRouter(destModuleId);
					r.removeTcpClient(ipAddress);
				}
			}
		}
		
	}

	public ModuleRepository getRepository() {
		return repository;
	}

	public void setRepository(ModuleRepository repository) {
		this.repository = repository;
	}
}
