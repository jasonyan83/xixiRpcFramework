/*package xixi.rpc.filter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import xixi.rpc.Invocation;
import xixi.rpc.RpcInvoker;

public class ThreadPoolFilter implements Filter {

	//TODO: difine more specific default threadpool here
	private  ExecutorService workService = Executors.newFixedThreadPool(1, new ThreadFactory(){

		@Override
		public Thread newThread(Runnable r) {
			// TODO use an uni id to identify the thread
			return new Thread(r, "ThreadPoolFilter");
		}
		
	});
	
	@Override
	public void doFilter(final RpcInvoker service, final Invocation inv) {
		// TODO Auto-generated method stub
		workService.submit(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				service.invoke(inv);
			}
		});
	}

	@Override
	public String filterName() {
		// TODO Auto-generated method stub
		return "threadpool";
	}

	public ExecutorService getWorkService() {
		return workService;
	}

	public void setWorkService(ExecutorService workService) {
		this.workService = workService;
	}

}
*/