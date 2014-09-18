/** 
 * This class is the representation of a boolean function with only and(s) or only or(s).
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.IO;

//System imports
import java.util.List;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//GRNSim imports
import it.unimib.disco.bimib.Atms.Atm;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.*;
import it.unimib.disco.bimib.Sampling.AttractorsFinder;
import it.unimib.disco.bimib.Utility.OutputConstants;
/*import Statistics.DynamicPerturbation;
import Statistics.NetworkStructureStatistics;
import Statistics.SimulationResult;
import Statistics.StandardDynamicStatistics;
import Statistics.StoredResults;
import TES.TesTree;*/

public class Output {

	//Constants
	//private final static int NETWORK_STATISTICS_NUMBER = 5;
	//private final static int COUPLE = 2;


	public Output() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This method creates the specified folder.
	 * @param path: The path followed by the new folder name
	 */
	public static void createFolder(String path){
		new File(path).mkdir();
	}

	/**
	 * This method creates a SIF file for the specified graph.
	 * @param network: The graph to be written
	 * @param fileName: The name of the output file
	 * @throws IOException: Some errors occurred during the writing operations
	 */
	public static void createSIFFile(GeneRegulatoryNetwork network, String fileName) throws IOException{

		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(network == null)
			throw new NullPointerException("The network must not be null");

		//Opens the output streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);
		//Gets the edges
		List<int[]> graphEdges = network.getEdges();

		//Writes the sif file
		for(int[] edge : graphEdges){
			//Each rows likes "n	_connected	m" where n and m are node numbers 
			printer.println("Node"+ edge[0] + "\tDirectedEdge\t" + "Node" + edge[1]);
			printer.flush();
		}
		//Closes the streams
		printer.close();
		writer.close();
	}

	/**
	 * This method writes the specified graph in a GML file
	 * @param network: The graph to be written
	 * @param fileName: The output file name
	 * @throws IOException: An error occurred during the writing actions
	 * @throws NullPointerException: Parameters must not be null
	 */
	public static void createGMLFile(GeneRegulatoryNetwork network, String fileName) throws IOException, NullPointerException{
		String section = ""; //File section to be written

		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(network == null)
			throw new NullPointerException("The network must not be null");

		//Gets edges and nodes
		int[] nodes = network.getNodes();
		List<int[]> edges = network.getEdges();

		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);

		//Writes the file header
		printer.println("graph [");
		printer.flush();

		//Writes the nodes
		for(int node : nodes){
			section = "\tnode [\n";
			section += "\t\tid " + node + "\n";
			section += "\t\tlabel " + "node_" + node + "\n";
			section += "\t]";
			printer.println(section);
			printer.flush();
		}

		//Writes the edges
		for(int[] edge : edges){
			section = "\tedge [\n";
			section += "\t\tsource " + edge[0] + "\n";
			section += "\t\ttarget " + edge[1] + "\n";
			section += "\t]";
			printer.println(section);
			printer.flush();
		}

		//Writes the footer
		printer.println("]");
		printer.flush();

