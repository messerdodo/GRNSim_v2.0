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
import it.unimib.disco.bimib.Threads.*;
import it.unimib.disco.bimib.Utility.ConsoleConstants;
import it.unimib.disco.bimib.Utility.SimulationFeaturesConstants;
import it.unimib.disco.bimib.Utility.TaskFeaturesConstants;

//System imports
import java.util.Properties;
import java.util.HashMap;
import java.util.ArrayList;

public class Console {

	public static void main(String[] args) {
		
		Properties inputArgs, taskFeatures, simulationFeatures;
		int threads, requiredNetworks;
		String taskToPerform, outputFolder, originalGRNMLPath;
		ArrayList<Thread> activeThreads = new ArrayList<Thread>();
		Task task = null;
		TaskScheduler scheduler = null; 
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
			
			//Checks the threads feature existence
			if(!taskFeatures.containsKey(TaskFeaturesConstants.THREADS))
				throw new MissingFeaturesException(TaskFeaturesConstants.THREADS + " key must be specified in the task features file.");
			//Gets the number of thread to use in the simulation
			threads = Integer.valueOf(taskFeatures.getProperty(TaskFeaturesConstants.THREADS));
		
			//Gets the task to perform
			if(!taskFeatures.containsKey(TaskFeaturesConstants.TASK_TO_PERFORM))
				throw new MissingFeaturesException(TaskFeaturesConstants.TASK_TO_PERFORM + " key must be specified in the task features file."); 
			taskToPerform = taskFeatures.getProperty(TaskFeaturesConstants.TASK_TO_PERFORM);
			
			//Gets the output folder
			if(!taskFeatures.containsKey(TaskFeaturesConstants.OUTPUT_FOLDER))
				throw new MissingFeaturesException(TaskFeaturesConstants.OUTPUT_FOLDER + " key must be specified in the task features file."); 
			outputFolder = taskFeatures.getProperty(TaskFeaturesConstants.OUTPUT_FOLDER);
			
			//Gets the number of required networks
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MATCHING_NETWORKS))
				throw new MissingFeaturesException("The " + SimulationFeaturesConstants.MATCHING_NETWORKS + " key must be specified in the simulation features file.");
			requiredNetworks = Integer.parseInt(simulationFeatures.getProperty(SimulationFeaturesConstants.MATCHING_NETWORKS));
			
			
			
			//Network creation (creation and simulation)
			if(taskToPerform.equals(TaskFeaturesConstants.NETWORK_CREATION)){
				//networkCreation(activeThreads, simulationFeatures, threads, outputFolder, requiredNetworks);
				task = new NetworkCreation(simulationFeatures,new HashMap<String,String>(), outputFolder); 
			//Network Modification (editing and simulation)
			}else if(taskToPerform.equals(TaskFeaturesConstants.NETWORK_MODIFICATION)){
				//Gets the original network grnml file name.
				if(!taskFeatures.containsKey(TaskFeaturesConstants.ORIGINAL_NETWORK_FILE))
					throw new MissingFeaturesException(TaskFeaturesConstants.ORIGINAL_NETWORK_FILE + " key must be specified in the task features file."); 
				originalGRNMLPath = taskFeatures.getProperty(TaskFeaturesConstants.ORIGINAL_NETWORK_FILE);
				//networkModification(activeThreads, simulationFeatures, threads, outputFolder, originalGRNMLPath, requiredNetworks);
				task = new NetworkModificationTask(simulationFeatures,new HashMap<String,String>(), outputFolder, originalGRNMLPath); 
			//Open and simulation (opens a given network and simulates it)
			}else if(taskToPerform.equals(TaskFeaturesConstants.OPEN_AND_SIMULATE)){
				//Gets the original network grnml file name.
				if(!taskFeatures.containsKey(TaskFeaturesConstants.ORIGINAL_NETWORK_FILE))
					throw new MissingFeaturesException(TaskFeaturesConstants.ORIGINAL_NETWORK_FILE + " key must be specified in the task features file."); 
				//Gets the given network
				originalGRNMLPath = taskFeatures.getProperty(TaskFeaturesConstants.ORIGINAL_NETWORK_FILE);
				task = new OpenAndSimulationTask(simulationFeatures,new HashMap<String,String>(), outputFolder, originalGRNMLPath); 
			}else{
				throw new InputFormatException("Incorrect " + TaskFeaturesConstants.TASK_TO_PERFORM + " value in the task file.");
			}
			
			//Starts the execution threads
			scheduler = new TaskScheduler(task, requiredNetworks);
			//Launches 'threads' executor threads
			for(int thread = 0; thread < threads; thread ++){
				activeThreads.add(new TasksExecutor(scheduler, "Executor " + (thread + 1)));
				activeThreads.get(thread).start();
			}
	
		}catch(Exception ex){
			
			
			//Displays the exception message
			if(ex.getMessage().equalsIgnoreCase(""))
				System.out.println(ex);
			else
				System.out.println(ex.getMessage());
		}finally{
			//Closes all the tasks
			
		}
	}

	
	
	/**
	 * Network creation task
	 * @param simulationFeatures: Simulation features
	 * @param threads: Number of threads to use
	 * @param outputFolder: Folder where place the output files.
	 */
/*	private static void networkCreation(ArrayList<Thread> activeThreads , Properties simulationFeatures, int threads, String outputFolder, int requiredNetworks){
		NetworkCreation creationTask = new NetworkCreation(simulationFeatures,new HashMap<String,String>(), outputFolder); 
		TaskScheduler scheduler = new TaskScheduler(creationTask, requiredNetworks);
		//Launches 'threads' executor threads
		for(int thread = 0; thread < threads; thread ++){
			activeThreads.add(new TasksExecutor(scheduler, "Executor " + (thread + 1)));
			activeThreads.get(thread).start();
		}

	}
	
	/**
	 * Network modification task
	 * @param simulationFeatures: Simulation features
	 * @param threads: Number of threads to use
	 * @param outputFolder: Folder where place the output files.
	 * @param originalGRNMLPath: The path of the original GRNML file.
	 * @throws NotExistingNodeException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws ParamDefinitionException 
	 */
/*	private static void networkModification(ArrayList<Thread> activeThreads, Properties simulationFeatures, int threads, String outputFolder, String originalGRNMLPath, int requiredNetworks) throws ParamDefinitionException, ParserConfigurationException, SAXException, IOException, NotExistingNodeException{

		NetworkModificationTask editingTask = new NetworkModificationTask(simulationFeatures,new HashMap<String,String>(), outputFolder, originalGRNMLPath); 
		TaskScheduler scheduler = new TaskScheduler(editingTask, requiredNetworks);
		
		//Launches 'threads' executor threads
		for(int thread = 0; thread < threads; thread ++){
			activeThreads.add(new TasksExecutor(scheduler, "Executor " + (thread + 1)));
			activeThreads.get(thread).start();
		}
		
	}
	
	*/
	
}
