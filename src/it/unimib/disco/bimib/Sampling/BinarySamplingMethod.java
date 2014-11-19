/**
 * This class is an abstract class that implements the AttractorFinder interface.
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

public abstract class BinarySamplingMethod implements AttractorsFinder {

	protected GraphManager graph;
	private HashMap<String, Double> fluctuations;
	protected HashMap<Object, Object[]> storedInformation;
	protected int attractorNotFound;


	/**
	 * Generic constructor
	 * @param manager: a graph manager object connected to the network
	 */
	public BinarySamplingMethod(GraphManager manager){
		if(manager != null)
			this.graph = manager;
		else
			throw new NullPointerException("No valid graphManager specified");
		this.fluctuations = new HashMap<String, Double>();

		this.storedInformation = null;
		this.attractorNotFound = 0;
	}


	/**
	 * This method converts an integer number in a boolean array
	 * @param state: The integer value. It must be grater or equal than 0
	 * @param n: the number of binary digit to be use
	 * @return a boolean array representing the network status
	 * @throws ParamDefinitionException An error occurred during the conversion
	 */
	protected Boolean[] fromIntegerToBinaryArray(int state, int n) throws ParamDefinitionException{
		//Checks the parameters
		if(state < 0 || n < 0)
			throw new ParamDefinitionException("Incorrect parameters");
		//Converts the integer value in a binary string
		String stringBinaryInt = Integer.toBinaryString(state);
		Boolean[] converted = new Boolean[n];
		int index = n - 1;
		//Copies the string in the array and adds the other false values. 
		//The array must have n values.
		for(int i = stringBinaryInt.length() - 1; i >= 0; i--){
			converted[index] = (stringBinaryInt.charAt(i) == '1' ? Boolean.TRUE : Boolean.FALSE);
			index--;
		}
		for(; index >= 0; index--){
			converted[index] = Boolean.FALSE;
		}
		//Returns the converted array
		return converted;

	}


	/**
	 * This method converts a boolean array in a binary string
	 * @param state: The boolean array. 
	 * @return a binary string representing the network status
	 */
	protected String fromArrayToString(Boolean[] state){
		//Checks the parameter
		if(state == null)
			throw new NullPointerException("The array mustn't be null");
		String converted = "";
		//Creates the binary string string
		for(int i = 0; i < state.length; i++){
			converted += (state[i] ? "1" : "0");
		}
		//Returns the converted string value
		return converted;
	}

	/**
	 * This method converts a binary string in a boolean array.
	 * @param state: the binary string
	 * @return a boolean array with n elements.
	 */
	protected Boolean[] fromStringToArray(String state){
		Boolean[] converted = new Boolean[state.length()];
		//Converts each binary char in a Boolean value
		for(int i = 0; i < state.length(); i++){
			converted[i] = (state.charAt(i) == '1' ? Boolean.TRUE : Boolean.FALSE);
		}
		//Returns the converted object
		return converted;
	}

	/**
	 * This method returns the array of states in the given attractor
	 * @param The attractor
	 * @return The array of status
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	public Object[] getStatesInAttractor(Object attractor) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		ArrayList<String> statesInAttractor;
		String newState;

		double fluctuation = 0.0;

		//Checks the inputs type. In this implementation it must be String
		if(!(attractor instanceof String))
			throw new ParamDefinitionException("Attractor must be a string");
		//Algorithm initialization
		statesInAttractor = new ArrayList<String>();
		newState = (String) attractor;

		do{
			statesInAttractor.add(newState);
			newState = this.fromArrayToString(this.graph.getNewState(this.fromStringToArray(newState)));
		}while(!(newState.equals((String) attractor)));

		if(!this.fluctuations.containsKey(attractor)){

			for(int i = 0; i < ((String)attractor).length(); i++){
				char first = statesInAttractor.get(0).charAt(i);
				int j = 1;
				boolean oscillating = false;
				while(j < statesInAttractor.size() && !oscillating){
					oscillating = statesInAttractor.get(j).charAt(i) != first;
					j++;
				}
				//Verifies if the nodes of one state is different from another
				//If there is only a change in a node is considered oscillating
				if(oscillating)
					fluctuation ++;
			}
			this.fluctuations.put((String)attractor, fluctuation/((String)attractor).length());
		}		

		//Returns the list of states in the given attractor
		return statesInAttractor.toArray();

	}

	/**
	 * This method returns for each attractor the percentage of nodes oscillating
	 * @return the ratio of oscillating nodes of the network
	 */
	public double getOscillatingNodesRatio(){
		double ratio = 0.0;
		double attractors = this.fluctuations.keySet().size();
		for(String key : this.fluctuations.keySet()){
			ratio = ratio + this.fluctuations.get(key);
		}
		//Returns the ratio of oscillating nodes of the network.
		return ratio/attractors;

	}

	/**
	 * This method returns the number of the attractors.
	 * @return the number of the attractors
	 */
	public int getAttractorsNumber(){
		return this.getAttractors().length;
	}
	
	/**
	 * This method returns the passed attractor length
	 * @param attractor: the attractor 
	 * @return the attractor length
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 */
	public int getAttractorLength(Object attractor) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		if(attractor == null)
			throw new NullPointerException("The attractor must be not null");
		return this.getStatesInAttractor(attractor).length;
	}

	/**
	 * This method returns the number of times that the attractor does not found
	 * @return the number of times when the attractor does not found.
	 */
	public int getAttractorsNotFound(){
		return this.attractorNotFound;
	}

}
