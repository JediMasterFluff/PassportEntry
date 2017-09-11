package Objects;

import org.apache.commons.lang3.tuple.Triple;

public class Restaurant<L, M, R> extends Triple<String, Integer, Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int count;
	private double percent;
	
	public Restaurant(String name) {
		super();
		this.name = name;
		this.count = 0;
		this.percent = 0.0;
	}

	public Restaurant() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLeft() {
		return name;
	}

	@Override
	public Integer getMiddle() {
		return count;
	}

	@Override
	public Double getRight() {
		return percent;
	}
	
	public void incrementCount(){
		this.count++;
	}
	
	public void recalculatePercent(int total){
		if(total != 0)
			this.percent = (double) Math.round(this.count * 100.0)  / total;
		else
			this.percent = 100.00;
	}
	
}
