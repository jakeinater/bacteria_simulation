package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import utils.Triplet;
import org.w3c.dom.*;

public class LJunction extends Junction {
	
	private DirEdge e1, e2;
	
	
	public LJunction(int ID, double x, double y, int e1, int e2) {
		super(ID, x, y);
		this.e1 = new DirEdge(ID, e1);
		this.e2 = new DirEdge(ID, e2);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents < MIN) return;
		
		if (prevID == e1.getDest()) {
			//coming from e1
			e2.incr(numAgents);
			q.add(new Triplet<>(this.getID(), numAgents, e2.getDest()));
		} else {
			//coming from e2
			e1.incr(numAgents);
			q.add(new Triplet<>(this.getID(), numAgents, e1.getDest()));
		}
	}

	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, e1);
		addEdgeXML(doc, graphElement, d2, e2);
	}

}