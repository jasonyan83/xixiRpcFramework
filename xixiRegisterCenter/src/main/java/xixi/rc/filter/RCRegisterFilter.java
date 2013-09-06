package xixi.rc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xix.rc.bean.ModuleInstanceInfo;
import xixi.rc.register.Registry;
import xixi.rpc.Callback;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.exception.TimeoutException;
import xixi.rpc.filter.Filter;
import xixi.rpc.future.Future;
import xixi.transport.channel.Channel;

public class RCRegisterFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(RCRegisterFilter.class);

	private String filterName = "rcRegisterFilter";

	private Registry registry;

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	public void doFilter(Invoker service, final Invocation inv) {
		logger.debug("entering RCRegisterFilter with Invocation {}" , inv);
		Future future = service.invoke(inv);

		try {
			future.addCallback(new Callback() {
				@Override
				public void invoke(Object resp) {
					int result = (int) resp;
					if (result==0) {
						Channel channel = (Channel) inv.getProperty("channel");
						Object[] args = inv.getArgs();
						if (args.length != 1) {
							logger.error("This is not a valid module register request");
						}
						ModuleInstanceInfo moduleInfo = (ModuleInstanceInfo)args[0];
						
						short moduleId = moduleInfo.getModuleId();
						String ipAddress = moduleInfo.getIpAddress();
						if (moduleId != 0 && ipAddress != null) {
							logger.debug(
									"Building relatetionship for module {}, ipAddress {} and channel {}",
									new Object[]{moduleId, ipAddress, channel});
							registry.buildInstanceChannelMap(moduleId,
									ipAddress, channel);
						}
					}
				}

			});
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String filterName() {
		return filterName;
	}

}
