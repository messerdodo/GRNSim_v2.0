/**
 * BIMIB @ Milan University - Bicocca 
 * 2013
 */

/**
 * This class is an abstract class that implements the GeneReulatoryNetwork 
 * interface. This implementation uses the incidence matrix for the graph 
 * representation.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

package it.unimib.disco.bimib.Networks;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Functions.Function;
import it.unimib.disco.bimib.Utility.UtilityRandom;

//System imports
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public abstract class IncidenceMatrixGraph implements GeneRegulatoryNetwork{

	private final int SOURCE_NODE = 0;
	private final int DESTINATION_NODE = 1;

	private int[][] incidenceMatrix;
	private int[] nodes;
	private String[] nodesName;
	private int nodesNumber;

	private String topology;

	//Gene changing state functions 
	private Function[] genesStateFunctions;

	/**
	 * This method creates a new Graph with the incidence matrix representation
	 * @param n is the graph nodes number.
	 */
	public IncidenceMatrixGraph(int n, String topology){

		this.nodesNumber = n;
		this.topology = topology;

		this.incidenceMatrix = new int[n][n];
		this.nodes = new int[n];

		//Node names initialization: Mnemonic code
		this.nodesName = new String[n];
		for(int i = 0; i < n; i++)
			this.nodesName[i] = "gene_" + i;

		//Initializes the genes changing state functions
		this.genesStateFunctions = new Function[n];

		//Nodes initialization (integer coding)
		for(int i = 0; i < n; i++){
			this.nodes[i] = i;
		}

		//Incidence matrix initialization: No edges
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				this.incidenceMatrix[i][j] = 0;
			}
		}
	}

	/**
	 * Generic constructor: It creates the network from the passed parameters
	 * @param nodesName: Array with the nodes name
	 * @param edges: ArrayList of the edges. Each edge is represented as a two element array.
	 * @param type: Network topology type
	 * @throws NotExistingNodeException
	 */
	public IncidenceMatrixGraph(String[] nodesName, int[][] edges, String type) throws NotExistingNodeException{
		//Creates the unconnected network
		this(nodesName.length, type);
		
		//Sets the nodes name
		for(int i = 0; i < nodesName.length; i++)
			this.setNodeName(i, nodesName[i]);
		
		//Adds the edges
		for(int[] edge : edges){
			this.addEdge(edge[SOURCE_NODE], edge[DESTINATION_NODE]);
		}
	}

	@Override
	/**
	 * This method return the array with all the nodes of the graph.
	 * @return The set of nodes
	 */
	public int[] getNodes() {
		return this.nodes;
	}

	@Override
	/**
	 * This method returns a list with the names of nodes of the graph
	 * @return nodes' names
	 */
	public String[] getNodesNames(){
		return this.nodesName;
	}

	@Override
	/**
	 * This method return the list of the edges of the graph. Each returned edge is 
	 * an array. The edge direction is edge[0]-edge[1]
	 * @return The list of the edges
	 */
	public List<int[]> getEdges() {
		ArrayList<int[]> edges = new ArrayList<int[]>();
		int edge[];
		//Retrieves the graph's edges 
		for(int i = 0; i < this.nodesNumber; i++){
			for(int j = 0; j < this.nodesNumber; j++){
				if(this.incidenceMatrix[i][j] == 1){
					//i-j directed edge
					edge = new int[2];
					edge[0] = i;	//Source
					edge[1] = j;	//Target
					edges.add(edge);
				}
			}
		}
		return edges;
	}


	/**
	 * This method returns a ArrayList with all the neighboring node starting from the specified node
	 * @param node
	 * @return ArrayList(node)
	 * @throws NotExistingNodeException: node doesn't exist
	 * 
	 */
	public ArrayList<Integer> getOutcomingNodes(int node) throws NotExistingNodeException{
		ArrayList<Integer> outcomingNodes = new ArrayList<Integer>();
		//Checks if the specified node exists
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + node +" doesn't exist");
		
		//For each node checks if it is connected to node.
		for(int i= 0; i < this.nodesNumber; i++){
			if(this.incidenceMatrix[node][i] == 1){
				outcomingNodes.add(i);
			}
		}
		return outcomingNodes;
	}
	
	/**
	 * This method returns ArrayList with all the incoming node of the specified node
	 * @param node: the specified node
	 * @return ArrayList(node)
	 * @throws NotExistingNodeException: node doesn't exist
	 */
	public ArrayList<Integer> getIncomingNodes(int node) throws NotExistingNodeException{
		ArrayList<Integer> incomingNodes = new ArrayList<Integer>();

		//Checks if the specified node exists
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + node +" doesn't exist " + this.nodesNumber);

		//For each node checks if it is connected to node.
		for(int i= 0; i < this.nodesNumber; i++){
			if(this.incidenceMatrix[i][node] == 1){
				incomingNodes.add(i);
			}
		}
		return incomingNodes;
	}


	/**
	 * This method details if the two specified nodes are connected (to each other)
	 * @param nodeA: the source node
	 * @param nodeB: the target node
	 * @return true if there is an edge between nodeA and nodeB, false otherwise.
	 * @throws NotExistingNodeException: nodeA or nodeB doesn't exist
	 * 
	 */
	public boolean areNodesConnected(int nodeA, int nodeB) throws NotExistingNodeException{

		//Checks if nodeA and nodeB exist.
		if(nodeA < 0 || nodeA >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + nodeA + " doesn't exist!");
		if(nodeB < 0 || nodeB >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + nodeB + " doesn't exist!");
		// == 1 connected == 0 not connected
		return this.incidenceMatrix[nodeA][nodeB] == 1;
	}


	/**
	 * This method creates an edge between the two specified nodes
	 * @param nodeA: source node
	 * @param nodeB: target node
	 * @throws NotExistingNodeException: nodeA or nodeB doesn't exist
	 */
	public void addEdge(int nodeA, int nodeB) throws NotExistingNodeException{

		//Checks if nodeA and nodeB exist.
		if(nodeA < 0 || nodeA >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + nodeA + " doesn't exist!");
		if(nodeB < 0 || nodeB >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + nodeB + " doesn't exist!");

		this.incidenceMatrix[nodeA][nodeB] = 1;
	}

	/**
	 * This method removes an edge between the two specified nodes
	 * @param nodeA: source node
	 * @param nodeB: target node
	 * @throws NotExistingNodeException: nodeA or nodeB doesn't exist
	 */
	public void removeEdge(int nodeA, int nodeB) throws NotExistingNodeException{

		//Checks if nodeA and nodeB exist.
		if(nodeA < 0 || nodeA >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + nodeA + " doesn't exist!");
		if(nodeB < 0 || nodeB >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + nodeB + " doesn't exist!");
		// == 0: edge removed.
		this.incidenceMatrix[nodeA][nodeB] = 0;
	}

	/**
	 * This function returns the number of the nodes in the graph
	 * @return : the number of the nodes in the graph
	 */
	public int getNodesNumber(){
		return this.nodesNumber;
	}

	/**
	 * This method sets the name to the specified node
	 * @param node: The number of the node
	 * @param nodeName: The name of the node to be added
	 * @throws NotExistingNodeException: The node does not exist
	 */
	public void setNodeName(int node, String nodeName) throws NotExistingNodeException{
		if(node < 0 || node >= nodesNumber)
			throw new NotExistingNodeException("gene_" + node + " doesn't exist!"); 	
		this.nodesName[node] = nodeName;
	}

	/**
	 * This method returns the name of the specified node
	 * @param node: The node number
	 * @return The name of the specified node
	 * @throws NotExistingNodeException: The node does not exist
	 */
	public String getNodeName(int node) throws NotExistingNodeException{
		if(node < 0 || node >= nodesNumber)
			throw new NotExistingNodeException("gene_" + node + " doesn't exist!");
		return this.nodesName[node];
	}

	/**
	 * This method returns the node number, given its name. 
	 * The node number will be -1 if the node doen't exist.
	 * @param nodeName: The name of the node
	 * @return The number of the node in the graph.
	 */
	public int getNodeNumber(String nodeName){

		for(int i = 0; i < this.nodesName.length; i++)
			if(this.nodesName[i].equals(nodeName))
				return i;

		return -1;
	}
	
	/**
	 * This function returns the degree of all its incoming and outcoming node
	 *  @return : the degree of the specified node
	 */
	public int getNodeDegree(int node) throws NotExistingNodeException{
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + node + " doesn't exist!"); 
		//Degree = incoming + outcoming
		return this.getNodeIncomingDegree(node) + this.getNodeOutcomingDegree(node);
	}

	/**
	 * This function returns the incoming degree of the specified node
	 *  @return : the incoming degree of the specified node
	 */
	public int getNodeIncomingDegree(int node) throws NotExistingNodeException{
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + node + " doesn't exist!"); 
		return this.getIncomingNodes(node).size();
	}
	
	/**
	 * This function returns the outcoming degree of the specified node
	 *  @return : the outcoming degree of the specified node
	 */
	public int getNodeOutcomingDegree(int node) throws NotExistingNodeException{
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("gene_" + node + " doesn't exist!"); 
		return this.getOutcomingNodes(node).size();
	}
	
	/**
	 * This functions returns the degree of the network
	 * @return : the degree of all the nodes
	 */
	public int getTotalDegree(){
		int totalDegree = 0;
		for(int i = 0; i < this.nodesNumber; i++){
			for(int j = 0; j < this.nodesNumber; j++){
				if(this.incidenceMatrix[i][j] == 1)
					totalDegree += 1;
			}
		}
		return 2 * totalDegree;
	}

	/**
	 * This method adds newEdges random edges to the graph
	 * @param newEdges: the number of new edges to be added.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void addRandomEdges(int newEdges, ArrayList<String> noSource, ArrayList<String> noTarget) throws ParamDefinitionException, NotExistingNodeException{
		int maxNewEdges = (nodesNumber * (nodesNumber - 1)) - this.getTotalDegree()/2;
		int sourceNode, targetNode;
		//Parameters checking
		if(newEdges < 0 || newEdges >  maxNewEdges)
			throw new ParamDefinitionException("The number of new edges must be between 0 and " +  maxNewEdges);
		//Adds the new edges
		for(int i = 0; i < newEdges; i++){
			//Selects the new random edge and checks that they'll not be the same.
			do{
				//Checks that the selected node can be a source node.
				do{
					sourceNode = UtilityRandom.randomUniform(0, nodesNumber);
				}while(noSource != null && noSource.contains(String.valueOf(sourceNode)));
				//Checks that the selected node can be a target node.
				do{
					targetNode = UtilityRandom.randomUniform(0, nodesNumber);
				}while(noTarget != null && noTarget.contains(String.valueOf(targetNode)));
				
			}while(sourceNode == targetNode || this.areNodesConnected(sourceNode, targetNode));
			//Adds the edge
			this.addEdge(sourceNode, targetNode);
			//Adds the new input to the function. Only if it exist.
			if(this.genesStateFunctions[targetNode] != null)
				this.genesStateFunctions[targetNode].addLink(sourceNode);
		}
	}

	/**
	 * This method adds newEdges random edges to the graph
	 * @param newEdges: the number of new edges to be added.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void addRandomEdges(int newEdges, ArrayList<String> noSource, ArrayList<String> noTarget, int maxInput) throws ParamDefinitionException, NotExistingNodeException{
		int maxNewEdges = (nodesNumber * maxInput) - this.getTotalDegree()/2;
		int sourceNode, targetNode;
		//Parameters checking
		if(newEdges < 0 || newEdges >  maxNewEdges)
			throw new ParamDefinitionException("The number of new edges must be between 0 and " +  maxNewEdges);
		//Adds the new edges
		for(int i = 0; i < newEdges; i++){
			//Selects the new random edge and checks that they'll not be the same.
			do{
				//Checks that the selected node can be a source node.
				do{
					sourceNode = UtilityRandom.randomUniform(0, nodesNumber);
				}while(noSource != null && noSource.contains(String.valueOf(sourceNode)));
				//Checks that the selected node can be a target node.
				do{
					targetNode = UtilityRandom.randomUniform(0, nodesNumber);
				}while(noTarget != null && noTarget.contains(String.valueOf(targetNode)));
				
			}while(sourceNode == targetNode || this.areNodesConnected(sourceNode, targetNode) || this.getNodeIncomingDegree(targetNode) == maxInput);
			//Adds the edge
			this.addEdge(sourceNode, targetNode);
			//Adds the new input to the function. Only if it exist.
			if(this.genesStateFunctions[targetNode] != null)
				this.genesStateFunctions[targetNode].addLink(sourceNode);
		}
	}
	
	/**
	 * This method adds a function to a node.
	 * @param node: the node in which the function will be added
	 * @param function: The function which will be added
	 * @throws NotExistingNodeException: The specified node doesn't exist
	 */
	public void addFunction(int node, Function function) throws NotExistingNodeException{
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("Wrong node! gene_" + node + " doesn't exist");
		//Adds the function to the node
		this.genesStateFunctions[node] = function;

	}

	/**
	 * This method adds a set of function to the graph (One function for each node).
	 * @param functions: the array of functions that will be added to the nodes. 
	 * The number of the functions and the number of nodes must be the same.
	 * @throws ParamDefinitionException: functions number and nodes number don't be the same. 
	 */
	public void addFunctions(Function[] functions) throws ParamDefinitionException{
		//Checks if there is one function for each node
		if(functions.length != this.nodesNumber)
			throw new ParamDefinitionException("Wrong number of function added! Functions must be " + this.nodesNumber);
		//Adds all the functions
		this.genesStateFunctions = functions;
	}

	/**
	 * This method returns the function associated to the specified node.
	 * @param node: The numeric name of the node.
	 * @return function: The node associated function.
	 * @throws NotExistingNodeException: The specified node doesn't exist.
	 */
	public Function getFunction(int node) throws NotExistingNodeException{
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("The node " + node + " doesn't exist! Nodes are in [0, " + this.nodesNumber + "]");
		return this.genesStateFunctions[node];
	}

	/**
	 * This method returns the function value for a specified node with a specified input.
	 * @param node: The node name.
	 * @param input: The function input (One input for each incoming edge)
	 * @return function: The node associated function.
	 * @throws NotExistingNodeException: The specified node doesn't exist.
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException: The input type or the input dimension isn't correct
	 */
	public Object evalFunction(int node, Object[] inputs) throws NotExistingNodeException, InputTypeException, ParamDefinitionException{
		if(node < 0 || node >= this.nodesNumber)
			throw new NotExistingNodeException("The node " + node + " doesn't exist! Nodes are in [0, " + this.nodesNumber + "]");
		//The eval method may lift up a InputTypeException or a ParamDefinitionException
		return this.genesStateFunctions[node].eval(inputs);
	}

	/**
	 * This method prints the incident matrix.
	 */
	public void printGraph(){
		for(int i = 0; i < this.nodesNumber; i++){
			for(int j = 0; j < this.nodesNumber; j++){
				System.out.print(this.incidenceMatrix[i][j] + " ");
			}
			System.out.println("\n");
		}
	}

	/**
	 * This methods performs a permutation to the graph nodes in order to introduce a sort of confusion in the nodes
	 */
	protected void nodesPermutation(){
		int changingNode;
		int swap;
		//For each node performs the swapping
		for(int i = 0; i < this.nodesNumber; i++){
			//Selects a node for the swapping
			changingNode = UtilityRandom.randomUniformChoice(this.nodes);
			for(int j = 0; j < this.nodesNumber; j++){
				//Swaps the rows
				swap = this.incidenceMatrix[i][j];
				this.incidenceMatrix[i][j] = this.incidenceMatrix[changingNode][j];
				this.incidenceMatrix[changingNode][j] = swap;
			}
			for(int j = 0; j < this.nodesNumber; j++){
				//Swaps the columns
				swap = this.incidenceMatrix[j][i];
				this.incidenceMatrix[j][i] = this.incidenceMatrix[j][changingNode];
				this.incidenceMatrix[j][changingNode] = swap;
			}
		}
	}

	/**
	 * This method returns the graph as a GRNML string in order to store it in a file.
	 * @return A GRNML string representing the graph.
	 */
	public String toGRNML(){
		String grnml = "<graph topology = \""+ this.topology +"\" nodes_number = \"" + this.nodesNumber + "\">\n";
		//Nodes
		for(int i = 0; i < nodes.length; i++){
			grnml += "\t\t<node id = \"" + nodes[i] +"\" name = \"" + nodesName[i] + "\" >\n";
			//Function
			grnml += genesStateFunctions[i].toGRNML();
			grnml += "\t\t</node>\n";
		}

		//Writes the edges
		for(int i = 0; i < this.nodesNumber; i++){
			for(int j = 0; j < this.nodesNumber; j++){
				if(this.incidenceMatrix[i][j] > 0){
					grnml += "\t\t\t<edge source = \"" + i + "\" destination = \"" + j + "\"> </edge>\n";
				}
			}
		}

		grnml += "</graph>";
		return grnml;
	}

	/**
	 * This method copies a GeneRegulatoryNetwork object
	 * @return a copy of the object
	 * @throws ParamDefinitionException 
	 */
	public GeneRegulatoryNetwork copy() throws ParamDefinitionException{
		IncidenceMatrixGraph newGraph = new RandomGraph(incidenceMatrix.length);
		newGraph.incidenceMatrix = new int[this.nodesNumber][this.nodesNumber];
		newGraph.genesStateFunctions = new Function[this.nodesNumber];
		newGraph.nodes = new int[this.nodesNumber];
		newGraph.nodesName = new String[this.nodesNumber];

		for(int i = 0; i < this.nodesNumber; i++){
			if(this.genesStateFunctions[i] == null)
				newGraph.genesStateFunctions[i] = null;
			else
				newGraph.genesStateFunctions[i] = this.genesStateFunctions[i].copy();
			newGraph.nodes[i] = this.nodes[i];
			newGraph.nodesName[i] = this.nodesName[i];
			for(int j = 0; j < this.nodesNumber; j++)
				newGraph.incidenceMatrix[i][j] = this.incidenceMatrix[i][j];
		}

		newGraph.nodesNumber = this.nodesNumber;
		newGraph.topology = this.topology;
		return newGraph;
	}
	
	/**
	 * This method adds a specified number of nodes in the network
	 * @param nodesNumber: The number of the nodes to be added
	 */
	public void addNodes(int nodesNumber){
		int newNodesNumber = this.nodesNumber + nodesNumber;
		int[][] newIncidenceMatrix = new int[newNodesNumber][newNodesNumber];
		int[] newNodes = new int[newNodesNumber];
		String[] newNodesNames = new String[newNodesNumber];
		Function[] newGenesStateFunctions = new Function[newNodesNumber];
		
		//Copies the old values in the new incident matrix
		for(int i = 0; i < this.nodesNumber; i++){
			newNodes[i] = this.nodes[i];
			newNodesNames[i] = this.nodesName[i];
			newGenesStateFunctions[i] = this.genesStateFunctions[i];
			for(int j = 0; j < this.nodesNumber; j++){
				newIncidenceMatrix[i][j] = this.incidenceMatrix[i][j];
			}
		}
		
		//Initializes the incidence matrix for the new nodes
		for(int i = 0; i < newNodesNumber; i++){
			for(int j = (i < this.nodesNumber ? this.nodesNumber : 0); j < newNodesNumber; j++){
				newIncidenceMatrix[i][j] = 0;
			}
		}
		
		for(int i = this.nodesNumber; i < newNodesNumber; i++){
			newNodes[i] = i;
			newNodesNames[i] = "gene_" + i;
		}
		
		//Sets the new values
		this.nodesNumber = newNodesNumber;
		this.incidenceMatrix = newIncidenceMatrix;
		this.nodes = newNodes;
		this.nodesName = newNodesNames;
		this.genesStateFunctions = newGenesStateFunctions;
		
	}
	
	/**
	 * This method selects randomly a node edge and sets the new node as the source.
	 * @throws NotExistingNodeException 
	 */
	public void changeRandomlyEdgeSource(int node, int newNode) throws NotExistingNodeException{
		ArrayList<Integer> targetNodes = new ArrayList<Integer>();
		for(int target = 0; target < this.incidenceMatrix.length; target ++){
			if(this.incidenceMatrix[node][target] == 1)
				targetNodes.add(target);
		}
		
		int target;
		do{
			target = UtilityRandom.randomUniformChoice(targetNodes);
		}while(this.incidenceMatrix[newNode][target] != 1);
		this.removeEdge(node, target);
		this.addEdge(newNode, target);
	}
	
	@Override
	/**
	 * toString method
	 */
	public String toString() {

		String returnedString = "G = (" + Arrays.toString(getNodes()) + ", ";
		List<int[]> a =  this.getEdges();
		for(int i = 0; i < a.size()-1; i++){
			returnedString += " (" + a.get(i)[0] + ", " + a.get(i)[1] + "), ";
		}

		if(a.size() != 0){
			returnedString += " (" + a.get(a.size()-1)[0] + ", " + a.get(a.size()-1)[1] + "))";
		}else{
			returnedString += "{})";
		}
		return returnedString;
	}
	
}

