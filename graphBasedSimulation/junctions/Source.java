package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import java.util.HashMap;

import utils.Triplet;
import utils.StringKey;
import org.w3c.dom.*;

public class Source extends Junction {
	private DirEdge next;
	
	public Source(int ID, double x, double y, int next) {
		super(ID, x, y);
		this.next = new DirEdge(ID, next);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents<MIN) return;
		incrementNodeWeight(numAgents);
		next.incr(numAgents);
		q.add(new Triplet<>(this.getID(), numAgents, next.getDest()));
	}
	
	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, next);
	}
	
	@Override
	public void addUndirEdges(HashMap<String, Double> loss) {
		String key = StringKey.stringKey(this.getID(), this.next.getDest());
		double weight = loss.getOrDefault(key, 0.) + next.getWeight();
		loss.put(key, weight);
	}

}