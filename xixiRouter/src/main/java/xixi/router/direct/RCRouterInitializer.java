package xixi.router.direct;

import xixi.common.constants.Constants;
import xixi.common.util.ConfigUtils;

public class RCRouterInitializer extends DirectRouterInitializer{

	public RCRouterInitializer(){
		super.routerAddresses = ConfigUtils.getProperty(
				Constants.RC_ADDRESSES_KEY, "");
	}
}
