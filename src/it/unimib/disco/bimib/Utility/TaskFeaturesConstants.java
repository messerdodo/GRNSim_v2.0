package it.unimib.disco.bimib.Utility;

/**
 * This class contains all the constants used in the task features file.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

public class TaskFeaturesConstants {

	/**
	 * This keyword is used in order to specify the task to perform.
	 */
	public static final String TASK_TO_PERFORM = "task-to-perform";
	
	/**
	 * This value specifies the network creation task
	 */
	public static final String NETWORK_CREATION = "network-creation";
	
	/**
	 * This value specifies the network modification task.
	 * This task takes a GRNML network and modifies it in according with 
	 * the passed features.
	 */
	public static final String NETWORK_MODIFICATION = "network-modification";
	
	/**
	 * This value specifies the simulation task with a given network.
	 */
	public static final String OPEN_AND_SIMULATE = "open-and-simulate";
	
	/**
	 * This value specifies the network creation and match with the given tree.
	 */
	public static final String CREATE_AND_MATCH = "create-and-match";
	
	/**
	 * This key specifies the number of threads to use
	 */
	public static final String THREADS = "threads";
	
	/**
	 * This key is used in order to specify the folder where the system stores
	 * the outputs.
	 */
	public static final String OUTPUT_FOLDER = "output-folder";
	
	/**
	 * This key is used in order to specify the original grnml network file path
	 */
	public static final String ORIGINAL_NETWORK_FILE = "original-network-file";
	
	/**
	 * This key is used in order to specify the tree path
	 */
	public static final String TREE_FILE = "tree-file";
	
	/**
	 * This key is used in order to specify if saves the (state, attractor, position) tuple file.
	 */
	public static final String STORE_STATES_ATTRACTORS_FILE = "store-states-attractors-file";
	
	/**
	 * YES value
	 */
	public static final String YES = "yes";
	
	/**
	 * NO value
	 */
	public static final String NO = "no";
}
