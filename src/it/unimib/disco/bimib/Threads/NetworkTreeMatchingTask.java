package it.unimib.disco.bimib.Threads;

/**
 * This class is the network creation and tree matching task.
 * This task creates a new network according with the specified features and
 * stores its definition, the atm (0) matrix and the attractors.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

//System imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;







//GRNSim imports
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Statistics.NetworkStructureStatistics;
import it.unimib.disco.bimib.Utility.OutputConstants;
import it.unimib.disco.bimib.Utility.SimulationFeaturesConstants;
import it.unimib.disco.bimib.Exceptions.InputFormatException;
import it.unimib.disco.bimib.Exceptions.ParamDefinitionException;
import it.unimib.disco.bimib.Exceptions.TesTreeException;
import it.unimib.disco.bimib.IO.*;
import it.unimib.disco.bimib.Atms.AtmManager;
import it.unimib.disco.bimib.Mutations.MutationManager;
import it.unimib.disco.bimib.Tes.TesManager;
import it.unimib.disco.bimib.Tes.TesTree;

public class NetworkTreeMatchingTask implements Task {

	private Properties simulationFeatures;
	private HashMap<String, String> outputs;
	private String outputFolder;
	private TesTree differentiationTree;
	private boolean unmatchingStore;
	
	/**
	 * Generic constructor
	 * @param listener: thread listener object
	 * @param simulationFeatures: Properties object with the simulation features
	 * @throws ParamDefinitionException 
	 * @throws InputFormatException 
	 * @throws FileNotFoundException 
	 * @throws NullPointerException 
	 * @throws TesTreeException 
	 * @throws NumberFormatException 
	 */
	public NetworkTreeMatchingTask(Properties simulationFeatures, HashMap<String, String> outputs, String outputFolder, String treePath) throws ParamDefinitionException, NullPointerException, FileNotFoundException, InputFormatException, NumberFormatException, TesTreeException{
		//Parameters checking
		if(simulationFeatures == null)
			throw new NullPointerException("The simulation features must be not null.");
		if(outputs == null)
			throw new NullPointerException("The outputs object must be not null.");
		if(outputFolder == null)
			throw new NullPointerException("The output folder object must be not null.");
		if(treePath == null)
			throw new NullPointerException("The tree path must be not null.");
		if(!(new File(treePath).exists()))
			throw new ParamDefinitionException("The tree file does not exist.");
		
		this.simulationFeatures = simulationFeatures;
		this.outputFolder = outputFolder;
		this.outputs = outputs;
		this.unmatchingStore = simulationFeatures.getProperty(SimulationFeaturesConstants.UNMATCHING_STORE).equals(SimulationFeaturesConstants.YES) ? true : false;
		
		//Creates the tree
		this.differentiationTree = TesManager.createTesTreeFromFile(Input.readTree(treePath));
		
		//Match and unmatch folder creation
		File matchDir = new File(this.outputFolder + "/Match");
		File unmatchDir = new File(this.outputFolder + "/Unmatch");
		if(!matchDir.exists())
			Output.createFolder(this.outputFolder + "/Match");
		if(!unmatchDir.exists())
			Output.createFolder(this.outputFolder + "/Unmatch");
	}
	
	@Override
	public boolean doTask() throws Exception{
		
		double [] deltas;
		String networkFolderName;
		
		//Creates the network
		GraphManager graphManager = new GraphManager();
		graphManager.createNetwork(simulationFeatures);

		//Samples the network in order to find the attractors
		SamplingManager samplingManager = new SamplingManager(simulationFeatures, graphManager);

		//Defines the mutation manager
		MutationManager mutationManager = new MutationManager(graphManager, samplingManager, simulationFeatures);

		//Creates the ATM manager and the ATM matrix
		AtmManager atmManager = new AtmManager(simulationFeatures, samplingManager, mutationManager, graphManager.getNodesNumber());

		//Creates the TES manager in order to match the network with the tree
		TesManager tesManager = new TesManager(atmManager, samplingManager);
		
		//Tries to match the network with the given differentiation tree
		deltas = tesManager.findCorrectTesTree(this.differentiationTree);

		if(deltas != null){
			//Match
			networkFolderName = "Match/network_" + graphManager.hashCode();
			System.out.print("Matching network found. ");
			System.out.println(Arrays.toString(deltas));
		}else{
			//Unmatch
			networkFolderName = "Unmatch/network_" + graphManager.hashCode();
		}
		//Stores the network only if unmatching-store is set as yes or if the network matches.
		if(deltas != null || this.unmatchingStore){
			//Saves the network in the correct folder
			String networkFileName = graphManager.hashCode() + "_network.grnml";
			String atmFileName = graphManager.hashCode() + "_atm.csv";
			String attractorsFileName = graphManager.hashCode() + "_attractors.csv";
			String synthesisFileName = graphManager.hashCode() + "_synthesis.csv";
			//Creates the folder
			Output.createFolder(this.outputFolder + "/" + networkFolderName);

			//Stores the network file (GRNML)
			Output.createGRNMLFile(graphManager.getGraph(), this.outputFolder + "/" + networkFolderName + "/" + networkFileName);
			//Stores the ATM matrix
			Output.createATMFile(atmManager.getAtm(), this.outputFolder + "/" + networkFolderName + "/" + atmFileName);
			//Stores the attractors
			Output.saveAttractorsFile(samplingManager.getAttractorFinder(), this.outputFolder + "/" + networkFolderName + "/" + attractorsFileName);

			//Saves the statistics
			Properties statistics = new Properties();
			statistics.put(OutputConstants.SIMULATION_ID, graphManager.hashCode());
			statistics.put(OutputConstants.CLUSTERING_COEFFICIENT, NetworkStructureStatistics.getClusteringCoefficient(graphManager));
			statistics.put(OutputConstants.AVERAGE_BIAS, NetworkStructureStatistics.getAverageBiasValue(graphManager));
			statistics.put(OutputConstants.AVERAGE_PATH_LENGTH, NetworkStructureStatistics.getAveragePath(graphManager));
			statistics.put(OutputConstants.NETWORK_DIAMETER, NetworkStructureStatistics.getNetworkDiameter(graphManager));
			statistics.put(OutputConstants.ATTRACTORS_NUMBER, samplingManager.getAttractorFinder().getAttractorsNumber());
			//Gets the average attractor length 
			double avgLength = 0.0;
			for(Object attractor : samplingManager.getAttractorFinder().getAttractors())
				avgLength = avgLength + samplingManager.getAttractorFinder().getAttractorLength(attractor);
			statistics.put(OutputConstants.ATTRACTORS_LENGTH, avgLength/samplingManager.getAttractorFinder().getAttractorsNumber());
			statistics.put(OutputConstants.TREE_DISTANCE, 0);
			statistics.put(OutputConstants.NOT_FOUND_ATTRACTORS, 0);

			Output.createSynthesisFile(statistics, this.outputFolder + "/" + networkFolderName + "/" + synthesisFileName);

			//Output message
			System.out.println("Network saved at " + this.outputFolder + "/" + networkFolderName + "/" + networkFileName);
		}
		return deltas != null;
		
	}

}
