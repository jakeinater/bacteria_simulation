package junctions;
import core.DirEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.HashMap;

import utils.Triplet;
import utils.StringKey;
import org.w3c.dom.*;

public class LJunction extends Junction {
	
	private DirEdge e1, e2;
	private static boolean initialized = false;
	private static Triplet<Double, Double, Double> p;
	
	public LJunction(boolean uniformProb, String species, int ID, double x, double y, int e1, int e2) {
		super(ID, x, y);
		
		if (!initialized) {
			try {
				String path;
				if (uniformProb) {
					path = "graphBasedSimulation/assets/probabilities/" + species + "/" + "LProb-uni.txt";
				} else {
					path = "graphBasedSimulation/assets/probabilities/" + species + "/" + "LProb-non-uni.txt";
				}
				Scanner f = new Scanner(new File(path));
				
				String[] line = f.nextLine().split("\\s+");
				if (line.length != 3) throw new RuntimeException("expect 3 args, got " + line.length);
				p = new Triplet<>(
						Double.parseDouble(line[0]) / 100.,
						Double.parseDouble(line[1]) / 100.,
						Double.parseDouble(line[2]) / 100.
						);
			} catch (FileNotFoundException e) {
				System.out.println("L file not found");
				System.exit(1);
			}
			initialized = true;
		}
		
		this.e1 = new DirEdge(ID, e1);
		this.e2 = new DirEdge(ID, e2);
	}
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents < MIN) return;
		incrementNodeWeight(numAgents);
		
		if (prevID == e1.getDest()) {
			//coming from e1
			e2.incr(p.e1*numAgents);
			e1.incr(p.e2*numAgents);
			q.add(new Triplet<>(this.getID(), p.e1*numAgents, e2.getDest()));
			q.add(new Triplet<>(this.getID(), p.e2*numAgents, e1.getDest()));
		} else {
			//coming from e2
			e1.incr(p.e1*numAgents);
			e2.incr(p.e2*numAgents);
			q.add(new Triplet<>(this.getID(), p.e1*numAgents, e1.getDest()));
			q.add(new Triplet<>(this.getID(), p.e2*numAgents, e2.getDest()));
		}
	}

	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, e1);
		addEdgeXML(doc, graphElement, d2, e2);
	}
	
	public static Triplet<Double, Double, Double> getCopyProbabilities() {
		return new Triplet<>(p.e1, p.e2, p.e3);
	}
	
	public static void setProbabilities(Double e1, Double e2, Double e3) {
		p.e1 = e1;
		p.e2 = e2;
		p.e3 = e3;
	}
	
	@Override
	public void addUndirEdges(HashMap<String, Double> loss) {
		String key = StringKey.stringKey(this.getID(), this.e1.getDest());
		double weight = loss.getOrDefault(key, 0.) + e1.getWeight();
		loss.put(key, weight);
	
		key = StringKey.stringKey(this.getID(), this.e2.getDest());
		weight = loss.getOrDefault(key, 0.) + e2.getWeight();
		loss.put(key, weight);
	}

	@Override
	public void resetEdgeWeights() {
		e1.resetWeight();
		e2.resetWeight();
	}
	
	public static void resetInit() {
		initialized = false;
	}
}