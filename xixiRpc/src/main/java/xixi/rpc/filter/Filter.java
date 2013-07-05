/**
 * 
 */
package xixi.rpc.filter;

import xixi.rpc.Invocation;
import xixi.rpc.Invoker;

/**
 * @author Jason.Yan
 *
 */
public interface Filter {

	void doFilter(Invoker service,Invocation inv);
	String filterName();
}
