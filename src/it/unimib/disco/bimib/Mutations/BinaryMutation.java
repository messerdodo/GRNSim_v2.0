/**
 * This class manages the mutations
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2013
 * 
 */
package it.unimib.disco.bimib.Mutations;

//System imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

//GRNSim imports
import it.unimib.disco.bimib.Atms.Atm;
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.AttractorsFinder;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.UtilityRandom;

public class BinaryMutation implements Mutation {

	private final boolean KNOCKIN  = true;
	private final boolean KNOCKOUT = false;

	private double[][] atmMatrix;
	private AttractorsFinder sampling;
	private GraphManager graphManager;
	private Atm mutatedAtm;
	
	
	private int[] sensitivity;
	private ArrayList<Integer> avalanches;

	/**
	 * This is the constructor and it is used the atm without normalization for the mutation post simulation
	 * @param graphManager
	 * @param attractorFinder
	 * @param atm
	 */
	public BinaryMutation(GraphManager graphManager, AttractorsFinder attractorFinder, Atm atm) {
		if(atm == null)
			throw new NullPointerException("Atm object must be not null");
		if(attractorFinder == null)
			throw new NullPointerException("Sampling method object must be not null");
		if(graphManager == null)
			throw new NullPointerException("Graph manager must be not null");

		this.atmMatrix = atm.getAtmWithoutNormalization();
		this.sampling = attractorFinder;
		this.graphManager = graphManager;
		this.mutatedAtm = null;

		sensitivity = new int[graphManager.getNodesNumber()];
		for(int i = 0; i < sensitivity.length; i++)
			sensitivity[i] = 0;
		this.avalanches = new ArrayList<Integer>();
		
	}
	
	/**
	 * This is the constructor for the mutation during the simulation
	 * @param graphManager
	 * @param attractorFinder
	 */
	public BinaryMutation(GraphManager graphManager, AttractorsFinder attractorFinder) {

		if(attractorFinder == null)
			throw new NullPointerException("Sampling method object must be not null");
		if(graphManager == null)
			throw new NullPointerException("Graph manager must be not null");

		this.sampling = attractorFinder;
		this.graphManager = graphManager;
		this.mutatedAtm = null;
		
		sensitivity = new int[graphManager.getNodesNumber()];
		for(int i = 0; i < sensitivity.length; i++)
			sensitivity[i] = 0;
		this.avalanches = new ArrayList<Integer>();
		
	}
	
