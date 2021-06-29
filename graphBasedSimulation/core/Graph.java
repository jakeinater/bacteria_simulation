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
	private int endID;
	private boolean start_at_source;
	private boolean start_at_sink;
	
	Graph(){
	}
	
	Graph(String file, boolean start_at_source, boolean start_at_sink, boolean uniformProb, String species) throws RuntimeException {
		//TODO: move the line parsing into their respective constructors
		this.start_at_source = start_at_source;
		this.start_at_sink = start_at_sink;
		
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
					nodes[ID] =  new TJunction( uniformProb, species, ID,
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
					nodes[ID] = new XJunction( uniformProb, species, ID,
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
					nodes[ID] = new LJunction( uniformProb, species, ID,
							Double.parseDouble(line[2]),
							Double.parseDouble(line[3]),
							Integer.parseInt(line[4]),
							Integer.parseInt(line[5])
							);
					break;
				case Y:
					//3 neighbours: leftID baseID rightID
					if (line.length != 7) throw new RuntimeException("there are " + line.length + "arguments while 7 are needed");
					nodes[ID] = new YJunction( uniformProb, species, ID, 
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
					if (start_at_source) {
						nodes[ID] = new Source( ID,
								Double.parseDouble(line[2]),
								Double.parseDouble(line[3]),
								Integer.parseInt(line[4]));
					} else {
						nodes[ID] = new Sink( ID,
								Double.parseDouble(line[2]),
								Double.parseDouble(line[3]),
								Integer.parseInt(line[4]));
					}
					startID = ID;
					break;
				case SINK:
					//1 neighbour: edgeID
					if (line.length != 5) throw new RuntimeException("there are " + line.length + "arguments while 5 are needed");
					if (start_at_sink) {
						nodes[ID] = new Source( ID,
								Double.parseDouble(line[2]),
								Double.parseDouble(line[3]),
								Integer.parseInt(line[4]));
					} else {
						nodes[ID] = new Sink( ID,
								Double.parseDouble(line[2]),
								Double.parseDouble(line[3]),
								Integer.parseInt(line[4]));
					}
					endID = ID;
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
		//we need to store the prev node, the number of agents entering the next node, and the next node, 
		//while incrementing the prev node
		ArrayDeque<Triplet<Integer, Double, Integer>> q = new ArrayDeque<>();
		Triplet<Integer, Double, Integer> cur;
		
		if (start_at_source) nodes[startID].passThrough(0, numAgents, q);
		if (start_at_sink) nodes[endID].passThrough(0, numAgents, q);
		
		while (!q.isEmpty()) {
			cur = q.remove();
			nodes[cur.e3].passThrough(cur.e1, cur.e2, q);
		}
		
		System.out.println("done!");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String species = "marinus";
		
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

}
