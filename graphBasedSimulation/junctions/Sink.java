package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import utils.Triplet;

public class Sink extends Junction {
	private DirEdge prev;
	
	public Sink(int ID, int prev) {
		super(ID);
		this.prev = new DirEdge(ID, prev);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
	}

}