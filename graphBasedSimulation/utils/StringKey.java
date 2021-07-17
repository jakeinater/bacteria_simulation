package utils;

public class StringKey {

	public static String stringKey(int n1, int n2) {
		if (n1 < n2) {
			return "" + n1 + " " + n2;
		} else {
			return "" + n2 + " " + n1;
		}
	}

}
