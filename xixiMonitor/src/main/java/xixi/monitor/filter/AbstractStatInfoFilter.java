package xixi.monitor.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.monitor.statistics.StatisticsService;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.filter.Filter;
import xixi.rpc.future.Future;

public abstract class AbstractStatInfoFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerStatInfoFilter.class);

	private StatisticsService statService;

	public StatisticsService getStatService() {
		return statService;
	}

	public void setStatService(StatisticsService statService) {
		this.statService = statService;
	}

	@Override
	public abstract String filterName();

	protected abstract String prefix();

	@Override
	public void doFilter(Invoker service, Invocation inv) {
		Long currentTime = System.currentTimeMillis();

		Future future = service.invoke(inv);

		if (statService != null) {

			Long transTime = System.currentTimeMillis() - currentTime;

			logger.debug("transaction time for this {} is {}", this.toString(),
					transTime);

			// statService.addSucceedTransaction(service.toString());
			statService.addTranactionTime(service.toString(), transTime);

		}
	}

}
