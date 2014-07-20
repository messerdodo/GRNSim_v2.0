package it.unimib.disco.bimib.Threads;

//System imports
import java.util.HashMap;
import java.util.Properties;

//GRNSim imports
import it.unimib.disco.bimib.Networks.GraphManager;

public class NetworkCreation implements Task {

	private Properties simulationFeatures;
	private GraphManager graphManager;
	private HashMap<String, String> outputs;
	private String outputFolder;
	
	/**
	 * Generic constructor
	 * @param listener: thread listener object
	 * @param simulationFeatures: Properties object with the simulation features
	 */
	public NetworkCreation(Properties simulationFeatures, HashMap<String, String> outputs, String outputFolder){
		//Parameters checking
		if(simulationFeatures == null)
			throw new NullPointerException("The simulation features must be not null.");
		if(outputs == null)
			throw new NullPointerException("The outputs object must be not null.");
		if(outputFolder == null)
			throw new NullPointerException("The output folder object must be not null.");
		
		this.simulationFeatures = simulationFeatures;
		this.graphManager = null;
		this.outputFolder = outputFolder;
		this.outputs = outputs;
	}
	
	@Override
	public void doTask() throws Exception{
		
		this.graphManager = new GraphManager();
		
		try {
			//Creates the network
			this.graphManager.createNetwork(this.simulationFeatures);
		} catch (Exception ex) {
			//Displays the exception message
			if(ex.getMessage().equalsIgnoreCase(""))
				System.out.println(ex);
			else
				System.out.println(ex.getMessage());
		}

	}

}
