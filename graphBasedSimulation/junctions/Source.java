package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import utils.Triplet;

public class Source extends Junction {
	private DirEdge next;
	
	public Source(int ID, int next) {
		super(ID);
		this.next = new DirEdge(ID, next);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents<MIN) return;
		next.incr(numAgents);
		q.add(new Triplet<>(this.getID(), numAgents, next.getDest()));
	}
}