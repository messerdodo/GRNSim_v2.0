package it.unimib.disco.bimib.GRNSim;

/**
 * This class is the console application entry point.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 */

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.IO.Input;
import it.unimib.disco.bimib.Utility.ConsoleConstants;

//System imports
import java.util.Properties;

public class Console {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		Properties inputArgs, inputFeatures;
		
		try{
			//Converts the input arguments from a string array to a properties object.
			inputArgs = Input.readInputArguments(args);
			
			//The input features file must be specified.
			if(!inputArgs.containsKey(ConsoleConstants.INPUT_FEATURES_FILE))
				throw new InputFormatException("The input features file must be "
						+ "specified using the " + ConsoleConstants.INPUT_FEATURES_FILE + " key");
			//Reads the input features file.
			inputFeatures = Input.readInputFeatures(
					inputArgs.getProperty(ConsoleConstants.INPUT_FEATURES_FILE));
			
			//Displays a operation successfully completed message
			System.out.println("Input features correctly read at \'" 
					+ inputArgs.getProperty(ConsoleConstants.INPUT_FEATURES_FILE) + "\'");
			
			//Reads the operation to perform: This key MUST be specified.
			if(!inputArgs.containsKey(ConsoleConstants.OPERATION_TO_PERFORM))
				throw new InputFormatException("The operation to perform must be "
						+ "specified using the " + ConsoleConstants.OPERATION_TO_PERFORM + " key");
			
		}catch(Exception ex){
			//Displays the exception message
			if(ex.getMessage().equalsIgnoreCase(""))
				System.out.println(ex);
			else
				System.out.println(ex.getMessage());
		} 
	}

}
