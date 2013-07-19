package xixi.codec.api;

public interface Coder {

	Object decode(byte[] inData);
	
	byte[] encoder(Object outData);
}
