/**
 * BIMIB @ Milan University - Bicocca 

 * 2013
 */

/**
 *
 * This class is a representation of a Tes tree
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

package it.unimib.disco.bimib.Tes;

//System imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.TesTreeException;;


public class TesTree{

	private TesTreeNode root;

	/**
	 * This is the default constructor 
	 * @param delta
	 * @param atm
	 * @param attractors
	 * @throws Exception
	 */
	public TesTree(double[] delta,double[][] atm, Object[] attractors) throws TesTreeException {
		this.root = null;
		this.treeGenerator(delta, atm, attractors);
	}

	public TesTree(int nodeId){
		this.root = new TesTreeNode(null, nodeId);
	}


	/**
	 * This method returns the tree root.
	 * @return The tree root
	 */
	public TesTreeNode getRoot(){
		return this.root;
	}

	/**
	 * This method creates the Tes Tree
	 * @param delta : threshold for the Tes
	 * @param atm : matrix of all the threshold
	 * @param attractors : array of attractors
	 * @throws TesTreeException: An error occurred during the tree creation
	 */
	public void treeGenerator(double[] delta, double[][] atm, Object[] attractors) throws TesTreeException{
		//Initializes the recursive call
		int[] nodeId = new int[1];
		nodeId[0] = 0;
		this.treeGenerator(this.root, delta, 0, atm, attractors, nodeId);
	}

	/**
	 * This service method returns the path matrix.
	 * The path matrix is a boolean matrix in which each i-j entry 
	 * is true if there is a path between nodes i and j.
	 * @param matrix: The atm matrix
	 * @return the path matrix
	 */
	private boolean[][] performPathMatrix(double[][] matrix){
		int nodes = matrix.length;
		boolean[][] pathMatrix = new boolean[nodes][nodes];
		//Path matrix initialization
		for(int i = 0; i < nodes; i++){
			for(int j = 0; j < nodes; j++){
				//the i-j entry is true if and only if there is a not zero probability
				//to pass from attractor i to attractor j
				pathMatrix[i][j] = matrix[i][j] > 0 ? true : false;
			}
		}
		//Perform the complete path matrix
		for(int i = 0; i < nodes; i++){
			for(int j = 0; j < nodes; j++){
				for(int k = 0; k < nodes; k++){
					/* There is a path between attractors i and j if and only if 
					 * they are already connected or if there is a path between attractors i and 
					 * there is a path k and between attractors k and j
					 */
					pathMatrix[i][j] = (pathMatrix[i][j] || (pathMatrix[i][k] && pathMatrix[k][j]));
				}
			}
		}
		return pathMatrix;
	}

	/**
	 * This service method creates the Tes Tree.
	 * @param node: The starting tree node.
	 * @param delta : threshold for the Tes.
	 * @param level: The current tree level.
	 * @param atm : matrix of all the threshold.
	 * @param attractors : array of attractors.
	 * @throws TesTreeException: An error occurred during the tree creation.
	 */
	private void treeGenerator(TesTreeNode node, double[] delta, int level, double[][] atm, Object[] attractors, int[] nodeId) throws TesTreeException{
		//Checks if is at the end of the ArrayLIst of delta
		if(level < delta.length ){

			//Removes the links from the atm
			atm = removeLinksByDelta(delta[level], atm, attractors.length);
			ArrayList<Tes> myTes = new ArrayList<Tes>();

			//Gets the path matrix
			boolean[][] pathMatrix = this.performPathMatrix(atm);

			//Creates all the Tes(es)
			for(Object attractor : attractors)
				myTes.add(new Tes(attractor));

			for(int att1 = 0; att1 < attractors.length; att1++){
				for(int att2 = att1 + 1; att2 < attractors.length; att2++){
					if(pathMatrix[att1][att2] && pathMatrix[att2][att1]){
						int t1 = getTesByAttractor(myTes, attractors[att1]);
						int t2 = getTesByAttractor(myTes, attractors[att2]);
						//If there is a links between two tes they are merge 
						if((t1 != -1 && t2 != -1)&&(t1 != t2)){
							myTes.get(t1).merge(myTes.get(t2));
							myTes.remove(myTes.get(t2));
						}
					}
				}
			}
			
			//Removes the SSC that are not TESs
			int t1, t2;
			boolean deleted;
			for(int att1 = 0; att1 < attractors.length; att1++){
				int att2 = 0; 
				deleted = false;
				t1 = getTesByAttractor(myTes, attractors[att1]);
				if(t1 != -1){
					while(att2 < attractors.length && !deleted){
						if(atm[att1][att2] != 0){
							t2 = getTesByAttractor(myTes, attractors[att2]);
							if(t1 != t2){
								myTes.remove(t1);
								deleted = true;
							}
						}
						att2 = att2 + 1;
					}
				}
			}			

			//Checks if the root is already initializes
			if(node == null){
				if(myTes.size() != 1)
					throw new TesTreeException("This thresholds don't create a tree");
				else{

					this.root = new TesTreeNode(myTes.get(0), nodeId[0]);
					nodeId[0] += 1;

					this.treeGenerator(this.root, delta, level + 1, atm, attractors, nodeId);
				}
			}else{
				//Creates a link between the parent and his children
				for(int i = 0; i < myTes.size(); i++){
					node.addChild(new TesTreeNode(myTes.get(i), nodeId[0]));
					nodeId[0] += 1;
					node.getChild(i).setParent(node);
				}

				//For every child creates a new atm with the specified attractor in the tes(child)
				for(TesTreeNode child : node.getChildren()){
					int nAttractors = child.getTes().sizeTes(), newRow = 0, newCol = 0;
					double[][] reducedAtm = new double[nAttractors][nAttractors];
					Object[] reducedAttractors = new Object[nAttractors];
					ArrayList<Integer> attPositions = new ArrayList<Integer>();

					for(int i = 0; i < attractors.length; i++){
						if(child.getTes().find(attractors[i]) != -1){
							attPositions.add(i);
							reducedAttractors[newCol] = attractors[i];
							newCol++;
						}
					}
					//Copies all the values in the atm into the reduced atm
					for(int i : attPositions){
						newCol = 0;
						for(int j : attPositions){
							reducedAtm[newRow][newCol] = atm[i][j];
							newCol ++;
						}
						newRow++;
					}
					//Calls the recursive call for the new node
					this.treeGenerator(child, delta, level + 1, reducedAtm, reducedAttractors, nodeId);
				}
			}			
		}
	}

	/**
	 * This method returns the specified index for a tes from an attractor
	 * @param teses
	 * @param attractor
	 * @return the specified index of the attractors' tes
	 */
	private int getTesByAttractor(ArrayList<Tes> teses, Object attractor){
		int i = 0;
		while(i < teses.size() && (teses.get(i).find(attractor) == -1))
			i++;
		return (i == teses.size() ? -1 : i);
	}

	/**
	 * This method returns the atm without links based on the delta's value
	 * @param delta
	 * @param atm
	 * @param n : dimension of the matrix
	 * @return the new atm
	 */
	private double[][] removeLinksByDelta(double delta, double[][] atm, int n){
		for(int line = 0; line < n; line++){
			for(int pillar = 0; pillar < n; pillar++){
				if(atm[line][pillar] < delta)
					atm[line][pillar] = 0;
			}
		}

		return atm;
	}

	/**
	 * This method returns the TES tree deepness
	 * @return deepness: The TES tree deepness
	 */
	public int getTreeDeppness(){
		return this.getTreeDeepness(this.root);
	}

	/**
	 * This is a service method for the TES deepness calculating.
	 * @param node: The initial TES tree node.
	 * @return: The TES tree deepness from the specified node.
	 */
	private int getTreeDeepness(TesTreeNode node){
		//Exit conditions
		if(node == null)
			return 0;
		if(node.getChildren() == null)
			return 0;
		if(node.getChildren().size() == 0)
			return 0;
		//Recursion portion
		int newDeepness = 0, childDeepness = 0;
		for(TesTreeNode child : node.getChildren()){
			childDeepness = this.getTreeDeepness(child); 
			if(newDeepness < childDeepness) 
				newDeepness = childDeepness;
		}
		return 1 + newDeepness;
	}

	/**
	 * This recursive method returns the node pointer with the specified id.
	 * @param startingLevel: The research initial level
	 * @param level: The limit level
	 * @param id: The node id to be found
	 * @return: The TesTreeNode if it is found or null if it isn0t found
	 */
	public TesTreeNode getNodeByIdAndLevel(int level, int id){
		return getNodeByIdAndLevel(this.root, 0, level, id);
	}

	/**
	 * This service recursive method returns the node pointer with the specified id.
	 * @param startingNode: The research starting node
	 * @param startingLevel: The research initial level
	 * @param level: The limit level
	 * @param id: The node id to be found
	 * @return: The TesTreeNode if it is found or null if it isn0t found
	 */
	private TesTreeNode getNodeByIdAndLevel(TesTreeNode startingNode, int startingLevel, int limit, int id){

		//Exit cases
		if(startingNode == null)
			return null;
		if(startingLevel <= limit && startingNode.getNodeId() == id){
			return startingNode;
		}

		if(startingLevel == limit && startingNode.getNodeId() != id)
			return null;

		TesTreeNode result;
		//Recursive call in each node child
		for(TesTreeNode node : startingNode.getChildren()){
			result = getNodeByIdAndLevel(node, startingLevel + 1, limit, id);

			//Node found
			if(result != null){
				return result;

			}
		}
		//Node not found
		return null;

	}


	/**
	 * This method adds a new node with the specified id to the tree.
	 * The parent node is specified with the parentId value.
	 * @param nodeId: the new node id
	 * @param level: the node level
	 * @param parrentId: the parent id
	 * @throws TesTreeException: An error occurred during the research
	 */
	public void addNodeManually(int nodeId, int level, int parentId) throws TesTreeException{
		TesTreeNode parent = this.getNodeByIdAndLevel(level - 1, parentId);
		if(parent == null)
			throw new TesTreeException("Node " + parentId + " not found");
		parent.addChild(new TesTreeNode(null, nodeId));
	}


	/**
	 * This method compares the TES tree with the specified one.
	 * @param givenTree: The TES tree to be compare with the caller one
	 * @return: True or false
	 */
	public boolean tesTreeCompare(TesTree givenTree){
		return this.tesTreeCompare(givenTree, givenTree.getTreeDeppness() + 1);
	}


	/**
	 * This method compares the TES tree with the specified one until the 'limit' level.
	 * @param givenTree: The TES tree to be compare with the caller one
	 * @param limit: The last trees deepness to be check 
	 * @return: True or false
	 */
	public boolean tesTreeCompare(TesTree givenTree, int limit){

		//Check the deepness
		if(this.getTreeDeppness() != givenTree.getTreeDeppness())
			return false;
		else
			return tesTreeCompare(this.root, givenTree.root, 0, limit);

	}


	/**
	 * This is a recursive service method used in order to compare two TES 
	 * trees with node and givenNode roots.
	 * @param node: The first tree checking starting point
	 * @param givenNode: The second tree checking starting point
	 * @param level: The current trees level (used for the recursion)
	 * @param limit: The maximum deepness to be checked
	 * @return a boolean value
	 */
	private boolean tesTreeCompare(TesTreeNode node, TesTreeNode givenNode, int level, int limit){
		//Exit cases
		if(level == limit)
			return true;
		if(node.getChildren().size() != givenNode.getChildren().size())
			return false;
		else if (node.getChildren().size() == 0)
			return true;

		//Recursive case
		boolean isEqual = false;
		int p = 0, i;
		int childrenNumber = node.getChildren().size();
		ArrayList<TesTreeNode> a = givenNode.getChildren();
		TesTreeNode[] b = new TesTreeNode[a.size()];
		for(int j = 0; j < a.size(); j++)
			b[j] = a.get(j);

		//Gets all the possible child permutations
		ArrayList<TesTreeNode[]> permutations = permutation(b, 0, childrenNumber);
		do{
			TesTreeNode[] A = node.getChildrenAsArray();
			TesTreeNode[] B = permutations.get(p);
			i = 0;
			//Recursive calls for each couple of nodes.
			while(i < A.length && tesTreeCompare(A[i], B[i], level + 1, limit))
				i++;
			//The nodes are equal in each couple.
			if(i == childrenNumber)
				isEqual = true;
			p++;
		}while(!isEqual && p < factorial(childrenNumber));
		return isEqual;
	}


	/**
	 * This method is a service method used in order to swap two elements in an array
	 * @param array: The original array
	 * @param from: first item index
	 * @param to: second item index
	 */
	private static void swap(Object[] array, int from, int to){
		Object obj = array[from];
		array[from] = array[to];
		array[to] = obj;
	}


	/**
	 * This method computes all the possible permutation of a given TesTreeNode array.
	 * @param array: The original TesTreeNode array
	 * @param start: Initial position
	 * @param end: Final position
	 * @return an array list which contains a TesTreeNode permutation of the given array in each position.
	 */
	private static ArrayList<TesTreeNode[]> permutation(TesTreeNode[] array, int start, int end){
		ArrayList<TesTreeNode[]> permutations = new ArrayList<TesTreeNode[]>();
		TesTree.permutation(array, 0, array.length, permutations);
		return permutations;	
	}


	/**
	 * This service method computes all the possible permutation of a given TesTreeNode array.
	 * @param array: The original TesTreeNode array
	 * @param start: Initial position
	 * @param end: Final position
	 * @param permutations: The array list with all the permutations
	 * @return an array list which contains a TesTreeNode permutation of the given array in each position.
	 */
	private static void permutation(TesTreeNode[] array, int start, int end, ArrayList<TesTreeNode[]> permutations){
		int j;
		//Exit case
		if(start == end){    
			permutations.add(array.clone());
		}else{
			//Recursive case
			for(j = start; j < end; j++){
				swap(array, start, j);            
				permutation(array, start + 1, end, permutations);  
				swap(array, start, j);             
			}                                        
		}
	}

	/**
	 * This method calculates the factorial of the given number.
	 * @param n: The number used in order to perform the factorial. 
	 * This value must be greater or equal than 0.
	 * @return: The factorial of the given number.
	 */
	private int factorial(int n){
		return (n == 0 ? 1 : n * factorial(n-1));
	}

	/**
	 * This method returns the edges of the tree
	 * @return An array list containing the edges as couples
	 */
	public ArrayList<String[]> getEdges(){
		ArrayList<String[]> edges = new ArrayList<String[]>();
		getEdges(this.root, edges);
		return edges;

	}

	/**
	 * This method returns the edges of the tree
	 * @return An array list containing the edges as couples
	 */
	private void getEdges(TesTreeNode node, ArrayList<String[]> edges){
		if(node != null){
			String[] edge;
			for(TesTreeNode child : node.getChildren()){
				edge = new String[2];
				edge[0] = String.valueOf(node.getNodeId());
				edge[1] = String.valueOf(child.getNodeId());
				edges.add(edge);
				getEdges(child, edges);
			}
		}}

	/**
	 * This method returns the number of nodes in the given tree or subtree, given the root.
	 * The output will not count the root.
	 */
	public static int getNumberOfNodes(TesTreeNode node){
		if(node.getChildren().size() == 0){
			return 0;
		}else{
			int count = 0;
			ArrayList<TesTreeNode> children = node.getChildren();
			for(TesTreeNode child: children){
				count = count + 1 + getNumberOfNodes(child);
			}
			return count;
		}
	}
	
	/**
	 * This method compares the TES tree with the specified one.
	 * @param givenTree: The TES tree to be compare with the caller one
	 * @return: True or false
	 */
	public int tesTreeDistance(TesTree givenTree){
		return tesTreeDistance(this.root, givenTree.root);
	}

	/**
	 * This is a recursive service method used in order to compare two TES 
	 * trees with node and givenNode roots.
	 * @param node: The first tree checking starting point
	 * @param givenNode: The second tree checking starting point
	 * @param level: The current trees level (used for the recursion)
	 * @param limit: The maximum deepness to be checked
	 * @return a boolean value
	 */
	private int tesTreeDistance(TesTreeNode node, TesTreeNode givenNode){
		
		//Both the nodes don't have any children
		if(node.getChildren().size() == 0 && givenNode.getChildren().size() == 0)
			return 0;
		//Only the node in the in the given tree has children.
		if(node.getChildren().size() == 0)
			return getNumberOfNodes(givenNode);
		//Only the network-tree node has children.
		if(givenNode.getChildren().size() == 0)
			return getNumberOfNodes(node);
		
		//Both the nodes have some children.
		int i = 0;
		ArrayList<TesTreeNode> networkNodeChildren = node.getChildren();
		ArrayList<TesTreeNode[]> permutations = permutation(givenNode.getChildrenAsArray(), 0, givenNode.getChildren().size());
		
		int distance = -1;
		int localDistance;
		for(TesTreeNode[] nodesPermutation : permutations){

			//Counter initialization
			i = 0;
			localDistance = 0;
			while(i < networkNodeChildren.size() && i < nodesPermutation.length){
				localDistance = localDistance + tesTreeDistance(networkNodeChildren.get(i), nodesPermutation[i]);
				i++;
			}
			if(i < networkNodeChildren.size()){
				for(; i < networkNodeChildren.size(); i++){
					localDistance = localDistance + 1 + getNumberOfNodes(networkNodeChildren.get(i));
				}
			}else{
				for(; i < nodesPermutation.length; i++){
					localDistance = localDistance + 1 + getNumberOfNodes(nodesPermutation[i]);
				}
			}
			distance = (distance == -1 ? localDistance : Math.min(distance, localDistance));
		}
		
		return distance;
	}
	
	public int tesTreeHistogramComparison(TesTree givenTree){
		ArrayList<TesTreeNode> currentRoot = new ArrayList<TesTreeNode>();
		ArrayList<TesTreeNode> givenRoot = new ArrayList<TesTreeNode>();
		
		currentRoot.add(this.root);
		givenRoot.add(givenTree.root);
		
		return this.tesTreeHistogramComparison(currentRoot, givenRoot);
	}
	
	private int tesTreeHistogramComparison(ArrayList<TesTreeNode> nodes, ArrayList<TesTreeNode> givenNodes){
		
		if(nodes.size() == 0 && givenNodes.size() == 0){
			return 0;
		}

		
		HashMap<Integer, Integer> treeDistribution = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> givenTreeDistribution = new HashMap<Integer, Integer>();
		ArrayList<TesTreeNode> createdTreeLevelChildren = new ArrayList<TesTreeNode>();
		ArrayList<TesTreeNode> givenTreeLevelChildren = new ArrayList<TesTreeNode>();
		
		int size;
		//Gets the distribution for the current level for the created tree
		for(TesTreeNode currentNode : nodes){
			size = currentNode.getChildren().size();
			createdTreeLevelChildren.addAll(currentNode.getChildren());
			if(treeDistribution.containsKey(size)){
				treeDistribution.put(size, treeDistribution.get(size) + 1);
			}else{
				treeDistribution.put(size, 1); 
			}
		}

		//Gets the distribution for the current level for the given tree
		for(TesTreeNode currentNode : givenNodes){
			size = currentNode.getChildren().size();
			givenTreeLevelChildren.addAll(currentNode.getChildren());
			if(givenTreeDistribution.containsKey(size)){
				givenTreeDistribution.put(size, givenTreeDistribution.get(size) + 1);
			}else{
				givenTreeDistribution.put(size, 1); 
			}
		}
		
		//Calculates the histogram distance
		return histogramDistance(treeDistribution, givenTreeDistribution) +
				tesTreeHistogramComparison(createdTreeLevelChildren, givenTreeLevelChildren);
				
		
		
	}
	
	private int histogramDistance(HashMap<Integer, Integer> dist1, HashMap<Integer, Integer> dist2){
		int distance = 0;
		HashSet<Integer> values = new HashSet<Integer>();
		values.addAll(dist1.keySet());
		values.addAll(dist2.keySet());
		for(Integer value : values){
			if(dist1.containsKey(value) && dist2.containsKey(value)){
				distance = distance + Math.abs(dist1.get(value) - dist2.get(value));
			}else if(dist1.containsKey(value)){
				distance = distance + dist1.get(value);
			}else{
				distance = distance + dist2.get(value);
			}
		}
		
		return distance;	
	}
	
	/*
	public ArrayList<TesTreeNode> getTreeNodes(){
		ArrayList<TesTreeNode> nodes = new ArrayList<TesTreeNode>();
		getTreeNodes(this.root, nodes);
		return nodes;
	}

	public void getTreeNodes(TesTreeNode node, ArrayList<TesTreeNode> nodes){
		
		nodes.add(node);
		
		ArrayList<TesTreeNode> children = node.getChildren();
		if(children.size() != 0){
			for(TesTreeNode child : children){
				getTreeNodes(child, nodes);
			}
		}
			
		
	}
	
*/
	
	/**
	 * This method returns the number of leaf nodes in the tree
	 * @return
	 */
	public int getLeafsNodesNumber(){
		return getLeafsNodesNumber(this.root);
	}
	
	/**
	 * Service method for the leaf nodes calculus.
	 * @param node: Node for starting
	 * @return The number of leaf nodes in the subtree
	 */
	private int getLeafsNodesNumber(TesTreeNode node){
		if(node.getChildren().size() == 0)
			return 1;
		else{
			int leafs = 0;
			for(TesTreeNode child : node.getChildren())
				leafs = leafs + getLeafsNodesNumber(child);
			return leafs;
		}
	}
	
	
	public void print(){
		print(this.root);
	}
	
	private void print(TesTreeNode node){
		System.out.print(node.getNodeId() + " parent: " + (node.getParent() == null ? "Root" :node.getParent().getNodeId()));
		for(TesTreeNode child : node.getChildren())
				print(child);
	}
	
}

