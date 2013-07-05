package xixi.router;

import xixi.transport.client.TcpClient;

public abstract class AbstractRouter implements Router{

	private int moduleId;
	
	public AbstractRouter(int moduleId){
		this.moduleId = moduleId;
	}
	
	public int moduleId(){
		return moduleId;
	}
	
	protected abstract TcpClient getTcpClient();

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}



}
