import core.Graph;
import io.xml.*;
import junctions.LJunction;
import junctions.TJunction;
import junctions.YJunction;

public class Main {
	
	private static void generateGraphs(String species) {

		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);

		Graph g0 = new Graph(path, true, false, false, species);
		g0.solve(); //from both ends
		WriteXML.write(g0, "Original/" + species + "_non-uni_start");

		Graph g1 = new Graph(path, true, true, false, species);
		g1.solve(); //from both ends
		WriteXML.write(g1, "Original/" + species + "_non-uni_both-ends");

		Graph g2 = new Graph(path, false, true, false, species);
		g2.solve(); //from end
		WriteXML.write(g2, "Original/" + species + "_non-uni_end");

		/*
		String maze2 = "uni-maze.txt";
		String path2 = "graphBasedSimulation/assets/maze_coords/" + maze2;
		System.out.println(path2);

		Graph g3 = new Graph(path2, true, false, false, species);
		g3.solve(); //from both ends
		WriteXML.write(g3, "Original/" + species + "_uni_start");

		Graph g4 = new Graph(path2, true, true, false, species);
		g4.solve(); //from both ends
		WriteXML.write(g4, "Original/" + species + "_uni_both-ends");

		Graph g5 = new Graph(path2, false, true, false, species);
		g5.solve(); //from end
		WriteXML.write(g5, "Original/" + species + "_uni_end");
*/

	}
	
	private static void testGrading() {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);

		Graph g0 = new Graph(path, true, true, false, "ecoli");
		g0.grade(true, true);
		
		g0.solve();
		
		g0.grade(true, true);
	}
	
	private static void testParamSweep() {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);
		
		new YJunction(true, "ecoli", 100, 0., 0., 100, 100, 100);

		Graph g0 = new Graph(path, true, true, false, "ecoli");
		
		long startTime = System.currentTimeMillis();
		
		double[] newProbs = g0.paramSweep();
	
		long endTime = System.currentTimeMillis();
		long dur = (endTime-startTime);
		System.out.println("It took " + dur + "ms");
		
		printProbs(newProbs);
		
		TJunction.setPFromLeft( newProbs[0], newProbs[1], newProbs[2] );
		TJunction.setPFromRight( newProbs[3], newProbs[4], newProbs[5] );	
		TJunction.setPFromMiddle( newProbs[6], newProbs[7], newProbs[8] );
		YJunction.setPFromLeft( newProbs[9], newProbs[10], newProbs[11] ); 	
		YJunction.setPFromRight( newProbs[12], newProbs[13], newProbs[14] );
		YJunction.setPFromMiddle( newProbs[15], newProbs[16], newProbs[17] );	
		LJunction.setProbabilities( newProbs[18], newProbs[19], newProbs[20] ); 	
	
		g0.solve();
		WriteXML.write(g0, "optimizedEcoliGraph", "graphBasedSimulation/assets/graphs/testing/");
		g0.grade(true, true);
		
		System.out.println("done!");
	}
	
	public static void printProbs(double[] newProbs) {
		String probs = " TL: " + newProbs[0] + " " + newProbs[1] + " " + newProbs[2];
		probs += "\n TR: " + newProbs[3] + " " + newProbs[4] + " " + newProbs[5];
		probs += "\n TM: " + newProbs[6] + " " + newProbs[7] + " " + newProbs[8];
		probs += "\n";
		probs += "\n YL: " + newProbs[9] + " " + newProbs[10] + " " + newProbs[11];
		probs += "\n YR: " + newProbs[12] + " " + newProbs[13] + " " + newProbs[14];
		probs += "\n YM: " + newProbs[15] + " " + newProbs[16] + " " + newProbs[17];
		probs += "\n";
		probs += "\n L: " + newProbs[18] + " " + newProbs[19] + " " + newProbs[20];
		probs += "\n";
		probs += "\n X: " + newProbs[21] + " " + newProbs[22] + " " + newProbs[23] + " " + newProbs[24];

		System.out.println(probs);
	}
	
	public static void testMultithread() {
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
	
	public static void testLoopSweep() {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);
		
		new YJunction(true, "ecoli", 100, 0., 0., 100, 100, 100);

		Graph g0 = new Graph(path, true, true, false, "ecoli");
		
		double[] newProbs = g0.loopSweep(10);
		
		TJunction.setPFromLeft( newProbs[0], newProbs[1], newProbs[2] );
		TJunction.setPFromRight( newProbs[3], newProbs[4], newProbs[5] );	
		TJunction.setPFromMiddle( newProbs[6], newProbs[7], newProbs[8] );
		YJunction.setPFromLeft( newProbs[9], newProbs[10], newProbs[11] ); 	
		YJunction.setPFromRight( newProbs[12], newProbs[13], newProbs[14] );
		YJunction.setPFromMiddle( newProbs[15], newProbs[16], newProbs[17] );	
		LJunction.setProbabilities( newProbs[18], newProbs[19], newProbs[20] ); 	
	
		g0.solve();
		WriteXML.write(g0, "LOOP_optimizedEcoliGraph", "graphBasedSimulation/assets/graphs/testing/");
		g0.grade(true, true);

	}

	public static void main(String[] args) {
		//testParamSweep();
		//testLoopSweep();
		generateGraphs("ecoli");
	}	
}
