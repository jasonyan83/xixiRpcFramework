package xixi.monitor.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.Invoker;

public class ClientStatInfoFilter extends AbstractStatInfoFilter {

	private static final Logger logger = LoggerFactory
			.getLogger(ClientStatInfoFilter.class);

	private String name = "clientstat";
	
	public ClientStatInfoFilter(String filterName){
		name = filterName;
	}

	@Override
	public String filterName(){
		return name;
	}

	@Override
	protected String prefix() {
		return "client";
	}
}