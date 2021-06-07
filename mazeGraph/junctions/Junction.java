package junctions;

public abstract class Junction {
	
	private int junctionID;
	private int x;
	private int y;
	
	public Junction(int ID) {
		junctionID = ID;
	}
	
	public int getID() {
		return junctionID;
	}
	
}
