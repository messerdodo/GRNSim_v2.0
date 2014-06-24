package it.unimib.disco.bimib.Networks;

/**
 * This class is partially random graph implementation.
 * Partially random means that the number of input of each node is controlled.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2014
 */

import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.UtilityRandom;

public class PartiallyRandomGraph extends IncidenceMatrixGraph {
	
	
	/**
	 * Generic constructor: It creates the network from the passed params
	 * @param nodesName: Array with the nodes name
	 * @param edges: ArrayList of the edges. Each edge is represented as a two element array.
	 * @throws NotExistingNodeException
	 */
	public PartiallyRandomGraph(String[] nodesName, int[][] edges) throws NotExistingNodeException{
		super(nodesName, edges, "PartiallyRandom");
	}
	
	/**
	 * Generic constructor
	 * @param n: number of nodes of the network
	 * @param fixedInputsNumber: Specified the (integer) number of input that each node must have.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public PartiallyRandomGraph(int n, int fixedInputsNumber) throws ParamDefinitionException, NotExistingNodeException {
		super(n, "PartiallyRandom");
		//Tests if the fixedInpus number is correct [0;n-1]
		if(fixedInputsNumber < 0 || fixedInputsNumber > n -1)
			throw new ParamDefinitionException("The fixrd input number must be between 0 and " + ((int)(n-1)));
		//Creates the graph with the specified fixed inputs number
		createGraph(n, fixedInputsNumber);
	}
	
	/**
	 * This method creates a fixed input random graph
	 * @param nodes
	 * @param fixedInputsNumber
	 * @throws NotExistingNodeException
	 */
	private void createGraph(int nodes, int fixedInputsNumber) throws NotExistingNodeException{
		int sourceNode;
		//Adds fixedInputsNumber input edges to each node.
		for(int node = 0; node < nodes; node++){
			for(int input = 0; input < fixedInputsNumber; input++){
				do{
					sourceNode = UtilityRandom.randomUniform(0, nodes);
				}while(sourceNode == node || this.areNodesConnected(sourceNode, node));
				this.addEdge(sourceNode, node);
			}
		}
	}

}
