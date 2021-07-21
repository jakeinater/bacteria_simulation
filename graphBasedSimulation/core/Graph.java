package core;
import java.io.*;
import java.util.*;

import junctions.*;
import utils.*;
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
	private int numNodes;
	private int numDirEdges = 0;
	private final boolean uniform;
	
	private boolean graded = false;
	private boolean wasMean = false;
	private HashMap<String, Double> loss = null;
	private HashMap<String, Double> expHeatmap = null;
	private double maxExpWeight = 0;
	
	
	public Graph(String file, boolean start_at_source, boolean start_at_sink, boolean uniformProb, String species) throws RuntimeException {
		//TODO: move the line parsing into their respective constructors
		this.start_at_source = start_at_source;
		this.start_at_sink = start_at_sink;
		if (uniformProb)
			uniform = true;
		else
			uniform = false;
		
		try {
			Scanner f = new Scanner(new File(file));
			Scanner count = new Scanner(new File(file));
			
			int i = 0;
			while (count.hasNextLine()) {
				i++;
				count.nextLine();
			}
			
			numNodes = i;
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
					numDirEdges += 3;
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
					numDirEdges += 4;
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
					numDirEdges += 2;
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
					numDirEdges += 3;
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
					numDirEdges += 1;
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
					numDirEdges += 1;
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
		
		//System.out.println("solving...");
	
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
		//System.out.println("done!");
	}

	
	public double grade(boolean mean, boolean storeGraphDiff) {
		//helper function for sweep method

		//only applicable to undirected graphs
		//convert to undirected graph -> grade -> return grade?
		Double sumLoss = 0.;
		
		if ( mean ^ wasMean || !graded ) {
			graded = true;
			//need to initialize the hashmap
			loss = new HashMap<>(numDirEdges/2);
			for (Junction node: this.nodes) {
				node.addUndirEdges(loss);
			}
			
			//parse through the asset file and create the map using that.
			try {
				String file = "non-uni-EC.txt";
				String path = "graphBasedSimulation/assets/exp_heatmaps/" + file;
				Scanner f = new Scanner(new File(path));
			
				String[] line;
				String key;
				double cur;
				
				expHeatmap = new HashMap<>(numDirEdges/2);
				
				if (mean) {
					wasMean = true;
					while (f.hasNext()) {
						line = f.nextLine().split("\\s+");
						key = StringKey.stringKey(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
						cur = Double.parseDouble(line[3]);
						if (cur > maxExpWeight) maxExpWeight = cur;
						expHeatmap.put(key, cur);
					}
				} else {
					wasMean = false;
					while (f.hasNext()) {
						line = f.nextLine().split("\\s+");
						key = StringKey.stringKey(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
						cur = Double.parseDouble(line[5]);
						if (cur > maxExpWeight) maxExpWeight = cur;
						expHeatmap.put(key, cur);
					}
				}
			} 
			catch (FileNotFoundException e) {
				System.out.println("file not found");
				System.exit(1);
			} catch (IllegalArgumentException e) {
				System.out.println("formatting error with file");
				e.printStackTrace();
				System.exit(1);
			}
			
			//store experimental measurements to view what it looks like
			if (storeGraphDiff) {
				WriteXML.writeUndir(this, expHeatmap, "exp_representation_mean", "graphBasedSimulation/assets/graphs/testing/");
			}
		}
	
		//if each edge had max possible difference
		//System.out.println("sqrt( 255^2 * num edges ): " + Math.sqrt(255*255*expHeatmap.size()));
		//System.out.println("255*num edges: " + 255*expHeatmap.size());
		
		//if each edge was 50% saturated (ie random edge values)
		Double sum = 0.;
		for (Double val: expHeatmap.values()) {
			sum += Math.pow(255./2 - val, 2);
			}
		//System.out.println("50%: " + Math.sqrt(sum));
		
		//create map
		for (Junction node: this.nodes) {
			node.addUndirEdges(loss);
			}
		
		//normalize sim heatmap max weight to the same as max experimental
		double maxSimWeight = 0;
		for (double cur: loss.values()) {
			if (cur > maxSimWeight) {
				maxSimWeight = cur;
				}
			}
		final double m = maxSimWeight == 0?1:maxSimWeight;
		loss.replaceAll((key, val) -> val * (maxExpWeight/m));
		
		
		//add up loss
		Double val;
		for (String key: loss.keySet()) {
			val = Math.pow(loss.get(key) - expHeatmap.get(key), 2);
			sumLoss += val;
			loss.put(key, Math.sqrt(val));
			}
		
		//System.out.println(Math.sqrt(sumLoss));
	
		//export graphml of difference
		if (storeGraphDiff) {
			//the node IDs must be the same as the ones used in exp heatmap file.
			WriteXML.writeUndir(this, loss, "optimizedDiff", "graphBasedSimulation/assets/graphs/testing/");
		}
		
		
		//reset to 0
		loss.replaceAll((key, value) -> 0.);
		//System.out.println("##############");
		return sumLoss;
	}
	
	//loop iter: if grade better store probabilities and continue, else continue. At end return the final stored probabilities.
	public double[] paramSweep() {
		//update probabilities TODO: include X junction later (4D?)
		//lots of nested loops, each layer defines a parameter we are sweeping over
		final double DIST_FROM_EXP = 0.1;			// limit radius to check, not inclusive of rad
		final int NUM_DISTANCES_FROM_EXP = 2;	// number of distances to check between DIST_FROM_EXP and the centre (EXP)
		final int NUM_POINTS_ON_CIRC = 8;			// number of points to check on one equidistant circle around the centre
		
		final double DEG_INCR = Math.PI*2/NUM_POINTS_ON_CIRC;
		final double RADII_INCR = DIST_FROM_EXP/NUM_DISTANCES_FROM_EXP; 
		

		if (uniform) {
			//uniform maze, has Y junctions
			
			double estLengthOfOp = Math.pow((NUM_DISTANCES_FROM_EXP - 1)*(NUM_POINTS_ON_CIRC + 1), 7); //7 layers of 10*10 nested operations. 100**7 operations
			System.out.println("Estimated time of calc assuming 7000 operations/second: " + estLengthOfOp /(7000*60*60) + "hrs");

			TripletProbabilities<Double, Double, Double> pTFromMiddle0, pTFromLeft0, pTFromRight0, pYFromMiddle0, pYFromLeft0, pYFromRight0;
			
			Triplet<Double, Double, Double> pL0 = LJunction.getCopyProbabilities();
			
			pTFromMiddle0 = TJunction.getCopyPFromMiddle();
			pTFromLeft0 = TJunction.getCopyPFromLeft();
			pTFromRight0 = TJunction.getCopyPFromRight();
			
			pYFromMiddle0 = YJunction.getCopyPFromMiddle();
			pYFromLeft0 = YJunction.getCopyPFromLeft();
			pYFromRight0 = YJunction.getCopyPFromRight();
			
			Triplet<Double, Double, Double> n1 = new Triplet<>(-1/Math.sqrt(2), 0., 1/Math.sqrt(2));
			Triplet<Double, Double, Double> n2 = new Triplet<>(1/Math.sqrt(6), -2/Math.sqrt(6), 1/Math.sqrt(2));
			
			double prevScore = Double.MAX_VALUE;
			double curScore;
			
			/*		array struct
			 * 		 * 		{pTFromLeft2pLeft, pTFromLeft2pRight, pTFromLeft2pMiddle,
			 * 		 * 		pTFromRight2pLeft, pTFromRight2pRight, pTFromRight2pMiddle,
			 * 		 * 		pTFromMiddle2pLeft, pTFromMiddle2pRight, pTFromMiddle2pMiddle,
			 * 		 * 		
			 * 		 * 		pYFromLeft2pLeft, pYFromLeft2pRight, pYFromLeft2pMiddle,
			 * 		 * 		pYFromRight2pLeft, pYFromRight2pRight, pYFromRight2pMiddle,
			 * 		 * 		pYFromMiddle2pLeft, pYFromMiddle2pRight, pYFromMiddle2pMiddle,
			 * 		 * 		
			 * 		 * 		pL2e1, pL2e2, pL2e3;
			 * 		 * 
			 * 		 * 		pXLeft, pXForward, pXRight, pXBack
			 * 		 * */
			
			double[] prob2 = new double[25];
			double[] probBest = new double[25];
			
			
			//sweeping all parameters, order 10^10 time complexity
			for (double tULeft = 0; tULeft < DIST_FROM_EXP;) {
				for (double tVLeft = 0; tVLeft <2*Math.PI;) {
					if (tULeft == 0.) tVLeft += 2*Math.PI;		//this is so the center, exp val, is only checked once
					prob2[0] = pTFromLeft0.pLeft + tULeft*(n1.e1*Math.cos(tVLeft) + n2.e1*Math.sin(tVLeft));
					prob2[1] = pTFromLeft0.pRight + tULeft*(n1.e2*Math.cos(tVLeft) + n2.e2*Math.sin(tVLeft));
					prob2[2] = pTFromLeft0.pMiddle + tULeft*(n1.e3*Math.cos(tVLeft) + n2.e3*Math.sin(tVLeft));
					if (prob2[0] > 1 || prob2[1] > 1 || prob2[2] > 1 || prob2[0] < 0 || prob2[1] < 0 || prob2[2] < 0) {
						tVLeft += DEG_INCR;
						continue;
						}
					TJunction.setPFromLeft(
							prob2[0],
							prob2[1],
							prob2[2]
									); 
					
					for (double tURight = 0; tURight < DIST_FROM_EXP;) {
						for (double tVRight = 0; tVRight <2*Math.PI;) {
							if (tURight == 0.) tVRight += 2*Math.PI;
							prob2[3] = pTFromRight0.pLeft + tURight*(n1.e1*Math.cos(tVRight) + n2.e1*Math.sin(tVRight));
							prob2[4] = pTFromRight0.pRight + tURight*(n1.e2*Math.cos(tVRight) + n2.e2*Math.sin(tVRight));
							prob2[5] = pTFromRight0.pMiddle + tURight*(n1.e3*Math.cos(tVRight) + n2.e3*Math.sin(tVRight));
							if (prob2[3]>1 || prob2[4] > 1 || prob2[5] > 1 || prob2[3] < 0 || prob2[4] < 0 || prob2[5] < 0) {
								tVRight += DEG_INCR;
								continue;
								}
							TJunction.setPFromRight(
									prob2[3],
									prob2[4],
									prob2[5]
											);
							
							for (double tUMiddle = 0; tUMiddle < DIST_FROM_EXP;) {
								for (double tVMiddle = 0; tVMiddle <2*Math.PI;) {
									if (tUMiddle == 0.) tVMiddle += 2*Math.PI;
									prob2[6] = pTFromMiddle0.pLeft + tUMiddle*(n1.e1*Math.cos(tVMiddle) + n2.e1*Math.sin(tVMiddle));
									prob2[7] = pTFromMiddle0.pRight + tUMiddle*(n1.e2*Math.cos(tVMiddle) + n2.e2*Math.sin(tVMiddle));
									prob2[8] = pTFromMiddle0.pMiddle + tUMiddle*(n1.e3*Math.cos(tVMiddle) + n2.e3*Math.sin(tVMiddle));
									if (prob2[6] > 1 || prob2[7] > 1 || prob2[8] > 1 || prob2[6] < 0 || prob2[7] < 0 || prob2[8] < 0) {
										tVMiddle += DEG_INCR;
										continue;
										}
									TJunction.setPFromMiddle(
											prob2[6],
											prob2[7],
											prob2[8]
													);
									
									for (double yULeft = 0; yULeft < DIST_FROM_EXP;) {
										for (double yVLeft = 0; yVLeft <2*Math.PI;) {
											if (yULeft == 0.) yVLeft += 2*Math.PI;
											prob2[9] = pYFromLeft0.pLeft + yULeft*(n1.e1*Math.cos(yVLeft) + n2.e1*Math.sin(yVLeft));
											prob2[10] = pYFromLeft0.pRight + yULeft*(n1.e2*Math.cos(yVLeft) + n2.e2*Math.sin(yVLeft));
											prob2[11] = pYFromLeft0.pMiddle + yULeft*(n1.e3*Math.cos(yVLeft) + n2.e3*Math.sin(yVLeft));
											if (prob2[9] > 1 || prob2[10] > 1 || prob2[11] > 1 || prob2[9] < 0 || prob2[10] < 0 || prob2[11] < 0) {
												yVLeft += DEG_INCR;
												continue;
												}
											YJunction.setPFromLeft(
													prob2[9],
													prob2[10],
													prob2[11]
															); 
											
											for (double yURight = 0; yURight < DIST_FROM_EXP;) {
												for (double yVRight = 0; yVRight <2*Math.PI;) {
													if (yURight == 0.) yVRight += 2*Math.PI;
													prob2[12] = pYFromRight0.pLeft + yURight*(n1.e1*Math.cos(yVRight) + n2.e1*Math.sin(yVRight));
													prob2[13] = pYFromRight0.pRight + yURight*(n1.e2*Math.cos(yVRight) + n2.e2*Math.sin(yVRight));
													prob2[14] = pYFromRight0.pMiddle + yURight*(n1.e3*Math.cos(yVRight) + n2.e3*Math.sin(yVRight));
													if (prob2[12] > 1 || prob2[13] > 1 || prob2[14] > 1 || prob2[12] < 0 || prob2[13] < 0 || prob2[14] < 0) {
														yVRight += DEG_INCR;
														continue;
														}
													YJunction.setPFromRight(
															prob2[12],
															prob2[13],
															prob2[14]
																	);
													
													for (double yUMiddle = 0; yUMiddle < DIST_FROM_EXP;) {
														for (double yVMiddle = 0; yVMiddle <2*Math.PI;) {
															if (yUMiddle == 0.) yVMiddle += 2*Math.PI;
															prob2[15] = pYFromMiddle0.pLeft + yUMiddle*(n1.e1*Math.cos(yVMiddle) + n2.e1*Math.sin(yVMiddle));
															prob2[16] = pYFromMiddle0.pRight + yUMiddle*(n1.e2*Math.cos(yVMiddle) + n2.e2*Math.sin(yVMiddle));
															prob2[17] = pYFromMiddle0.pMiddle + yUMiddle*(n1.e3*Math.cos(yVMiddle) + n2.e3*Math.sin(yVMiddle));
															if (prob2[15] > 1 || prob2[16] > 1 || prob2[17] > 1 || prob2[15] < 0 || prob2[16] < 0 || prob2[17] < 0) {
																yVMiddle += DEG_INCR;
																continue;
																}
															YJunction.setPFromMiddle(
																	prob2[15],
																	prob2[16],
																	prob2[17]
																			);
															
															for (double lU = 0; lU < DIST_FROM_EXP;) {
																for (double lV = 0; lV <2*Math.PI;) {
																	if (lU == 0.) lV += 2*Math.PI;
																	prob2[18] = pL0.e1 + lU*(n1.e1*Math.cos(lV) + n2.e1*Math.sin(lV));
																	prob2[19] = pL0.e2 + lU*(n1.e2*Math.cos(lV) + n2.e2*Math.sin(lV));
																	prob2[20] = pL0.e3 + lU*(n1.e3*Math.cos(lV) + n2.e3*Math.sin(lV));
																	if (prob2[18] > 1 || prob2[19] > 1 || prob2[20] > 1 || prob2[18] < 0 || prob2[19] < 0 || prob2[20] < 0 ) {
																		lV += DEG_INCR;//FIXXXXX
																		continue;
																		}
																	LJunction.setProbabilities( 
																			prob2[18], 
																			prob2[19], 
																			prob2[20] 
																					); 
																	
																	//**********************************************************
																	//grade and stuff
																	this.solve();
																	curScore = this.grade(true, false);
																	//System.out.println("running");
																	if (curScore < prevScore) {
																		System.out.println(Math.sqrt(curScore));
																		//store the values
																		for (int i = 0; i < prob2.length; i++) {
																			probBest[i] = prob2[i];
																			}
																		prevScore = curScore;
																		}
																	this.resetWeights();
																	//***********************************************************
																	
																	lV += DEG_INCR;
																	}
																lU += RADII_INCR;
																}	
															//END L
															
															yVMiddle += DEG_INCR;
															}
														yUMiddle += RADII_INCR;
														}
													yVRight += DEG_INCR;
													}
												yURight += RADII_INCR;
												}
											yVLeft += DEG_INCR;
											}
										yULeft += RADII_INCR;
										}					
									//END Y
									
									tVMiddle += DEG_INCR;
									}
								tUMiddle += RADII_INCR;
								}
							tVRight += DEG_INCR;
							}
						tURight += RADII_INCR;
						}
					tVLeft += DEG_INCR;
					}
				tULeft += RADII_INCR;
				}
			return probBest;
			} else {
			
				//non uniform maze, doesnt have Y junctions
				double estLengthOfOp = Math.pow((NUM_DISTANCES_FROM_EXP - 1)*(NUM_POINTS_ON_CIRC + 1), 4); //4 layers of 10*10 nested operations. 100**4 operations
				System.out.println("Estimated time of calc assuming 7000 operations/second: " + estLengthOfOp /(7000) + "s");
				
				TripletProbabilities<Double, Double, Double> pTFromMiddle0, pTFromLeft0, pTFromRight0;
				pTFromMiddle0 = TJunction.getCopyPFromMiddle();
				pTFromLeft0 = TJunction.getCopyPFromLeft();
				pTFromRight0 = TJunction.getCopyPFromRight();
				Triplet<Double, Double, Double> pL0 = LJunction.getCopyProbabilities();
				
				Triplet<Double, Double, Double> n1 = new Triplet<>(-1/Math.sqrt(2), 0., 1/Math.sqrt(2));
				Triplet<Double, Double, Double> n2 = new Triplet<>(1/Math.sqrt(6), -2/Math.sqrt(6), 1/Math.sqrt(2));
				
				double prevScore = Double.MAX_VALUE;
				double curScore;
				
				double[] prob2 = new double[25];
				double[] probBest = new double[25];
				
				//sweeping all parameters, order 10^10 time complexity
				for (double tULeft = 0; tULeft < DIST_FROM_EXP;) {
					for (double tVLeft = 0; tVLeft <2*Math.PI;) {
						if (tULeft == 0.) tVLeft += 2*Math.PI;		//this is so the center, exp val, is only checked once
						prob2[0] = pTFromLeft0.pLeft + tULeft*(n1.e1*Math.cos(tVLeft) + n2.e1*Math.sin(tVLeft));
						prob2[1] = pTFromLeft0.pRight + tULeft*(n1.e2*Math.cos(tVLeft) + n2.e2*Math.sin(tVLeft));
						prob2[2] = pTFromLeft0.pMiddle + tULeft*(n1.e3*Math.cos(tVLeft) + n2.e3*Math.sin(tVLeft));
						if (prob2[0] > 1 || prob2[1] > 1 || prob2[2] > 1 || prob2[0] < 0 || prob2[1] < 0 || prob2[2] < 0) {
							tVLeft += DEG_INCR;
							continue;
							}
						TJunction.setPFromLeft(
								prob2[0],
								prob2[1],
								prob2[2]
										); 
						
						for (double tURight = 0; tURight < DIST_FROM_EXP;) {
							for (double tVRight = 0; tVRight <2*Math.PI;) {
								if (tURight == 0.) tVRight += 2*Math.PI;
								prob2[3] = pTFromRight0.pLeft + tURight*(n1.e1*Math.cos(tVRight) + n2.e1*Math.sin(tVRight));
								prob2[4] = pTFromRight0.pRight + tURight*(n1.e2*Math.cos(tVRight) + n2.e2*Math.sin(tVRight));
								prob2[5] = pTFromRight0.pMiddle + tURight*(n1.e3*Math.cos(tVRight) + n2.e3*Math.sin(tVRight));
								if (prob2[3]>1 || prob2[4] > 1 || prob2[5] > 1 || prob2[3] < 0 || prob2[4] < 0 || prob2[5] < 0) {
									tVRight += DEG_INCR;
									continue;
									}
								TJunction.setPFromRight(
										prob2[3],
										prob2[4],
										prob2[5]
												);
								
								for (double tUMiddle = 0; tUMiddle < DIST_FROM_EXP;) {
									for (double tVMiddle = 0; tVMiddle <2*Math.PI;) {
										if (tUMiddle == 0.) tVMiddle += 2*Math.PI;
										prob2[6] = pTFromMiddle0.pLeft + tUMiddle*(n1.e1*Math.cos(tVMiddle) + n2.e1*Math.sin(tVMiddle));
										prob2[7] = pTFromMiddle0.pRight + tUMiddle*(n1.e2*Math.cos(tVMiddle) + n2.e2*Math.sin(tVMiddle));
										prob2[8] = pTFromMiddle0.pMiddle + tUMiddle*(n1.e3*Math.cos(tVMiddle) + n2.e3*Math.sin(tVMiddle));
										if (prob2[6] > 1 || prob2[7] > 1 || prob2[8] > 1 || prob2[6] < 0 || prob2[7] < 0 || prob2[8] < 0) {
											tVMiddle += DEG_INCR;
											continue;
											}
										TJunction.setPFromMiddle(
												prob2[6],
												prob2[7],
												prob2[8]
														);
										
										for (double lU = 0; lU < DIST_FROM_EXP;) {
											for (double lV = 0; lV <2*Math.PI;) {
												if (lU == 0.) lV += 2*Math.PI;
												prob2[18] = pL0.e1 + lU*(n1.e1*Math.cos(lV) + n2.e1*Math.sin(lV));
												prob2[19] = pL0.e2 + lU*(n1.e2*Math.cos(lV) + n2.e2*Math.sin(lV));
												prob2[20] = pL0.e3 + lU*(n1.e3*Math.cos(lV) + n2.e3*Math.sin(lV));
												if (prob2[18] > 1 || prob2[19] > 1 || prob2[20] > 1 || prob2[18] < 0 || prob2[19] < 0 || prob2[20] < 0 ) {
													lV += DEG_INCR;//FIXXXXX
													continue;
													}
												LJunction.setProbabilities( 
														prob2[18], 
														prob2[19], 
														prob2[20] 
																); 
												
												//**********************************************************
												//grade and stuff
												this.solve();
												curScore = this.grade(true, false);
												//System.out.println("running");
												if (curScore < prevScore) {
													System.out.println(Math.sqrt(curScore));
													//store the values
													for (int i = 0; i < prob2.length; i++) {
														probBest[i] = prob2[i];
														}
													prevScore = curScore;
													}
												this.resetWeights();
												//***********************************************************
												
												lV += DEG_INCR;
												}
											lU += RADII_INCR;
											}	
										//END L
										
										tVMiddle += DEG_INCR;
										}
									tUMiddle += RADII_INCR;
									}
								tVRight += DEG_INCR;
								}
							tURight += RADII_INCR;
							}
						tVLeft += DEG_INCR;
						}
					tULeft += RADII_INCR;
					}
				
				return probBest;
				
			}
		}

	public double[] loopSweep(int iterations) {
		double[] ans = null;
		for (int i = 0; i<iterations; i++) {
			ans = this.paramSweep();
			TJunction.setPFromLeft( ans[0], ans[1], ans[2] );
			TJunction.setPFromRight( ans[3], ans[4], ans[5] );	
			TJunction.setPFromMiddle( ans[6], ans[7], ans[8] );
			YJunction.setPFromLeft( ans[9], ans[10], ans[11] ); 	
			YJunction.setPFromRight( ans[12], ans[13], ans[14] );
			YJunction.setPFromMiddle( ans[15], ans[16], ans[17] );	
			LJunction.setProbabilities( ans[18], ans[19], ans[20] ); 	
			printProbs(ans);
		}
		return ans;
	}
	
	public static void printProbs(double[] newProbs) {
		String probs = " TL: " + newProbs[0] + " " + newProbs[1] + " " + newProbs[2];
		probs += "\n TR: " + newProbs[3] + " " + newProbs[4] + " " + newProbs[5];
		probs += "\n TM: " + newProbs[6] + " " + newProbs[7] + " " + newProbs[8];
		probs += "\n";
		probs += "\n YL: " + newProbs[9] + " " + newProbs[10] + " " + newProbs[11];
		probs += "\n YR: " + newProbs[12] + " " + newProbs[13] + " " + newProbs[14];
		probs += "\n YM: " + newProbs[15] + " " + newProbs[16] + " " + newProbs[17];
		probs += "\n";
		probs += "\n L: " + newProbs[18] + " " + newProbs[19] + " " + newProbs[20];
		probs += "\n";
		probs += "\n X: " + newProbs[21] + " " + newProbs[22] + " " + newProbs[23] + " " + newProbs[24];

		System.out.println(probs);
	}

	public void resetWeights() {
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].resetEdgeWeights();
			nodes[i].resetNodeWeight();
		}
	}

}
