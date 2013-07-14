package xixi.transport.client;

import xixi.common.annotation.DefaultImplement;

@DefaultImplement(value="nettyclient")
public interface TcpClient {
	public void start();
    public void stop(); 
    public void send(Object msg);
    public void setDestIp(String ip);
    public void setDestPort(int port);
    public String getDestIpAddress();
}
