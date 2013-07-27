package xixi.codec.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.codec.api.Coder;
import xixi.common.annotation.XixiBean;
import xixi.common.respository.ArgumentTypeRepository;
import xixi.rpc.bean.RpcRequest;
import xixi.rpc.bean.RpcResponse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoCoder implements Coder {

	private static final Logger logger = LoggerFactory
			.getLogger(KryoCoder.class);

	private final static Kryo kryo = new Kryo();

	private boolean classRegisterFlag = false;

	private void registerClass() {
		// kryo.setRegistrationRequired(true);
		Map<String, Class<?>> argClassType = ArgumentTypeRepository
				.getAllArgumentType();
		for (Class<?> clz : argClassType.values()) {
			XixiBean a = clz.getAnnotation(XixiBean.class);
			if (a != null) {
				if (a.id() > 0) {
					kryo.register(clz, a.id());
				} else {
					logger.error("Id can not be navigate when registering Kryo");
					throw new RuntimeException(
							"Id can not be navigate when registering Kryo");
				}
			} else {
				kryo.register(clz, Math.abs(clz.toString().hashCode()));
				logger.warn("Class {} does not have a user-defined Id, will use class.toString.hashcode insteand.", clz.toString());
			}

			logger.debug("Registering class {}");
		}
		
		kryo.register(RpcRequest.class, 300301);
		kryo.register(RpcResponse.class, 300302);
		classRegisterFlag = true;
	}

	public void registerClass(Class<?> clz) {
		kryo.register(clz);
	}

	public void registerClass(Class<?> clz, int id) {
		kryo.register(clz, id);
	}

	@Override
	public Object decode(byte[] inData) {
		if (classRegisterFlag) {
			Input input = new Input(new ByteArrayInputStream(inData));
			Object ret = kryo.readClassAndObject(input);
			return ret;
		} else {
			registerClass();
			return decode(inData);
		}

	}

	@Override
	public byte[] encoder(Object outData) {
		if (classRegisterFlag) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			Output output = new Output(outStream);
			kryo.writeClassAndObject(output, outData);
			output.flush();
			return outStream.toByteArray();
		} else {
			registerClass();
			return encoder(outData);
		}

	}

}
