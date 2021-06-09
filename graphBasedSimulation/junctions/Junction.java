package junctions;
import java.util.ArrayDeque;
import utils.Triplet;
import org.w3c.dom.*;

public abstract class Junction {
	
	private int junctionID;
	private double x, y;
	static final double MIN = 1;
	
	public Junction(int ID) {
		junctionID = ID;
	}
	
	public int getID() {
		return junctionID;
	}

	//
	public abstract void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q);
	
	//this method should take in a graph element and append an edge element with a source and target attribute,
	//and the edge should have a data child element with attr key="d2" and a child: doc.createTextNode(weight)
	public abstract void addEdgesXML(Document doc, Element graphElement, Attr d2);
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
