package xixi.monitor.filter;


public class ServerStatInfoFilter extends AbstractStatInfoFilter {
	
	private String name = "serverstat";
	
	public ServerStatInfoFilter(){
		
	}
	
	public ServerStatInfoFilter(String filterName){
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
