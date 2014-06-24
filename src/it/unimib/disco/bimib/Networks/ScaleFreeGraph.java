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

//System imports
import java.util.ArrayList;
//GRNSim imports
import it.unimib.disco.bimib.Utility.UtilityRandom;
import it.unimib.disco.bimib.Exceptions.*;



public class ScaleFreeGraph extends IncidenceMatrixGraph{
	/**
	 * Default constructor
	 * @param n: number of nodes in the graph
	 */
	public ScaleFreeGraph(int n){
		super(n, "ScaleFree");	
	}

	/**
	 * The constructor initializes the values for the powerLaw method
	 * @param n: numbers of nodes
	 * @param gamma : probability
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public ScaleFreeGraph(int n,double gamma) throws NotExistingNodeException, ParamDefinitionException{
		this(n);
		powerLawGraph(n, gamma);
		//Introduces a sort of confusion in the nodes in order to randomly distributes the hubs
		this.nodesPermutation();

	}
	
	public ScaleFreeGraph(int n, double gamma, int fixedInput) throws ParamDefinitionException, NotExistingNodeException{
		this(n);
		fixedPowerLawGraph(n, fixedInput, gamma);
		this.nodesPermutation();
	}

	/**
	 * The constructor initializes the values for the barabasi method
	 * @param n : numbers of total nodes
	 * @param ni : numbers of initial nodes
	 * @param k : connectivity
	 * @throws NotExistingNodeException
	 * @throws ParamDefinitionException
	 */
	public ScaleFreeGraph(int ni, int n, int k) throws NotExistingNodeException, ParamDefinitionException{
		this(n);
		barabasiGraph(ni, n ,k);
		//Introduces a sort of confusion in the nodes in order to randomly distributes the hubs
		this.nodesPermutation();
	}

	/**
	 * Generic constructor: It creates the network from the passed params
	 * @param nodesName: Array with the nodes name
	 * @param edges: ArrayList of the edges. Each edge is represented as a two element array.
	 * @throws NotExistingNodeException
	 */
	public ScaleFreeGraph(String[] nodesName, int[][] edges) throws NotExistingNodeException{
		super(nodesName, edges, "ScaleFree");
	}

	/**
	 * This method adds the edges in the network following Power Law model.
	 * @param n: number of nodes
	 * @param gamma: probability
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 */
	private void powerLawGraph(int n, double gamma) throws NotExistingNodeException, ParamDefinitionException{
		//Checks if the variable n or gamma is acceptable
		if(n <= 0 || gamma <= 0){
			throw new ParamDefinitionException("the value for variable n or gamma is incorrect");
		}
		//Initializes the connectivity and the variable total that is 
		//the summation from 1 to n-1 of the node's degree
		int k;//, total = 0;
		double[] pk;
		
		double y = 0;
		for(int degree = 1; degree < n; degree++){
			y = y + Math.pow(degree, -gamma);
		}
		
		//Calculate all the pk probabilities	
		pk = new double[n];
		for(int degree = 1; degree < n; degree++){
			pk[degree] = Math.pow(degree, -gamma)/y;
		}
		
		for(int nodeA = 0; nodeA < n; nodeA++){
			//Calculates the value for the connectivity  
			k = UtilityRandom.randomIntegerDiscreteDistribuitedChoice(pk);

			//Calculates a new node with the RandomUniformChioce
			for(int i=0; i < k; i++){
				int nodeB;
				do{
					nodeB = UtilityRandom.randomUniformChoice(this.getNodes());
				}while(this.areNodesConnected(nodeA, nodeB) || (nodeA == nodeB));
				//Creates a new edge between the two nodes
				this.addEdge(nodeA, nodeB);
			}
		}
	}

	/**
	 * This method adds the edges in the network following Barabasi model.
	 * @param ni: number of initial nodes
	 * @param nt: total nodes
	 * @param k: connectivity
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 */
	private void barabasiGraph(int ni, int nt, int k) throws NotExistingNodeException, ParamDefinitionException{
		if(ni > nt || k > nt || k >= ni){
			throw new ParamDefinitionException("the initial node or the conncetivity are incorrect");
		}
		double[] probabilities;

		//Initial connections
		for(int nodeA = 0; nodeA < ni; nodeA++){
			for(int nodeB = 0; nodeB < ni; nodeB++){
				if(nodeA != nodeB)
					this.addEdge(nodeA, nodeB);
			}
		}

		//Generates nt-ni new nodes 
		for(int nodeA = ni; nodeA < nt; nodeA++){
			int nodeB;
			//The connection probability array
			probabilities = new double[nodeA];
			//Calculates all the pi probabilities (from node 0 to node nodeA-1)
			int totalDegree = this.getTotalDegree();
			for(int i = 0; i < nodeA; i++){
				probabilities[i] = (double)this.getNodeDegree(i)/(double)totalDegree;
			}

			//For each edges is generated a probability from the connectivity 
			for(int edge = 0; edge < k; edge++){
				do{
					nodeB = UtilityRandom.randomIntegerDiscreteDistribuitedChoice(probabilities);
				}while(this.areNodesConnected(nodeA, nodeB) || (nodeA == nodeB));
				//Creates a new edge between the two nodes
				this.addEdge(nodeA, nodeB);
			}
		}
	}                                                                                                                                                            
	
