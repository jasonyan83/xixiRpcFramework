package xixi.router.direct;

import xixi.router.AbstractRouter;
import xixi.rpc.bean.RpcMessage;
import xixi.rpc.bean.RpcRequest;
import xixi.transport.client.TcpClient;

public class DirectlyConnectRouter extends AbstractRouter {

	public static DirectlyConnectRouter getOrAddRouter(short moduleId) {
		if (moduleId < 0) {
			return null;
		}
		DirectlyConnectRouter router = (DirectlyConnectRouter) ROUTERMAP
				.get(moduleId);
		if (router == null) {
			DirectlyConnectRouter r = new DirectlyConnectRouter(moduleId);
			router = (DirectlyConnectRouter) ROUTERMAP.putIfAbsent(moduleId, r);
			if (router == null) {
				router = r;
			}
		}
		return router;
	}

	private TcpClient tcpClient;

	private DirectlyConnectRouter(int moduleId) {
		super(moduleId);
	}

	@Override
	protected TcpClient getTcpClient() {
		return tcpClient;
	}

	@Override
	public void addTcpClient(TcpClient client) {
		this.tcpClient = client;
	}

	@Override
	public void router(RpcMessage message) {
		RpcRequest request = (RpcRequest) message;
		getTcpClient().send(request);
	}

	@Override
	public void destory() {
		getTcpClient().stop();
	}
}
