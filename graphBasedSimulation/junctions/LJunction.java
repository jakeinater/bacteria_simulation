package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import utils.Triplet;

public class LJunction extends Junction {
	
	private DirEdge e1, e2;
	
	
	public LJunction(int ID, int e1, int e2) {
		super(ID);
		this.e1 = new DirEdge(ID, e1);
		this.e2 = new DirEdge(ID, e2);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents < MIN) return;
		
		if (prevID == e1.getDest()) {
			//coming from e1
			q.add(new Triplet<>(this.getID(), numAgents, e2.getDest()));
		} else {
			//coming from e2
			q.add(new Triplet<>(this.getID(), numAgents, e1.getDest()));
		}
	}

	
}