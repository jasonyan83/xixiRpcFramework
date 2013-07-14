package xixi.common.bean;

public abstract class AbstractLBProperty implements LBProperty{

	private int weight; 
	
	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}

}
