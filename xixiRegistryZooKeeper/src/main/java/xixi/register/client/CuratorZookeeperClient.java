package xixi.register.client;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;

public class CuratorZookeeperClient extends AbstractZookeeperClient<CuratorWatcher> {

	private final CuratorFramework client;

	public CuratorZookeeperClient(String connectString) {
		
		
		client = CuratorFrameworkFactory.builder()
        .connectString(connectString)
        .retryPolicy( new RetryNTimes(Integer.MAX_VALUE, 1000))
        .connectionTimeoutMs(5000)
        //.sessionTimeoutMs(sessionTimeoutMs)
        // etc. etc.
        .build();
		client.start();
		
	/*		Builder builder = CuratorFrameworkFactory.builder()
					.connectString(connectString)
			        .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))  
			        .connectionTimeoutMs(5000);*/
			//String authority = url.getAuthority();
			/*if (authority != null && authority.length() > 0) {
				builder = builder.authorization("digest", authority.getBytes());
			}*/
		/*	client = builder.build();
			client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
				public void stateChanged(CuratorFramework client, ConnectionState state) {
					if (state == ConnectionState.LOST) {
						CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
					} else if (state == ConnectionState.CONNECTED) {
						CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
					} else if (state == ConnectionState.RECONNECTED) {
						CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
					}
				}
			});
			client.start();*/
		
	}
	
	public void createPersistent(String path, byte[] data) {
		try {
			client.create().creatingParentsIfNeeded().forPath(path,data);
		} catch (NodeExistsException e) {
			logger.error("Node Already exist for path {}", path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void createEphemeral(String path,byte[] data) {
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,data);
		} catch (NodeExistsException e) {
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void delete(String path) {
		try {
			client.delete().forPath(path);
		} catch (NoNodeException e) {
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public List<String> getChildren(String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (NoNodeException e) {
			logger.error("Exception : {}", e);
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}

	public void doClose() {
		client.close();
	}
	
	private class CuratorWatcherImpl implements CuratorWatcher {
		
		private volatile ChildListener listener;
		
		public CuratorWatcherImpl(ChildListener listener) {
			this.listener = listener;
		}
		
		public void unwatch() {
			this.listener = null;
		}
		
		public void process(WatchedEvent event) throws Exception {
			if (listener != null) {
				switch(event.getType()){
				case NodeDataChanged:
					logger.debug("Path {} , Node data changed!", event.getPath());
					listener.nodeDataChanged(event.getPath(), client.getData().usingWatcher(this).forPath(event.getPath()));
					break;
				case NodeChildrenChanged:
					logger.debug("Path {} , Children changed!", event.getPath());
					listener.childChanged(event.getPath(), client.getChildren().usingWatcher(this).forPath(event.getPath()));
					break;
				case NodeDeleted:
					logger.debug("Path {} , Node deleted", event.getPath());
					break;
					
				case NodeCreated:
					logger.debug("Path {} , Node created", event.getPath());
					break;
				default :
					listener.childChanged(event.getPath(), client.getChildren().usingWatcher(this).forPath(event.getPath()));
				}
			}
		}
	}
	
	public CuratorWatcher createTargetChildListener(String path, ChildListener listener) {
		return new CuratorWatcherImpl(listener);
	}
	
	public List<String> addTargetChildListener(String path, CuratorWatcher listener) {
		try {
			return client.getChildren().usingWatcher(listener).forPath(path);
		} catch (NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	public void removeTargetChildListener(String path, CuratorWatcher listener) {
		((CuratorWatcherImpl) listener).unwatch();
	}

	public byte[] addNodeDataListener(String path, CuratorWatcher listener) {
		try {
			return client.getData().usingWatcher(listener).forPath(path);
		} catch (NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	@Override
	public byte[] getData(String path) {
		try {
			return client.getData().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setData(String path, byte[] data) {
		try {
			client.setData().inBackground().forPath(path, data);
		} catch (Exception e) {
			logger.error("setData failed fo node {}. Exception is {}", path,e);
			e.printStackTrace();
		}
	}

}
