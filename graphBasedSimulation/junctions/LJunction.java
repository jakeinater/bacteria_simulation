package junctions;
import core.DirEdge;

public class LJunction extends Junction {
	
	private DirEdge e1, e2;
	
	
	public LJunction(int ID, int e1, int e2) {
		super(ID);
		this.e1 = new DirEdge(ID, e1);
		this.e2 = new DirEdge(ID, e2);
	}
	
	
}