package junctions;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

import core.DirEdge;
import utils.*;

public class YJunction extends Junction {
	private DirEdge left, mid, right;
	private static boolean initialized = false;

	//probabilities, e1 = pLeft, e2 = pRight, e3 = pMiddle
	private static TripletProbabilities<Double, Double, Double> pFromMiddle, pFromLeft, pFromRight;
	
	public YJunction(boolean uniformProb, String species, int ID, double x, double y, int leftID, int midID, int rightID ) {
		super(ID, x, y);
		if (!initialized) {
			try {
				String path;
				if (uniformProb) {
					path = "graphBasedSimulation/assets/probabilities/" + species + "/YProb-uni.txt";
				} else {
					path = "graphBasedSimulation/assets/probabilities/" + species + "/YProb-uni.txt";
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
				System.out.println("Y file not found");
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

	public static TripletProbabilities<Double, Double, Double> getCopyPFromLeft() {
		return new TripletProbabilities<>(pFromLeft.pLeft, pFromLeft.pRight, pFromLeft.pMiddle);
	}
	
	public static void setPFromLeft(Double left, Double right, Double middle) {
		pFromLeft.pLeft = left;
		pFromLeft.pRight = right;
		pFromLeft.pMiddle = middle;
	}

	public static TripletProbabilities<Double, Double, Double> getCopyPFromRight() {
		return new TripletProbabilities<>(pFromRight.pLeft, pFromRight.pRight, pFromRight.pMiddle);
	}
	
	public static void setPFromRight(Double left, Double right, Double middle) {
		pFromRight.pLeft = left;
		pFromRight.pRight = right;
		pFromRight.pMiddle = middle;
	}

	public static TripletProbabilities<Double, Double, Double> getCopyPFromMiddle() {
		return new TripletProbabilities<>(pFromMiddle.pLeft, pFromMiddle.pRight, pFromMiddle.pMiddle);
	}
	
	public static void setPFromMiddle(Double left, Double right, Double middle) {
		pFromMiddle.pLeft = left;
		pFromMiddle.pRight = right;
		pFromMiddle.pMiddle = middle;
	}
	
	@Override
	public void addUndirEdges(HashMap<String, Double> loss) {
		String key = StringKey.stringKey(this.getID(), this.left.getDest());
		double weight = loss.getOrDefault(key, 0.) + left.getWeight();
		loss.put(key, weight);
	
		key = StringKey.stringKey(this.getID(), this.mid.getDest());
		weight = loss.getOrDefault(key, 0.) + mid.getWeight();
		loss.put(key, weight);
		
		key = StringKey.stringKey(this.getID(), this.right.getDest());
		weight = loss.getOrDefault(key, 0.) + right.getWeight();
		loss.put(key, weight);
	}
	
	@Override
	public void resetEdgeWeights() {
		left.resetWeight();
		right.resetWeight();
		mid.resetWeight();
	}
	
	public static void resetInit() {
		initialized = false;
	}

}
