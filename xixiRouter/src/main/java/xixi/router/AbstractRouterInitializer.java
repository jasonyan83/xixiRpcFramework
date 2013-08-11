package xixi.router;

import static xixi.router.Router.ROUTERMAP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.transport.client.TcpClient;
import xixi.transport.listener.ConnectorListener;

public abstract class AbstractRouterInitializer implements RouterInitializer{

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractRouterInitializer.class);
	
	protected void addListener(final TcpClient client){
		logger.debug("Entering router addListener");
		client.addConnectorListener(new ConnectorListener(){
			@Override
			public void onConnected() {
				logger.debug("DirectRouterInitializer on client connected");
				Router router = ROUTERMAP.get(client.getModuleId());
				if(router!=null){
					router.addTcpClient(client);
				}
				else{
					logger.error("Add client to router failed. Router {} is NULL", router);
				}
			}

			@Override
			public void onDisConnected() {
				Router router = ROUTERMAP.get(client.getModuleId());
				if(router!=null){
					router.removeTcpClient(client);
				}
				else{
					logger.error("Remove client from router failed.  Router {} is NULL", router);
				}
			}
		});
	}
}
