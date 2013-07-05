package xixi.transport.channel;


public interface Channel {

	void onMessageReceived(Object message);
	
	void onChannelConntected();
	
	void onChannelDisconntected();
	
	void onExceptionCaught(Throwable cause);
	
	void send(Object message);
	
	boolean isConnected();
	
	boolean isClosed();
	
	void close();

	
}
