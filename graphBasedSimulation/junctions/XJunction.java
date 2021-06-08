package junctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import core.DirEdge;
import utils.QuartetProbabilities;

public class XJunction extends Junction {
	private DirEdge e1, e2, e3, e4; //defined counter clockwise
	private static boolean initialized = false;

	private static QuartetProbabilities<Double, Double, Double, Double> p;
	
	public XJunction(int ID, int e1ID, int e2ID, int e3ID, int e4ID ) {
		super(ID);
		if (!initialized) {
			try {
				String path = "graphBasedSimulation/core/tests/" + "XProb-uni.txt";
				Scanner f = new Scanner(new File(path));
			
				String[] line = f.nextLine().split("\\s+");
				if (line.length != 4) throw new RuntimeException("expect 4 args, got " + line.length);
				p = new QuartetProbabilities<>(
						Double.parseDouble(line[0]), 
						Double.parseDouble(line[1]), 
						Double.parseDouble(line[2]), 
						Double.parseDouble(line[3])
						);
		
			} catch (FileNotFoundException e) {
				System.out.println("X file not found");
				System.exit(1);
			}
			
			initialized = true;
		}
		
		e1 = new DirEdge(ID, e1ID);
		e2 = new DirEdge(ID, e2ID);
		e3 = new DirEdge(ID, e3ID);
		e4 = new DirEdge(ID, e4ID);
	}
}
