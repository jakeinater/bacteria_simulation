package junctions;
import java.util.ArrayDeque;
import utils.Triplet;

public abstract class Junction {
	
	private int junctionID;
	private int x, y;
	static final double MIN = 1;
	
	public Junction(int ID) {
		junctionID = ID;
	}
	
	public int getID() {
		return junctionID;
	}

	//
	public abstract void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q);
	
	//public abstract void getProbabilities();
	
}
