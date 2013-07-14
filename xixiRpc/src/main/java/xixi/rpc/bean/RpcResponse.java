package xixi.rpc.bean;

public class RpcResponse extends RpcMessage{
	
	public RpcResponse(){
		super();
		this.setType((byte)1);
	}
	
	public static final int TIMEOUT = 1;
	public static final int SUCCEED = 0;
	public static final int ERROR = 2;
}
