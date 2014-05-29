package it.unimib.disco.bimib.IO;

/**
 * This class defines a set of static methods to use in order to 
 * reads inputs for the simulator.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 */

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;

//System imports
import java.util.Properties;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Input {
	
	/**
	 * This method returns the arguments properties object from a 
	 * given string args array
	 * @param args[]: The arguments string array
	 * @return A Properties object with the given arguments inside.
	 * @throws InputFormatException: The input arguments format is not correct
	 */
	public static Properties readInputArguments(String[] args) throws InputFormatException{
		Properties inputArguments = new Properties();

		//Check the arguments format validity
		if(args.length % 2 != 0 || args.length == 0){
			throw new InputFormatException("Input arguments are not in the correct form");
		}else{
			//Puts the input arguments in the Properties object
			for(int i = 0; i < args.length; i = i + 2){
				inputArguments.put(args[i], args[i+1]);
			}
		}
		//Returns the input arguments properties object
		return inputArguments;
	}

	/**
	 * This method reads all the task features specified in the fileName file.
	 * @param fileName: specified the task features file name and path.
	 * @return A Properties object with all the task features.  
	 * @throws FileNotFoundException : The specified file is not found.
	 * @throws InputFormatException : The specified file format is not correct.
	 * @throws MissedParamException : A parameter has not the corresponding value.
	 */
	public static Properties readTaskFeatures(String fileName) throws FileNotFoundException, InputFormatException, MissedParamException{
		//Input file not specified
		if(fileName == null)			
			throw new NullPointerException("The file name must not be null");
		
		Properties taskFeatures = new Properties();
		String line;
		String[] readLine;

		//Opens the input streams
		File inputFile = new File(fileName);
		Scanner reader = new Scanner(inputFile);

		if(!(line = reader.nextLine()).equals("<task features file>"))
			throw new InputFormatException("No simulation features file specified");
		
		//Reads all the parameters
		while(reader.hasNext()){
			line = reader.nextLine();					
			readLine = line.split(" ");
			//Checks if the read line is a couple key value
			if(readLine.length < 2)
				throw new MissedParamException("Missed value in the input file");	
			else{
				//Adds the features in the properties.
				taskFeatures.setProperty(readLine[0], readLine[1]);
			}
		}
		//Closes the input stream
		reader.close();
	
		return taskFeatures;
	}
	
	
	/**
	 * This method reads all the simulation features specified in the fileName file.
	 * That features are used as simulation parameters.
	 * @param fileName The file where the features are specified
	 * @return A Properties object with all the features
	 * @throws NullPointerException: No file name specified
	 * @throws FileNotFoundException: The file name is not correct
	 * @throws MissedParamException: The features are not well formed (each as a key-value couple)
	 * @throws InputFormatException: File format not correct
	 */
	public static Properties readSimulationFeatures(String fileName) 
			throws NullPointerException, FileNotFoundException, InputFormatException, MissedParamException{
		//Input file not specified
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");

		Properties features = new Properties();
		String line;
		String[] readLine;

		//Opens the input streams
		File inputFile = new File(fileName);
		Scanner reader = new Scanner(inputFile);

		if(!(line = reader.nextLine()).equals("<simulation features file>"))
			throw new InputFormatException("No simulation features file specified");

		//Reads all the parameters
		while(reader.hasNext()){
			line = reader.nextLine();
			readLine = line.split(" ");
			//Checks if the read line is a couple key value
			if(readLine.length < 2)
				throw new MissedParamException("Missed value in the input file");	
			/*
			//Checks if the inserted feature allows multiple values 
			else if(readLine[0].equals(Constants.EXCLUDES_SOURCE_GENES) ||
					readLine[0].equals(Constants.EXCLUDES_TARGET_GENES)){
				//Reads the multiple values
				ArrayList<String> values = new ArrayList<String>();
				for(int i = 1; i < readLine.length; i++)
					values.add(readLine[i]);
				//Adds the property
				features.put(readLine[0], values);
			}
			*/
			else{
				//Adds the features in the properties.
				features.setProperty(readLine[0], readLine[1]);
			}
		}
		//Closes the input stream
		reader.close();
		return features;
	}
	
}
