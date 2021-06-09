package junctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayDeque;
import org.w3c.dom.*;

import utils.Triplet;
import core.DirEdge;
import utils.QuartetProbabilities;

public class XJunction extends Junction {
	private DirEdge e1, e2, e3, e4; //defined counter clockwise
	private static boolean initialized = false;

	private static QuartetProbabilities<Double, Double, Double, Double> p;
	
	public XJunction(int ID, double x, double y, int e1ID, int e2ID, int e3ID, int e4ID ) {
		super(ID, x, y);
		if (!initialized) {
			try {
				String path = "graphBasedSimulation/assets/" + "XProb-uni.txt";
				Scanner f = new Scanner(new File(path));
			
				String[] line = f.nextLine().split("\\s+");
				if (line.length != 4) throw new RuntimeException("expect 4 args, got " + line.length);
				p = new QuartetProbabilities<>(
						Double.parseDouble(line[0]) / 100., 
						Double.parseDouble(line[1]) / 100., 
						Double.parseDouble(line[2]) / 100., 
						Double.parseDouble(line[3]) / 100.
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
	
	@Override
	public void passThrough(int prevID, double numAgents, ArrayDeque<Triplet<Integer, Double, Integer>> q) {
		if (numAgents<MIN) return; //below threshold
		
		if (prevID == e1.getDest()) {
			//from e1
			e1.incr(p.pBack * numAgents);
			e3.incr(p.pForward * numAgents);
			e4.incr(p.pRight * numAgents);
			e2.incr(p.pLeft * numAgents);
			q.add(new Triplet<>(this.getID(), p.pBack * numAgents, e1.getDest()));
			q.add(new Triplet<>(this.getID(), p.pForward * numAgents, e3.getDest()));
			q.add(new Triplet<>(this.getID(), p.pRight * numAgents, e4.getDest()));
			q.add(new Triplet<>(this.getID(), p.pLeft * numAgents, e2.getDest()));
		} else if (prevID == e2.getDest()) {
			//e2
			e2.incr(p.pBack * numAgents);
			e4.incr(p.pForward * numAgents);
			e1.incr(p.pRight * numAgents);
			e3.incr(p.pLeft * numAgents);
			q.add(new Triplet<>(this.getID(), p.pBack * numAgents, e2.getDest()));
			q.add(new Triplet<>(this.getID(), p.pForward * numAgents, e4.getDest()));
			q.add(new Triplet<>(this.getID(), p.pRight * numAgents, e1.getDest()));
			q.add(new Triplet<>(this.getID(), p.pLeft * numAgents, e3.getDest()));
		} else if (prevID == e3.getDest()) {
			//e3
			e3.incr(p.pBack * numAgents);
			e1.incr(p.pForward * numAgents);
			e2.incr(p.pRight * numAgents);
			e4.incr(p.pLeft * numAgents);
			q.add(new Triplet<>(this.getID(), p.pBack * numAgents, e3.getDest()));
			q.add(new Triplet<>(this.getID(), p.pForward * numAgents, e1.getDest()));
			q.add(new Triplet<>(this.getID(), p.pRight * numAgents, e2.getDest()));
			q.add(new Triplet<>(this.getID(), p.pLeft * numAgents, e4.getDest()));
		} else {
			//e4
			e4.incr(p.pBack * numAgents);
			e2.incr(p.pForward * numAgents);
			e3.incr(p.pRight * numAgents);
			e1.incr(p.pLeft * numAgents);
			q.add(new Triplet<>(this.getID(), p.pBack * numAgents, e4.getDest()));
			q.add(new Triplet<>(this.getID(), p.pForward * numAgents, e2.getDest()));
			q.add(new Triplet<>(this.getID(), p.pRight * numAgents, e3.getDest()));
			q.add(new Triplet<>(this.getID(), p.pLeft * numAgents, e1.getDest()));
		}
	}

	@Override
	public void addEdgesXML(Document doc, Element graphElement, String d2) {
		addEdgeXML(doc, graphElement, d2, e1);		
		addEdgeXML(doc, graphElement, d2, e2);		
		addEdgeXML(doc, graphElement, d2, e3);		
		addEdgeXML(doc, graphElement, d2, e4);		
	}

}
