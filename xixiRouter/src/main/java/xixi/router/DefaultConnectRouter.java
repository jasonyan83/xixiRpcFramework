package xixi.router;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.spring.BeanFactoryUtil;
import xixi.router.multi.ModuleRepository;
import xixi.router.schedule.RouterSchedules;
import xixi.rpc.bean.RpcMessage;
import xixi.transport.client.TcpClient;

public class DefaultConnectRouter extends AbstractRouter {
	
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultConnectRouter.class);

	private static ScheduledExecutorService exe = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "MultiRouterCleanThread");
				}
			});

	private final ModuleRepository moduleRepository;

	public static DefaultConnectRouter getOrAddRouter(short moduleId) {
		if (moduleId < 0) {
			return null;
		}
		
		DefaultConnectRouter router = (DefaultConnectRouter) ROUTERMAP
				.get(moduleId);
		if (router == null) {
			ModuleRepository moduleRepository = (ModuleRepository)BeanFactoryUtil.getBean("moduleRepository");
			DefaultConnectRouter r = new DefaultConnectRouter(moduleId,moduleRepository);
			router = (DefaultConnectRouter) ROUTERMAP.putIfAbsent(moduleId, r);
			if (router == null) {
				router = r;
			}
		}
		return router;
	}

	private final List<TcpClient> clientList = new ArrayList<TcpClient>();

	public DefaultConnectRouter(short moduleId,ModuleRepository moduleRepository) {
		super(moduleId);
		this.moduleRepository = moduleRepository;
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
	
	@Override
	public void destory() {
		logger.debug("destory the DefaultConnecetRouter. Currently the client number is:" + clientList.size());
		for (TcpClient client : clientList) {
			 exe.schedule(new RouterCleanTask(client), 5000, TimeUnit.MILLISECONDS);
		}
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

}
