package core;
import java.io.*;
import java.util.*;

import junctions.*;
import utils.Triplet;
import io.xml.WriteXML;

enum Type {
	SOURCE, Y, T, X, L, SINK 
}

public class Graph {
	
	//we have n junctions with contiguous IDs 0, 1, ... n-1. 
	//We use adjacency list to store the edges node with ID i at index i
	//edges contain weight signifying the number of times it has been walked on
	//we need to give each junction a probability for entering each of its neighbors.
	//eg. L Junction, enter from one node, 100% of agents enter other edge
	//eg. T junction, enter from middle, 55% left, 45% right
	//eg. T junction, enter from left, 70% right, 30% middle
	//must define probabilities for each scenario
	//also must define a source and sink, both have one edge, source junction has 100% agents entering it
	//sink has it such that when an agent enters it, it no longer continues
	
	private Junction[] nodes;
	private int startID;
	
	Graph(){
	}
	
	Graph(String file) throws RuntimeException {
		//TODO: move the line parsing into their respective constructors
		try {
			Scanner f = new Scanner(new File(file));
			Scanner count = new Scanner(new File(file));
			
			int i = 0;
			while (count.hasNextLine()) {
				i++;
				count.nextLine();
			}
			
			this.nodes = new Junction[i];

			String[] line;
			int ID;
			
			while (f.hasNext()) {
				line = f.nextLine().split("\\s+");
				ID = Integer.parseInt(line[0]);
				if (nodes[ID] != null) throw new RuntimeException("ID has already been used");
				
				switch (Type.valueOf(line[1])) {
				case T:
					//3 neigbours: leftID baseID rightID
					if (line.length != 7) throw new RuntimeException("there are " + line.length + "arguments while 7 are needed");
					nodes[ID] =  new TJunction( ID,
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]), 
							Integer.parseInt(line[5]), 
							Integer.parseInt(line[6])
							);
					break;
				
				case X:
					//4 neighbours: firstID secondID thirdID fourthID
					if (line.length != 8) throw new RuntimeException("there are " + line.length + "arguments while 8 are needed");
					nodes[ID] = new XJunction( ID,
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]),
							Integer.parseInt(line[5]),
							Integer.parseInt(line[6]),
							Integer.parseInt(line[7])
							);
					break;
				case L:
					//2 neighbours: leftID rightID
					if (line.length != 6) throw new RuntimeException("there are " + line.length + "arguments while 6 are needed");
					nodes[ID] = new LJunction( ID,
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]),
							Integer.parseInt(line[5])
							);
					break;
				case Y:
					//3 neighbours: leftID baseID rightID
					if (line.length != 7) throw new RuntimeException("there are " + line.length + "arguments while 7 are needed");
					nodes[ID] = new YJunction( ID, 
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]), 
							Integer.parseInt(line[5]), 
							Integer.parseInt(line[6])
							);
					break;
				case SOURCE:
					//1 neighbour: edgeID
					if (line.length != 5) throw new RuntimeException("there are " + line.length + "arguments while 5 are needed");
					nodes[ID] = new Source( ID,
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]));
					startID = ID;
					break;
				case SINK:
					//1 neighbour: edgeID
					if (line.length != 5) throw new RuntimeException("there are " + line.length + "arguments while 5 are needed");
					nodes[ID] = new Sink( ID,
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]));
					break;
				default:
					System.out.println("not defined");
					System.exit(1);
					break;
				}
				
				
			}
		
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			System.exit(1);
		} catch (IllegalArgumentException e) {
			System.out.println("formatting error with file");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Junction[] getJunctions(){
		return nodes;
	}
	
	public void solve() {
		
		System.out.println("solving...");
	
		double numAgents = 1000;
		//final double MIN = 1;
		//we need to store the prev node, the number of agents entering the next node, and the next node, 
		//while incrementing the prev node
		//Pair<prevID, num agents>
		//if (agents < MIN) return;
		
		ArrayDeque<Triplet<Integer, Double, Integer>> q = new ArrayDeque<>();
		Triplet<Integer, Double, Integer> cur;
		/*int nextID = ((Source) nodes[startID]).getNext(agents);
		Triplet<Integer, Double, Integer> cur = new Triplet<>(
				((Source) nodes[startID]).getID(),
				agents, 
				((Source) nodes[startID]).getNext(agents));
		q.add(cur);*/
		nodes[startID].passThrough(0, numAgents, q);
		while (!q.isEmpty()) {
			System.out.println(q.size());
			cur = q.remove();
			nodes[cur.e3].passThrough(cur.e1, cur.e2, q);
		}
		
		System.out.println("done!");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String maze = args[0];
		String maze = "uni-maze.txt";
		String path = "graphBasedSimulation/assets/" + maze;
		System.out.println(path);
		//File f = new File(path);
		Graph g = new Graph(path);
		WriteXML.write(g, "test_init");
		g.solve();
		WriteXML.write(g, "test_solved");
	}

}
