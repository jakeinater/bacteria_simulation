package mazeGraph;
import java.io.*;
import java.util.*;
import junctions.*;

class DirEdge {

	private Junction src;
	private Junction dest;
	private int weight = 0;
	
	public DirEdge(Junction src, Junction dest) {
		this.src = src;
		this.dest = dest;
	}
	
	public Junction getSrc() {
		return src;
	}
	
	public Junction getDest() {
		return dest;
	}
	
	public void incr() {
		//increment the count, agent walks over edge
		weight++;
	}
	
	public int getWeight() {
		return weight;
	}
	
}

class Pair<E1, E2> {
	E1 e1;
	E2 e2;
	
	public void Pair(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
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
	
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
