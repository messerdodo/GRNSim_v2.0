/**
 * This class is the representation of a boolean function with random outputs.
 *
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 */

package it.unimib.disco.bimib.Functions;

//System imports
import java.util.ArrayList;
import java.util.HashMap;


//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.UtilityRandom;

public class RandomFunction extends BooleanFunction {

	/*
	 * Internal function representation. 
	 * Each binary input is converted to a decimal number. 
	 * The function output is stored in the input position of the array.
	 */
	private HashMap<String, Boolean> functionTable;

	private double bias;

	/**
	 * This is a constructor for the RandomFunction class.
	 * @param inputs is the number of the inputs of the function.
	 * @param bias is the true values rate
	 * @param completelyDefined is true if you want a completely defined function
	 * @throws InputTypeException: wrong parameters passed
	 */
	public RandomFunction(ArrayList<Integer> inputs, double bias, boolean completelyDefined) throws ParamDefinitionException{
		//Checks the value of the parameters
		super(inputs);
		if(bias < 0 || bias > 1)
			throw new ParamDefinitionException("Wrong bias value! Bias must be between 0 and 1");

		this.functionTable = new HashMap<String, Boolean>();
		this.bias = bias;
		
		String binaryInputs;
		//Defines the function
		if(completelyDefined){
			for(int i = 0; i < Math.pow(2, inputs.size()); i++){
				binaryInputs = Integer.toBinaryString(i);
				while(binaryInputs.length() < inputs.size()){
					binaryInputs = "0" + binaryInputs;
				}
				//Populates the function table
				this.functionTable.put(binaryInputs, UtilityRandom.randomBooleanChoice(bias));	
			}
		}

			
	}

	/**
	 * This is a constructor for the RandomFunction class.
	 * The bias is 0.5
	 * @param inputs is the number of the inputs of the function.
	 * @param completelyDefined is true if you want a completely defined function
	 * @throws ParamDefinitionException wrong parameter passed
	 */
	public RandomFunction(ArrayList<Integer> inputs, boolean completelyDefined) throws ParamDefinitionException{
		this(inputs, 0.5, completelyDefined);
	}

	public RandomFunction(double bias, String[] inputs, Boolean[] outputs, ArrayList<Integer> inputNodes) throws ParamDefinitionException{
		super();
		if(bias < 0 || bias > 1)
			throw new ParamDefinitionException("Bias value must be between 0 and 1");
		if(inputs == null || outputs == null)
			throw new ParamDefinitionException("Inputs and outputs must be not null");
		if(inputs.length != outputs.length)
			throw new ParamDefinitionException("The number of inputs and outpust must be the same");

		this.bias = bias;
		super.inputs = inputNodes;

		functionTable = new HashMap<String, Boolean>();
		//Create the functionTable as passed
		for(int i = 0; i < inputs.length; i++){
			functionTable.put(inputs[i], outputs[i]);
		}
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
	public Boolean eval(Object[] input) throws InputTypeException, ParamDefinitionException{
		//Check the correct input type (Boolean)
		if(!(input instanceof Boolean[]))
			throw new InputTypeException("Wrong input type. Inputs must be Boolean");

		//Mutation in action flow
		if(super.getMutationInAction())
			return super.getKnockIn();

		String inputValue = super.prepareFunctionInput((Boolean[]) input);

		//Checks if the input is already in the table 
		if(! this.functionTable.containsKey(inputValue)){
			//Stores the new input and the corresponding Boolean result
			this.functionTable.put(inputValue, UtilityRandom.randomBooleanChoice(bias));
		}
		//Returns the function value
		return this.functionTable.get(inputValue);
	}

	/**
	 * This method returns the function xml representation.
	 * @return: the xml function representation
	 */
	public String toGRNML(){
		String xmlFunction = "\t\t\t<function type = \"random\" input_number = \""+ super.inputs.size() + "\">\n";

		xmlFunction += "\t\t\t\t<bias> " + bias + " </bias>\n";
		for(int input : super.inputs)
			xmlFunction += "\t\t\t\t\t<input_node >" + input +
			"</input_node>\n";
		//Writes all the entries
		for(String key : this.functionTable.keySet()){
			xmlFunction += "\t\t\t\t\t<entry input = \"" + key + 
					"\" output = \"" + (this.functionTable.get(key).equals(Boolean.TRUE) ? "1" : "0") + "\">\n";
			xmlFunction += "\t\t\t\t\t</entry>\n";
		}
		xmlFunction += "\t\t\t</function>\n";
		return xmlFunction;
	}

	/**
	 * This method return the bias of the function
	 * @return bias
	 */
	public double getBias(){
		return this.bias;
	}

	/**
	 * This method adds a new function's input to it.
	 * Note: The function table will be deleted. All the previous values will be lost.
	 * @param inputLink: The new input link 
	 * @throws ParamDefinitionException 
	 */
	public void addLink(int inputLink) throws ParamDefinitionException{
		if(inputLink < 0)
			throw new ParamDefinitionException("The inputLink must be greater than 0");
		super.inputs.add(inputLink);
		this.functionTable.clear();
	}

	/**
	 * This method copies the function.
	 * @return a copy of the function.
	 */
	public Function copy() throws ParamDefinitionException{
		ArrayList<Integer> newInputs = new ArrayList<Integer>();
		for(Integer i : super.inputs)
			newInputs.add(i);

		RandomFunction newRand = new RandomFunction(newInputs, this.bias, false);
	
		for(String input : this.functionTable.keySet())
			newRand.functionTable.put(input, this.functionTable.get(input));

		return newRand;	
	}

	/**
	 * This method returns the type of the function as a String
	 * @return Function type
	 */
	public String getType(){
		return (bias == 0.5 ? "Random" : "Random with bias");
	}

	@Override
	/**
	 * This method return the function table as a string, string hash map
	 */
	public HashMap<String, String> getTable() {
		HashMap<String, String> funcTable = new HashMap<String, String>();
		for(String inputs : this.functionTable.keySet())
			funcTable.put(inputs, this.functionTable.get(inputs) == Boolean.TRUE ? "1" : "0");
		return funcTable;
	}
	
}
