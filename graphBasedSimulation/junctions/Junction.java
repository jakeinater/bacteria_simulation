package junctions;

public abstract class Junction {
	
	private int junctionID;
	private int x, y;
	
	public Junction(int ID) {
		junctionID = ID;
	}
	
	public int getID() {
		return junctionID;
	}
	
	//public abstract void getProbabilities();
	
}
