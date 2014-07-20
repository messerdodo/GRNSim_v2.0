/**
 * This class is the manager for the sampling package.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Sampling;

//System imports
import java.util.Properties;

//GRNSim imports
import it.unimib.disco.bimib.Utility.*;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.GraphManager;

public class SamplingManager {


	private AttractorsFinder sampling;

	/**
	 * Default constructor.
	 * In this method the correct sampling algorithm is chosen.
	 * @param features: Input parameters
	 * @param graph: The graph manager which contains the network graph
	 * @throws NullPointerException: Null parameter passed
	 * @throws ParamDefinitionException: Incorrect parameter passed
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 */
	public SamplingManager(Properties simulationFeatures, GraphManager graph) throws NullPointerException, ParamDefinitionException, NotExistingNodeException, InputTypeException, AttractorNotFoundException {

		int initialConditions, cutoff;

		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.SAMPLING_METHOD))
			throw new ParamDefinitionException("Sampling method type missed");
		
		//Chooses the sampling method to be execute
		//*** BRUTE FORCE ***
		if(simulationFeatures.get(SimulationFeaturesConstants.SAMPLING_METHOD).equals(SimulationFeaturesConstants.BRUTE_FORCE)){
			this.sampling = new BruteForceSampling(graph);

			//*** PARTIAL SAMPLING ***
		}else if(simulationFeatures.get(SimulationFeaturesConstants.SAMPLING_METHOD).equals(SimulationFeaturesConstants.PARTIAL_SAMPLING)){
			//Checks if the number of steps are specified
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.INITIAL_CONDITIONS))
				throw new ParamDefinitionException("Number of initial condition missed");
			initialConditions = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.INITIAL_CONDITIONS).toString());
			//Validates the number of initial condition to try
			if(initialConditions <= 0)
				throw new ParamDefinitionException("The number of initial condition must be greater than 0");
			//Gets the cutoff
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MAX_SIMULATION_TIMES))
				throw new ParamDefinitionException(SimulationFeaturesConstants.MAX_SIMULATION_TIMES + " key missed");
			cutoff = Integer.valueOf(simulationFeatures.get(SimulationFeaturesConstants.MAX_SIMULATION_TIMES).toString());
			//Validates the cutoff
			if(cutoff < -2)
				throw new ParamDefinitionException("The " + SimulationFeaturesConstants.MAX_SIMULATION_TIMES + " value must be greater than 0");

			//Sampling
			this.sampling = new PartialSampling(graph, initialConditions, cutoff);
		}

	}

	/**
	 * Default constructor
	 * @param attractorsFinder: Specifies the attractorsFinder object managed. 
	 */
	public SamplingManager(AttractorsFinder attractorsFinder){
		if(attractorsFinder == null)
			throw new NullPointerException("The attractor finder object must be not null");
		this.sampling = attractorsFinder;
	}


	/**
	 * This method returns the attractor finder object
	 * @return the attractor finder object
	 */
	public AttractorsFinder getAttractorFinder(){
		return this.sampling;
	}

}

