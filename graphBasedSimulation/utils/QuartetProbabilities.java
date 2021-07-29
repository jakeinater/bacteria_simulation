package utils;

public class QuartetProbabilities <E1, E2, E3, E4> {
	public E1 pLeft;
	public E2 pForward;
	public E3 pRight;
	public E4 pBack;
	
	public QuartetProbabilities(E1 p1, E2 p2, E3 p3, E4 p4) {
		this.pLeft = p1;
		this.pForward = p2;
		this.pRight = p3;
		this.pBack = p4;
	}
	
}
