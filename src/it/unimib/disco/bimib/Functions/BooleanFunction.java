/**
 *
 * This class is the representation of a boolean function and implements the 
 * Function interface.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Functions;
//System imports
import java.util.ArrayList;
//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;


public abstract class BooleanFunction implements Function {

	//Number of inputs
	protected ArrayList<Integer> inputs;

	//Mutation variables
	//The mutation in action variable is set true if the function value doesn't depend from the inputs
	private boolean mutationInAction;
	//The knockin variable is set true if the gene is always on, false if it is always false.
	private boolean knockIn;

	/**
	 * Generic constructor
	 * @param inputs: number of the function inputs
	 * @throws ParamDefinitionException
	 */
	public BooleanFunction(ArrayList<Integer> inputs) throws ParamDefinitionException{
		if(inputs.size() < 0)
			throw new ParamDefinitionException("Wrong number of inputs. Inputs must be greater than 0");
		this.inputs = inputs;
		this.mutationInAction = false;
		this.knockIn = true;
	}

	/**
	 * Default constructor
	 * @throws ParamDefinitionException
	 */
	public BooleanFunction() throws ParamDefinitionException{
		this.inputs = new ArrayList<Integer>();
		this.mutationInAction = false;
		this.knockIn = true;
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

	@Override
	/**
	 * This method evaluates the function with the passed inputs.
	 * @param inputs: Is a Boolean array with the function inputs. 
	 * In this case they must be Booleans.
	 * @return Returns a Boolean value
	 * throws InputTypeException: This exception will lift up when the 
	 * inputs won't be Booleans.
	 */
	public abstract Boolean eval(Object[] inputs) throws InputTypeException, ParamDefinitionException;

	/**
	 * This method returns the number of input for calculate the function of a node
	 * @return the number of input
	 */
	public ArrayList<Integer> getInputs(){
		return this.inputs;
	}

	/**
	 * This method sets the mutationInAction variable state.
	 * @param value: the mutationInAction variable state: true if the knock-in variable is in use, 
	 * false if the knock-in variable isn't in use.
	 */
	public void setMutationInAction(boolean value){
		this.mutationInAction = value;
	}

	/**
	 * This method returns the mutationInAction variable state.
	 * @return the mutationInAction variable state: true if the knock-in variable is in use, 
	 * false if the knock-in variable isn't in use.
	 */
	public boolean getMutationInAction(){
		return this.mutationInAction;
	}

	/**
	 * This method sets the knock-in variable state.
	 * @param knockIn: boolean, true for setting the gene as always on, false fort setting it always off.
	 */
	public void setKnockIn(boolean knockIn){
		this.knockIn = knockIn;
	}

	/**
	 * This method returns the knockIn variable state.
	 * @return the knock-in variable state: false if the gene is always off, true if it always on.
	 */
	public boolean getKnockIn(){
		return this.knockIn;
	}

	/**
	 * This method activates the perpetually mutation
	 * @param knockIn: It is true if the gene will be always active, false if it will be always off.
	 */
	public void perpetuallyMutationActivation(boolean knockIn){
		this.setMutationInAction(true);
		this.setKnockIn(knockIn);
	}

	/**
	 * This method deactivates the perpetually mutation
	 */
	public void perpetuallyMutationDeactivation(){
		this.setMutationInAction(false);
	}

	/**
	 * This method prepare the input as required by the function and
	 * returns it as a String.
	 * @param functionInput: The entire network state
	 * @return
	 */
	public String prepareFunctionInput(Boolean[] functionInput){
		String preparedInput = "";
		for(int i : this.inputs){
			if((Boolean)functionInput[i].equals(Boolean.TRUE))
				preparedInput = preparedInput + "1";
			else
				preparedInput = preparedInput + "0";
		}

		return preparedInput;
	}

	/**
	 * This method returns the input ad a string
	 * @return the input as a string
	 */
	public String toStringInputs(){
		return inputs.toString();
	}
}
