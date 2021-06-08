package junctions;
import core.DirEdge;

public class Source extends Junction {
	private DirEdge next;
	
	public Source(int ID, int next) {
		super(ID);
		this.next = new DirEdge(ID, next);
	}
}