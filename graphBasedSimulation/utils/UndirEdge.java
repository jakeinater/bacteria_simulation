package utils;

public class UndirEdge {
	
	public int n1; //smaller ID
	public int n2; //larger ID
	
	public UndirEdge(int n1, int n2) {
		if (n1 > n2) {
			this.n1 = n2;
			this.n2 = n1;
		} else {
			this.n1 = n1;
			this.n2 = n2;
		}
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash*31 + n1;
		hash = hash*31 + n2;
		return hash;
	}
	
}
