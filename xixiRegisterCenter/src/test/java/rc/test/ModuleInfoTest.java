/*package rc.test;

import xix.rc.bean.ModuleInstanceInfo;

public class ModuleInfoTest {

	public static void main(String[] sf) {
		List<ModuleInfo> list = new ArrayList<ModuleInfo>();

		list.add(buildModuleInfo());
		list.add(buildModuleInfo());

		KryoCoder coder = new KryoCoder();
		coder.registerClass(HeartBeat.class,13);
		
		HeartBeat hb = new HeartBeat(Short.valueOf("302"), "127.0.0.1:8070", 10000);
		
		Object[] hbA = new Object[]{hb};
		
		byte[] coderRet = coder.encoder(hbA);
		
		System.out.print("encoder result is " + ByteUtils.bytesAsHexString(coderRet, coderRet.length) );
		byte[] ret = new byte[] {1, 0, 91, 76, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 79, 98, 106, 101, 99, 116, -69, 1, 2, 15, 1, -96, -100, 1, 1, 49, 50, 55, 46, 48, 46, 48, 46, 49, 58, 56, 48, 55, -80, 1, 46};
		System.out.print("compare result is " + ByteUtils.bytesAsHexString(ret, ret.length) );
		// coder.encoder(list);

		Object[] o = (Object[])coder.decode(ret);

		System.out.print("result is " + o[0]);
		
		System.out.println("Aa".hashCode());
		System.out.println("BB".hashCode());

	}

	static ModuleInstanceInfo buildModuleInfo() {
		ModuleInstanceInfo m = new ModuleInstanceInfo();
		m.setModuleId(Short.valueOf("301"));

		m.setIpAddress("127.0.0.1:8070");
		m.setWeight(1000);
		return m;
	}

}
*/