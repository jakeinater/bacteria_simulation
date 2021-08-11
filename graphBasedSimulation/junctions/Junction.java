package junctions;
import java.util.ArrayDeque;
import java.util.HashMap;
import utils.Triplet;
import utils.UndirEdge;
import core.DirEdge;
import org.w3c.dom.*;

public abstract class Junction {
	
	private int junctionID;
	private double x, y;
	static final double MIN = 1;
	private double weight = 0;
	
	public Junction(int ID, double x, double y) {
		junctionID = ID;
		this.x = x;
		this.y = y;
	}
	
	public int getID() {
		return junctionID;
	}

	//
	public void incrementNodeWeight(double inc) {
		weight += inc;
	}
	
	public double getNodeWeight() {
		return weight;
	}
	
	public abstract void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q);
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	//this method should take in a graph element and append an edge element with a source and target attribute,
	//and the edge should have a data child element with attr key="d2" and a child: doc.createTextNode(weight)
	public abstract void addEdgesXML(Document doc, Element graphElement, String d2);
	
	void addEdgeXML(Document doc, Element graphElement, String d2, DirEdge e) {
		Element edge = doc.createElement("edge");
		graphElement.appendChild(edge);
		edge.setAttribute("source", Integer.toString(e.getSrc()));
		edge.setAttribute("target", Integer.toString(e.getDest()));
		
		Element data = doc.createElement("data");
		edge.appendChild(data);
		data.setAttribute("key", d2);
		data.appendChild(doc.createTextNode(Double.toString(e.getWeight())));
	}
	
	
	
	//take a hashmap and add the junctions edge weights to the map with a string of nodeIDs as keys, smaller ID first
	public abstract void addUndirEdges(HashMap<String, Double> loss);
	
	public abstract void resetEdgeWeights();
	
	public void resetNodeWeight() {
		this.weight = 0;
	}
	
	
}
