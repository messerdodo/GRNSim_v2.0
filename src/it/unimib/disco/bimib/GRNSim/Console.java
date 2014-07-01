package it.unimib.disco.bimib.GRNSim;

/**
 * This class is the console application entry point.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
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
		
		Properties inputArgs, taskFeatures, simulationFeatures;
		
		try{
			//Converts the input arguments from a string array to a properties object.
			inputArgs = Input.readInputArguments(args);
			
			//The task features file must be specified.
			if(!inputArgs.containsKey(ConsoleConstants.TASK_FEATURES_FILE))
				throw new InputFormatException("The task features file must be "
						+ "specified using the " + ConsoleConstants.TASK_FEATURES_FILE + " key");
			//Reads the task features file.
			taskFeatures = Input.readTaskFeatures(
					inputArgs.getProperty(ConsoleConstants.TASK_FEATURES_FILE));
			
			//Displays a operation successfully completed message
			System.out.println("Task features correctly read at \'" 
					+ inputArgs.getProperty(ConsoleConstants.TASK_FEATURES_FILE) + "\'");
			
			//The simulation features file must be specified.
			if(!inputArgs.containsKey(ConsoleConstants.SIMULATION_FEATURES_FILE))
				throw new InputFormatException("The simulation features file must be "
						+ "specified using the " + ConsoleConstants.SIMULATION_FEATURES_FILE + " key");
			//Reads the simulation features file.
			simulationFeatures = Input.readSimulationFeatures(
					inputArgs.getProperty(ConsoleConstants.SIMULATION_FEATURES_FILE));
			
			//Displays a operation successfully completed message
			System.out.println("Input features correctly read at \'" 
					+ inputArgs.getProperty(ConsoleConstants.SIMULATION_FEATURES_FILE) + "\'");
			
			
		}catch(Exception ex){
			//Displays the exception message
			if(ex.getMessage().equalsIgnoreCase(""))
				System.out.println(ex);
			else
				System.out.println(ex.getMessage());
		} 
	}

	
	
	
	/*private void networkCreation(Properties simulationFeatures){
		
	}*/
	
	
	
	
}
