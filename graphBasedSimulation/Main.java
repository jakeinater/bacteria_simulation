import core.Graph;
import io.xml.*;

public class Main {
	
	private static void generateGraphs(String species) {

		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);

		Graph g0 = new Graph(path, true, false, false, species);
		g0.solve(); //from both ends
		WriteXML.write(g0, "LJunction/" + species + "_non-uni_start");

		Graph g1 = new Graph(path, true, true, false, species);
		g1.solve(); //from both ends
		WriteXML.write(g1, "LJunction/" + species + "_non-uni_both-ends");

		Graph g2 = new Graph(path, false, true, false, species);
		g2.solve(); //from end
		WriteXML.write(g2, "LJunction/" + species + "_non-uni_end");

		String maze2 = "uni-maze.txt";
		String path2 = "graphBasedSimulation/assets/maze_coords/" + maze2;
		System.out.println(path2);

		Graph g3 = new Graph(path2, true, false, false, species);
		g3.solve(); //from both ends
		WriteXML.write(g3, "LJunction/" + species + "_uni_start");

		Graph g4 = new Graph(path2, true, true, false, species);
		g4.solve(); //from both ends
		WriteXML.write(g4, "LJunction/" + species + "_uni_both-ends");

		Graph g5 = new Graph(path2, false, true, false, species);
		g5.solve(); //from end
		WriteXML.write(g5, "LJunction/" + species + "_uni_end");


	}
	
	private static void test() {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);

		Graph g0 = new Graph(path, true, false, false, "ecoli");
		g0.grade(true);
		
	}
	

	public static void main(String[] args) {
		test();
	}	
}
