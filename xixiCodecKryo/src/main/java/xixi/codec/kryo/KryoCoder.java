package xixi.codec.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import xixi.codec.api.Coder;
import xixi.common.respository.ArgumentTypeRepository;
import xixi.rpc.bean.RpcRequest;
import xixi.rpc.bean.RpcResponse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoCoder implements Coder {

	private final static Kryo kryo = new Kryo();

	private boolean classRegisterFlag=false;
	
	private  void registerClass(){
		//kryo.setRegistrationRequired(true);
		Map<String,Class<?>> argClassType =  ArgumentTypeRepository.getAllArgumentType();
		for(Class<?> clz : argClassType.values()){
			kryo.register(clz);
		}
		kryo.register(RpcRequest.class);
		kryo.register(RpcResponse.class);
		classRegisterFlag=true;
	}
	
	public void registerClass(Class<?> clz){
		kryo.register(clz);
	}
	
	@Override
	public Object decode(byte[] inData) {
		if(classRegisterFlag){
			Input input = new Input(new ByteArrayInputStream(inData));
			Object ret = kryo.readClassAndObject(input);
			return ret;
		}
		else{
			registerClass();
			return decode(inData);
		}

		
	}

	@Override
	public byte[] encoder(Object outData) {
		if(classRegisterFlag){
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			Output output = new Output(outStream);
			kryo.writeClassAndObject(output, outData);
			output.flush();
			return outStream.toByteArray();
		}
		else{
			registerClass();
			return encoder(outData);
		}

	}

}
