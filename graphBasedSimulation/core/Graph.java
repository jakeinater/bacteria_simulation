package core;
import java.io.*;
import java.util.*;

import junctions.*;



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
	
	private ArrayList<Junction> nodes;
	
	Graph(){
	}
	
	Graph(String file) throws RuntimeException {
		//TODO: move the line parsing into their respective constructors
		try {
			Scanner f = new Scanner(new File(file));
			String[] line;
			int ID;
			
			while (f.hasNext()) {
				line = f.nextLine().split("\\s+");
				ID = Integer.parseInt(line[0]);
				if (nodes.get(ID) != null) throw new RuntimeException("ID has already been used");
				
				switch (Type.valueOf(line[1])) {
				case T:
					//3 neigbours: leftID baseID rightID
					if (line.length != 5) throw new RuntimeException("there are " + line.length + "arguments while 5 are needed");
					nodes.add( ID, 
							new TJunction( ID, 
									Integer.parseInt(line[2]), 
									Integer.parseInt(line[3]), 
									Integer.parseInt(line[5])
									)
							);
					break;
				case X:
					//4 neighbours: firstID secondID thirdID fourthID
					if (line.length != 6) throw new RuntimeException("there are " + line.length + "arguments while 6 are needed");
					nodes.add( ID,
							new XJunction( ID,
									Integer.parseInt(line[2]),
									Integer.parseInt(line[3]),
									Integer.parseInt(line[4]),
									Integer.parseInt(line[5])
									)
							);
					break;
				case L:
					//2 neighbours: leftID rightID
					if (line.length != 4) throw new RuntimeException("there are " + line.length + "arguments while 4 are needed");
					nodes.add(ID,
							new LJunction( ID,
									Integer.parseInt(line[2]),
									Integer.parseInt(line[3])
									)
							);
					break;
				case Y:
					//3 neighbours: leftID baseID rightID
					if (line.length != 5) throw new RuntimeException("there are " + line.length + "arguments while 5 are needed");
					nodes.add( ID, 
							new YJunction( ID, 
									Integer.parseInt(line[2]), 
									Integer.parseInt(line[3]), 
									Integer.parseInt(line[5])
									)
							);
					break;
				case SOURCE:
					//1 neighbour: edgeID
					if (line.length != 3) throw new RuntimeException("there are " + line.length + "arguments while 3 are needed");
					nodes.add(ID,
							new Source( ID,
									Integer.parseInt(line[2])
									)
							);
					break;
				case SINK:
					//1 neighbour: edgeID
					if (line.length != 3) throw new RuntimeException("there are " + line.length + "arguments while 3 are needed");
					nodes.add(ID, new Sink( ID, Integer.parseInt(line[2])));
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String maze = args[0];
		String maze = "uni-maze.txt";
		String path = "tests/" + maze;
		File f = new File(path);
		Graph g = new Graph(path);

	}

}
