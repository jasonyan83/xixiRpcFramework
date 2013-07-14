package xixi.rc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rc.register.RegisterListener;
import xixi.rc.register.Registry;
import xixi.rpc.Callback;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.exception.TimeoutException;
import xixi.rpc.filter.Filter;
import xixi.rpc.future.Future;
import xixi.transport.channel.Channel;

public class RCRegisterFilter implements Filter{

	private static final Logger logger = LoggerFactory
			.getLogger(RCRegisterFilter.class);
	
	private String filterName ="rcRegisterFilter";
	
	private Registry registry;
	
	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	public void doFilter(Invoker service, final Invocation inv) {
		logger.debug("entering RCRegisterFilter");
		
		Future future = service.invoke(inv);

		try {
			future.addCallback(new Callback(){
				@Override
				public void invoke(Object resp) {
					boolean result = (boolean) resp;
					if(result){
						Channel channel = (Channel)inv.getProperty("channel");
						Object[] args = inv.getArgs();
						if(args.length!=3){
							logger.error("This is not a valid module register request");
						}
						short moduleId = (short)args[0];
						String ipAddress = (String)args[1];
						if(moduleId!=0&&ipAddress!=null){
							registry.buildInstanceChannelMap(moduleId,ipAddress, channel);
						}
					}
					
				}
				
			});
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String filterName() {
		return filterName;
	}

}
