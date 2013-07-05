/*package jason.xixi.transport.router.strategy;

import jason.xixi.transport.router.RouterFactory;
import jason.xixi.transport.router.RouterRepository;

public class StrategyFactory {
	
	public static String DEFAULT_STRATEGY = "order";
	

	public static RouterStrategy createStrategy(String strategy,RouterFactory routerFactory,
			RouterRepository repository){
		if(strategy.equals(DEFAULT_STRATEGY)){
			return new DefaultRouterStrategy(routerFactory,repository);
		}
		return new DefaultRouterStrategy(routerFactory,repository);
		
	}
}
*/