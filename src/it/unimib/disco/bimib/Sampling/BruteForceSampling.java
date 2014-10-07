/**
 * This class implements the brute force sampling methods.
 * It tests each possible initial state (2^n states).
 * This implementation isn't suggested for networks with a lot of nodes.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013 
 */

package it.unimib.disco.bimib.Sampling;
//System imports
import java.util.ArrayList;
import java.util.HashMap;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.GraphManager;

public class BruteForceSampling extends BinarySamplingMethod {

	private String[] attractors;
	private int [] positions;
	private String[] oldAttractors;


	/**
	 * Generic constructor
	 * @param graph: A GraphManager object connected to the network
	 * @throws NullPointerException 
	 * @throws ParamDefinitionException An error occurred during a internal conversion
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	public BruteForceSampling(GraphManager graph) throws NullPointerException, ParamDefinitionException, NotExistingNodeException, InputTypeException{
		super(graph);
		this.attractors = new String[(int)Math.pow(2, graph.getNodesNumber())];
		this.positions = new int[(int)Math.pow(2, graph.getNodesNumber())];
		//Calculates all the attractors
		this.calculatesAttractors();
		this.storedInformation = null;
	}

	/**
	 * This methods calculates all the attractors of a given graph.
	 * All the possible states are analyzed. This method is not suggested 
	 * for a network with a lot of nodes.
	 * @throws ParamDefinitionException An error during a conversion 
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	private void calculatesAttractors() throws ParamDefinitionException, NotExistingNodeException, InputTypeException {

		int nodes = this.graph.getNodesNumber();
		int states = this.attractors.length;
		//Generates a new random initial state
		Boolean[] currentState;
		String currentAttractor;
		ArrayList<String> visited;
		Boolean[] newState;
		int count, attractor, newStateIndex;

		//For each possible state gets its attractor
		for(int state = 0; state < states; state++){

			//Convert the i(th) state in a Boolean sequence
			currentState = fromIntegerToBinaryArray(state, nodes);
			currentAttractor = null;
			newState = null;
			visited = new ArrayList<String>();
			count = 1;
			do{
				//Saves the transient
				this.positions[state] = count;
				count ++;

				//Stores the current state as visited
				visited.add(fromArrayToString(currentState));
				//Perform the new network state
				newState = this.graph.getNewState(currentState);
				if(visited.contains(fromArrayToString(newState))){
					currentAttractor = fromArrayToString(newState);
				}else if(this.attractors[Integer.parseInt(fromArrayToString(newState), 2)] != null){
					newStateIndex = Integer.parseInt(fromArrayToString(newState), 2);
					currentAttractor = this.attractors[newStateIndex];
					attractor = Integer.parseInt(currentAttractor,2);
					if(this.positions[attractor] < this.positions[newStateIndex] + 
							this.positions[attractor] - this.positions[state] ){
						this.positions[attractor] += this.positions[newStateIndex] - this.positions[state];
					}
				}

				if(currentAttractor != null){
					for(String s : visited){
						this.attractors[Integer.parseInt(s, 2)] = currentAttractor;
					}
				}else{
					currentState = newState;
				}
			}while(currentAttractor == null);
		}
	}


	/**
	 * This method returns the attractor of a given network status.
	 * The status must be a binary string; it's value must be between 0 and the 2^n - 1 
	 * where n is the number of the nodes in the network.
	 * @param status: The string value representing the network status.
	 * @ParamDefinitionException: The passed status isn't correct, it doesn't follow 
	 * the previous rules.
	 */
	@Override
	public Object getAttractor(Object status) throws ParamDefinitionException {
		int intStatus;
		//Checks if the status is a string
		if(!(status instanceof String))
			throw new ParamDefinitionException("The staus must be a string value");
		//Gets the corresponding integer status
		intStatus = Integer.parseInt((String) status, 2);
		//Checks the value of the status
		if(intStatus >= this.attractors.length)
			throw new ParamDefinitionException("Invalid status");
		//Returns the status attractor
		return this.attractors[intStatus];
	}


