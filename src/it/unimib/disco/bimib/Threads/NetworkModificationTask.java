package it.unimib.disco.bimib.Threads;

/**
 * This class is the network creation task.
 * This task creates a new network according with the specified features and
 * stores its definition, the atm (0) matrix and the attractors.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */



//System imports
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;







import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;





//GRNSim imports
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Statistics.NetworkStructureStatistics;
import it.unimib.disco.bimib.Utility.OutputConstants;
import it.unimib.disco.bimib.Exceptions.NotExistingNodeException;
import it.unimib.disco.bimib.Exceptions.ParamDefinitionException;
import it.unimib.disco.bimib.IO.Input;
import it.unimib.disco.bimib.IO.Output;
import it.unimib.disco.bimib.Atms.AtmManager;
import it.unimib.disco.bimib.Mutations.MutationManager;

public class NetworkModificationTask implements Task {

	private Properties simulationFeatures;
	private HashMap<String, String> outputs;
	private String outputFolder;
	private GraphManager originalNetwork;
	private boolean statesAttractorsStoring;
	
	/**
	 * Generic constructor
	 * @param simulationFeatures: Properties object with the simulation features
	 * @param outputs: network-output file matching.
	 * @param outputFolder: The folder where put the outputs
	 * @param originalNetworkFile: The original network grnml file (with its path)
	 * @param statesAttractorsStoring: Specifies if the statesAttractors file must be stored
	 * @throws NotExistingNodeException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws ParamDefinitionException 
	 */
	public NetworkModificationTask(Properties simulationFeatures, HashMap<String, String> outputs, String outputFolder, String originalNetworkFile, boolean  statesAttractorsStoring) throws ParamDefinitionException, ParserConfigurationException, SAXException, IOException, NotExistingNodeException{
		//Parameters checking
		if(simulationFeatures == null)
			throw new NullPointerException("The simulation features must be not null.");
		if(outputs == null)
			throw new NullPointerException("The outputs object must be not null.");
		if(outputFolder == null)
			throw new NullPointerException("The output folder object must be not null.");
		
		this.simulationFeatures = simulationFeatures;
		this.outputFolder = outputFolder;
		this.outputs = outputs;
		this.originalNetwork = Input.readGRNMLFile(originalNetworkFile);
		this.statesAttractorsStoring =  statesAttractorsStoring;
		
	}
	
	@Override
	public boolean doTask() throws Exception{

		//Creates the network
		GraphManager graphManager = this.originalNetwork.copy();

		//Modifies the network
		graphManager.modify(simulationFeatures);

		//Samples the network in order to find the attractors
		SamplingManager samplingManager = new SamplingManager(simulationFeatures, graphManager);

		//Defines the mutation manager
		MutationManager mutationManager = new MutationManager(graphManager, samplingManager, simulationFeatures);

		//Creates the ATM manager and the ATM matrix
		AtmManager atmManager = new AtmManager(simulationFeatures, samplingManager, mutationManager, graphManager.getNodesNumber());

		//Saves the network in the correct folder
		String simulationID = String.valueOf(graphManager.hashCode());
		String networkFolderName = "network_" + simulationID;
		String networkFileName = simulationID + "_network.grnml";
		String atmFileName = simulationID + "_atm.csv";
		String attractorsFileName = simulationID + "_attractors.csv";
		String synthesisFileName = simulationID + "_synthesis.csv";
		String statesAttractorsFileName = simulationID + "_statesAttractors.csv";
		
		//Creates the folder
		Output.createFolder(this.outputFolder + "/" + networkFolderName);

		//Stores the network file (GRNML)
		Output.createGRNMLFile(graphManager.getGraph(), this.outputFolder + "/" + networkFolderName + "/" + networkFileName);

		//Stores the ATM matrix
		Output.createATMFile(atmManager.getAtm(), this.outputFolder + "/" + networkFolderName + "/" + atmFileName);
		//Stores the attractors
		Output.saveAttractorsFile(samplingManager.getAttractorFinder(), this.outputFolder + "/" + networkFolderName + "/" + attractorsFileName);

		//If required, stores the state-attractors file.
		if(statesAttractorsStoring)
			Output.saveStatesAttractorsFile(this.outputFolder + "/" + networkFolderName + "/" + statesAttractorsFileName, samplingManager.getAttractorFinder());

		
		//Saves the statistics
		Properties statistics = new Properties();
		statistics.put(OutputConstants.SIMULATION_ID, simulationID);
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

		//Stores the outputs folder
		this.outputs.put(simulationID, this.outputFolder + "/" + networkFolderName + "/");
				
		//Output message
		System.out.println("Network saved at " + this.outputFolder + "/" + networkFolderName + "/" + networkFileName);
				
		//Always match
		return true;


	}

}
