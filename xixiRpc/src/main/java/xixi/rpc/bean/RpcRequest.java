package xixi.rpc.bean;


public class RpcRequest extends RpcMessage{

	public RpcResponse copytoResponse(){
	
		RpcResponse response = new RpcResponse();
		
		response.setBasicVer(this.getBasicVer())
		.setDstModule(this.getSrcModule())
		.setSrcModule(this.getDstModule())
		.setFirstTransaction(this.getFirstTransaction())
		.setSecondTransaction(this.getSecondTransaction())
		.setInterfaceName(this.getInterfaceName())
		.setMethodName(this.getMethodName());
		
		return response;
	}
}
