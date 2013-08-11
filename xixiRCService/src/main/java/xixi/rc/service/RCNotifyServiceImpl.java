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
import xixi.transport.listener.ConnectorListener;

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
							final Router r = DefaultConnectRouter.getOrAddRouter(destModuleId);
							final TcpClient client = TransportFacade.initClient(
									ModuleStringUtil.getIp(ipAddress),
									ModuleStringUtil.getPort(ipAddress));
							client.setWeight(m.getWeight());
							client.setModuleId(destModuleId);
							logger.debug("Adding new client{} to Router {}", client,r);
							client.addConnectorListener(new ConnectorListener(){
								@Override
								public void onConnected() {
									if(r!=null){
										r.addTcpClient(client);		
									}
									else{
										logger.error("Router is NULL for module: {}", client.getModuleId());
									}
								}

								@Override
								public void onDisConnected() {
									if(r!=null){
										r.removeTcpClient(client);
									}
									else{
										logger.error("Router is NULL for module: {}", client.getModuleId());
									}
								}
							});
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
