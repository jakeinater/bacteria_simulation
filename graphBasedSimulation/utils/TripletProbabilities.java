package utils;

public class TripletProbabilities<E1, E2, E3> {
	public E1 pLeft;
	public E2 pRight;
	public E3 pMiddle;
	
	public TripletProbabilities(E1 e1, E2 e2, E3 e3) {
		this.pLeft = e1;
		this.pRight = e2;
		this.pMiddle = e3;
	}
}