	/**
	 * This method allows to mutated a generic group of node in a specific times during the simulation
	 * @param mutatedNodesNumber: numbers of node to be mutated
	 * @param times: mutation's duration
	 * @param mutationRate: rate of the mutation in attractors
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws NotExistingAttractorsException
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws AttractorNotFoundException 
	 */
	public void mutationGenerator(int mutatedNodesNumber, int times, double mutationRate) throws ParamDefinitionException, NotExistingAttractorsException, ArrayIndexOutOfBoundsException, NotExistingNodeException, InputTypeException, AttractorNotFoundException{

		int mutatedStates;
		@SuppressWarnings("unused")
		String initialStringState, sSelectedState, sMutatedState;
		
		Object[] attractors = this.sampling.getAttractors();

		//Copies the attractor's array into an ArrayList
		ArrayList<Object> attractorVet = new ArrayList<Object>();

		for(int index = 0; index < attractors.length; index++){
			attractorVet.add(attractors[index]);
		}
		
		//Chooses a group of node to be mutated
		ArrayList<Integer> mutatedNodes = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), mutatedNodesNumber);
		
		//For every attractors considers every state in it
		for(int i = 0; i < attractors.length; i++){
			Object[] statesInAttractor = this.sampling.getStatesInAttractor(attractors[i]);
			ArrayList<String> selectedStates = new ArrayList<String>();
			mutatedStates = 0;
			do{
				//Select the initial state in order to perform the mutation
				do{
					initialStringState = (String)statesInAttractor[UtilityRandom.randomUniform(0, statesInAttractor.length)];
				}while(selectedStates.contains(initialStringState));
				
				selectedStates.add(initialStringState);
				Object mutatedState = doMutation(mutatedNodes, times, initialStringState);
				
				sSelectedState = (String)initialStringState;
				sMutatedState = (String)mutatedState;
				
				//Gets the new state's attractor
				Object attractorNewState = this.sampling.getAttractor(mutatedState);
				
				//Verifies if the new state is just considered
				if(!attractorVet.contains(attractorNewState)){
					//If not it recreates the atm with the new attractor
					reCreateAtm(this.sampling.getAttractors());
					this.atmMatrix[i][attractorVet.size()] =+ 1;
					attractorVet.add(attractorNewState);
				}
				else{//If yes it increases the occurrences of the attractor
					this.atmMatrix[i][attractorVet.indexOf(attractorNewState)] =+ 1;
				}
				mutatedStates ++;
			}while(mutatedStates <= Math.floor(statesInAttractor.length * mutationRate));
			
			//Creates the mutated Atm
			this.mutatedAtm = new Atm(this.sampling, this, this.atmMatrix);
		}
	}
	
	/**
	 * This method allows to mutate temporally a set of nodes after the simulation
	 * @param graphManager
	 * @param mutationRate: rate of the mutation in attractors
	 * @param mutatedNodesSet: set of nodes to be mutated
	 * @param perpetualType: rate of permanent mutation
	 * @param times: mutation's duration
	 * @param knockInRate: rate of the node's activation
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws NotExistingAttractorsException
	 * @throws AttractorNotFoundException 
	 */
	public void mutationGenerator(GraphManager graphManager, double mutationRate, 
			ArrayList<Integer> mutatedNodesSet, double perpetualType, int times, 
			double knockInRate) throws ParamDefinitionException, 
			NotExistingNodeException, InputTypeException, NotExistingAttractorsException, AttractorNotFoundException{

		//Prepares the inputs for the mutation.
		int perpetualMutations = (int)(Math.floor(mutatedNodesSet.size() * perpetualType));

		HashMap<Integer, Boolean> perpetualMutationsSet = new HashMap<Integer, Boolean>();
		HashMap<Integer, Boolean> notPerpetualMutationsSet = new HashMap<Integer, Boolean>();
		HashMap<Integer, Integer> notPerpetualMutationsSetTimes = new HashMap<Integer, Integer>();

		for(Integer node : UtilityRandom.randomSubset(mutatedNodesSet, perpetualMutations)){
			perpetualMutationsSet.put(node, 
					UtilityRandom.randomBooleanChoice(knockInRate));
		}

		//Defines the temporary mutated nodes
		for(int i = 0; i < mutatedNodesSet.size(); i++){
			if(!perpetualMutationsSet.keySet().contains(mutatedNodesSet.get(i))){
				notPerpetualMutationsSet.put(mutatedNodesSet.get(i), 
						UtilityRandom.randomBooleanChoice(knockInRate));
				notPerpetualMutationsSetTimes.put(mutatedNodesSet.get(i), times);
			}
		}

		//Executes the mutation process with the given features.
		this.mutationGenerator(graphManager, mutationRate, perpetualMutationsSet, notPerpetualMutationsSet, notPerpetualMutationsSetTimes);
	}

	/**
	 * This method allows to mutate permanently a set of nodes after the simulation
	 * @param graphManager
	 * @param mutationRate: rate of the mutation in attractors
	 * @param perpertualMutatedNodes:
	 * @param notPerpertualMutatedNodes:
	 * @param notPerpertualMutatedTimes:
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws NotExistingAttractorsException
	 * @throws AttractorNotFoundException 
	 */
	public void mutationGenerator(GraphManager graphManager, double mutationRate, HashMap<Integer, Boolean> perpetualMutatedNodes, 
			HashMap<Integer, Boolean> notPerpetualMutatedNodes, HashMap<Integer, Integer> notPerpetualMutationsTimes) throws ParamDefinitionException, NotExistingNodeException, InputTypeException, NotExistingAttractorsException, AttractorNotFoundException{

		int mutatedStates;
		Object[] statesInAttractor;
		ArrayList<Object> selectedStates;
		Object[] newAttractor;
		Object selectedState, newState, found;
		ArrayList<Object> newAttractorsList;

		//Gets all the attractors and their states.
		this.sampling.clearAndStore();

		HashMap<Object, Object[]> oldAttractors = this.sampling.getStoredAttractors();

		//Sets the perpetual mutations
		for(int node : perpetualMutatedNodes.keySet()){
			if(perpetualMutatedNodes.get(node).equals(Boolean.TRUE))
				graphManager.getGraph().getFunction(node).perpetuallyMutationActivation(KNOCKIN);
			else
				graphManager.getGraph().getFunction(node).perpetuallyMutationActivation(KNOCKOUT);
		}

		//Defines the new attractors from the olds.
		Object[] statesInNewAttractor;
		for(Object attractorFirstState : oldAttractors.keySet()){
			statesInNewAttractor = oldAttractors.get(attractorFirstState);
			for(Object state : statesInNewAttractor)
				this.sampling.getAttractor(state);
		}
		

		Collection<Object[]> storedOldAttractors = oldAttractors.values();
		
		/* 
		 * Calculates the avalanches and the sensitivity.
		 */
		for(Object[] oldAttractor : storedOldAttractors){
			
			selectedStates = new ArrayList<Object>();
			mutatedStates = 0; //Number of states mutated in the specified attractor.
			
			do{
				//Selects randomly a state in the current attractor in order to perform the mutations
				do{
					selectedState = oldAttractor[UtilityRandom.randomUniform(0, oldAttractor.length)];
				}while(selectedStates.contains(selectedState));
				selectedStates.add(selectedState);
				
				//Does the mutations and gets the corresponding attractor
				newState = this.doMutation(notPerpetualMutationsTimes, notPerpetualMutatedNodes, selectedState);
				found = this.sampling.getAttractor(newState);
				newAttractor = this.sampling.getStatesInAttractor(found);
	
				//Calculates the minimum avalanche and the associated sensitivity
				this.avalanchesAndSensitivityCalulator(oldAttractor, newAttractor, graphManager.getNodesNumber());
		
				mutatedStates ++;
			}while(mutatedStates <= Math.floor(oldAttractor.length * mutationRate));
			
		}
		
		/* 
		 *Calculates the new TES Tree 
		 */
		
		//Initializes the new atm matrix
		int attractorsNumber = this.sampling.getAttractorsNumber();
		this.atmMatrix = new double[attractorsNumber][attractorsNumber];
		for(int i = 0; i < this.atmMatrix.length; i++)
			for(int j = 0; j < this.atmMatrix.length; j++)
				this.atmMatrix[i][j] = 0;
	
		//Adds the found attractors in a local list
		newAttractorsList = new ArrayList<Object>();
		for(Object a : this.sampling.getAttractors()){
			newAttractorsList.add(a);
		}
		
		//Calculates the ATM matrix using the doMutation method
		for(Object attractor : this.sampling.getAttractors()){
			//Retrieves the states in the old selected attractor.
			statesInAttractor = this.sampling.getStatesInAttractor(attractor);
			selectedStates = new ArrayList<Object>();
			mutatedStates = 0; //Number of states mutated in the specified attractor.
			do{
				do{
					selectedState = statesInAttractor[UtilityRandom.randomUniform(0, statesInAttractor.length)];
				}while(selectedStates.contains(selectedState));

				selectedStates.add(selectedState);
				newState = this.doMutation(notPerpetualMutationsTimes, notPerpetualMutatedNodes, selectedState);
				
				found = this.sampling.getAttractor(newState);
				if(!newAttractorsList.contains(found)){
					newAttractorsList.add(found);
					this.reCreateAtm(newAttractorsList.toArray());
				}	
				this.atmMatrix[newAttractorsList.indexOf(attractor)][newAttractorsList.indexOf(found)] += 1;	

				mutatedStates ++;

			}while(mutatedStates <= Math.floor(statesInAttractor.length * mutationRate));	
		}
		this.mutatedAtm = new Atm(this.sampling, this, this.atmMatrix);
	}

	/**
	 * This method allows to mutate a specific state
	 * @param times: mutation's duration		
	 * @param mutations: specified mutations that will be done
	 * @param state: specified state where will be the mutation
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public Object doMutation(HashMap<Integer, Integer> times, HashMap<Integer, Boolean> mutations, Object state) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		if(!(state instanceof String))
			throw new ParamDefinitionException("State object must be a string");

		String stringState = (String)state;
		Boolean[] initialState = new Boolean[stringState.length()];
		//Converts the string state in a boolean array
		for(int i = 0; i < initialState.length; i++){
			initialState[i] = stringState.charAt(i) == '1' ? Boolean.TRUE : Boolean.FALSE;
		}

		return doMutation(initialState, times, mutations, 0);
	}
	
	/**
	 * This method allows to mutate a specific state with specific mutation for nodes
	 * @param state: specified state where will be the mutation
	 * @param times: mutation's duration
	 * @param mutations: specified mutations that will be done
	 * @param currentTime: specified the current time of the mutation progress
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	private Object doMutation(Boolean[] state, HashMap<Integer, Integer> times, HashMap<Integer, Boolean> mutations, int currentTime) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{

		//Exit case

		boolean finish = true;
		for(int node : times.keySet()){
			if(!(times.get(node) < currentTime)){
				finish = false;
				break;
			}
		}

		if(finish){
			String stringState = "";
			for(int i = 0; i < state.length; i++)
				stringState += (state[i] == Boolean.TRUE) ? "1" : "0";
			return stringState;//this.sampling.getAttractor(stringState);
		}

		//Recursive case: time > 0
		for(int node : mutations.keySet()){
			if(times.get(node) > 0){
				state[node] = mutations.get(node);
			}
		}

		return doMutation(graphManager.getNewState(state), times, mutations, currentTime + 1);

	}
	
	/**
	 * This method allows to mutated a specific state without specified the specific mutation for nodes
	 * @param state: specified the state that will be mutated
	 * @param mutatedNodes: specified the values of nodes in the state
	 * @param times: mutation's duration
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public Object doMutation(ArrayList<Integer> mutatedNodes, int times, Object state) throws ParamDefinitionException, ArrayIndexOutOfBoundsException, NotExistingNodeException, InputTypeException{

		if(!(state instanceof String))
			throw new ParamDefinitionException("State object must be a string");

		String stringState = (String)state;
		Boolean[] initialState;
		Boolean newValues[] = new Boolean[mutatedNodes.size()];


		//Chooses the random gene values
		for(int i = 0; i < mutatedNodes.size(); i++){
			newValues[i] = UtilityRandom.randomBooleanChoice(0.5);
		}

		initialState = new Boolean[stringState.length()];
		//Converts the binary string into a Boolean array.
		for(int i = 0; i < initialState.length; i++){
			initialState[i] = stringState.charAt(i) == '1' ? Boolean.TRUE : Boolean.FALSE;
		}
		
		//Performs the mutation
		return doMutation(initialState, mutatedNodes, newValues, times);

	}
	
	/**
	 * This method allows to mutated a specific state without specified the specific mutation for nodes
	 * @param state: specified the state that will be mutated
	 * @param mutatedNodes: specified the values of nodes in the state
	 * @param newValues: specified the new values of nodes
	 * @param times: mutation's duration
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	private Object doMutation(Boolean[] state, ArrayList<Integer> mutatedNodes, Boolean[] newValues, int times)throws ArrayIndexOutOfBoundsException, ParamDefinitionException, NotExistingNodeException, InputTypeException{

		//Exit case: Time finished.
		if(times == 0){
			String stringState = "";
			for(int i = 0; i < state.length; i++)
				stringState += (state[i] == Boolean.TRUE) ? "1" : "0";
			return stringState;
		}

		//Recursive case: time > 0
		for(int i = 0; i < mutatedNodes.size(); i++){
			state[mutatedNodes.get(i)] = newValues[i];
		}
		return doMutation(graphManager.getNewState(state), mutatedNodes, newValues, times - 1);

	}

	/**
	 * This method returns the state mutated.
	 * The mutations are only flips and the nodes are randomly selected.
	 * @return the mutated state
	 * @throws ParamDefinitionException
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	public Object doFlips(int nodesForFlip, Object state, int times) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{

		int nodes = this.graphManager.getNodesNumber();
		
		//Parameters checking
		if(!(state instanceof String))
			throw new ParamDefinitionException("State object must be a string");

		String stringState = new String((String)state);
		
		if(nodesForFlip <= 0 && nodesForFlip > nodes)
			throw new ParamDefinitionException("The number of nodes to flip is negative or too big");


		int index = 0;
		ArrayList<Integer> flippedNodes = new ArrayList<Integer>();
		
		//Selects the nodes to flip
		while(index < nodesForFlip){
			//Selects a random number in [1, nodesForFlip]
			int random = UtilityRandom.randomUniform(0, nodes);
			//If the number is already selected try another number
			while(flippedNodes.contains(random)){
				random = UtilityRandom.randomUniform(0, nodes);
			}
			//Adds the number in the ArrayList
			flippedNodes.add(random);
			index++;
		}

		//Converts the state in a Boolean array
		Boolean[] booleanState = new Boolean[this.graphManager.getNodesNumber()];
		for(int i = 0; i < nodes; i++)
			booleanState[i] = (stringState.charAt(i) == '1' ? Boolean.TRUE : Boolean.FALSE);
		
		//Performs the temporally flipping
		Boolean[] newBooleanState = (Boolean[]) doFlips(flippedNodes, booleanState, times, 0);
		
		//Converts the obtained state in a string
		String flippedState = "";
		for(int i = 0; i < newBooleanState.length; i++)
			flippedState += (newBooleanState[i] == Boolean.TRUE ? "1" : "0");
		
		return flippedState;	
	}
	
	/**
	 * This method returns the state mutated.
	 * The mutations are only flips and the nodes are randomly selected.
	 * @return the mutated state
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException
	 */
	private Object[] doFlips(ArrayList<Integer> flippedNodes, Object[] state, int times, int currentTime) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		if(currentTime == times)
			return state;
		
		//Performs the flips
		for(int gene : flippedNodes){
			state[gene] = (state[gene] == Boolean.TRUE ? Boolean.FALSE : Boolean.TRUE);
		}
		
		Boolean[] newState = this.graphManager.getNewState(state);
		//Applies the states function
		return doFlips(flippedNodes, newState, times, currentTime + 1);
		
	}
	
	/**
	 * This method recreates the Atm matrix when for a state there isn't its attractors
	 * @param newAttractors : an array with all the attractors of the network
	 * @throws NotExistingAttractorsException
	 */
	private void reCreateAtm(Object[] newAttractors) throws NotExistingAttractorsException{

		int numbersOfNewAttractors = newAttractors.length;

		//Creates the new Atm matrix
		double[][] newAtm = new double[numbersOfNewAttractors][numbersOfNewAttractors];

		//Recopies all the values from the old Atm matrix
		for(int line = 0; line < numbersOfNewAttractors-1; line++){
			for(int pillar = 0; pillar < numbersOfNewAttractors-1; pillar++){
				newAtm[line][pillar] = this.atmMatrix[line][pillar];
			}
		}

		//Adds the values for the new pillar
		for(int pillar = 0; pillar < numbersOfNewAttractors; pillar++){

			newAtm[numbersOfNewAttractors -1][pillar] = 0;
		}

		//Adds the values for the new line
		for(int line = 0; line < numbersOfNewAttractors; line++){
			newAtm[line][numbersOfNewAttractors -1] = 0;
		}
		this.atmMatrix = newAtm;
		//Initializes the new Atm
		//this.atmMatrix = new double[numbersOfNewAttractors][numbersOfNewAttractors];
		//newAtm = this.atmMatrix;

	}
	
	/**
	 * This method returns the mutated atm
	 * @return atm
	 */
	public Atm getMutatedAtm(){
		return this.mutatedAtm;
	}
	
	/**
	 * This method allows to make a random perpetually mutation
	 * @param nodes: specified the number of nodes that will be mutated
	 * @param knockInRate: rate of the nodes activation 
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 */
	public void randomPerpetuallyMutation(int nodes, double knockInRate) throws ParamDefinitionException, NotExistingNodeException{
		if(nodes < 0 || nodes > this.graphManager.getNodesNumber())
			throw new ParamDefinitionException("The nodes must be between 0 and " + this.graphManager.getNodesNumber());

		ArrayList<Integer> selectedNodes = new ArrayList<Integer>();
		int knockInGenes = 0;

		int node;
		do{
			//Chooses the node randomly
			node = UtilityRandom.randomUniform(0, this.graphManager.getNodesNumber()-1);
			if(!selectedNodes.contains(node)){
				selectedNodes.add(node);
				//Check the knockIn genes number
				if(knockInGenes < Math.floor(knockInRate * nodes)){
					knockInGenes++;
					//Knock in the gene
					this.graphManager.perpetuallyChangeFunctionValue(node, true);
				}else{
					//Knock out the gene
					this.graphManager.perpetuallyChangeFunctionValue(node, false);
				}
			}

		}while(selectedNodes.size() < nodes);

	}
	
	/**
	 * This method allows to make a perpetually mutation in a specific node
	 * @param nodes: specified the node that will be mutated
	 * @param knockIn: specified if is a knockOut or a knockIn
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 */
	public void perpetuallyMutation(int node, boolean knockIn) throws ParamDefinitionException, NotExistingNodeException{
		if(node < 0 || node >= this.graphManager.getNodesNumber())
			throw new ParamDefinitionException("The nodes must be between 0 and " + (this.graphManager.getNodesNumber() -1));

		//KnockIn/knockOut the gene
		this.graphManager.perpetuallyChangeFunctionValue(node, true);

	}
	
	/**
	 * This method returns all attractors mutated
	 * @return sampling
	 */
	public AttractorsFinder getMutatedAttractorFinder(){
		return this.sampling;
	}
	
	/**
	 * This method returns the avalanche of the network considered
	 * @return avalanche
	 */
	public ArrayList<Integer> getAvalanches(){
		return this.avalanches;
	}
	
	/**
	 * This method returns the sensitivity of the network considered
	 * @return sensitivity
	 */
	public int[] getSensitivity(){
		return sensitivity;
	}
	
	/**
	 * This method allows to calculate the avalanche and the sensitivity contribution
	 * performing all the possible state shifts in the two passed attractors. 
	 * @param oldAttractor : the first attractor. It must be a String array
	 * @param newAttractor : the second attractor. It must be a String array.
	 * @param genesNumber : The number of genes in the network
	 */
	private void avalanchesAndSensitivityCalulator(Object[] oldAttractor, Object[] newAttractor, int genesNumber){

		int minAvalanche = genesNumber + 1; //The avalanche must be less or equal than the number of the genes
		int i, j, avalanche;
		ArrayList<Integer> positions;
		int localSensitivity[], minSensitivity[] = null;
		
		//Performs all the possible attractor states shifts.
		for(int shift = 0; shift < newAttractor.length; shift++){

			avalanche = 0;
			i = 0;
			j = shift;
			//Initializes the unchanged genes arraylist and the local sensitivity
			//for the current shifting test
			positions = new ArrayList<Integer>();
			localSensitivity = new int[genesNumber];
			for(int gene = 0; gene < genesNumber; gene++){
				positions.add(gene);
				localSensitivity[gene] = 0;
			}
				

			for(int k = 0; k < lcm(oldAttractor.length, newAttractor.length); k++){
				String stateInOldAttractor = (String) oldAttractor[i];
				String stateInNewAttractor = (String) newAttractor[j];
				
				//Compares gene by gene the two states of the attractors
				for(int gene = 0; gene < positions.size(); gene++){
					if(stateInOldAttractor.charAt(positions.get(gene)) != stateInNewAttractor.charAt(positions.get(gene))){
						localSensitivity[positions.get(gene)] += 1;
						avalanche ++;
						positions.remove(gene);
					}	
				}
				//Define the new state index for both the attractors
				i = (i + 1) % oldAttractor.length;
				j = (j + 1) % newAttractor.length;
			}

			//Checks if this shift has produced the minimum avalanche
			if(avalanche < minAvalanche){
				//Updates the minimum avalanche and the corresponding sensitivity configuration
				minAvalanche = avalanche;
				minSensitivity = localSensitivity;
			}
		}
		
		//Saves the minimum avalanche calculated.
		this.avalanches.add(minAvalanche);
		//Seves the sensitivity associated to the minimum avalanche
		for(int gene = 0; gene < genesNumber; gene++){
			this.sensitivity[gene] += minSensitivity[gene];
		}

	}
	
	/**
	 * This method returns the least common multiple (lcm) between two values.
	 * @param x The first integer and positive number
	 * @param y The second integer and positive number
	 * @return the lcm of x and y
	 */
	private int lcm (int x, int y){
		return (x * y)/gcd(x, y);
	}
	
	/**
	 * This method returns the greatest common divisor (GCD) between two values 
	 * using the Euclid's algorithm.
	 * @param x The first integer and positive number
	 * @param y The second integer and positive number
	 * @return The GCD of x and y
	 */
	private int gcd(int x, int y){
		if(y == 0)
			return x;
		else
			return gcd(y, x % y);
	}
}
