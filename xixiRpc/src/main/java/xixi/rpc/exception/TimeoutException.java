package xixi.rpc.exception;

//TODO: needs to add another abstract excetion between Timeout and exception
public class TimeoutException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	
	public  TimeoutException(String message){
		super(message);
	}

}
