package junctions;
import java.io.*;
import java.util.*;

import core.DirEdge;
import utils.*;

public class TJunction extends Junction {
	private DirEdge left, mid, right;
	private static boolean initialized = false;

	//probabilities, e1 = pLeft, e2 = pRight, e3 = pMiddle
	private static TripletProbabilities<Double, Double, Double> pFromMiddle, pFromLeft, pFromRight;
	
	public TJunction(int ID, int leftID, int midID, int rightID ) {
		super(ID);
		
		if (!initialized) {
			try {
				String path = "graphBasedSimulation/core/tests/" + "TProb-uni.txt";
				Scanner f = new Scanner(new File(path));
			
				String[] line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				pFromMiddle = new TripletProbabilities<>
					(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]));
			
				line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				pFromLeft = new TripletProbabilities<>
					(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]));
			
				line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				pFromRight = new TripletProbabilities<>
					(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]));
				
			} catch (FileNotFoundException e) {
				System.out.println("T file not found");
				System.exit(1);
			}
			
			initialized = true;
		}
		
		left = new DirEdge(ID, leftID);
		mid = new DirEdge(ID, midID);
		right = new DirEdge(ID, rightID);
	}
}
