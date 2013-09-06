package xixi.common.bean;

public class ModuleInfo {

	private short moduleId;

	private String version;

	public short getModuleId() {
		return moduleId;
	}

	public void setModuleId(short moduleId) {
		this.moduleId = moduleId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + moduleId;
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleInfo other = (ModuleInfo) obj;
		if (moduleId != other.moduleId)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	
}
