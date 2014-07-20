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
import it.unimib.disco.bimib.Utility.TaskFeaturesConstants;


//System imports
import java.util.Properties;
import java.util.HashMap;
import java.util.ArrayList;

public class Console {

	public static void main(String[] args) {
		
		Properties inputArgs, taskFeatures, simulationFeatures;
		int threads;
		String taskToPerform, outputFolder;
		ArrayList<Thread> runningThreads = new ArrayList<Thread>();
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
			
			
			
			//Network creation
			if(taskToPerform.equals(TaskFeaturesConstants.NETWORK_CREATION)){
				networkCreation(simulationFeatures, threads, outputFolder, runningThreads);
			}
			
		}catch(Exception ex){
			
			
			//Displays the exception message
			if(ex.getMessage().equalsIgnoreCase(""))
				System.out.println(ex);
			else
				System.out.println(ex.getMessage());
		}finally{
			//Stops the generator and execution threads
			for(int thread = 0; thread < runningThreads.size(); thread ++)
				runningThreads.get(thread).interrupt();
		}
	}

	
	
	/**
	 * Network creation task
	 * @param simulationFeatures: Simulation features
	 * @param threads: Number of threads to use
	 * @param outputFolder: Folder where place the output files.
	 */
	private static void networkCreation(Properties simulationFeatures, int threads, String outputFolder, ArrayList<Thread> runningThreads){
		
		//Executors creation
		TasksQueue tasksQueue = new TasksQueue(10);
		
		NetworkCreation creationTask = new NetworkCreation(simulationFeatures,new HashMap<String,String>(), outputFolder); 
		
		
		
		runningThreads.add(new TasksGenerator(tasksQueue, creationTask, "Task generator"));
		//Launches 'threads' executor threads
		for(int thread = 0; thread < threads; thread ++){
			runningThreads.add(new TasksExecutor(tasksQueue, "Executor " + (thread + 1)));
		}
		
		for(Thread thread : runningThreads){
			thread.start();
			System.out.println(thread.getName());
		}
		 
		
	}
	
	
	
	
}
