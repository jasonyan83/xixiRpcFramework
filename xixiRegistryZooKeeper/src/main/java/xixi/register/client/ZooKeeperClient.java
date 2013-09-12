package xixi.register.client;

import java.util.List;

public interface ZooKeeperClient {

	void create(String path, boolean ephemeral);

	public void create(String path, boolean ephemeral, byte[] data);
	
	void delete(String path);

	List<String> getChildren(String path);

	List<String> addChildListener(String path, ChildListener listener);

	void removeChildListener(String path, ChildListener listener);

	void addStateListener(StateListener listener);
	
	void removeStateListener(StateListener listener);

	boolean isConnected();

	void close();
	
	byte[] getData(String path);

	byte[] getData(String path, ChildListener listener);
	
	void setData(String path,byte[] data);
}
