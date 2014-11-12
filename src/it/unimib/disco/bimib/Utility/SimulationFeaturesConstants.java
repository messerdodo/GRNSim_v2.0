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
	 * NO value
	 */
	public static final String NO = "no";
	
	/**
	 * Excludes the passed genes as source.
	 */
	public static final String EXCLUDES_SOURCE_GENES = "excludes-source-genes";
	
	/**
	 * Excludes the passed genes as target.
	 */
	public static final String EXCLUDES_TARGET_GENES = "excludes-target-genes";
	
	/**
	 * Sampling method key. This key is used in order to define the
	 * chosen sampling method. For the corresponding value see the 
	 * 'SamplingMethodType' enumeration file.
	 * Note: this key is mandatory.
	 */
	public static final String SAMPLING_METHOD = "sampling-method";
	
	/**
	 * This key is used in order to specify the brute force sampling method.
	 */
	public static final String BRUTE_FORCE = "BruteForce";
	
	/**
	 * This key is used in order to specify the partial sampling method.
	 */
	public static final String PARTIAL_SAMPLING = "Partial";
	
	/**
	 * Initial conditions number key. This value, that must be integer and less than 2^nodes_number, 
	 * indicates the number of random initial conditions to test. 
	 */
	public static final String INITIAL_CONDITIONS = "initial-conditions";
	
	/**
	 * Max simulation times key. This value, that must be integer and greater than 0, is a cutoff value
	 * for the attractors searching.
	 */
	public static String MAX_SIMULATION_TIMES = "max-simulation-times";
	
	/**
	 * Number of perturb experiments key. This value, that must be integer, indicates the number of perturbation 
	 * will be done in each experiment.
	 */
	public static final String HOW_MANY_PERTURB_EXP = "how-many-perturb-exp";
	
	/**
	 * Mutation type key. It is uses in order to define the mutations type.
	 * The corresponding value must be custom or random in the mutation 
	 * features file or must be Flip or Temporary in the simulation features
	 * file. See 'MutationType' enumeration file for more information. 
	 */
	public static final String MUTATION_TYPE = "mutation-type";
    
    /**
	 * Mutated nodes key.
	 * It is used in order to define the number of nodes which must be mutated.
	 */
	public static final String HOW_MANY_NODES_TO_PERTURB = "how-many-nodes-to-perturb";
    
    /**
	 * Mutation rate key. It is used in order to define the ratio of the states
	 * in the attractor to be mutated.
	 */
	public static final String RATIO_OF_STATES_TO_PERTURB = "ratio-of-states-to-perturb";

	/**
	 * Minimum duration of perturb key. It is used in order to define
	 * the minimum simulation times in which the temporary mutation will be 
	 * active. The corresponding value must be a positive integer.
	 */
	public static final String MIN_DURATION_OF_PERTURB = "min-duration-of-perturb";

	/**
	 * Maximum duration of perturb key. It is used in order to define
	 * the maximum simulation times in which the temporary mutation will be 
	 * active. The corresponding value must be a positive integer.
	 */
	public static final String MAX_DURATION_OF_PERTURB = "max-duration-of-perturb";
	
	/**
	 * Min duration of the knock-in event key.
	 */
	public static final String MIN_KNOCKIN_DURATION = "min-knockin-duration";
	
	/**
	 * Max duration of the knock-in event key.
	 */
	public static final String MAX_KNOCKIN_DURATION = "max-knockin-duration";
	
	/**
	 * Min duration of the knock-out event key.
	 */
	public static final String MIN_KNOCKOUT_DURATION = "min-knockout-duration";
	
	/**
	 * Max duration of the knock-out event key.
	 */
	public static final String MAX_KNOCKOUT_DURATION = "max-knockout-duration";
	
	/**
	 * This key defines a Flip mutation
	 */
	public static final String FLIP_MUTATIONS = "Flip";
	
	/**
	 * This key defines a Temporary mutation
	 */
	public static final String TEMPORARY_MUTATIONS = "Temporary";
	
	/**
	 * This key defines a KnockIn/Knock out mutation
	 */
	public static final String KNOCKIN_KNOCKOUT_MUTATIONS = "KnockIn-KnockOut";
	
	/**
	 * This key defines the number of nodes to be set as active.
	 */
	public static final String KNOCKIN_NODES = "knockIn-nodes";
	
	/**
	 * This key defines the number of nodes to be set as deactive
	 */
	public static final String KNOCKOUT_NODES = "knockOut-nodes";
	
	/**
	 * This key is used in order to specify if the computation of the avalanches and sensitivity 
	 * distribution is required.
	 */
	public static final String COMPUTE_AVALANCHES_AND_SENSITIVITY = "avalanches-sensitivity-computation";
	
	/**
	 * Number of matching network required key.
	 */
	public static final String MATCHING_NETWORKS = "matching-networks";
	
	/**
	 * This key is used in order to specify if the unmatching network
	 * must be stored.
	 */
	public static final String UNMATCHING_STORE = "unmatching-store";
	
	/**
	 * This key is used in order to specify the tree comparison method.
	 */
	public static final String MATCHING_METHOD = "matching-method";
	
	/**
	 * Value for perfect comparison between two trees.
	 */
	public static final String PERFECT = "Perfect";
	
	/**
	 * Value for min distance comparison between two trees.
	 */
	public static final String MIN_DISTANCE = "MinDistance";
	
	/**
	 * Value for min histogram distance comparison between two trees.
	 */
	public static final String MIN_HISTOGRAM_DISTANCE = "MinHistogramDistance";
	
	/**
	 * This key is used in order to specify the threshold for the matching
	 */
	public static final String THRESHOLD = "threshold";
	
	/**
	 * This key is used in order to specify the names of the specific nodes to perturb
	 */
	public static final String SPECIFIC_PERTURB_NODES = "specific-perturb-nodes";
	
	/**
	 * This key is used in order to specify the names of the specific nodes to knock-in
	 */
	public static final String SPECIFIC_KNOCK_IN_NODES = "specific-knock-in-nodes";
	
	/**
	 * This key is used in order to specify the names of the specific nodes to knock-out
	 */
	public static final String SPECIFIC_KNOCK_OUT_NODES = "specific-knock-out-nodes";
	
	/**
	 * This key is used in order to specify the names of the nodes to permanently knock-in
	 */
	public static final String PERMANENTLY_KNOCK_IN_NODES = "permanently-knock-in-nodes";
	
	/**
	 * This key is used in order to specify the names of the nodes to permanently knock-out
	 */
	public static final String PERMANENTLY_KNOCK_OUT_NODES = "permanently-knock-out-nodes";
}
