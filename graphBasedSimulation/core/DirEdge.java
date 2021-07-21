package core;

public class DirEdge {
	
	private int src_ID;
	private int dest_ID;
	private double weight = 0;
	
	public DirEdge(int src, int dest) {
		this.src_ID = src;
		this.dest_ID = dest;
	}
	
	public int getSrc() {
		return src_ID;
	}
	
	public int getDest() {
		return dest_ID;
	}
	
	public void incr() {
		//increment the count, agent walks over edge
		weight++;
	}
	
	public void incr(double inc) {
		//increment the count, agent walks over edge
		weight += inc;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void resetWeight() {
		weight = 0;
	}
}
