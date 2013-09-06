package xixi.register.client;

import java.util.List;

public interface ChildListener {

	void childChanged(String path, List<String> children);
	void nodeDataChanged(String path,byte[] data);

}
