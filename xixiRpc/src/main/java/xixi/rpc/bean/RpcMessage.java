package xixi.rpc.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RpcMessage {

	private Map<String, Object> properties = new HashMap<String, Object>();
	
	private Object[] data;

	private byte basicVer = 1;

	private int length = 0;

	private short srcModule;

	private short dstModule;

    private long firstTransaction;
	
	private long secondTransaction;

	private byte type;

	private int messageLength;

	private String interfaceName;
	
	private String methodName;
	
	private int status;
	
	public RpcMessage(){
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public int getStatus() {
		return status;
	}

	public RpcMessage setStatus(int status) {
		this.status = status;
		return this;
	}

	public UUID getTransactionId(){
		return new UUID(firstTransaction,secondTransaction);
	}
	
	public RpcMessage setTransactionId(UUID transactioinId){
		this.firstTransaction = transactioinId.getMostSignificantBits();
		this.secondTransaction = transactioinId.getLeastSignificantBits();
		return this;
	}
	
	
    public byte getBasicVer() {
		return basicVer;
	}

	public RpcMessage setBasicVer(byte basicVer) {
		this.basicVer = basicVer;
		return this;
	}

	public int getLength() {
		return length;
	}

	public RpcMessage setLength(int length) {
		this.length = length;
		return this;
	}

	public short getSrcModule() {
		return srcModule;
	}

	public RpcMessage setSrcModule(short srcModule) {
		this.srcModule = srcModule;
		return this;
	}

	public short getDstModule() {
		return dstModule;
	}

	public RpcMessage setDstModule(short dstModule) {
		this.dstModule = dstModule;
		return this;
	}

	public long getFirstTransaction() {
		return firstTransaction;
	}

	public RpcMessage setFirstTransaction(long firstTransaction) {
		this.firstTransaction = firstTransaction;
		return this;
	}

	public long getSecondTransaction() {
		return secondTransaction;
	}

	public RpcMessage setSecondTransaction(long secondTransaction) {
		this.secondTransaction = secondTransaction;
		return this;
	}

	public byte getType() {
		return type;
	}

	public RpcMessage setType(byte type) {
		this.type = type;
		return this;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public RpcMessage setMessageLength(int messageLength) {
		this.messageLength = messageLength;
		return this;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public RpcMessage setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public String getMethodName() {
		return methodName;
	}

	public RpcMessage setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public Object[] getData() {
		return data;
	}

	public RpcMessage setData(Object[] data) {
		this.data = data;
		return this;
	}

	public RpcMessage setProperty(String key, Object value) {
        properties.put(key, value);
    	return this;
    }
    
    public Object getProperty(String key) {
        return  properties.get(key);
    }
    
    
}
