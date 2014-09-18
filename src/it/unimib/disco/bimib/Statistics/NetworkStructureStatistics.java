package it.unimib.disco.bimib.Statistics;
/**
 * BIMIB @ Milan University - Bicocca 
 * 2013
 */

/**
 * This class calculates the statistics of the network's structure
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

//System imports
import java.util.ArrayList;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.NotExistingNodeException;
import it.unimib.disco.bimib.Networks.GraphManager;

public class NetworkStructureStatistics {

	/**
	 * This method calculates the specified network diameter
	 * @param graph: The graph manager
	 * @return: The network diameter
	 * @throws NotExistingNodeException: An error occurred during the calculation.
	 */
	public static int getNetworkDiameter(GraphManager graph) 
			throws NotExistingNodeException, NullPointerException{

		if(graph == null)
			throw new NullPointerException("The graph manager must be not null!");

		int diameter = 0;
		int[][] shortestPathMatrix = createWeightedGraph(graph);

		//Searches the biggest value in the graphMatrix
		for(int line = 0; line < shortestPathMatrix.length; line++){
			for(int column = 0; column < shortestPathMatrix.length; column++){
				if(diameter < shortestPathMatrix[line][column]){
					diameter = shortestPathMatrix[line][column];
				}

			}
		}
		return diameter;
	}
	
	/**
	 * This method allows to create a weighted graph
	 * @param graph
	 * @return
	 * @throws NotExistingNodeException
	 */
	private static int[][] createWeightedGraph(GraphManager graph) throws NotExistingNodeException{

		int[][] graphMatrix = new int[graph.getNodesNumber()][graph.getNodesNumber()];
		int[][] path = new int[graph.getNodesNumber()][graph.getNodesNumber()];

		
		for(int line = 0; line < graphMatrix.length; line++){
			for(int column = 0; column < graphMatrix.length; column++){
				if(line == column){
					graphMatrix[line][column] = 0;
				}
				else{
					if(graph.getGraph().areNodesConnected(line, column)){
						//if the node are connected puts 1 in the matrix 
						//and the following node in the path matrix
						graphMatrix[line][column] = 1;
					}
					else{
						//if the node aren't connected puts 0 in the matrix and -1 in the path
						graphMatrix[line][column] = Integer.MAX_VALUE;
					}
				}
			}
		}
		for (int i = 0; i < graphMatrix.length; i++){
			for (int j = 0; j < graphMatrix.length; j++){
				if (graphMatrix[i][j] != 0 && graphMatrix[i][j] != Integer.MAX_VALUE)
					path[i][j] = i;
				else
					path[i][j] = -1;
			}
		}
		
		//Searches the shortest path with Floyd-Warshall algorithm
		for (int k = 0; k < path.length; k++){
			for (int i = 0; i < path.length; i++){
				for (int j = 0; j < path.length; j++){
					if (graphMatrix[i][k] == Integer.MAX_VALUE || graphMatrix[k][j] == Integer.MAX_VALUE)
						continue;
					if(graphMatrix[i][j] > graphMatrix[i][k] + graphMatrix[k][j]){
						graphMatrix[i][j] = graphMatrix[i][k] + graphMatrix[k][j];
						path[i][j] = path[k][j];
					}
				}
			}
		}
		return path;
	}


	/**
	 * This method returns the average path length
	 * @param graph: The graph manager object
	 * @return: The average path length
	 * @throws NotExistingNodeException
	 */
	public static double getAveragePath(GraphManager graph) throws NotExistingNodeException{
		double sum = 0;
		int[][] shortestPathMatrix = createWeightedGraph(graph);

		//Sums all the value in the shortestPathMatrix
		for(int line = 0; line < shortestPathMatrix.length; line++){
			for(int column = 0; column < shortestPathMatrix.length; column++){
				if(shortestPathMatrix[line][column] != -1)
					sum = sum + shortestPathMatrix[line][column];
			}
		}
		//Calculates the average
		return sum/(shortestPathMatrix.length * shortestPathMatrix.length);
	}


	/**
	 * This method calculates the clustering coefficient of the network
	 * @param graph: The graph manager object
	 * @return: The clustering coefficient of the network
	 * @throws NotExistingNodeException
	 */
	public static double getClusteringCoefficient(GraphManager graph) throws NotExistingNodeException{
		//Parameters checking
		if(graph == null)
			throw new NullPointerException("The graph manager object must be not null");

		ArrayList<Double> clusters = new ArrayList<Double>();

		//Searches all the neighbors of the specified node
		for(int line = 0; line < graph.getNodesNumber(); line++){
			ArrayList<Integer> neighbours = new ArrayList<Integer>();
			int edge = 0;
			for(int column = 0; column < graph.getNodesNumber(); column++){
				if(graph.getGraph().areNodesConnected(line, column)){
					neighbours.add(column);
				}	
			}

			//For every neighbors calculates if there are connected each other
			for(int neighbourA = 0; neighbourA < neighbours.size(); neighbourA++){
				for(int neighbourB = neighbourA + 1; neighbourB < neighbours.size(); neighbourB++){
					if(graph.getGraph().areNodesConnected(neighbours.get(neighbourA), neighbours.get(neighbourB)))
						edge++;
				}
			}

			//Calculates the clustering coefficient for a single node
			double maxEdge = (neighbours.size()*(neighbours.size()-1))/2;
			if(maxEdge == 0)
				clusters.add(0.0);
			else
				clusters.add(edge/maxEdge);
		}

		//Sums all the clustering coefficient
		double sum = 0;
		for(int cluster = 0; cluster < clusters.size(); cluster++){
			sum = sum + clusters.get(cluster);
		}
		//Return the clustering coefficient of the network
		return sum/(double)graph.getNodesNumber();
	}

	/**
	 * This method calculates the network average bias value.
	 * @param graph: The graph manager object
	 * @return: The average bias value.
	 * @throws NotExistingNodeException
	 */
	public static double getAverageBiasValue(GraphManager graph) throws NotExistingNodeException{
		//Parameters checking
		if(graph == null)
			throw new NullPointerException("The graph manager object must be not null");

		ArrayList<Double> functionsBias = new ArrayList<Double>();

		//For every function of the specified node calculates the bias
		for(int node = 0; node < graph.getNodesNumber(); node++){
			double bias = graph.getGraph().getFunction(node).getBias();

			functionsBias.add(bias);
		}
		//Sums all the bias
		double sum = 0;
		for(int bias = 0; bias < functionsBias.size(); bias++){
			sum = sum + functionsBias.get(bias);
		}
		//Returns the average bias
		return sum/(double)graph.getNodesNumber();		
	}
	
	/**
	 * This method returns the assortativity of a graph
	 * @param graph: the specified graph
	 * @return
	 * @throws NotExistingNodeException
	 */
	public static double getAssortativity(GraphManager graph) throws NotExistingNodeException{

		int [] nodes = graph.getGraph().getNodes();
		ArrayList<Integer> newNodes = new ArrayList<Integer>();
		ArrayList<Integer> degreeNotDuplicate = new ArrayList<Integer>();
		int totalEdges = 0;
		boolean [][] connectionMatrix = new boolean[nodes.length][];

		//Calculates the degree of each node
		for(int i = 0; i < nodes.length; i++){
			int degree = graph.getGraph().getNodeDegree(nodes[i]);
			if(!newNodes.contains(degree))
				degreeNotDuplicate.add(degree);
			newNodes.add(degree);
		}
		
		//Verifies if two nodes are connected
		for(int i = 0; i < nodes.length; i++){
			for(int j = 0; j < nodes.length; j++){
				//If are connected each other decrease the numbers of degree 
				if(graph.getGraph().areNodesConnected(i, j) && graph.getGraph().areNodesConnected(j, i)){
					totalEdges++;
					int deg = newNodes.get(i) -1;
					newNodes.remove(i);
					newNodes.add(i, deg);
					connectionMatrix[i][j] = true;
				}
				else if(graph.getGraph().areNodesConnected(i, j)){
					totalEdges++;
					connectionMatrix[i][j] = true;
				}

			}
		}

		int a[] = new int[degreeNotDuplicate.size()];
		int e[] = new int[degreeNotDuplicate.size()];
		
		//Calculates if nodes have the same degree
		for(int i = 0; i < degreeNotDuplicate.size(); i++){
			int A = degreeNotDuplicate.get(i);
			for(int k = 0; k < nodes.length; k++){
				if(newNodes.get(k) == A){
					for(int j = 0; j < nodes.length; j++){
						//Verifies if the two nodes considered are connected each other
						if(connectionMatrix[k][j] == true){
							//Increases the numbers of connection exiting to the i-th nodes
							a[i] = a[i] +1;
							if(newNodes.get(j) == A){
								connectionMatrix[j][k] = false;
								e[i] = e[i] +1;
							}
						}	
					}
				}
			}
		}
		int sume = 0;
		int suma = 0;
		for(int i = 0; i < e.length; i++){
			a[i] = a[i]*a[i];
		}

		for(int i = 0; i < e.length; i++){
			sume = sume + e[i];
			suma = suma + a[i];
		}

		double num = (sume/totalEdges) - (suma/Math.pow(totalEdges, 2));
		double den = 1 - (suma/Math.pow(totalEdges, 2));
		return num/den;

	}
}
