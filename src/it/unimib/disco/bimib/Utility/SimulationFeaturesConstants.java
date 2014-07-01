/**
 * This class contains all the constants used in the simulation features file.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 * 
 */

package it.unimib.disco.bimib.Utility;

public class SimulationFeaturesConstants {

	
	/**
	 * Topology key. It used to define the network topology.
	 * The corresponding value must be Random, ScaleFree or SmallWorld.
	 */
	public static final String TOPOLOGY = "topology";
	
	/**
	 * Nodes key. It defines the number of nodes in the network.
	 * The corresponding value must be a positive integer.
	 */
	public static final String NODES = "nodes";
	
	/**
	 * Edge key. It specifies the number of edges required in the network.
	 * It used for the random topology with ErdosRenyi method.
	 * The corresponding value must be a positive integer.
	 * For more information see the Erdos Renyi algorithm specifics.
	 */
	public static final String EDGES = "edges";
	
	/**
	 * Random topology key.
	 */
	public static final String RANDOM_TOPOLOGY = "Random";
	
	/**
	 * Scale-free topology key
	 */
	public static final String SCALE_FREE_TOPOLOGY = "ScaleFree";
	
	/**
	 * Small world topology key
	 */
	public static final String SMALL_WORLD_TOPOLOGY = "SmallWorld";
	
	/**
	 * Partially Random topology key
	 */
	public static final String PARTIALLY_RANDOM_TOPOLOGY = "PartiallyRandom";
	
	/**
	 * Partially Random topology key
	 */
	public static final String HIERARCHICAL_TOPOLOGY = "Hierarchical";
	
	/**
	 * Algorithm key. It defines the chosen algorithm for the network 
	 * construction. The corresponding value can be found in the 
	 * 'GraphGenerationAlgorithm' enumeration.
	 */
	public static final String ALGORITHM = "algorithm";
	
	/**
	 * Barabasi-Alberts algorithm key.
	 * It is used only with a Scale-Free topology.
	 */
	public static final String BARABASI_ALBERTZ_ALGORITHM = "BarabasiAlbertz";
	
	/**
	 * Power law algorithm key.
	 * It is used only with a Scale-Free topology.
	 */
	public static final String POWER_LAW_ALGORITHM = "PowerLaw";
	
	/**
	 * Fixed power law algorithm key.
	 * It is used only with a Scale-free topology.
	 */
	public static final String FIXED_POWER_LAW_ALGORITHM = "FixedPowerLaw";
	
	/**
	 * Average connectivity key. It is used to define the average connectivity
	 * in a scale free or small world topology graph construction.
	 * The corresponding value must be a positive integer.
	 */
	public static final String AVERAGE_CONNECTIVITY = "k";

	/**
	 * Beta key. It is used in order to define the edge changing probability
	 * in the SmallWorld topology graph construction.
	 * The corresponding value must be a double value in [0, 1]
	 */
	public static final String BETA = "beta";

	/**
	 * Gamma key. It is used in order to define the gamma parameter 
	 * for the power law model graph.
	 * the corresponding value must be a positive double.
	 */
	public static final String GAMMA = "gamma";

	/**
	 * Initial nodes number key. It defines the number of initial nodes in 
	 * the Barabasi-Alberts algorithm.
	 */
	public static final String NI = "ni";
	
	/**
	 * Fixed inputs number key. It defines the  number of incoming edges 
	 * that each node must have.
	 */
	public static final String FIXED_INPUTS_NUMBER = "fixed-inputs-number";
	
	//Functions constants.

	/**
	 * Completely defined functions key. This mandatory parameter is used in order to specify 
	 * the function definition. If the functions are completely defined the entire function
	 * table will be created.
	 */
	public static final String COMPLETELY_DEFINED_FUNCTIONS = "completely-defined-functions";
	
	/**
	 * Function type key. It used in order to define the function type.
	 * The corresponding value can be found in the 'FunctionType' enumeration
	 * file.
	 */
	public static final String FUNCTION_TYPE = "function-type";

	/**
	 * Random type key. It used in order to specify the random function rate.
	 * The corresponding value must be a double in [0, 1].
	 * This key is not mandatory.
	 */
	public static final String RANDOM_TYPE = "random-type";

	/**
	 * Bias type key. It used in order to define the random with bias 
	 * function rate. The corresponding value must be a double in [0, 1].
	 * This key is not mandatory.
	 */
	public static final String BIAS_TYPE = "bias-type";

	/**
	 * Bias value key. It used in order to define the bias in the bias random
	 * functions. The corresponding value must be a double in [0, 1].
	 * Note that this key are mandatory if the BIAS_TYPE is defined.
	 */
	public static final String BIAS_VALUE = "bias-value";

	/**
	 * And function key. It defines the and functions rate.
	 * The corresponding value must be a double in [0, 1].
	 * This key is not mandatory.
	 */
	public static final String AND_FUNCTION_TYPE = "and-type";

	/**
	 * Or function key. It defines the or functions rate.
	 * The corresponding value must be a double in [0, 1].
	 * This key is not mandatory.
	 */
	public static final String OR_FUNCTION_TYPE = "or-type";

	/**
	 * Canalized function key. It defines the canalized functions rate.
	 * The corresponding value must be a double in [0, 1].
	 * This key is not mandatory.
	 */
	public static final String CANALIZED_TYPE = "canalized-type";
	
	/**
	 * Boolean function key.
	 */
	public static final String BOOLEAN_FUNCTION = "Boolean";
	
	/**
	 * YES value
	 */
	public static final String YES = "yes";
	
	/**
	 * Excludes the passed genes as source.
	 */
	public static final String EXCLUDES_SOURCE_GENES = "excludes-source-genes";
	
	/**
	 * Excludes the passed genes as target.
	 */
	public static final String EXCLUDES_TARGET_GENES = "excludes-target-genes";
}
