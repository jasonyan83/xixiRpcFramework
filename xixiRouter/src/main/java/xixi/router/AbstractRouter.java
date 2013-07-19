package xixi.router;

import xixi.transport.client.TcpClient;

public abstract class AbstractRouter implements Router{

	private short moduleId;
	
	public AbstractRouter(short moduleId){
		this.moduleId = moduleId;
	}
	
	public short moduleId(){
		return moduleId;
	}
	
	protected abstract TcpClient getTcpClient();

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}



}
