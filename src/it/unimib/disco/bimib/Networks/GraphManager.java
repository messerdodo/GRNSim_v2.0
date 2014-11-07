/**
 * This class is a manager for the graph creation and for the function assign
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Networks;

//System imports
import java.util.ArrayList;
import java.util.Properties;

//GRNSim imports
import it.unimib.disco.bimib.Utility.*;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Functions.*;

public class GraphManager {

	//Gene Regulatory Network object
	private GeneRegulatoryNetwork geneRegulatoryNetwork;

	/**
	 * Default constructor
	 */
	public GraphManager(){
		//Set to null the network
		this.geneRegulatoryNetwork = null;
	}

	/**
	 * This method creates the gene regulatory network using the features 
	 * passed. 
	 * Only the correct features are considered.
	 * @param features: The feature parameter is an hashMap where each key is 
	 * connected to a feature. Keys and values are Strings.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 * @throws GraphGeneration.Function.ParamDefinitionException 
	 * @throws FeaturesException 
	 * @throws MissingFeaturesException 
	 */
	public void createNetwork(Properties simulationFeatures) 
			throws ParamDefinitionException, 
			NotExistingNodeException, FeaturesException, MissingFeaturesException{

		int nodes, edges, averageConnectivity, ni, fixedInputsNumber;
		float beta;
		double randomRate = 0, biasRate = 0, andRate = 0, orRate = 0, canalizedRate = 0, biasValue, gamma;
		boolean completelyDefined;
		Function[] functions;

		//Gets the common parameters
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.NODES))
			throw new ParamDefinitionException("Number of nodes missed");
		nodes = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.NODES).toString());

		//Checks the topology
		//RANDOM TOPOLOGY
		if(simulationFeatures.get(SimulationFeaturesConstants.TOPOLOGY).equals(SimulationFeaturesConstants.RANDOM_TOPOLOGY)){
			//Gets the edges (required parameter for random topology)
			edges = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.EDGES).toString());
			//Creates the random network using the Erdos Renyi
			this.geneRegulatoryNetwork = new RandomGraph(nodes, edges);

			//SCALE FREE TOPOLOGY
		}else if(simulationFeatures.get(SimulationFeaturesConstants.TOPOLOGY).equals(SimulationFeaturesConstants.SCALE_FREE_TOPOLOGY)){

			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.ALGORITHM))
				throw new ParamDefinitionException("Algorithm key param missed");

			//Checks the selected generation algorithm
			if(simulationFeatures.get(SimulationFeaturesConstants.ALGORITHM).equals(SimulationFeaturesConstants.BARABASI_ALBERTZ_ALGORITHM)){
				//Gets the Barabasi Albertz params
				if(!simulationFeatures.containsKey(SimulationFeaturesConstants.NI))
					throw new ParamDefinitionException("Initial nodes parameter missed");
				ni = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.NI).toString());
				if(!simulationFeatures.containsKey(SimulationFeaturesConstants.AVERAGE_CONNECTIVITY))
					throw new ParamDefinitionException("Average connectivity parameter missed");
				averageConnectivity = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.AVERAGE_CONNECTIVITY).toString());

				//Creates the graph which follows the Barabasi Albertz Model
				this.geneRegulatoryNetwork = new ScaleFreeGraph(ni, nodes, averageConnectivity);

			}else if(simulationFeatures.get(SimulationFeaturesConstants.ALGORITHM).equals(SimulationFeaturesConstants.POWER_LAW_ALGORITHM)){
				if(!simulationFeatures.containsKey(SimulationFeaturesConstants.GAMMA))
					throw new ParamDefinitionException("Gamma parameter missed");
				//Gets gamma parameter
				gamma = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.GAMMA).toString());
				//Creates the scale free graph which follows the power low distribution
				this.geneRegulatoryNetwork = new ScaleFreeGraph(nodes, gamma);
			}else if(simulationFeatures.get(SimulationFeaturesConstants.ALGORITHM).equals(SimulationFeaturesConstants.FIXED_POWER_LAW_ALGORITHM)){
				if(!simulationFeatures.containsKey(SimulationFeaturesConstants.GAMMA))
					throw new ParamDefinitionException("Gamma parameter missed");
				if(!simulationFeatures.containsKey(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER))
					throw new ParamDefinitionException(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER + " parameter missed");
				//Gets gamma parameter
				gamma = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.GAMMA).toString());
				//Gets fixed input number
				fixedInputsNumber = Integer.valueOf(simulationFeatures.getProperty(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER));
				//Creates the scale free graph which follows the power low distribution
				this.geneRegulatoryNetwork = new ScaleFreeGraph(nodes, gamma, fixedInputsNumber);
			}else{
				throw new ParamDefinitionException("Not supported algorithm");
			}

			//SMALL WORLD TOPOLOGY
		}else if(simulationFeatures.get(SimulationFeaturesConstants.TOPOLOGY).equals(SimulationFeaturesConstants.SMALL_WORLD_TOPOLOGY)){
			//Gets the average connectivity 
			averageConnectivity = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.AVERAGE_CONNECTIVITY).toString());
			//Gets the beta parameter
			beta = Float.valueOf(simulationFeatures.get(SimulationFeaturesConstants.BETA).toString());
			//Creates the small world graph following the Strogatz model
			this.geneRegulatoryNetwork = new SmallWorldGraph(nodes, averageConnectivity, beta);

			//PARTIALLY RANDOM TOPOLOGY
		}else if(simulationFeatures.get(SimulationFeaturesConstants.TOPOLOGY).equals(SimulationFeaturesConstants.PARTIALLY_RANDOM_TOPOLOGY)){
			//Gets the fixed input number
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER))
				throw new FeaturesException(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER + " feature required.");
			fixedInputsNumber = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER).toString());
			//Creates the network with the selected topology.
			this.geneRegulatoryNetwork = new PartiallyRandomGraph(nodes, fixedInputsNumber);
			//HIERARCHICAL TOPOLOGY
		}else if(simulationFeatures.get(SimulationFeaturesConstants.TOPOLOGY).equals(SimulationFeaturesConstants.HIERARCHICAL_TOPOLOGY)){

			//To be implemented soon

		}else{
			throw new ParamDefinitionException("Not supported topology");
		}


		//Functions generation
		//Boolean functions
		if(simulationFeatures.get(SimulationFeaturesConstants.FUNCTION_TYPE).equals(SimulationFeaturesConstants.BOOLEAN_FUNCTION)){

			//Creates the boolean function array
			functions = new BooleanFunction[nodes];

			//Random type functions
			if(simulationFeatures.containsKey(SimulationFeaturesConstants.RANDOM_TYPE))
				randomRate = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.RANDOM_TYPE).toString());
			//Random bias type functions
			if(simulationFeatures.containsKey(SimulationFeaturesConstants.BIAS_TYPE))
				biasRate = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.BIAS_TYPE).toString());
			biasValue = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.BIAS_VALUE).toString());
			//And type functions
			if(simulationFeatures.containsKey(SimulationFeaturesConstants.AND_FUNCTION_TYPE))
				andRate = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.AND_FUNCTION_TYPE).toString());
			//Or type functions
			if(simulationFeatures.containsKey(SimulationFeaturesConstants.OR_FUNCTION_TYPE))
				orRate = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.OR_FUNCTION_TYPE).toString());
			//Canalized functions
			if(simulationFeatures.containsKey(SimulationFeaturesConstants.CANALIZED_TYPE))
				canalizedRate = Double.valueOf(simulationFeatures.get(SimulationFeaturesConstants.CANALIZED_TYPE).toString());

			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.COMPLETELY_DEFINED_FUNCTIONS))
				throw new MissingFeaturesException(SimulationFeaturesConstants.COMPLETELY_DEFINED_FUNCTIONS + " key must be specified");
			completelyDefined = simulationFeatures.getProperty(SimulationFeaturesConstants.COMPLETELY_DEFINED_FUNCTIONS).equals(SimulationFeaturesConstants.YES) ? true : false;
			
			
			//Checks if the rates are correct. The sum must be 1
			if(randomRate + biasRate + andRate + orRate + canalizedRate != 1)
				throw new ParamDefinitionException("The sum of the rates must be 1");

			int createdFunctions = 0;

			//Creates the random functions
			for(int i = 0; i < Math.floor(randomRate * nodes); i++){
				functions[createdFunctions] = new RandomFunction(
						geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined);
				createdFunctions++;
			}

			//Creates the bias random functions
			for(int i = 0; i < Math.floor(biasRate * nodes); i++){
				functions[createdFunctions] = new RandomFunction(
						geneRegulatoryNetwork.getIncomingNodes(createdFunctions), biasValue, completelyDefined);
				createdFunctions++;
			}

			//Creates the or functions
			for(int i = 0; i < Math.floor(orRate * nodes); i++){
				functions[createdFunctions] = new AndOrFunction(false,
						geneRegulatoryNetwork.getIncomingNodes(createdFunctions));
				createdFunctions++;
			}

			//Creates the and functions
			for(int i = 0; i < Math.floor(andRate * nodes); i++){
				functions[createdFunctions] = new AndOrFunction(true,
						geneRegulatoryNetwork.getIncomingNodes(createdFunctions));
				createdFunctions++;
			}

			//Creates the canalized functions
			for(int i = 0; i < Math.floor(canalizedRate * nodes); i++){
				functions[createdFunctions] = new CanalizedFunction(
						geneRegulatoryNetwork.getIncomingNodes(createdFunctions),completelyDefined);
				createdFunctions++;
			}

			//BIOLOGICAL FUNCTIONS TO BE IMPLEMENTED SOON

			if(createdFunctions < nodes){
				double rates[] = {randomRate, biasRate, andRate, orRate, canalizedRate };

				double maxRate = max(rates);
				if(maxRate == randomRate){
					//Creates the other random functions
					for(; createdFunctions < nodes; createdFunctions++)
						functions[createdFunctions] = new RandomFunction(
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined);
				}else if(maxRate == biasRate){
					//Creates the other bias random functions
					for(; createdFunctions < nodes; createdFunctions++)
						functions[createdFunctions] = new RandomFunction(
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions), biasValue, completelyDefined);
				}else if(maxRate == andRate){
					//Creates the other and functions
					for(; createdFunctions < nodes; createdFunctions++)
						functions[createdFunctions] = new AndOrFunction(true,
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions));
				}else if(maxRate == orRate){
					//Creates the other or functions
					for(; createdFunctions < nodes; createdFunctions++)
						functions[createdFunctions] = new AndOrFunction(false,
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions));
				}else if(maxRate == canalizedRate){
					//Creates the other or functions
					for(; createdFunctions < nodes; createdFunctions++)
						functions[createdFunctions] = new CanalizedFunction(
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined);
				}

				//BIOLOGICAL FUNCTIONS TO BE IMPLEMENTED SOON


			}

			//Adds the functions to the graph
			geneRegulatoryNetwork.addFunctions(functions);

		}else{
			System.err.println("Not supported function type");
		}		
	}

	/**
	 * This function performs the maximum of an array
	 * @param dataArray: double array
	 * @return the maximum value in the array
	 */
	private double max(double[] dataArray){
		double max = dataArray[0];
		for(int i = 0; i < dataArray.length; i++){
			if(dataArray[i] > max)
				max = dataArray[i];
		}
		return max;
	}

	/**
	 * This method creates the graph with nodes, edges and function passed.
	 * @param nodesName: The array of the nodes names
	 * @param edges: the edge matrix each row is a couple of connected nodes. 
	 * @param functions: the array of the nodes functions
	 * @param graphTopology: the graph topology. Use the NetworkTopology enumeration
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void createGraph(String[] nodesName, int[][] edges, Function[] functions, String graphTopology) throws ParamDefinitionException, NotExistingNodeException{
		if(nodesName == null)
			throw new ParamDefinitionException("The nodes name array must be not null");
		if(edges == null)
			throw new ParamDefinitionException("The edge set must be not null");
		if(functions == null)
			throw new ParamDefinitionException("The functions array must be not null");
		if(nodesName.length != functions.length)
			throw new ParamDefinitionException("The functions array and the nodesName must have the same length");

		//Creates the correct topology graph object
		if(graphTopology.equals(SimulationFeaturesConstants.RANDOM_TOPOLOGY))
			this.geneRegulatoryNetwork = new RandomGraph(nodesName, edges);
		else if(graphTopology.equals(SimulationFeaturesConstants.SCALE_FREE_TOPOLOGY))
			this.geneRegulatoryNetwork = new ScaleFreeGraph(nodesName, edges);
		else if(graphTopology.equals(SimulationFeaturesConstants.SMALL_WORLD_TOPOLOGY))
			this.geneRegulatoryNetwork = new SmallWorldGraph(nodesName, edges);
		else if(graphTopology.equals(SimulationFeaturesConstants.PARTIALLY_RANDOM_TOPOLOGY))
			this.geneRegulatoryNetwork = new PartiallyRandomGraph(nodesName, edges); //Da aggiungere il numero di input
		//Adds the functions
		this.geneRegulatoryNetwork.addFunctions(functions);

	}
	
	/**
	 * This method creates the graph with nodes and edges passed.
	 * @param nodesName: The array of the nodes names
	 * @param edges: the edge matrix each row is a couple of connected nodes. 
	 * @param graphTopology: the graph topology. Use the NetworkTopology enumeration
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void createGraph(String[] nodesName, int[][] edges, String graphTopology) 
			throws ParamDefinitionException, NotExistingNodeException{
		if(nodesName == null)
			throw new ParamDefinitionException("The nodes name array must be not null");
		if(edges == null)
			throw new ParamDefinitionException("The edge set must be not null");
	
		//Creates the correct topology graph object
		if(graphTopology.equals(SimulationFeaturesConstants.RANDOM_TOPOLOGY))
			this.geneRegulatoryNetwork = new RandomGraph(nodesName, edges);
		else if(graphTopology.equals(SimulationFeaturesConstants.SCALE_FREE_TOPOLOGY))
			this.geneRegulatoryNetwork = new ScaleFreeGraph(nodesName, edges);
		else if(graphTopology.equals(SimulationFeaturesConstants.SMALL_WORLD_TOPOLOGY))
			this.geneRegulatoryNetwork = new SmallWorldGraph(nodesName, edges);
		else if(graphTopology.equals(SimulationFeaturesConstants.PARTIALLY_RANDOM_TOPOLOGY))
			this.geneRegulatoryNetwork = new PartiallyRandomGraph(nodesName, edges); //Da aggiungere il numero di input

	}

	/**
	 * This method modifies an existing gene regulatory network following the specified features.
	 * @param features: A key-value hashMap with all the features.
	 * @param noSource: An integer array with the ids of nodes that can not be a source.
	 * @param noTarget: An integer array with the ids of node that can not be a target.
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws FeaturesException 
	 */
	@SuppressWarnings("unchecked")
	public void modify(Properties features) throws ParamDefinitionException, NotExistingNodeException, FeaturesException{
		int undefinedFunctionsNumber = 0, totalNodes = 0;
		double randomRate = 0, biasRate = 0, biasValue = 0, andRate = 0, orRate = 0, canalizedRate = 0;
		ArrayList<Integer> undefinedFunctions = new ArrayList<Integer>();
		
		//Adds the undefined nodes (if specified)
		if(features.containsKey(SimulationFeaturesConstants.NODES)){
			totalNodes = Integer.parseInt(features.get(SimulationFeaturesConstants.NODES).toString());
			//Checks the nodes number
			if(totalNodes < this.geneRegulatoryNetwork.getNodesNumber())
				throw new FeaturesException("The number of the nodes in the network must be less than the nodes feature value");
			//Adds the new nodes
			this.geneRegulatoryNetwork.addNodes(totalNodes - this.geneRegulatoryNetwork.getNodesNumber());
		}
		
		int fixedInputNumber = -1;
		if(features.containsKey(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER)){
			fixedInputNumber = Integer.parseInt(features.getProperty(SimulationFeaturesConstants.FIXED_INPUTS_NUMBER));
		}
		
		int targetNode;
		ArrayList<String> noSource = null;
		ArrayList<String> noTarget = null;
		
		if(features.containsKey(SimulationFeaturesConstants.EXCLUDES_SOURCE_GENES))
			noSource = (ArrayList<String>) features.get(SimulationFeaturesConstants.EXCLUDES_SOURCE_GENES);
		
		if(features.containsKey(SimulationFeaturesConstants.EXCLUDES_TARGET_GENES))
			noTarget = (ArrayList<String>) features.get(SimulationFeaturesConstants.EXCLUDES_TARGET_GENES);
		

		//Adds the mandatory edges
		for(int i = 0; i < this.getNodesNumber(); i++){
			//Checks if the i-th node has at least one connection
			if(this.geneRegulatoryNetwork.getNodeDegree(i) == 0 && (noSource == null || !noSource.contains(geneRegulatoryNetwork.getNodesNames().get(i)))){
				//Adds a random connection that respects the noTarget rule.
				do{
					targetNode = UtilityRandom.randomUniform(0, this.getNodesNumber());
				}while(targetNode == i || (noTarget != null && noTarget.contains(geneRegulatoryNetwork.getNodesNames().get(targetNode))) || 
						((this.geneRegulatoryNetwork.getNodeIncomingDegree(targetNode) >= fixedInputNumber)  && (fixedInputNumber != -1)));
				this.geneRegulatoryNetwork.addEdge(i, targetNode);
			}
		}
		
		//Edges already added
		int insertedEdges = this.geneRegulatoryNetwork.getEdges().size();
		int requiredEdges = Integer.parseInt(features.get(SimulationFeaturesConstants.EDGES).toString());
		
		//Adds random edges
		if(insertedEdges < requiredEdges){
			if(fixedInputNumber == -1){
				this.geneRegulatoryNetwork.addRandomEdges(requiredEdges - insertedEdges, noSource, noTarget);
			}else{
				this.geneRegulatoryNetwork.addRandomEdges(requiredEdges - insertedEdges, noSource, noTarget, fixedInputNumber);
			}
		}
		
		//Adds random functions
		//---Boolean functions
		if(features.get(SimulationFeaturesConstants.FUNCTION_TYPE).equals(SimulationFeaturesConstants.BOOLEAN_FUNCTION)){

			//Random type functions
			if(features.containsKey(SimulationFeaturesConstants.RANDOM_TYPE))
				randomRate = Double.valueOf(features.get(SimulationFeaturesConstants.RANDOM_TYPE).toString());
			//Random bias type functions
			if(features.containsKey(SimulationFeaturesConstants.BIAS_TYPE))
				biasRate = Double.valueOf(features.get(SimulationFeaturesConstants.BIAS_TYPE).toString());
			biasValue = Double.valueOf(features.get(SimulationFeaturesConstants.BIAS_VALUE).toString());
			//And type functions
			if(features.containsKey(SimulationFeaturesConstants.AND_FUNCTION_TYPE))
				andRate = Double.valueOf(features.get(SimulationFeaturesConstants.AND_FUNCTION_TYPE).toString());
			//Or type functions
			if(features.containsKey(SimulationFeaturesConstants.OR_FUNCTION_TYPE))
				orRate = Double.valueOf(features.get(SimulationFeaturesConstants.OR_FUNCTION_TYPE).toString());
			//Canalized functions
			if(features.containsKey(SimulationFeaturesConstants.CANALIZED_TYPE))
				canalizedRate = Double.valueOf(features.get(SimulationFeaturesConstants.CANALIZED_TYPE).toString());


			//Checks if the rates are correct. The sum must be 1
			if(randomRate + biasRate + andRate + orRate + canalizedRate != 1)
				throw new ParamDefinitionException("The sum of the rates must be 1");


			//Gets the undefined functions
			for(int i = 0; i < geneRegulatoryNetwork.getNodesNumber(); i++){
				if(geneRegulatoryNetwork.getFunction(i) == null){
					undefinedFunctionsNumber ++;
					undefinedFunctions.add(i);
				}
			}

			

			int createdFunctions = 0;
			boolean completelyDefined = features.getProperty(SimulationFeaturesConstants.COMPLETELY_DEFINED_FUNCTIONS).equals(SimulationFeaturesConstants.YES);

			//Creates the random functions
			for(int i = 0; i < Math.floor(randomRate * undefinedFunctionsNumber); i++){
				this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions),
						new RandomFunction(geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined));
				createdFunctions++;
			}

			//Creates the bias random functions
			for(int i = 0; i < Math.floor(biasRate * undefinedFunctionsNumber); i++){
				this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions), 
						new RandomFunction(geneRegulatoryNetwork.getIncomingNodes(createdFunctions), 
								biasValue, completelyDefined));
				createdFunctions++;
			}

			//Creates the or functions
			for(int i = 0; i < Math.floor(orRate * undefinedFunctionsNumber); i++){
				this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions), 
						new AndOrFunction(false, 
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions)));
				createdFunctions++;
			}

			//Creates the and functions
			for(int i = 0; i < Math.floor(andRate * undefinedFunctionsNumber); i++){
				this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions),  
						new AndOrFunction(true,
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions)));
				createdFunctions++;
			}

			//Creates the canalized functions
			for(int i = 0; i < Math.floor(canalizedRate * undefinedFunctionsNumber); i++){
				this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions),   
						new CanalizedFunction(
								geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined));
				createdFunctions++;
			}

			//BIOLOGICAL FUNCTIONS TO BE IMPLEMENTED SOON

			//Adds the remaining functions selecting the maximum rate
			if(createdFunctions < undefinedFunctionsNumber){
				double rates[] = {randomRate, biasRate, andRate, orRate, canalizedRate };

				double maxRate = max(rates);


				if(maxRate == randomRate){
					//Creates the other random functions
					for(; createdFunctions < undefinedFunctionsNumber; createdFunctions++)
						this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions),
								new RandomFunction(geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined));
				}else if(maxRate == biasRate){
					//Creates the other bias random functions
					for(; createdFunctions < undefinedFunctionsNumber; createdFunctions++)
						this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions), 
								new RandomFunction(geneRegulatoryNetwork.getIncomingNodes(createdFunctions), 
										biasValue, completelyDefined));
				}else if(maxRate == andRate){
					//Creates the other and functions
					for(; createdFunctions < undefinedFunctionsNumber; createdFunctions++){
						this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions), 
								new AndOrFunction(true, 
										geneRegulatoryNetwork.getIncomingNodes(createdFunctions)));}
				}else if(maxRate == orRate){
					//Creates the other or functions
					for(; createdFunctions < undefinedFunctionsNumber; createdFunctions++)
						this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions),  
								new AndOrFunction(false,
										geneRegulatoryNetwork.getIncomingNodes(createdFunctions)));
				}else if(maxRate == canalizedRate){
					//Creates the other or functions
					for(; createdFunctions < undefinedFunctionsNumber; createdFunctions++)
						this.geneRegulatoryNetwork.addFunction(undefinedFunctions.get(createdFunctions),   
								new CanalizedFunction(
										geneRegulatoryNetwork.getIncomingNodes(createdFunctions), completelyDefined));
				}

				//BIOLOGICAL FUNCTIONS TO BE IMPLEMENTED SOON

			}

		}


	}

	/**
	 * This method returns the following network state of a given state.
	 * For each node it evaluates the 
	 * @param currentState: The Boolean array which represents the current network state
	 * @return The following boolean network state
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 * @throws InputTypeException 
	 */
	public Boolean[] getNewState(Object[] currentState) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{

		//Gets the nodes
		Boolean newState[] = new Boolean[this.geneRegulatoryNetwork.getNodesNumber()];

		//For each node performs the new state
		for(int node = 0; node < this.geneRegulatoryNetwork.getNodesNumber(); node++){
			
			//Evaluates the node associated function and gets the new node state
			newState[node] = (Boolean) this.geneRegulatoryNetwork.evalFunction(node, (Boolean[])currentState);
		}

		return newState;
	}

	/**
	 * This method returns the number of the nodes in the graph
	 * @return the number of the node in the graph
	 */
	public int getNodesNumber(){
		return this.geneRegulatoryNetwork.getNodesNumber();
	}

	/**
	 * This method returns the associated network
	 * @return the associated network
	 */
	public GeneRegulatoryNetwork getGraph(){
		return this.geneRegulatoryNetwork;
	}

	/**
	 * This method modify an existing function perpetually.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void perpetuallyChangeFunctionValue(int nodeNumber, boolean knockIn) throws ParamDefinitionException, NotExistingNodeException{
		if(nodeNumber < 0 || nodeNumber > this.getNodesNumber())
			throw new ParamDefinitionException("The node number value must be between 0 and " + this.getNodesNumber());

		this.geneRegulatoryNetwork.getFunction(nodeNumber).perpetuallyMutationActivation(knockIn);

	}
	
	/**
	 * This method modify an existing function perpetually.
	 * @param nodeName: the node name
	 * @param knockIn: the fixed function value
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void perpetuallyChangeFunctionValue(String nodeName, boolean knockIn) throws ParamDefinitionException, NotExistingNodeException{
		if(nodeName == null)
			throw new ParamDefinitionException("The node name must be not null!");
		int nodeNumber = this.geneRegulatoryNetwork.getNodeNumber(nodeName);
		this.geneRegulatoryNetwork.getFunction(nodeNumber).perpetuallyMutationActivation(knockIn);

	}

	/**
	 * This method restores a perpetually mutated function.
	 * @throws ParamDefinitionException 
	 * @throws NotExistingNodeException 
	 */
	public void restoreFunction(int nodeNumber) throws ParamDefinitionException, NotExistingNodeException{
		if(nodeNumber < 0 || nodeNumber > this.getNodesNumber())
			throw new ParamDefinitionException("The node number value must be between 0 and " + this.getNodesNumber());

		this.geneRegulatoryNetwork.getFunction(nodeNumber).perpetuallyMutationDeactivation();

	}


	/**
	 * This method copies an existing graph manager
	 * @return the copied graph manager
	 * @throws ParamDefinitionException 
	 */
	public GraphManager copy() throws ParamDefinitionException{

		GraphManager newGraphManager = new GraphManager();
		newGraphManager.geneRegulatoryNetwork = this.geneRegulatoryNetwork.copy();

		return newGraphManager;
	}

	/**
	 * This method returns the node number, given its name. 
	 * The node number will be -1 if the node doen't exist.
	 * @param nodeName: The name og the node
	 * @return The number of the node in the graph.
	 */
	public int getNodeNumber(String nodeName){
		return this.geneRegulatoryNetwork.getNodeNumber(nodeName);
	}
}