		//closes the streams
		printer.close();
		writer.close();

	}

	/**
	 * This method creates a GRML file
	 * @param network
	 * @param fileName
	 * @throws IOException
	 */
	public static void createGRNMLFile(GeneRegulatoryNetwork network, String fileName) throws IOException{
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(network == null)
			throw new NullPointerException("The network must not be null");

		//Gets the GRNML file content
		String grnmlContent = network.toGRNML();

		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);
		//Writes the file
		printer.write(grnmlContent);
		printer.flush();

		//Closes the streams
		printer.close();
		writer.close();
	}

	/**
	 * This method saves the ATM in TSV format
	 * @param atm: The ATM to be saved
	 * @param fileName: The name of the file to be saved
	 * @throws IOException: I/O error
	 */
	public static void createATMFile(Atm atm, String fileName) throws IOException{
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(atm == null)
			throw new NullPointerException("The ATM must not be null");
		
		//Gets the ATM in tsv format
		String atmString = atm.getCsvAtm();
		
		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);
		//Writes the file
		printer.write(atmString);
		printer.flush();

		//Closes the streams
		printer.close();
		writer.close();
	}
	
	/**
	 * This method saves the states of the attractors in a csv file.
	 * Each line corresponds to an attractor. Each column corresponds to a state of the i-th attractor.
	 * @param attractorFinder: An attractor finder object
	 * @param fileName: The name of the file to be saved
	 * @throws IOException: An error occurred during the saving time
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeExceptions
	 */
	public static void saveAttractorsFile(AttractorsFinder attractorFinder, String fileName) throws IOException, ParamDefinitionException, NotExistingNodeException, InputTypeException{
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(attractorFinder == null)
			throw new NullPointerException("The attractor finder must not be null");
		
		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);
		//Gets the attractors: a new line for each attractor
		for(Object attractor : attractorFinder.getAttractors()){
			//Gets the states in the attractor
			for(Object state : attractorFinder.getStatesInAttractor(attractor)){
				printer.print(state.toString() + ";");
				printer.flush();
			}
			printer.println("");
			printer.flush();
		}
		
		printer.close();
		writer.close();	
	}
	
	/**
	 * This method saves the synthesis file
	 * @param results: a property object with all the statistics for the network
	 * @param fileName: The name of the file to be saved
	 * @throws Exception: This exception is thrown if the property object does not contain all the required statistics.
	 */
	public static void createSynthesisFile(Properties results, String fileName) throws Exception{
	
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(results == null)
			throw new NullPointerException("The StoredResults object must not be null");
		
		//Network ID
		if(!results.containsKey(OutputConstants.SIMULATION_ID))
			throw new Exception(OutputConstants.SIMULATION_ID + " key must be in the results object");
		//Clustering coefficient
		if(!results.containsKey(OutputConstants.CLUSTERING_COEFFICIENT))
			throw new Exception(OutputConstants.CLUSTERING_COEFFICIENT + " key must be in the results object");
		//Average bias value
		if(!results.containsKey(OutputConstants.AVERAGE_BIAS))
			throw new Exception(OutputConstants.AVERAGE_BIAS + " key must be in the results object");
		//Average path length
		if(!results.containsKey(OutputConstants.AVERAGE_PATH_LENGTH))
			throw new Exception(OutputConstants.AVERAGE_PATH_LENGTH + " key must be in the results object");
		//Network diameter
		if(!results.containsKey(OutputConstants.NETWORK_DIAMETER))
			throw new Exception(OutputConstants.NETWORK_DIAMETER + " key must be in the results object");
		//Number of attractors in the network
		if(!results.containsKey(OutputConstants.ATTRACTORS_NUMBER))
			throw new Exception(OutputConstants.ATTRACTORS_NUMBER + " key must be in the results object");
		//Average attractors lenght
		if(!results.containsKey(OutputConstants.ATTRACTORS_LENGTH))
			throw new Exception(OutputConstants.ATTRACTORS_LENGTH + " key must be in the results object");
		//Distance from the given tree
		if(!results.containsKey(OutputConstants.TREE_DISTANCE))
			throw new Exception(OutputConstants.TREE_DISTANCE + " key must be in the results object");
		//Not found attractors
		if(!results.containsKey(OutputConstants.NOT_FOUND_ATTRACTORS))
			throw new Exception(OutputConstants.NOT_FOUND_ATTRACTORS + " key must be in the results object");

		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);	
		
		//Writes the simulation id
		printer.print(results.get(OutputConstants.SIMULATION_ID) + ",");
		printer.flush();
		
		//Writes the simulation id
		printer.print(results.get(OutputConstants.CLUSTERING_COEFFICIENT) + ",");
		printer.flush();
		
		//Writes the simulation id
		printer.print(results.get(OutputConstants.AVERAGE_BIAS) + ",");
		printer.flush();
		
		//Writes the average path length
		printer.print(results.get(OutputConstants.AVERAGE_PATH_LENGTH) + ",");
		printer.flush();

		//Writes the network diameter
		printer.print(results.get(OutputConstants.NETWORK_DIAMETER) + ",");
		printer.flush();

		//Writes the number of attractors
		printer.print(results.get(OutputConstants.ATTRACTORS_NUMBER) + ",");
		printer.flush();
				
		//Writes the attractors average length
		printer.print(results.get(OutputConstants.ATTRACTORS_LENGTH) + ",");
		printer.flush();	
		
		//Writes the distance from the given tree
		printer.print(results.get(OutputConstants.TREE_DISTANCE) + ",");
		printer.flush();	
		
		//Writes the number of not found attractors
		printer.print(results.get(OutputConstants.NOT_FOUND_ATTRACTORS));
		printer.flush();	
		
		printer.close();
		writer.close();
	}
	
	
	
	
	/**
	 * This method returns the network statistics in the correct format for the visualization.
	 * The object matrix has 5 rows, one for each network statistic: Average network bias value, 
	 * Network clustering coefficient, Network diameter, Average path length and assortativity/dissortativity.
	 * The first column represent the statistic name and the second its value.
	 * @param simulation; A Simulation result object
	 * @return: An object matrix.
	 * @throws NotExistingNodeException
	 */
	/*public static Object[][] getNetworkStatisticsForVisualization(SimulationResult simulation) throws NotExistingNodeException{

		if(simulation == null)
			throw new NullPointerException("The simulation must be not null!");

		Object[][] networkStatistics = new Object[NETWORK_STATISTICS_NUMBER][COUPLE];

		//Average bias value
		networkStatistics[0][0] = "Average network bias value";
		networkStatistics[0][1] = String.valueOf(simulation.getAverageBiasValue());

		//Clustering coefficient
		networkStatistics[1][0] = "Network clustering coefficient";
		networkStatistics[1][1] = String.valueOf(simulation.getClusteringCoefficient());

		//Network diameter
		networkStatistics[2][0] = "Network diameter";
		networkStatistics[2][1] = String.valueOf(simulation.getNetworkDiameter());

		//Average path length
		networkStatistics[3][0] = "Average path length";
		networkStatistics[3][1] = String.valueOf(simulation.getAveragePathLength());

		return networkStatistics;

	}*/
	
	/**
	 * This method allows to save all the statistics
	 * @param fileName
	 * @param matchingSimulations
	 * @param unmatchingSimulations
	 * @throws IOException
	 * @throws NotExistingSimulationException
	 * @throws NullPointerException
	 * @throws NotExistingNodeException
	 * @throws ParamDefinitionException
	 * @throws InputTypeException
	 */
	/*public static void saveStatistics(String fileName, StoredResults matchingSimulations, 
			StoredResults unmatchingSimulations) throws IOException, 
			NotExistingSimulationException, NullPointerException, 
			NotExistingNodeException, ParamDefinitionException, InputTypeException{
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");

		int match = 0, unmatch = 0;

		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);

		PrintWriter printer = new PrintWriter(writer);

		printer.write("######### GRNSim statistics output file #########\n");
		printer.flush();

		printer.write("Date: " + (new Date()).toString() + "\n\n");
		printer.flush();

		match = matchingSimulations == null ? 0 : matchingSimulations.getSize();
		unmatch = unmatchingSimulations == null ? 0 : unmatchingSimulations.getSize();

		printer.write("Tested gene regulatory networks: " + (match + unmatch) + "\n");
		printer.flush();

		if(matchingSimulations != null){
			printer.write("Matching networks found: " + matchingSimulations.getSize() + "\n");
			printer.flush();
		}
		if(unmatchingSimulations != null){
			printer.write("Unmatching networks found: " + unmatchingSimulations.getSize() + "\n\n\n");
			printer.flush();
		}
		printer.write("********* NETWORK STRUCTURE STATISTICS *********\n");
		printer.flush();
		if(matchingSimulations != null){
			printer.write("*********       Matching networks       *********\n");
			printer.flush();
			for(String simName : matchingSimulations.getSimulationIds()){
				SimulationResult sim = matchingSimulations.getStoredSimulation(simName);
				printer.write("Matching simulation: " + simName + "\n");
				printer.flush();

				//Network diameter
				printer.write("Network diameter: " + NetworkStructureStatistics.getNetworkDiameter(
						sim.getGraphManager()) + "\n");
				printer.flush();

				//Average path length
				printer.write("Average path length: " + NetworkStructureStatistics.getAveragePath(
						sim.getGraphManager()) + "\n");
				printer.flush();

				//Clustering coefficient
				printer.write("Clustering coefficient: " + NetworkStructureStatistics.getClusteringCoefficient(
						sim.getGraphManager()) + "\n");
				printer.flush();

				//Average bias value
				printer.write("Average bias value: " + NetworkStructureStatistics.getAverageBiasValue(
						sim.getGraphManager()) + "\n");
				printer.flush();

				printer.write("------------------------------\n");
				printer.flush();
			}
		}
		
		if(unmatchingSimulations != null){
			printer.write("\n*********      Unmatching networks      *********\n");
			printer.flush();
			for(String simName : unmatchingSimulations.getSimulationIds()){
				SimulationResult sim = unmatchingSimulations.getStoredSimulation(simName);
				printer.write("Unmatching simulation: " + simName + "\n");
				printer.flush();

				//Network diameter
				printer.write("Network diameter: " + NetworkStructureStatistics.getNetworkDiameter(
						sim.getGraphManager()) + "\n");
				printer.flush();

				//Average path length
				printer.write("Average path length: " + NetworkStructureStatistics.getAveragePath(
						sim.getGraphManager()) + "\n");
				printer.flush();

				//Clustering coefficient
				printer.write("Clustering coefficient: " + NetworkStructureStatistics.getClusteringCoefficient(
						sim.getGraphManager()) + "\n");
				printer.flush();

				//Average bias value
				printer.write("Average bias value: " + NetworkStructureStatistics.getAverageBiasValue(
						sim.getGraphManager()) + "\n");
				printer.flush();

				printer.write("------------------------------\n");
				printer.flush();
			}
		}
		HashMap<Integer, Integer> dist;

		printer.write("\n\n\n********* STANDARD DYNAMIC STATISTICS *********\n");
		if(matchingSimulations != null){
			printer.write("*********       Matching networks       *********\n");
			printer.flush();

			printer.write("@@@ Basin of attraction\n\n");
			printer.flush();

			printer.write("Size\t\t\t|Occurences\n");
			printer.flush();
			dist = StandardDynamicStatistics.basinOfAttraction(matchingSimulations.getSimulations());

			for(Integer key : dist.keySet()){
				printer.write(key + "\t\t\t|" + dist.get(key) + "\n");
				printer.flush();
			}

			printer.write("\n@@@ Attractor length\n\n");
			printer.flush();

			printer.write("Length\t\t\t|Occurences\n");
			printer.flush();
			dist = StandardDynamicStatistics.getAttractorsLength(matchingSimulations.getSimulations());

			for(Integer key : dist.keySet()){
				printer.write(key + "\t\t\t|" + dist.get(key) + "\n");
				printer.flush();
			}

			printer.write("\n@@@ Transient length\n\n");
			printer.flush();

			printer.write("Length\t\t\t|Occurences\n");
			printer.flush();
			dist = StandardDynamicStatistics.getTransientLength(matchingSimulations.getSimulations());

			for(Integer key : dist.keySet()){
				printer.write(key + "\t\t\t|" + dist.get(key) + "\n");
				printer.flush();
			}

			printer.write("@@@ Oscillating vs not oscillating nodes \n\n");
			printer.flush();
			double oscillating = StandardDynamicStatistics.getOscillatingNodesRatio(matchingSimulations.getSimulations());
			printer.write(oscillating + "  -  " + (1 - oscillating) );
		}
		if(unmatchingSimulations != null){
			printer.write("\n\n*********      Unmatching networks      *********\n");
			printer.flush();
			if(!unmatchingSimulations.getSimulationIds().isEmpty()){
			printer.write("@@@ Basin of attraction\n\n");
			printer.flush();

			printer.write("Size\t\t\t|Occurences\n");
			printer.flush();
			dist = StandardDynamicStatistics.basinOfAttraction(unmatchingSimulations.getSimulations());

			for(Integer key : dist.keySet()){
				printer.write(key + "\t\t\t|" + dist.get(key) + "\n");
				printer.flush();
			}

			printer.write("\n@@@ Attractor length\n\n");
			printer.flush();

			printer.write("Length\t\t\t|Occurences\n");
			printer.flush();
			dist = StandardDynamicStatistics.getAttractorsLength(unmatchingSimulations.getSimulations());

			for(Integer key : dist.keySet()){
				printer.write(key + "\t\t\t|" + dist.get(key) + "\n");
				printer.flush();
			}

			printer.write("\n@@@ Transient length\n\n");
			printer.flush();

			printer.write("Length\t\t\t|Occurences\n");
			printer.flush();
			dist = StandardDynamicStatistics.getTransientLength(unmatchingSimulations.getSimulations());

			for(Integer key : dist.keySet()){
				printer.write(key + "\t\t\t|" + dist.get(key) + "\n");
				printer.flush();
			}

			printer.write("\n@@@ Oscillating vs not oscillating nodes \n\n");
			printer.flush();
			double oscillatingNodes = StandardDynamicStatistics.getOscillatingNodesRatio(unmatchingSimulations.getSimulations());
			printer.write(oscillatingNodes + "  -  " + (1 - oscillatingNodes) + "\n");
			}
		}
		printer.close();

	}*/
	
	
	/**
	 * This method allows to save all the statistics
	 * @param fileName
	 * @param matchingSimulations
	 * @param unmatchingSimulations
	 * @throws IOException
	 * @throws NotExistingSimulationException
	 * @throws NullPointerException
	 * @throws NotExistingNodeException
	 * @throws ParamDefinitionException
	 * @throws InputTypeException
	 */
	/*public static void saveMutationStatistics(String fileName, HashMap<String, MutationManager> mutations, 
			String[] genesNames) throws IOException{
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");

		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);

		PrintWriter printer = new PrintWriter(writer);

		printer.write("######### GRNSim mutations statistics output file #########\n");
		printer.flush();

		printer.write("Date: " + (new Date()).toString() + "\n\n");
		printer.flush();
		
		for(String experiment : mutations.keySet()){
			printer.write("Experiment: " + experiment + "\n\n");
			printer.flush();
			printer.write("Avalanches distribution: \n");
			printer.flush();
			printer.write("Size;Occurences\n");
			printer.flush();
			HashMap<Integer, Integer> avalancheDistribution = DynamicPerturbation.getAvalanche(mutations.get(experiment));
			Object[] keys =  avalancheDistribution.keySet().toArray();
			Arrays.sort(keys);

			for(Object key : keys){
				printer.write((Integer)key + ";" + avalancheDistribution.get(key) + "\n");
				printer.flush();
			}

			printer.write("\n\nSensitivity distribution: \n");
			printer.flush();
			printer.write("Gene name;Occurences\n");
			printer.flush();
			int[] sensitivityDistribution = 
    				DynamicPerturbation.getSensitivity(mutations.get(experiment));
    		for(int i = 0; i < sensitivityDistribution.length; i++){
    			printer.write(genesNames[i] + ";" + sensitivityDistribution[i] + "\n");
    			printer.flush();
    		}
    		printer.write("\n\n\n");
    		printer.flush();
    		
		}
		
		printer.close();
		writer.close();
	}
	*/
	
	
	
	/**
	 * This method saves the ATM in TSV format
	 * @param atm: The ATM to be saved
	 * @param fileName: The name of the file to be saved
	 * @throws IOException: I/O error
	 * @throws NotExistingSimulationException 
	 * @throws NotExistingNodeException 
	 * @throws InputTypeException 
	 * @throws ParamDefinitionException 
	 */
	/*public static void createSynthesisFile(StoredResults results, String fileName) throws IOException, NotExistingSimulationException, NotExistingNodeException, ParamDefinitionException, InputTypeException{
		//Checks the param values
		if(fileName == null)
			throw new NullPointerException("The file name must not be null");
		if(results == null)
			throw new NullPointerException("The StoredResults object must not be null");
		//Defines the writer streams
		FileWriter writer = new FileWriter(fileName);
		PrintWriter printer = new PrintWriter(writer);
		
		for(String simulationId : results.getSimulationIds()){
			//Gets the specific simulation 
			SimulationResult result = results.getStoredSimulation(simulationId);
			//Writes the simulation id
			printer.print(simulationId + ",");
			printer.flush();
			//Writes the clustering coefficient 
			printer.print(result.getClusteringCoefficient() + ",");
			printer.flush();
			//Writes the average bias value
			printer.print(result.getAverageBiasValue() + ",");
			printer.flush();
			//Writes the average path length
			printer.print(result.getAveragePathLength() + ",");
			printer.flush();
			//Writes the network diameter
			printer.print(result.getNetworkDiameter() + ",");
			printer.flush();
			//Writes the attractor number
			printer.print(result.getSamplingManager().getAttractorFinder().getAttractorsNumber() + ",");
			printer.flush();
			//Writes the average attractors length
			printer.print(Statistics.StandardDynamicStatistics.getAverageAttractorsLength(result) + ",");
			printer.flush();
			//Writes the distance from the given tree
			printer.print(result.getDistance() + ",");
			printer.flush();
			//Writes the number of attractors not found
			printer.println(result.getSamplingManager().getAttractorFinder().getAttractorsNotFound());
			printer.flush();
			
		}
		
		printer.close();
		writer.close();
		
		
	}*/
	
	/**
	 * This method saves the tree distances in a file
	 * @throws IOException 
	 */
	/*public static void saveDistances(ArrayList<Integer> distances) throws IOException{
		//Defines the writer streams
		FileWriter writer = new FileWriter("distances.csv");
		PrintWriter printer = new PrintWriter(writer);

		for(Integer distance : distances){
			//Writes the distace
			printer.print(distance + ",");
			printer.flush();
		}

		printer.close();
		writer.close();
	}*/
}
