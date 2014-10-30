/**
 * This class defines a set of static methods to use in order to 
 * reads inputs for the simulator.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 */


package it.unimib.disco.bimib.IO;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Functions.*;
import it.unimib.disco.bimib.Utility.SimulationFeaturesConstants;






//System imports
import java.util.Properties;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Input {
	
	private static final int SOURCE = 0;
	private static final int DESTINATION = 1;

	
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
	@SuppressWarnings("resource")
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
	@SuppressWarnings("resource")
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
			
			//Checks if the inserted feature allows multiple values 
			else if(readLine[0].equals(SimulationFeaturesConstants.EXCLUDES_SOURCE_GENES) ||
					readLine[0].equals(SimulationFeaturesConstants.EXCLUDES_TARGET_GENES)){
				//Reads the multiple values
				ArrayList<String> values = new ArrayList<String>();
				for(int i = 1; i < readLine.length; i++)
					values.add(readLine[i]);
				//Adds the property
				features.put(readLine[0], values);
			}

			else{
				//Adds the features in the properties.
				features.setProperty(readLine[0], readLine[1]);
			}
		}
		//Closes the input stream
		reader.close();
		return features;
	}
	
	/**
	 * This method reads a GRNML file and returns the associated GraphManager.
	 * @param fileName: The file name.
	 * @return A GraphManager object with the read graph inside.
	 * @throws ParamDefinitionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NotExistingNodeException
	 */
	public static GraphManager readGRNMLFile(String fileName) throws ParamDefinitionException, ParserConfigurationException, SAXException, IOException, NotExistingNodeException{

		GraphManager graphManager = null;
		String[] nodesName;
		Function[] functions;

		//Param checking
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");

		File grnmlFile = new File(fileName);

		if(grnmlFile.exists()){
			graphManager = new GraphManager();


			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(grnmlFile);

			String graphTopology = doc.getDocumentElement().getAttribute("topology");

			int nodesNumber = Integer.valueOf(doc.getDocumentElement().getAttribute("nodes_number"));
			nodesName = new String[nodesNumber];
			functions = new Function[nodesNumber];

			NodeList nodesList = doc.getElementsByTagName("node");
			NodeList edgesList = doc.getElementsByTagName("edge");

			for (int i = 0; i < nodesList.getLength(); i++) {
				nodesName[i] = nodesList.item(i).getAttributes().getNamedItem("name").getNodeValue();

				//Gets the associated function
				NodeList functionList = nodesList.item(i).getChildNodes();
				//No function is specified.
				if(functionList.getLength() == 0)
					functions[i] = null;

				for(int j = 0; j < functionList.getLength(); j++){
					if(functionList.item(j).getNodeType() == Node.ELEMENT_NODE){
						Element funct = (Element)functionList.item(j);

						String functionType = funct.getAttribute("type");
						double bias;
						String[] inputsTable;
						Boolean[] outputsTable;

						NodeList inputsList = funct.getElementsByTagName("input_node");
						ArrayList<Integer> inputNodes = new ArrayList<Integer>();

						for(int k = 0; k < inputsList.getLength(); k++)
							inputNodes.add(Integer.valueOf(inputsList.item(k).getTextContent()));

						//Checks the function type
						if(functionType.equals("random") || functionType.equals("canalizing")){

							bias = Double.valueOf(funct.getElementsByTagName("bias").item(0).getTextContent());

							//Reads the function table
							NodeList entries = funct.getElementsByTagName("entry");
							inputsTable = new String[entries.getLength()];
							outputsTable = new Boolean[entries.getLength()];
							
							for(int h = 0; h < entries.getLength(); h++){
								String input = ((Element)entries.item(h)).getAttribute("input");
								Boolean output = ((Element)entries.item(h)).getAttribute("output").equals("0") ? Boolean.FALSE : Boolean.TRUE;
								inputsTable[h] = input;
								outputsTable[h] = output;
							}

							//Canalizing functions
							if(functionType.equals("canalizing")){
								NodeList canalizingList = funct.getElementsByTagName("canalizing_input");
								int[] usefullInputs = new int[canalizingList.getLength()];

								for(int k = 0; k < canalizingList.getLength(); k++){

									usefullInputs[k] = Integer.valueOf((
											canalizingList.item(k).getTextContent().replace("\n", "")
											).replace("\t", ""));
								}
								functions[i] = new CanalizedFunction(bias, usefullInputs, inputsTable, outputsTable, inputNodes);
								//Random functions
							}else{
								functions[i] = new RandomFunction(bias, inputsTable, outputsTable, inputNodes);
							}
							//AND functions
						}else if(functionType.equals("AND")){
							functions[i] = new AndOrFunction(true, inputNodes);
							//OR functions
						}else if(functionType.equals("OR")){
							functions[i] = new AndOrFunction(false, inputNodes);
						}else{
							functions[i] = null;
						}

					}
				}	
			}

			//Gets the edges
			int[][] edges = new int[edgesList.getLength()][2];
			for(int i = 0; i < edgesList.getLength(); i++){
				int source = Integer.valueOf(edgesList.item(i).getAttributes().getNamedItem("source").getNodeValue());
				int destination = Integer.valueOf(edgesList.item(i).getAttributes().getNamedItem("destination").getNodeValue());
				edges[i][SOURCE] = source;
				edges[i][DESTINATION] = destination;
			}

			graphManager.createGraph(nodesName, edges, functions, graphTopology);
		}
		return graphManager;	
	}
	
	/**
	 * This method allows to read a differentiation tree from file.
	 * @param fileName: The name of the file
	 * @return: An ArrayList of tree nodes. Each node is represented as an three items array.
	 * The first one is the level, the second the node name and the last one is the parent.
	 * @throws NullPointerException
	 * @throws FileNotFoundException
	 * @throws InputFormatException
	 */
	@SuppressWarnings("resource")
	public static ArrayList<String[]> readTree(String fileName) 
			throws NullPointerException, FileNotFoundException, InputFormatException{

		if(fileName == null)
			throw new NullPointerException("The file name must not be null");

		String line;
		ArrayList<String[]> readTree = new ArrayList<String[]>();
		//Opens the output streams
		File inputFile = new File(fileName);
		Scanner reader = new Scanner(inputFile);
		if(!(line = reader.nextLine()).equals("<Differentiation tree description file>"))
			throw new InputFormatException("No differentiation tree description file specified");
		//Reads all the file
		while(reader.hasNext()){
			line = reader.nextLine();
			readTree.add(line.split(" "));
		}
		return readTree;
	}
	
	/**
	 * This method allows to read and import an ATM from file.
	 * The atm file must be a csv file with the matrix inside.
	 * @param atmFileName: The atm file path.
	 * @return The atm read from file.
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static double[][] readAtm(String atmFileName) throws IOException{
		double[][] atm;
		int n, row = 0;
		String[] splittedLine;
		
		//Param checking
		if(atmFileName == null)
			throw new NullPointerException("The atm file name must be not null"); 
		//Opens the input streams
		File inputFile = new File(atmFileName);
		Scanner reader = new Scanner(inputFile);
		//Reads the first line of the atm matrix file in order to obtain the matrix dimension.
		splittedLine = reader.nextLine().split(",");
		n = splittedLine.length;
		atm = new double[n][n];
		//Puts the values for the first row in the atm matrix
		for(int i = 0; i < n; i ++){
			atm[row][i] = Double.valueOf(splittedLine[i]);
		}
		row = row + 1;
		//Puts all the values in the atm matrix
		while(reader.hasNext() && row < n){
			splittedLine = reader.nextLine().split(",");
			for(int i = 0; i < n; i ++){
				atm[row][i] = Double.valueOf(splittedLine[i]);
			}
			row = row + 1;
		}
		//Checks the file format (n by n matrix) 
		if(row != 0)
			throw new IOException("Format error in the atm file. The matrix must be " + n + "x" + n +" matrix");
		return atm;
	}
}
