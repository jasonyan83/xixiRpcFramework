/*package xixi.rpc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.rpc.Invocation;
import xixi.rpc.Invoker;

public class StatisticInfoFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(StatisticInfoFilter.class);

	private DashBoard dashBoard;
	
	private String name = "stat";
	
	public StatisticInfoFilter(String filterName){
		name = filterName;
	}

	@Override
	public String filterName(){
		return name;
	}

	@Override
	public void doFilter(Invoker service,Invocation inv) {
		Long currentTime = System.currentTimeMillis();

		service.invoke(inv);
		
		Long transTime = System.currentTimeMillis() - currentTime;

		logger.debug("transaction time for this {} is {}", this.toString(),
				transTime);

		if (dashBoard != null) {
			if(((ResultFuture)inv.future()).isSuccess()){
				dashBoard.addCompletedTaskCount(service.toString());
			}
			dashBoard.addTransTime(service.toString(), transTime);
		}
	}

	public DashBoard getDashBoard() {
		return dashBoard;
	}

	public void setDashBoard(DashBoard dashBoard) {
		this.dashBoard = dashBoard;
	}

}
*/