	/**
	 * This method returns an array with all the calculated attractors.
	 * Each attractor is represented as a binary string.
	 * @return a String array with the attractors is returned.
	 */
	@Override
	public Object[] getAttractors(){
		//Returns all the attractors
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < this.attractors.length; i++)
			if(!l.contains(this.attractors[i]))
				l.add(this.attractors[i]);
		return l.toArray();
	}

	/**
	 * This method returns the length of the transient states in a network
	 * @return the length of transients
	 */
	public ArrayList<Integer> getTransientLength() {
		ArrayList<Integer> transientLength = new ArrayList<Integer>();

		Object[] attractors = this.getAttractors();
		for(Object attractor : attractors){
			transientLength.add(this.positions[Integer.parseInt(attractor.toString(),2)]);
		}

		return transientLength;
	}

	/**
	 * This method returns the basin of attractions of each attractors
	 * @return HashMap(attractors,#transient)
	 */
	public ArrayList<Integer> getBasinOfAttraction(){
		HashMap<String, Integer> basin = new HashMap<String, Integer>();
		ArrayList<Integer> basinDistribuction = new ArrayList<Integer>();
		for(String attractor : attractors){
			if(basin.containsKey(attractor)){
				basin.put(attractor, basin.get(attractor) + 1);
			}else{
				basin.put(attractor, 1);
			}
		}
		for(String attractor : basin.keySet())
			basinDistribuction.add(basin.get(attractor));
		return basinDistribuction;

	}

	/**
	 * This method rewireds the attractor finder element.
	 * It is used when the perpetual mutations are introduced
	 * @throws ParamDefinitionException Something gone wrong
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	public void rewiredAttractorFinder() throws ParamDefinitionException, NotExistingNodeException, InputTypeException {

		this.oldAttractors = this.attractors;

		this.attractors = new String [this.attractors.length];
		this.positions = new int [this.positions.length];

		//Calculates the new attractors
		this.calculatesAttractors();

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

		this.attractors = new String[this.attractors.length];
		this.positions = new int[this.positions.length];
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
		//Returns all the attractors
		ArrayList<String> oldAttractorlist = new ArrayList<String>();

		for(int i = 0; i < this.oldAttractors.length; i++)
			if(!oldAttractorlist.contains(this.oldAttractors[i]))
				oldAttractorlist.add(this.oldAttractors[i]);
		return oldAttractorlist.toArray();

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
		BruteForceSampling copiedSampling = new BruteForceSampling(this.graph);

		String[] copiedAttractors = new String[this.attractors.length];
		int [] copiedPositions = new int[this.positions.length];

		//Copies the arrays
		for(int i = 0; i < this.attractors.length; i++){
			copiedAttractors[i] = this.attractors[i];
			copiedPositions[i] = this.positions[i];
		}

		//Puts the copies in the new sampling
		copiedSampling.attractors = copiedAttractors;
		copiedSampling.positions = copiedPositions;

		return copiedSampling;

	}

	@Override
	/**
	 * This method returns the couples state-attractor as an HashMap
	 */
	public HashMap<String, String> getStatesAttractorsCouples() {
		HashMap<String, String> statesAttractors = new HashMap<String, String>();
		//Puts the each couple (state, attractor) in the statesAttractors object
		for(int i = 0; i < this.attractors.length; i++)
			statesAttractors.put(Integer.toBinaryString(i), this.attractors[i]);

		return statesAttractors;
	}

	@Override
	/**
	 * This method returns the couples state-position as an HashMap.
	 */
	public HashMap<String, Integer> getStatesPositionsCouples() {
		HashMap<String, Integer> statesPositions = new HashMap<String, Integer>();
		//Puts the each couple (state, position) in the statesPositions object
		for(int i = 0; i < this.positions.length; i++)
			statesPositions.put(Integer.toBinaryString(i), this.positions[i]);
		return statesPositions;
	}

}
