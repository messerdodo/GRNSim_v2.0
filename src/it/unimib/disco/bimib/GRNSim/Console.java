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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Console {

	public static void main(String[] args) {
		
		Properties inputArgs, taskFeatures, simulationFeatures;
		int threads = 1, requiredNetworks;
		String taskToPerform, outputFolder, originalGRNMLPath, treeFile;
		ArrayList<Thread> activeThreads = new ArrayList<Thread>();
		Task task = null;
		TaskScheduler scheduler = null; 
	
		String beginningDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	
		//Program output initialization
		System.out.println("GRNSim ver. 2.0 - 2014");
		System.out.println("BIMIB @ DISCo - University of Milano Bicocca");
		System.out.println("****************************************************************");
		
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
			//Creates and tries to match with the given tree
			}else if(taskToPerform.equals(TaskFeaturesConstants.CREATE_AND_MATCH)){
				if(!taskFeatures.containsKey(TaskFeaturesConstants.TREE_FILE))
					throw new MissingFeaturesException(TaskFeaturesConstants.TREE_FILE + " key must be specified in the task file.");
				treeFile = taskFeatures.getProperty(TaskFeaturesConstants.TREE_FILE);
				if(!simulationFeatures.containsKey(SimulationFeaturesConstants.UNMATCHING_STORE))
					throw new MissingFeaturesException(SimulationFeaturesConstants.UNMATCHING_STORE + " key must be specified in the simulation file.");
				task = new NetworkTreeMatchingTask(simulationFeatures,new HashMap<String,String>(), outputFolder, treeFile); 
			}else{
				throw new InputFormatException("Incorrect " + TaskFeaturesConstants.TASK_TO_PERFORM + " value in the task file.");
			}
			
			//Output message
			System.out.println("****************************************************************");
			System.out.println("Dedicated threads: " + threads);
			System.out.println(beginningDate + ": Simulation beginning");
			System.out.println("****************************************************************");
			
			//Starts the execution threads
			scheduler = new TaskScheduler(task, requiredNetworks);
			//Launches 'threads' executor threads
			for(int thread = 0; thread < threads; thread ++){
				activeThreads.add(new TasksExecutor(scheduler, "Executor " + (thread + 1)));
				activeThreads.get(thread).start();
			}
			
			//Main thread waits for sub threads conclusion
			for(int thread = 0; thread < threads; thread ++){
				try{
					activeThreads.get(thread).join();
				}catch(InterruptedException e){
					
				}
			}
			
		}catch(Exception ex){
			
			
			//Displays the exception message
			if(ex.getMessage().equalsIgnoreCase(""))
				System.out.println(ex);
			else
				System.out.println(ex.getMessage());
		}finally{
			//Closes all the tasks
			for(int thread = 0; thread < threads; thread ++){
				activeThreads.get(thread).interrupt();
			}
			
			//Ending simulation message
			System.out.println("****************************************************************");
			String endingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			System.out.println(endingDate + ": Simulation completed");
		}
		
		
	}
}
