package utils;

public class QuartetProbabilities <E1, E2, E3, E4> {
	final public E1 pLeft;
	final public E2 pForward;
	final public E3 pRight;
	final public E4 pBack;
	
	public QuartetProbabilities(E1 p1, E2 p2, E3 p3, E4 p4) {
		this.pLeft = p1;
		this.pForward = p2;
		this.pRight = p3;
		this.pBack = p4;
	}
	
}
