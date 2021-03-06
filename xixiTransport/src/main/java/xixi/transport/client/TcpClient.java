package xixi.transport.client;

import xixi.common.annotation.DefaultImplement;
import xixi.common.bean.LBProperty;
import xixi.transport.listener.ConnectorListener;

@DefaultImplement(value="nettyclient")
public interface TcpClient extends LBProperty {
	public void start();
    public void stop(); 
    public void send(Object msg);
    public void setDestIp(String ip);
    public void setDestPort(int port);
    public String getDestIpAddress();
    public void setModuleId(short moduleId);
    public short getModuleId();
    public void setMaxRetryTimes(int maxRetryTimes);
    public void addConnectorListener(ConnectorListener transportListener);
}
