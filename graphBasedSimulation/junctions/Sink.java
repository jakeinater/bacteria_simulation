package junctions;
import core.DirEdge;

public class Sink extends Junction {
	private DirEdge prev;
	
	public Sink(int ID, int prev) {
		super(ID);
		this.prev = new DirEdge(ID, prev);
	}
}