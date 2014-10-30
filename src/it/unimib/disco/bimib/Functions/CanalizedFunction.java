/**
 * This class is the representation of a canalized function with random outputs.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 */

package it.unimib.disco.bimib.Functions;

//System imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.UtilityRandom;

public class CanalizedFunction extends BooleanFunction {

	private int[] usefullInputs;
	private HashMap <String, Boolean> functionTable;
	private double bias;

	/**
	 * Generic constructor
	 * @param inputs: number of function inputs
	 * @param bias: true values ratio
	 * @param completelyDefined is true if you want a completely defined function
	 * @throws ParamDefinitionException
	 */
	public CanalizedFunction(ArrayList<Integer> inputs, double bias, boolean completelyDefined) throws ParamDefinitionException {
		super(inputs);

		Integer[] correctFormat = new Integer[inputs.size()];
		for(int i = 0; i < inputs.size(); i++)
			correctFormat[i] = inputs.get(i);

		this.usefullInputs = UtilityRandom.randomSubset(correctFormat);
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
	 * Generic constructor
	 * @param bias: bias value
	 * @param usefullInputs: array of the canalized inputs
	 * @param inputs: Array of the tested input values
	 * @param outputs: Array of the corresponding output
	 * @throws ParamDefinitionException
	 */
	public CanalizedFunction(double bias, int[] usefullInputs, String[] inputs, Boolean[] outputs, ArrayList<Integer> inputNodes) throws ParamDefinitionException{
		super();
		if(bias < 0 || bias > 1)
			throw new ParamDefinitionException("Bias value must be between 0 and 1");
		if(inputs == null || outputs == null)
			throw new ParamDefinitionException("Inputs and outputs must be not null");
		if(inputs.length != outputs.length)
			throw new ParamDefinitionException("The number of inputs and outpust must be the same");
		if(usefullInputs == null || usefullInputs.length < 0)
			throw new ParamDefinitionException("The number of canalizing inputs must be grater or equal to 1");

		functionTable = new HashMap<String, Boolean>();

		this.bias = bias;
		this.usefullInputs = usefullInputs;
		super.inputs = inputNodes;

		//Create the functionTable as passed
		for(int i = 0; i < inputs.length; i++){
			functionTable.put(inputs[i], outputs[i]);
		}

	}

	/**
	 * Generic constructor
	 * @param inputs: number of function inputs
	 * @param completelyDefined is true if you want a completely defined function
	 * @throws ParamDefinitionException
	 */
	public CanalizedFunction(ArrayList<Integer> inputs, boolean completelyDefined) throws ParamDefinitionException {
		this(inputs, 0.5, completelyDefined);
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

		//Creates the correct input
		String inputValue = "";
		for(int i = 0; i < this.usefullInputs.length; i++){
			inputValue += (input[this.usefullInputs[i]].equals(Boolean.TRUE) ? "1" : "0");
		}
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
		String xmlFunction = "\t\t\t<function type = \"canalizing\" input_number = \""+ super.inputs.size() + "\">\n";
		//Function inputs
		for(int input : super.inputs)
			xmlFunction += "\t\t\t\t\t<input_node >" + input +
			"</input_node>\n";
		//Canalized inputs
		for(int input : usefullInputs){
			xmlFunction += "\t\t\t\t\t<canalizing_input>\n";
			xmlFunction += "\t\t\t\t\t\t" + input + "\n";
			xmlFunction += "\t\t\t\t\t</canalizing_input>\n";
		}
		
		xmlFunction += "\t\t\t\t<bias> " + bias + " </bias>\n";
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
	 * The new input could be canalizing.
	 * @param inputLink: The new input link 
	 * @param epsilon: The probability that the new input be canalizing
	 * @throws ParamDefinitionException 
	 */
	public void addLink(int inputLink, double epsilon) throws ParamDefinitionException{
		if(inputLink < 0)
			throw new ParamDefinitionException("The inputLink must be greater than 0");
		super.inputs.add(inputLink);
		this.functionTable.clear();
		if(UtilityRandom.randomBooleanChoice(epsilon)){
			this.usefullInputs = Arrays.copyOf(this.usefullInputs, this.usefullInputs.length + 1);
			this.usefullInputs[this.usefullInputs.length - 1] = inputLink;
		}

	}

	/**
	 * This method adds a new function's input to it.
	 * Note: The function table will be deleted. All the previous values will be lost.
	 * The new input must not be canalizing
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
	 * This method returns the function as a string.
	 * @return String, the function as a string
	 */
	public String toString(){
		return Arrays.toString(this.usefullInputs);
	}

	/**
	 * This method copies the function.
	 * @return a copy of the function.
	 */
	public Function copy() throws ParamDefinitionException{
		ArrayList<Integer> newInputs = new ArrayList<Integer>();
		for(Integer i : super.inputs)
			newInputs.add(i);

		CanalizedFunction newCana= new CanalizedFunction(newInputs, this.bias, false);

		newCana.usefullInputs = new int[this.usefullInputs.length];
		for(int i = 0; i < this.usefullInputs.length; i++)
			newCana.usefullInputs[i] = this.usefullInputs[i];

		newCana.functionTable = new HashMap<String, Boolean>();
		for(String input : this.functionTable.keySet())
			newCana.functionTable.put(input, this.functionTable.get(input));
		return newCana;	
	}

	/**
	 * This method returns the type of the function as a String
	 * @return Function type
	 */
	public String getType(){
		return "Canalizing";
	}

	/**
	 * This method returns the input used for the canalizing function
	 * @return the input used for the canalizing function
	 */
	public int[] getUsefullInputs(){
		return this.usefullInputs;
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
