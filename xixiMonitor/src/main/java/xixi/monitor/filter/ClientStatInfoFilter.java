package xixi.monitor.filter;

public class ClientStatInfoFilter extends AbstractStatInfoFilter {
	
	private String name = "clientstat";
	
	public ClientStatInfoFilter(){
	}
	
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