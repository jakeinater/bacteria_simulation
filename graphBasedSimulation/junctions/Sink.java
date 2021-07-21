package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import java.util.HashMap;
import utils.Triplet;
import utils.StringKey;
import org.w3c.dom.*;

public class Sink extends Junction {
	private DirEdge prev;
	
	public Sink(int ID, double x, double y, int prev) {
		super(ID, x, y);
		this.prev = new DirEdge(ID, prev);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents < MIN) return;
		incrementNodeWeight(numAgents);
	}
	
	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, prev);
	}
	
	@Override
	public void addUndirEdges(HashMap<String, Double> loss) {
		String key = StringKey.stringKey(this.getID(), this.prev.getDest());
		double weight = loss.getOrDefault(key, 0.) + prev.getWeight();
		loss.put(key, weight);
	}
	
	@Override
	public void resetEdgeWeights() {
		prev.resetWeight();
	}

}