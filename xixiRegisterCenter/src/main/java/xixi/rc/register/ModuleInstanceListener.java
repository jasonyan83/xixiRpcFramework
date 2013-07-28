package xixi.rc.register;


public interface ModuleInstanceListener {

	public void onRegistered(short moduleId, String ipAddress);
	
	public void onUnRegistered(short moduleId, String ipAddress);

}
