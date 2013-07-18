package xixi.router.schedule;

import java.util.List;

import xixi.common.util.AtomicPositiveInteger;
import xixi.transport.client.TcpClient;

public class RoundRobinSchedule implements RouterSchedule {

	private final AtomicPositiveInteger sequence =new AtomicPositiveInteger();
	
	@Override
	public TcpClient schedule(Short moduleId, List<TcpClient> clientList) {
		return clientList.get(sequence.getAndIncrement()%clientList.size());
	}

}
