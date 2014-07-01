package it.unimib.disco.bimib.threads;

//System imports
import java.util.Properties;

//GRNSim imports
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Utility.ConsoleConstants;

public class NetworkCreation implements Runnable {

	private ThreadListener threadListener;
	private Properties simulationFeatures;
	private GraphManager graphManager;
	private int threadId;
	
	/**
	 * Generic constructor
	 * @param listener: thread listener object
	 * @param simulationFeatures: Properties object with the simulation features
	 * @param threadId: Numeric thread identifier
	 */
	public NetworkCreation(ThreadListener listener, Properties simulationFeatures, int threadId){
		//Parameters checking
		if(listener == null)
			throw new NullPointerException("The thread listener must be not null.");
		if(simulationFeatures == null)
			throw new NullPointerException("The simulation features must be not null.");
		
		this.threadListener = listener;
		this.simulationFeatures = simulationFeatures;
		this.graphManager = null;
		this.threadId = threadId;
	}
	
	
	@Override
	public void run() {
		
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
		}finally{
			//Calls the lister
			this.threadListener.threadFinished(ConsoleConstants.NETWORK_CREATION_TASK, this.threadId);
		}

	}

}
