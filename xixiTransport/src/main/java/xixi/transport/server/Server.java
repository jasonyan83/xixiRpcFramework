package xixi.transport.server;

import xixi.common.annotation.DefaultImplement;


@DefaultImplement(value="nettyserver")
public interface Server {

	void start();
	
	void stop();
}
