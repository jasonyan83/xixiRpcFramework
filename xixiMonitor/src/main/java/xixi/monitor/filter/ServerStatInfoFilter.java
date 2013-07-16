package xixi.monitor.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.Invoker;

public class ServerStatInfoFilter extends AbstractStatInfoFilter {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerStatInfoFilter.class);
	
	private String name = "serverstat";
	
	public ServerStatInfoFilter(String filterName){
		name = filterName;
	}

	@Override
	public String filterName(){
		return name;
	}

	protected String getKey(Invoker service){
		return "server-" + service.toString();
	}
	
}
