/**
 * This class manages the atm
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 * 
 */

package it.unimib.disco.bimib.Atms;

//System imports
import java.util.Properties;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Mutations.MutationManager;
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Utility.*;

public class AtmManager {

	private Atm atm;
	public AtmManager(){}
	/**
	 * This is the constructor
	 * @param simulationFeatures
	 * @param samplingManager
	 * @param mutationManager
	 * @param nodes
	 * @throws MissingFeaturesException
	 * @throws NumberFormatException
	 * @throws ParamDefinitionException
	 * @throws NotExistingAttractorsException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws FeaturesException
	 * @throws AttractorNotFoundException 
	 */
	public AtmManager(Properties simulationFeatures, SamplingManager samplingManager, MutationManager mutationManager, int nodes) 
			throws MissingFeaturesException, NumberFormatException, 
			ParamDefinitionException, NotExistingAttractorsException, NotExistingNodeException, InputTypeException, FeaturesException, AttractorNotFoundException {

		double mutationRate;
		int mutatedNodesNumber;
		int times;
		int perturbExperiments;

		if(simulationFeatures == null)
			throw new MissingFeaturesException("Features must be not null");
		if(samplingManager == null)
			throw new MissingFeaturesException("The sampling manager must be not null");
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB)) 
			throw new MissingFeaturesException("Features must contain the mutation rate value");
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MUTATION_TYPE)) 
			throw new MissingFeaturesException("Features must contain the mutation type value");
		if(nodes < 0)
			throw new ParamDefinitionException("The nodes number must be greater then 0");

		//Checks the mutation rate parameter existence.
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB))
			throw new FeaturesException(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB + " key must be specified");
		mutationRate = Double.parseDouble(simulationFeatures.get(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB).toString());

		//Checks if the mutations rate is between 0 and 1
		if(mutationRate < 0)
			mutationRate = 0;
		else if(mutationRate > 1)
			mutationRate = 1;

		//Create a new ATM object
		this.atm = new Atm(samplingManager.getAttractorFinder(), mutationManager.getMutation());

		//Checks the mutation type parameter existence.
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MUTATION_TYPE))
			throw new FeaturesException(SimulationFeaturesConstants.MUTATION_TYPE + " key must be specified");

		//Checks the mutated nodes parameter existence.
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB))
			throw new FeaturesException(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB + " key must be specified");
		mutatedNodesNumber = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB).toString());
		
		//Checks the number of mutated nodes
		if(mutatedNodesNumber > nodes)
			throw new FeaturesException("Mutated nodes must be less than the network nodes");

		//Gets the perturb experiments
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.HOW_MANY_PERTURB_EXP)) 
			throw new MissingFeaturesException("Features must contain the " + SimulationFeaturesConstants.HOW_MANY_PERTURB_EXP + " key");
		perturbExperiments = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.HOW_MANY_PERTURB_EXP).toString());
		
		//Checks the mutation times existence and defines its value.
		if(simulationFeatures.containsKey(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB) && 
				simulationFeatures.containsKey(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB))
			times = UtilityRandom.randomUniform(
					Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB).toString()),
					Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB).toString()));
		else
			throw new FeaturesException("The mutations times must be specified in a temporary mutation.");

		//Flip mutations
		if(simulationFeatures.get(SimulationFeaturesConstants.MUTATION_TYPE).equals(SimulationFeaturesConstants.FLIP_MUTATIONS)){
			//Calculate the atm with flips
			this.atm.createAtmWithFlip(samplingManager.getAttractorFinder().getAttractors(),
					mutatedNodesNumber, perturbExperiments, mutationRate, times);
			//Random temporary mutations mutations
		}else if(simulationFeatures.get(SimulationFeaturesConstants.MUTATION_TYPE).equals(SimulationFeaturesConstants.TEMPORARY_MUTATIONS)){
			
			//Calculates the atm with mutations in nth times
			this.atm.createAtmWithMutation(samplingManager.getAttractorFinder().getAttractors(), 
					mutationRate, nodes, mutatedNodesNumber, times, perturbExperiments);  
		}else 
			throw new MissingFeaturesException("Unknown mutation type");
	}

	/**
	 * This method returns the atm matrix
	 * @return
	 */
	public Atm getAtm(){
		return this.atm; 
	}


}
