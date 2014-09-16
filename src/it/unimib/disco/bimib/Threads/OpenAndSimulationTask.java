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
import it.unimib.disco.bimib.Exceptions.NotExistingNodeException;
import it.unimib.disco.bimib.Exceptions.ParamDefinitionException;
import it.unimib.disco.bimib.IO.Input;
import it.unimib.disco.bimib.IO.Output;
import it.unimib.disco.bimib.Atms.AtmManager;
import it.unimib.disco.bimib.Mutations.MutationManager;

public class OpenAndSimulationTask implements Task {

	private Properties simulationFeatures;
	private HashMap<String, String> outputs;
	private String outputFolder;
	private GraphManager originalNetwork;
	
	/**
	 * Generic constructor
	 * @param listener: thread listener object
	 * @param simulationFeatures: Properties object with the simulation features
	 * @throws NotExistingNodeException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws ParamDefinitionException 
	 */
	public OpenAndSimulationTask(Properties simulationFeatures, HashMap<String, String> outputs, String outputFolder, String originalNetworkFile) throws ParamDefinitionException, ParserConfigurationException, SAXException, IOException, NotExistingNodeException{
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
	
	}
	
	@Override
	public boolean doTask() throws Exception{

		//Creates the network
		GraphManager graphManager = this.originalNetwork.copy();

		//Modifies the network
		//graphManager.modify(simulationFeatures);

		//Samples the network in order to find the attractors
		SamplingManager samplingManager = new SamplingManager(simulationFeatures, graphManager);

		//Defines the mutation manager
		MutationManager mutationManager = new MutationManager(graphManager, samplingManager, simulationFeatures);

		//Creates the ATM manager and the ATM matrix
		AtmManager atmManager = new AtmManager(simulationFeatures, samplingManager, mutationManager, graphManager.getNodesNumber());

		//Saves the network in the correct folder
		String networkFolderName = "network_" + graphManager.hashCode();
		String networkFileName = graphManager.hashCode() + "_network.grnml";
		String atmFileName = graphManager.hashCode() + "_atm.csv";
		String attractorsFileName = graphManager.hashCode() + "_attractors.csv";

		//Creates the folder
		Output.createFolder(this.outputFolder + "/" + networkFolderName);

		//Stores the network file (GRNML)
		System.out.println(this.outputFolder + "/" + networkFolderName + "/" + networkFileName);
		Output.createGRNMLFile(graphManager.getGraph(), this.outputFolder + "/" + networkFolderName + "/" + networkFileName);

		//Stores the ATM matrix
		Output.createATMFile(atmManager.getAtm(), this.outputFolder + "/" + networkFolderName + "/" + atmFileName);
		//Stores the attractors
		Output.saveAttractorsFile(samplingManager.getAttractorFinder(), this.outputFolder + "/" + networkFolderName + "/" + attractorsFileName);

		//Allways match
		return true;


	}

}