/**
 * This class implements the partial sampling methods.
 * It tests only a subset of all the possible initial states.
 * This implementation is suggested for networks with a lot of nodes.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Sampling;

//System imports
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;

import it.unimib.disco.bimib.Utility.UtilityRandom;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.GraphManager;

public class PartialSampling extends BinarySamplingMethod {

	private final double ACTIVATION_PROBABILITY = 0.5;

	//This hash map contains the founded attractors 
	private HashMap<String, String> attractors;
	//This hash map contains the positions of a state in the attractor transient
	private HashMap<String, Integer> positions;
	//This has map contains the old attractor when a permanent perturbation is made
	private HashMap<String, String> oldAttractors;
	
	private int cutoff;

	/**
	 * Generic constructor
	 * @param manager: The graph manager
	 */
	public PartialSampling(GraphManager manager){
		super(manager);
		this.attractors = new HashMap<String, String>();
		this.positions = new HashMap<String, Integer>();
		this.cutoff = -1;
	}

	/**
	 * Generic constructor
	 * @param manager: It's the connected Graph manager in which there is the network
	 * @param steps: The number of the steps that must be performed in order to calculate the attractors
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 */
	public PartialSampling(GraphManager manager, int initialConditions, int cutoff) throws ParamDefinitionException, NotExistingNodeException, InputTypeException, AttractorNotFoundException {
		super(manager);
		this.attractors = new HashMap<String, String>();
		this.positions = new HashMap<String, Integer>();
		this.cutoff = cutoff;
		//Calculates a set of attractors
		this.calculatesAttractors(initialConditions);
	}

	/**
	 * This method calculate the attractors testing only steps initial states.
	 * Each initial state is randomly selected from all the initial states.
	 * @param steps: Number of initial condition to be test
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 */
	private void calculatesAttractors(int initialConditions) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		String state;
		int nodes = this.graph.getNodesNumber();

		for(int i = 0; i < initialConditions; i++){
			//Generates a new random state
			state = UtilityRandom.createRandomBinarySequence(nodes, ACTIVATION_PROBABILITY);
			//Calculates the attractor for the generated state
			try{
				this.searchAttractorWithInitialState(state, this.cutoff);
			}catch(AttractorNotFoundException e){
				//No attractor found
				super.attractorNotFound = super.attractorNotFound + 1;
			}
		}
	}

	/**
	 * This method calculates the attractor starting with the 'state' initial state.
	 * @param state: The initial state to be test
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 */
	private void searchAttractorWithInitialState(String state, int cutoff) throws ParamDefinitionException, NotExistingNodeException, InputTypeException, AttractorNotFoundException{

		//Generates a new random initial state
		String currentState = state;
		String currentAttractor;
		ArrayList<String> visited;
		String newState;
		int count = 1;
		currentAttractor = null;
		newState = null;
		visited = new ArrayList<String>();
		do{
			//Stores the position for the transient
			this.positions.put(currentState, count);
			count ++;
			//Stores the current state as visited
			visited.add(currentState);
			//Perform the new network state

			newState = super.fromArrayToString(this.graph.getNewState(super.fromStringToArray(currentState)));

			if(visited.contains(newState)){
				currentAttractor = newState;

			}else if(this.attractors.containsKey(newState)){
				currentAttractor = attractors.get(newState);
				if(this.positions.get(currentAttractor) < this.positions.get(newState) +
						this.positions.get(currentAttractor) - this.positions.get(state)){

					this.positions.put(currentAttractor, 
							this.positions.get(currentAttractor) + 
							this.positions.get(newState) - 
							this.positions.get(state));
				}
			}
			if(currentAttractor != null){
				for(String s : visited){
					this.attractors.put(s, currentAttractor);
				}
			}else{
				currentState = newState;
			}
		}while(currentAttractor == null && (count <= cutoff || cutoff == -1 ));
		
		//Attractor not found
		if((cutoff != -1) && (count > cutoff)){
			throw new AttractorNotFoundException();
		}
	}

	/**
	 * This method returns the attractor of a given network status.
	 * The status must be a binary string; it's value must be between 0 and the 2^n - 1 
	 * where n is the number of the nodes in the network.
	 * @param status: The string value representing the network status.
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 * @ParamDefinitionException: The passed status isn't correct, it doesn't follow 
	 * the previous rules.
	 */
	@Override
	public Object getAttractor(Object status) throws ParamDefinitionException, NotExistingNodeException, InputTypeException {
		//Checks if the status is a string
		if(!(status instanceof String))
			throw new ParamDefinitionException("The staus must be a string value");
		
		//The attractor of the passed state is unknown
		if(!this.attractors.containsKey(status)){
			try{
				//Calculates the attractor for the given state
				this.searchAttractorWithInitialState((String)status, this.cutoff);
				//Returns the required attractor
				return this.attractors.get(status);
			}catch(AttractorNotFoundException e){
				return null;
			}
		}
		return this.attractors.get(status);	
	}

	/**
	 * This method return an array with all the calculated attractors.
	 * Each attractor is represented as a binary string.
	 * @return a String array with the attractors is returned.
	 */
	@Override
	public Object[] getAttractors() {
		ArrayList<String> attractorsList = new ArrayList<String>();
		Object[] states = this.attractors.values().toArray();
		for(int i = 0; i < this.attractors.size(); i++){
			if(!attractorsList.contains((String)states[i])){
				attractorsList.add((String)states[i]);
			}
		}
		//Returns the calculated attractors
		return attractorsList.toArray();
	}

	/**
	 * This method returns the length of the transient states in a network
	 * @return the length of transients
	 */
	@Override
	public ArrayList<Integer> getTransientLength() {
		ArrayList<Integer> transientLength = new ArrayList<Integer>();
		Object[] attractorsArray = this.getAttractors();
		for(Object attractor : attractorsArray){
			transientLength.add(this.positions.get(attractor));
		}
		return transientLength;
	}

	/**
	 * This method returns the basin of attractions of each attractors.
	 * @return An array list with each basin dimension.
	 */
	public ArrayList<Integer> getBasinOfAttraction(){
		HashMap<String, Integer> basin = new HashMap<String, Integer>();
		ArrayList<Integer> basinDistribuction = new ArrayList<Integer>();
		for(String state : attractors.keySet()){
			if(basin.containsKey(attractors.get(state))){
				basin.put(attractors.get(state), basin.get(attractors.get(state)) + 1);
			}else{
				basin.put(attractors.get(state), 1);
			}
		}
		for(String attractor : basin.keySet())
			basinDistribuction.add(basin.get(attractor));
		return basinDistribuction;

	}

	/**
	 * This method rewires the attractor finder element.
	 * It is used when the perpetual mutations are introduced
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 */
	public void rewiredAttractorFinder() throws ParamDefinitionException, NotExistingNodeException, InputTypeException, AttractorNotFoundException {
		Set<String> oldStates = this.attractors.keySet();
		this.oldAttractors = this.attractors;

		this.attractors = new HashMap<String, String>();
		this.positions = new HashMap<String, Integer>();

		//Calculates the new attractor for the specified state
		for(String state : oldStates){
			this.searchAttractorWithInitialState(state, this.cutoff);
		}

	}

	/**
	 * This method saves the old state of the sampling object.
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public void clearAndStore() throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		this.storedInformation = new HashMap<Object, Object[]>();


		Object[] attractorsSet = this.getAttractors();

		for(Object attractor : attractorsSet){
			this.storedInformation.put(attractor, 
					this.getStatesInAttractor(attractor));
		}

		this.attractors = new HashMap<String, String>();
		this.positions = new HashMap<String, Integer>();

	}

	/**
	 * This method returns the stored attractors.
	 * The returned HashMap has the attractor id as key an an object array, representing the
	 * states in the attractor, as value.
	 * @return The stored attractors
	 */
	public HashMap<Object, Object[]> getStoredAttractors(){
		return this.storedInformation;
	}

	/**
	 * This method returns all the old attractor when is made a permanent perturbation
	 * @return all the old attractors
	 */
	public Object[] getOldAttractors(){
		ArrayList<String> oldAttractorsList = new ArrayList<String>();
		Object[] states = this.oldAttractors.values().toArray();
		for(int i = 0; i < this.oldAttractors.size(); i++){
			if(!oldAttractorsList.contains((String)states[i])){
				oldAttractorsList.add((String)states[i]);
			}
		}
		//Returns the calculated attractors
		return oldAttractorsList.toArray();
	}

	/**
	 * This method returns a copy of itself.
	 * @return a copy of itself.
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 * @throws NullPointerException 
	 */
	public AttractorsFinder copy() throws NullPointerException, ParamDefinitionException, NotExistingNodeException, InputTypeException{

		PartialSampling copiedSampling = new PartialSampling(this.graph);

		HashMap<String, String> copiedAttractors = new HashMap<String, String>();
		HashMap<String, Integer> copiedPositions = new HashMap<String, Integer>();

		for(String state : this.attractors.keySet()){
			copiedAttractors.put(new String(state), new String(this.attractors.get(state)));
			copiedPositions.put(new String(state), new Integer(this.positions.get(state)));
		}

		//Sets the copied attributes in the copied sampling object
		copiedSampling.attractors = copiedAttractors;
		copiedSampling.positions = copiedPositions;
		copiedSampling.cutoff = this.cutoff;
		copiedSampling.oldAttractors = null;

		return copiedSampling;

	}

	@Override
	/**
	 * This method returns the couples state-attractor as an HashMap
	 */
	public HashMap<String, String> getStatesAttractorsCouples() {
		return this.attractors;
	}

	@Override
	/**
	 * This method returns the couples state-position as an HashMap.
	 */
	public HashMap<String, Integer> getStatesPositionsCouples() {
		return this.positions;
	}


}
