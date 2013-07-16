package xixi.monitor.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.monitor.dashboard.DashBoard;
import xixi.rpc.Invocation;
import xixi.rpc.Invoker;
import xixi.rpc.exception.TimeoutException;
import xixi.rpc.filter.Filter;
import xixi.rpc.future.Future;

public abstract class AbstractStatInfoFilter implements Filter{

	private static final Logger logger = LoggerFactory
			.getLogger(ServerStatInfoFilter.class);

	private DashBoard dashBoard;
	
	@Override
	public abstract String filterName();

	@Override
	public void doFilter(Invoker service,Invocation inv) {
		Long currentTime = System.currentTimeMillis();

		Future future = service.invoke(inv);

		if (dashBoard != null) {
			try {
				if(future.getValue()!=null){
					Long transTime = System.currentTimeMillis() - currentTime;

					logger.debug("transaction time for this {} is {}", this.toString(),
							transTime);
					
					dashBoard.addSucceedTransaction(getKey(service));
					dashBoard.addTranactionTime(getKey(service), transTime);
				}
			} catch (TimeoutException e) {
				dashBoard.addFaildeTransaction(getKey(service));
			}
		}
	}

	protected abstract String getKey(Invoker service);
	
	public DashBoard getDashBoard() {
		return dashBoard;
	}

	public void setDashBoard(DashBoard dashBoard) {
		this.dashBoard = dashBoard;
	}
}