	/**
	 * This method creates a Scale Free graph with the Power Law method with
	 * a fixed ingoing edges.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void fixedPowerLawGraph(int n, int fixedInput, double gamma) throws ParamDefinitionException, NotExistingNodeException{
		//Checks if the variable n or gamma is acceptable
		if(n <= 0 || gamma <= 0 || fixedInput < 1 || fixedInput > n - 1){
			throw new ParamDefinitionException("the value for variable n or gamma is incorrect");
		}

		//Gets the Power Law distribuction
		double[] distribuction = this.powerLawDistribution(n-1, gamma);

		int maximumEdges = n * fixedInput;
		int addedEdges = 0;
		int nodesWithKConnections;
		int k = 1;
		int focusedNode = 0;
		int targetNode, sourceNode;
		int node = 0;

		//Sets the available nodes. Available nodes are all the nodes with less than 2 inputs.
		ArrayList<Integer> availableNodes = new ArrayList<Integer>();
		for(Integer i : this.getNodes())
			availableNodes.add(i);

		while(addedEdges <= maximumEdges && focusedNode < n && k < n){
			nodesWithKConnections = (int) Math.ceil(n * distribuction[k]);
			//Adds the outgoing edges to all nodes with k connections until is possible
			node = 0;
			while(focusedNode < n && node < nodesWithKConnections && (k + addedEdges <= maximumEdges)){
				//Chooses randomly the k target nodes which can have another link
				//(node incoming degree less than fixedInput value)
				for(int con = 0; con < k; con++){
					do{
						targetNode = UtilityRandom.randomUniformChoice(availableNodes);
						//The selection must be repeated until the target node has already fixedNodes input and the edge already exist
					}while(targetNode == focusedNode || this.areNodesConnected(focusedNode, targetNode));
					//Adds the edge
					this.addEdge(focusedNode, targetNode);

					//Checks if the node is full. A node is full if it has already fixedInput inputs.
					if(this.getNodeIncomingDegree(targetNode) == fixedInput)
						availableNodes.remove((Integer)targetNode);
				}
				node = node + 1;
				addedEdges = addedEdges + k;
				//Increments the focused node
				focusedNode = focusedNode + 1;
			}
			//Increments the connectivity.
			k = k + 1;
		}

		//Adds the remaining edges to the less degree nodes.
		while(focusedNode < n && addedEdges < maximumEdges){
			do{
				targetNode = UtilityRandom.randomUniformChoice(availableNodes);
				//The selection must be repeated until the target node has already fixedNodes input and the edge already exist
			}while(targetNode == focusedNode || this.areNodesConnected(focusedNode, targetNode));
			//Adds the edge
			this.addEdge(focusedNode, targetNode);
			addedEdges = addedEdges + 1;
			focusedNode = focusedNode + 1;
		}

		//Adds the remaining edges randomly 
		while(addedEdges < maximumEdges){
			do{
				sourceNode = UtilityRandom.randomUniformChoice(this.getNodes());
				targetNode = UtilityRandom.randomUniformChoice(availableNodes);
				
				//The selection must be repeated until the target node has already fixedNodes input and the edge already exist
			}while(targetNode == sourceNode || this.areNodesConnected(sourceNode, targetNode) );
			//Adds the edge
			this.addEdge(sourceNode, targetNode);
			
			//Checks if the node is full. A node is full if it has already fixedInput inputs.
			if(this.getNodeIncomingDegree(targetNode) == fixedInput)
				availableNodes.remove((Integer)targetNode);
			
			addedEdges = addedEdges + 1;
		}

		//Changes some edges in order to give at least one outcoming edge to each node.
		while(focusedNode < n){

			//Gets a random node in order to take its edge
			do{
				targetNode = UtilityRandom.randomUniformChoice(this.getNodes());
				//The selection must be repeated until the target node has already fixedNodes input and the edge already exist
			}while(targetNode == focusedNode || this.areNodesConnected(focusedNode, targetNode) || this.getNodeOutcomingDegree(targetNode) <= 1);

			//Removes one edge to targetnode
			this.changeRandomlyEdgeSource(targetNode, focusedNode);
			focusedNode = focusedNode + 1;
		}

	}

	/**
	 * This function performs the Power Law distribution.
	 * @param maxK: the maximum connections allowed
	 * @param gamma: The gamma value
	 * @return the Power Law distribution
	 */
	private double[] powerLawDistribution(int maxK, double gamma){
		double[] distribuction = new double[maxK + 1];
		
		//Calculates the zeta function value
		double zeta = zeta(maxK, gamma);
		distribuction[0] = 0;
		for(int k = 1; k <= maxK; k++)
			distribuction[k] = 1.0/zeta * Math.pow(k, -gamma); 

		return distribuction;
	}
	
	/**
	 * This function calculates the Riemann zeta function
	 * @param maxK: the maximum connections allowed
	 * @param gamma: The gamma value
	 * @return the zeta function
	 */
	private double zeta(int maxK, double gamma){
		double zeta = 0;
		for(int k = 1; k < maxK; k++){
			zeta = zeta + Math.pow(k, -gamma);
		}
		return zeta;
	}
}

