package junctions;
import java.io.*;
import java.util.*;

import core.DirEdge;
import utils.*;
import org.w3c.dom.*;

public class TJunction extends Junction {
	private DirEdge left, mid, right;
	private static boolean initialized = false;

	//probabilities, e1 = pLeft, e2 = pRight, e3 = pMiddle
	private static TripletProbabilities<Double, Double, Double> pFromMiddle, pFromLeft, pFromRight;
	
	public TJunction(boolean uniformProbabilities, String species, int ID, double x, double y, int leftID, int midID, int rightID ) {
		super(ID, x, y);
		
		if (!initialized) {
			try {
				String path;
				if (uniformProbabilities) {
					path = "graphBasedSimulation/assets/probabilities/" + species + "/" + "TProb-uni.txt";
				} else {
					path = "graphBasedSimulation/assets/probabilities/" + species + "/" + "TProb-non-uni.txt";
				}
				Scanner f = new Scanner(new File(path));
			
				String[] line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				pFromMiddle = new TripletProbabilities<>(
						Double.parseDouble(line[0]) / 100., 
						Double.parseDouble(line[1]) / 100., 
						Double.parseDouble(line[2]) / 100.);
			
				line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				pFromLeft = new TripletProbabilities<>(
						Double.parseDouble(line[0]) / 100., 
						Double.parseDouble(line[1]) / 100., 
						Double.parseDouble(line[2]) / 100.);
			
				line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				pFromRight = new TripletProbabilities<>(
						Double.parseDouble(line[0]) / 100., 
						Double.parseDouble(line[1]) / 100., 
						Double.parseDouble(line[2]) / 100.);
				
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

	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents<MIN) return;
		incrementNodeWeight(numAgents);
	
		if (prevID == left.getDest()) {
			//came from left
			left.incr(pFromLeft.pLeft * numAgents);
			mid.incr(pFromLeft.pMiddle * numAgents);
			right.incr(pFromLeft.pRight * numAgents);
			q.add(new Triplet<>(this.getID(), pFromLeft.pLeft * numAgents, left.getDest()));
			q.add(new Triplet<>(this.getID(), pFromLeft.pMiddle * numAgents, mid.getDest()));
			q.add(new Triplet<>(this.getID(), pFromLeft.pRight * numAgents, right.getDest()));
		} else if ( prevID == mid.getDest()) {
			//mid
			left.incr(pFromMiddle.pLeft * numAgents);
			mid.incr(pFromMiddle.pMiddle * numAgents);
			right.incr(pFromMiddle.pRight * numAgents);
			q.add(new Triplet<>(this.getID(), pFromMiddle.pLeft * numAgents, left.getDest()));
			q.add(new Triplet<>(this.getID(), pFromMiddle.pMiddle * numAgents, mid.getDest()));
			q.add(new Triplet<>(this.getID(), pFromMiddle.pRight * numAgents, right.getDest()));
		} else {
			//right
			left.incr(pFromRight.pLeft * numAgents);
			mid.incr(pFromRight.pMiddle * numAgents);
			right.incr(pFromRight.pRight * numAgents);
			q.add(new Triplet<>(this.getID(), pFromRight.pLeft * numAgents, left.getDest()));
			q.add(new Triplet<>(this.getID(), pFromRight.pMiddle * numAgents, mid.getDest()));
			q.add(new Triplet<>(this.getID(), pFromRight.pRight * numAgents, right.getDest()));
		}
			
	}

	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, left);
		addEdgeXML(doc, graphElement, d2, mid);	
		addEdgeXML(doc, graphElement, d2, right);	
	}

}
