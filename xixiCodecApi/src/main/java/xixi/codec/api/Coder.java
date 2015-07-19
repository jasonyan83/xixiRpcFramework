package xixi.codec.api;

public interface Coder {

	public Object decode(byte[] inData);
	
	public byte[] encoder(Object outData);
}
