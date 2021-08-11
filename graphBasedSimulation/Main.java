import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import core.Graph;
import io.xml.*;
import junctions.LJunction;
import junctions.TJunction;
import junctions.XJunction;
import junctions.YJunction;
import utils.QuartetProbabilities;

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
	
	private static void testGrading(String species) {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);

		Graph g0 = new Graph(path, true, true, false, species);
		g0.grade(true, true, species);
		
		g0.solve();
		
		g0.grade(true, true, species);
	}
	
	private static void testParamSweep(String species) {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);
		
		new YJunction(true, species, 100, 0., 0., 100, 100, 100);

		Graph g0 = new Graph(path, true, true, false, species);
		
		long startTime = System.currentTimeMillis();
		
		double[] newProbs = g0.paramSweep(species);
	
		long endTime = System.currentTimeMillis();
		long dur = (endTime-startTime);
		System.out.println("It took " + dur + "ms");
		
		printProbs(newProbs);
		storeProbs(species, newProbs);
		
		TJunction.setPFromLeft( newProbs[0], newProbs[1], newProbs[2] );
		TJunction.setPFromRight( newProbs[3], newProbs[4], newProbs[5] );	
		TJunction.setPFromMiddle( newProbs[6], newProbs[7], newProbs[8] );
		YJunction.setPFromLeft( newProbs[9], newProbs[10], newProbs[11] ); 	
		YJunction.setPFromRight( newProbs[12], newProbs[13], newProbs[14] );
		YJunction.setPFromMiddle( newProbs[15], newProbs[16], newProbs[17] );	
		LJunction.setProbabilities( newProbs[18], newProbs[19], newProbs[20] ); 	
		XJunction.setProbabilities( newProbs[21], newProbs[22], newProbs[23], newProbs[24] ); 	
	
		g0.solve();
		WriteXML.write(g0, "optimized_" + species + "Graph", "graphBasedSimulation/assets/graphs/testing/");
		g0.grade(true, true, species);
		
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
	
	public static void testLoopSweep(String species) {
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		System.out.println(path);
		
		new YJunction(true, "ecoli", 100, 0., 0., 100, 100, 100);

		Graph g0 = new Graph(path, true, true, false, species);
		
		double[] newProbs = g0.loopSweep(10, species);
		
		TJunction.setPFromLeft( newProbs[0], newProbs[1], newProbs[2] );
		TJunction.setPFromRight( newProbs[3], newProbs[4], newProbs[5] );	
		TJunction.setPFromMiddle( newProbs[6], newProbs[7], newProbs[8] );
		YJunction.setPFromLeft( newProbs[9], newProbs[10], newProbs[11] ); 	
		YJunction.setPFromRight( newProbs[12], newProbs[13], newProbs[14] );
		YJunction.setPFromMiddle( newProbs[15], newProbs[16], newProbs[17] );	
		LJunction.setProbabilities( newProbs[18], newProbs[19], newProbs[20] ); 	
		XJunction.setProbabilities( newProbs[21], newProbs[22], newProbs[23], newProbs[24] ); 	
	
		g0.solve();
		WriteXML.write(g0, "LOOP_optimized_" + species, "graphBasedSimulation/assets/graphs/testing/");
		g0.grade(true, true, species);
		storeProbs(species, newProbs);
		g0.generateExpGraph(species, true);
	}
	
	public static void storeProbs(String species, double[] newProbs) {
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
		
		String path = "graphBasedSimulation/assets/new_probabilities/" + species + "/";
		try (PrintStream out = new PrintStream(new FileOutputStream(path + "probs.txt"))) {
			out.print(probs);
			out.close();
			} catch (FileNotFoundException e) {
				System.out.println("error in storing");
				}
		}
	

	
	public static void test4PointGen() {
		double[] XProbs = new double[4*(5*2 + 1)];
		String maze = "non-uni-maze.txt";
		String path = "graphBasedSimulation/assets/maze_coords/" + maze;

		Graph g0 = new Graph(path, true, true, false, "ecoli");

		QuartetProbabilities<Double, Double, Double, Double> pX0 = XJunction.getCopyP();
		XProbs[0] = pX0.pLeft;
		XProbs[1] = pX0.pForward;
		XProbs[2] = pX0.pRight;
		XProbs[3] = pX0.pBack;
		
		for (int i = 1; i < 5*2 + 1; i++) {
			double x0, x1, x2, x3, sum, mag;
			while (true) {
				x0 = Math.random()*2.-1.;
				x1 = Math.random()*2.-1.;
				x2 = Math.random()*2.-1.;
				x3 = Math.random()*2.-1.;
			
				if (x0*x0 + x1*x1 + x2*x2 + x3*x3 >= 1) 
					continue;
				
				System.out.println("before: " + x0 + ", " + x1 + ", " + x2 + ", " + x3);
				sum = x0 + x1 + x2 + x3;
				x0 -= 0.25*sum;
				x1 -= 0.25*sum;
				x2 -= 0.25*sum;
				x3 -= 0.25*sum;
				mag = Math.sqrt(x0*x0 + x1*x1 + x2*x2 + x3*x3);
			
				System.out.println("after: " + x0 + ", " + x1 + ", " + x2 + ", " + x3);
				XProbs[i*4 + 0] = XProbs[0] + 0.1*x0/mag;
				XProbs[i*4 + 1] = XProbs[1] + 0.1*x1/mag;
				XProbs[i*4 + 2] = XProbs[2] + 0.1*x2/mag;
				XProbs[i*4 + 3] = XProbs[3] + 0.1*x3/mag;
				if (
						XProbs[i*4 + 0] < 0 || XProbs[i*4 + 0] > 1 || 
						XProbs[i*4 + 1] < 0 || XProbs[i*4 + 1] > 1 ||
						XProbs[i*4 + 2] < 0 || XProbs[i*4 + 2] > 1 ||
						XProbs[i*4 + 3] < 0 || XProbs[i*4 + 3] > 1
					)
					continue;
				break;
			}
		}
	
		for (int i = 0; i < 5*2 + 1; i++) {
			System.out.println("" + XProbs[4*i] + ", " + XProbs[4*i+1] + ", " + XProbs[4*i+2] + ", " +  XProbs[4*i+3]);
		}

	}

	public static void main(String[] args) {
		//testParamSweep("ecoli");

		testLoopSweep("ecoli");
		Graph.resetJunctionProbs();
		testLoopSweep("fischeri");
		Graph.resetJunctionProbs();
		testLoopSweep("marinus");
		Graph.resetJunctionProbs();
		testLoopSweep("natriegens");
		Graph.resetJunctionProbs();
		testLoopSweep("putida");
		
		
		//generateGraphs("ecoli");
//		generateGraphs("fischeri");
//		generateGraphs("marinus");
//		generateGraphs("natriegens");
		//generateGraphs("putida");
		//String maze = "non-uni-maze.txt";
		//String path = "graphBasedSimulation/assets/maze_coords/" + maze;
		//System.out.println(path);

		//Graph g0 = new Graph(path, true, true, false, "ecoli");
		//g0.grade(true, true);
	
		//test4PointGen();

	}	
}
