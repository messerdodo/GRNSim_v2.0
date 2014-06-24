package it.unimib.disco.bimib.Networks;
/**
 * This class is a random graph implementation.
 * RandomGraph derived from the IncidentMatrixGraph class
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 */

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.UtilityRandom;


public class RandomGraph extends IncidenceMatrixGraph {

	/**
	 * Default constructor
	 * @param n: number of nodes in the graph
	 */
	public RandomGraph(int n) {
		//Parent constructor invocation
		super(n, "Random");
	}

	/**
	 * Constructor
	 * @param n: number of nodes in the network
	 * @param m: number of edges in the network
	 * @throws ParamDefinitionException: Wrong parameter definition 
	 * @throws NotExistingNodeException: Wrong node 
	 * 
	 */
	public RandomGraph(int n, int m) throws NotExistingNodeException, ParamDefinitionException{
		this(n);
		//Creates the edges following the Erdos Ranyi model
		erdosRenyiGraph(n, m);
	}

	/**
	 * Generic constructor: It creates the network from the passed params
	 * @param nodesName: Array with the nodes name
	 * @param edges: ArrayList of the edges. Each edge is represented as a two element array.
	 * @throws NotExistingNodeException
	 */
	public RandomGraph(String[] nodesName, int[][] edges) throws NotExistingNodeException{
		super(nodesName, edges, "Random");
	}

	/**
	 * This method adds the edges in the network following Erdos Ranyi model.
	 * @param n: number of nodes
	 * @param m: number of edges
	 * @throws NotExistingNodeException: a not existing node has been generated 
	 * 									 during the random selection procedure.
	 * @throws ParamDefinitionException 
	 */
	private void erdosRenyiGraph(int n, int m) throws NotExistingNodeException, ParamDefinitionException{
		int nodeA, nodeB;

		//Checks the maximum edges number
		if(m > n*(n-1)){
			throw new ParamDefinitionException("The number of edges must be smaller than the nodes number squared"); 
		}
		//Adds each edge
		for(int i = 0; i < m; i ++){
			do{
				//Generates the random edge
				nodeA = UtilityRandom.randomUniformChoice(this.getNodes());
				nodeB = UtilityRandom.randomUniformChoice(this.getNodes());
				//If the node already exist it will be changed
			}while(nodeA == nodeB || this.areNodesConnected(nodeA, nodeB)); 
			//Adds the new random edge
			this.addEdge(nodeA, nodeB);
		}
	}


}
