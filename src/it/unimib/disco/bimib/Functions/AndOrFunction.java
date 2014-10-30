/**
 * 
 * This class is the representation of a boolean function with only and(s) or only or(s).
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

import java.util.HashMap;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;

public class AndOrFunction extends BooleanFunction {

	//Function type onlyAnd -> andFunction == TRUE 
	//              onlyOr  -> andFunction == FALSE
	private boolean andFunction;


	/**
	 * Generic constructor 
	 * @param andFunction: function type, True for only AND function, 
	 * false for OR functions
	 * @throws ParamDefinitionException: Wrong inputs number
	 */
	public AndOrFunction(boolean andFunction, ArrayList<Integer> inputs) throws ParamDefinitionException{
		super(inputs);
		this.andFunction = andFunction;
	}

	/**
	 * Default constructor
	 * @throws ParamDefinitionException: Wrong inputs number
	 */
	public AndOrFunction(ArrayList<Integer> inputs) throws ParamDefinitionException{
		this(true, inputs);
	}

	@Override
	/**
	 * This method evaluates the function with the passed inputs.
	 * @param inputs: Is a Boolean array with the function inputs. 
	 * In this case they must be Booleans.
	 * @return Returns a Boolean value
	 * @throws InputTypeException: This exception will lift up when the 
	 * inputs won't be Booleans.
	 * @throws ParamDefinitionException: Wrong dimension of the input array
	 */
	public Boolean eval(Object[] input) throws InputTypeException, ParamDefinitionException{
		//Check the correct input type (Boolean)
		if(!(input instanceof Boolean[]))
			throw new InputTypeException("Wrong input type. Inputs must be Boolean");

		//Mutation in action flow
		if(super.getMutationInAction())
			return super.getKnockIn();


		Boolean output = false;

		//Checks the correct inputs type
		if(!(input instanceof Boolean[]))
			throw new InputTypeException("Wrong input type. Inputs must be Boolean");
		int i = 1;
		if(super.inputs.size() > 0){
			output = (Boolean)input[super.inputs.get(0)];
			//And Function
			if(this.andFunction){
				//Performs the and function with all the inputs, 
				//with only one false input the function is going to become false.
				while(i < super.inputs.size() && output){
					output &= (Boolean)input[super.inputs.get(i)];
					i++;
				}
			}else{ //Or function
				//Performs the or function with all the inputs, 
				//with only one true input the function is going to become true.
				while(i < super.inputs.size() && !output){
					output |= (Boolean)input[super.inputs.get(i)];
					i++;
				}
			}
		}
		return output;
	}

	/**
	 * This method returns the function xml representation.
	 * @return: the xml function representation
	 */
	public String toGRNML(){
		String xmlFunction = "\t\t\t<function type = \""+ (this.andFunction ? "AND" : "OR") +
				"\" input_number = \""+ super.inputs.size() +"\">\n";
		for(int input : super.inputs)
			xmlFunction += "\t\t\t\t\t<input_node >" + input +
			"</input_node>\n";
		xmlFunction += "\t\t\t</function>\n";
		return xmlFunction;
	}

	@Override
	/**
	 * This function returns the bias of the function.
	 * @return: the bias of the function
	 */
	public double getBias() {
		if(this.andFunction == true)
			return (1/(Math.pow(2, super.inputs.size())));
		else
			return ((Math.pow(2, super.inputs.size()) - 1)/(Math.pow(2, super.inputs.size())));

	}

	/**
	 * This method adds a new input link to the function
	 * @param inputLink: The new input way.
	 */
	public void addLink(int inputLink) throws ParamDefinitionException{
		super.inputs.add(inputLink);
	}

	/**
	 * This method copies the function.
	 * @return a copy of the function.
	 */
	public Function copy() throws ParamDefinitionException{
		ArrayList<Integer> newInputs = new ArrayList<Integer>();
		for(Integer i : super.inputs)
			newInputs.add(i);
		AndOrFunction newAndOr = new AndOrFunction(this.andFunction, newInputs);
		return newAndOr;	
	}

	/**
	 * This method returns the type of the function as a String
	 * @return Function type
	 */
	public String getType(){
		return (andFunction ? "AND" : "OR");
	}

	@Override
	/**
	 * This method return the function table as a string, string hash map
	 */
	public HashMap<String, String> getTable() {
		HashMap<String, String> funcTable = new HashMap<String, String>();
		String binaryInputs;
		//Creates the completed function table for the and/or function
		for(int i = 0; i < Math.pow(2, inputs.size()); i++){
			binaryInputs = Integer.toBinaryString(i);
			while(binaryInputs.length() < inputs.size()){
				binaryInputs = "0" + binaryInputs;
			}
			//And function
			if(this.andFunction){
				if(i == Math.pow(2, inputs.size()) - 1)
					funcTable.put(binaryInputs, "1");
				else
					funcTable.put(binaryInputs, "0");
			//Or function
			}else{
				if(i != 0)
					funcTable.put(binaryInputs, "1");
				else
					funcTable.put(binaryInputs, "0");
			}
		}
		
		return funcTable;
	}
}
