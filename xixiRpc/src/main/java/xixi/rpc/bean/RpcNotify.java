package xixi.rpc.bean;

public class RpcNotify extends RpcMessage{
	
	public RpcNotify(){
		super();
		this.setType((byte)3);
	}
}
