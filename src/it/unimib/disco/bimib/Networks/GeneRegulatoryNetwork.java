package it.unimib.disco.bimib.Networks;

/**
 * This class is the GeneRegulatoryNetwork interface that 
 * specifies all the methods that the network must have.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * 
 */

//System imports
import java.util.ArrayList;
import java.util.List;



public interface GeneRegulatoryNetwork {

	/**
	 * This method returns a list with the nodes of the graph
	 * @return [node]
	 */
	public int[] getNodes();
	
	/**
	 * This method returns a list with the nodes of the graph names
	 * @return [node]
	 */
	public String[] getNodesNames();

	/**
	 * This method returns a list with the edges of the graph. 
	 * Each edge is represented as an two-item array. The first position is the source 
	 * and the second position is the target of the edge.
	 * @return List(edge)
	 */
	public List<int[]> getEdges();

	/**
	 * 
	 * @param node
	 * @return ArrayList(node)
	 * @throws NotExistingNodeException: node doesn't exist
	 */
	public ArrayList<Integer> getOutcomingNodes(int node) throws NotExistingNodeException;

	/**
	 * This method details if the two specified nodes are connected (to each other)
	 * @param nodeA
	 * @param nodeB
	 * @return true/false
	 * @throws NotExistingNodeException: nodeA or nodeB doesn't exist
	 */
	public boolean areNodesConnected(int nodeA, int nodeB) throws NotExistingNodeException;

	/**
	 * This method returns ArrayList with all the incoming node of the specified node
	 * @param node
	 * @return ArrayList(node)
	 * @throws NotExistingNodeException: node doesn't exist
	 * 
	 */
	public ArrayList<Integer> getIncomingNodes(int node) throws NotExistingNodeException;

	/**
	 * This method creates an edge between the two specified nodes
	 * @param nodeA
	 * @param nodeB
	 * @throws NotExistingNodeException: nodeA or nodeB doesn't exist
	 */
	public void addEdge(int nodeA, int nodeB) throws NotExistingNodeException;

	/**
	 * This method removes an edge between the two specified nodes
	 * @param nodeA
	 * @param nodeB
	 * @throws NotExistingNodeException: nodeA or nodeB doesn't exist
	 */
	public void removeEdge(int nodeA, int nodeB) throws NotExistingNodeException;

	/**
	 * This method returns the number of the nodes in the graph
	 * @return : the number of the nodes in the graph
	 */
	public int getNodesNumber();

	/**
	 * This method returns the degree of the specified node
	 * @param node
	 * @return : the node's degree
	 * @throws NotExistingNodeException
	 */
	public int getNodeDegree(int node) throws NotExistingNodeException;
	
	/**
	 * This method returns the incoming degree of the specified node
	 * @param node
	 * @return : the node's degree
	 * @throws NotExistingNodeException
	 */
	public int getNodeIncomingDegree(int node) throws NotExistingNodeException;
	
	/**
	 * This function returns the outcoming degree of the specified node
	 *  @return : the outcoming degree of the specified node
	 */
	public int getNodeOutcomingDegree(int node) throws NotExistingNodeException;

	/**
	 * This method calculates the degree of all nodes
	 * @return : the total degree of the network
	 */
	public int getTotalDegree();

	/**
	 * This method adds newEdges random edges to the graph
	 * @param newEdges: the number of new edges to be added.
	 * @param noSource: An array containing the ids of the nodes that can not be a source.
	 * @param noTarget: An array containing the ids of the nodes that can not be a target.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void addRandomEdges(int newEdges, ArrayList<String> noSource, ArrayList<String> noTarget) throws ParamDefinitionException, NotExistingNodeException;

	/**
	 * This method adds a function in a node.
	 * @param node: the node in which the function will be added
	 * @param function: The function which will be added
	 * @throws NotExistingNodeException: The specified node doesn't exist
	 */
	public void addFunction(int node, Function function) throws NotExistingNodeException;

	/**
	 * This method adds a set of function to the graph (One function for each node).
	 * @param functions: the array of functions that will be added to the nodes. 
	 * The number of the functions and the number of nodes must be the same.
	 * @throws ParamDefinitionException: functions number and nodes number don't be the same. 
	 * @throws GraphGeneration.ParamDefinitionException: The input type or the input dimension isn't correct
	 */
	public void addFunctions(Function[] functions) throws ParamDefinitionException;

	/**
	 * This method returns the function associated to the specified node.
	 * @param node: The node name.
	 * @return function: The node associated function.
	 * @throws NotExistingNodeException: The specified node doesn't exist.
	 */
	public Function getFunction(int node) throws NotExistingNodeException;

	/**
	 * This method returns the function value for a specified node with a specified input.
	 * @param node: The node name.
	 * @param input: The function input (One input for each incoming edge)
	 * @return function: The node associated function.
	 * @throws NotExistingNodeException: The specified node doesn't exist.
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException: The input type or the input dimension isn't correct
	 */
	public Object evalFunction(int node, Object[] inputs) throws NotExistingNodeException, InputTypeException, ParamDefinitionException;

	/**
	 * This method returns the network as a GRNML string set.
	 * The GRNML format is the simulator inner exchange format.
	 * @return: A string in the GRNML format
	 */
	public String toGRNML();

	/**
	 * This method copies a GeneRegulatoryNetwork object
	 * @return a copy of the object
	 * @throws ParamDefinitionException 
	 */
	public GeneRegulatoryNetwork copy() throws ParamDefinitionException;

	/**
	 * This method returns the node number, given its name. 
	 * The node number will be -1 if the node doen't exist.
	 * @param nodeName: The name og the node
	 * @return The number of the node in the graph.
	 */
	public int getNodeNumber(String nodeName);

	/**
	 * This method adds a specified number of nodes in the network
	 * @param nodesNumber: The number of the nodes to be added
	 */
	public void addNodes(int nodesNumber);
	
	/**
	 * This method selects randomly a node edge and sets the new node as the source.
	 * @throws NotExistingNodeException 
	 */
	public void changeRandomlyEdgeSource(int node, int newNode) throws NotExistingNodeException;
	
}

