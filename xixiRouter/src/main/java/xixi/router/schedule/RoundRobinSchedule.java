package xixi.router.schedule;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import xixi.common.util.AtomicPositiveInteger;
import xixi.transport.client.TcpClient;

public class RoundRobinSchedule extends AbstractRouterSchedule implements RouterSchedule {

	private final ConcurrentMap<Short, AtomicPositiveInteger> sequences = new ConcurrentHashMap<Short, AtomicPositiveInteger>();
	
	@Override
	public TcpClient schedule(Short moduleId, List<TcpClient> clientList) {
		AtomicPositiveInteger sequence = sequences.get(moduleId);
		if(sequence==null){
			sequences.putIfAbsent(moduleId, new AtomicPositiveInteger());
			sequence = sequences.get(moduleId);
		}
		
		return clientList.get(sequence.getAndIncrement()%clientList.size());
	}


}
