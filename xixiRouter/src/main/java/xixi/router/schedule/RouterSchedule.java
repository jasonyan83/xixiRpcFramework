package xixi.router.schedule;

import java.util.List;

import xixi.transport.client.TcpClient;

public interface RouterSchedule {

	public TcpClient schedule(Short moduleId, List<TcpClient> clientList);
}
