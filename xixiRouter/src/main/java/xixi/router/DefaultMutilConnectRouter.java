package xixi.router;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.router.multi.ModuleRepository;
import xixi.router.schedule.RouterSchedules;
import xixi.rpc.bean.RpcMessage;
import xixi.transport.client.TcpClient;

public class DefaultMutilConnectRouter extends AbstractRouter {
	
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultMutilConnectRouter.class);

	private static ScheduledExecutorService exe = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "MultiRouterCleanThread");
				}
			});

	private ModuleRepository moduleRepository;

	public static DefaultMutilConnectRouter getOrAddRouter(short moduleId) {
		if (moduleId < 0) {
			return null;
		}
		DefaultMutilConnectRouter router = (DefaultMutilConnectRouter) ROUTERMAP
				.get(moduleId);
		if (router == null) {
			DefaultMutilConnectRouter r = new DefaultMutilConnectRouter(moduleId);
			router = (DefaultMutilConnectRouter) ROUTERMAP.putIfAbsent(moduleId, r);
			if (router == null) {
				router = r;
			}
		}
		return router;
	}

	private final List<TcpClient> clientList = new ArrayList<TcpClient>();


	public DefaultMutilConnectRouter(short moduleId) {

		super(moduleId);
	}

	@Override
	public void addTcpClient(TcpClient client) {
		logger.debug("Add new client for mutilConnectRouter: " + client);
		clientList.add(client);
		moduleRepository.addNewInstance(this.moduleId(), client.getDestIpAddress());
	}

	@Override
	public void router(RpcMessage message) {
		TcpClient client = RouterSchedules.schedule(this.moduleId(), clientList);
		client.send(message);
	}

	@Override
	public void removeTcpClient(String ipAddress) {
		for (TcpClient client : clientList) {
			if (ipAddress.equals(client.getDestIpAddress())) {
				logger.debug("Preparing to remove client:" + client);
				exe.schedule(new RouterCleanTask(client), 5000, TimeUnit.MILLISECONDS);
			}
		}
	}
	
	public ModuleRepository getModuleRepository() {
		return moduleRepository;
	}

	public void setModuleRepository(ModuleRepository moduleRepository) {
		this.moduleRepository = moduleRepository;
	}


	private static class RouterCleanTask implements Runnable {

		private final TcpClient client;

		public RouterCleanTask(TcpClient client) {
			this.client = client;
		}

		@Override
		public void run() {
			logger.debug("Stopping the client:" + client);
			
			client.stop();
		}
	}


	@Override
	protected TcpClient getTcpClient() {
		// TODO Auto-generated method stub
		return null;
	}

}
