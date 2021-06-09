package junctions;
import core.DirEdge;
import java.util.ArrayDeque;
import utils.Triplet;
import org.w3c.dom.*;

public class Sink extends Junction {
	private DirEdge prev;
	
	public Sink(int ID, int prev) {
		super(ID);
		this.prev = new DirEdge(ID, prev);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
	}
	
	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, prev);
	}

}