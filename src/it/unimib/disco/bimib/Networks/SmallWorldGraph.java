package it.unimib.disco.bimib.Networks;
/**
 * This class is a Small World graph implementation
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */

//System imports
import java.util.List;
//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.UtilityRandom;

public class SmallWorldGraph extends IncidenceMatrixGraph{


	/**
	 * Default constructor
	 * @param n: number of nodes
	 */

	public SmallWorldGraph(int n) {
		//Nodes generation
		super(n, "SmallWorld");
		//Introduces a sort of confusion in the nodes in order to randomly distributes the hubs
		this.nodesPermutation();
	}

	/**
	 * Constructor, this adds the edges in the network following Strogatz model.
	 * @param n: number of nodes
	 * @param k: average connectivity parameter
	 * @param beta: probability
	 * @throws NotExistingNodeException: An error occurred during the edges definition
	 * @throws ParamDefinitionException: One or more wrong parameter  
	 */
	public SmallWorldGraph(int n, int k, double beta) throws NotExistingNodeException, ParamDefinitionException{
		//Nodes generation
		this(n); 

		//Checks if the variables are acceptable
		if(beta > 1 || beta < 0 )
			throw new ParamDefinitionException("the value for variable beta is incorrect");
		if(n < 0 )
			throw new ParamDefinitionException("the value for variable n is incorrect");
		if( k <= 0 || k > n)
			throw new ParamDefinitionException("the value for variable k is incorrect");
		// For each node creates edge with the nearest neighbour's node
		for(int nodeA = 0; nodeA < n-1; nodeA++ ){
			//Search a neighbour's node in the first half
			for(int h = 1; h <= k/2; h++){
				int nodeB;
				//Looking for a new node
				if( ( nodeA - h) < 0)
					nodeB = (nodeA - h) + n;
				else
					nodeB = (nodeA - h);

				//Creates a new edge between nodeA and NobeB
				if( 0 < Math.abs(nodeA-nodeB) % (n - k/2) && Math.abs(nodeA-nodeB) % (n - k/2) <= k/2)
					this.addEdge(nodeA, nodeB);
				//Looking for a new node in the second half
				if( ( nodeA + h) >= n) 
					nodeB = (nodeA + h) - n;
				else
					nodeB = (nodeA + h);

				//Creates a new edge between nodeA and NobeB
				if( 0 < Math.abs(nodeA-nodeB) % (n - k/2) && Math.abs(nodeA-nodeB) % (n - k/2) <= k/2)
					this.addEdge(nodeA, nodeB);


			}
		}

		//Randomly rewires each edge of the network with probability beta
		int nodeK;
		List<int[]> edges = this.getEdges();
		for(int[] edge : edges){
			if(UtilityRandom.randomBooleanChoice(beta) == true){
				do{ 
					nodeK = UtilityRandom.randomUniformChoice(this.getNodes());
				}while(edge[0] == nodeK || edge[1] == nodeK || this.areNodesConnected(edge[0], nodeK));
				//If the new node respects all the condition is creates a new edge 
				//between the node specified and the new node and the old edge is removed
				this.removeEdge(edge[0],edge[1]);
				this.addEdge(edge[0], nodeK);
			}	
		}

	}

	/**
	 * Generic constructor: It creates the network from the passed params
	 * @param nodesName: Array with the nodes name
	 * @param edges: ArrayList of the edges. Each edge is represented as a two element array.
	 * @throws NotExistingNodeException
	 */
	public SmallWorldGraph(String[] nodesName, int[][] edges) throws NotExistingNodeException{
		super(nodesName, edges, "SmallWorld");
	}

}



