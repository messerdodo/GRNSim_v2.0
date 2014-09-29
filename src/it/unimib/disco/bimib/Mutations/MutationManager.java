/**
 * This class is the manager for the mutations package.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Mutations;

import it.unimib.disco.bimib.Exceptions.FeaturesException;
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Utility.SimulationFeaturesConstants;

import java.util.Properties;

public class MutationManager {
	
	private Mutation mutation;
	private String mutationType;

	/**
	 * Main constructor
	 * @param graphManager
	 * @param samplingManager
	 * @param simulationFeatures
	 * @throws FeaturesException
	 */
	public MutationManager(GraphManager graphManager, SamplingManager samplingManager, Properties simulationFeatures) throws FeaturesException {
		//Parameters checking
		if(samplingManager == null)
			throw new NullPointerException("Sampling manager must be not null");
		if(graphManager == null)
			throw new NullPointerException("Graph manager must be not null");
		
		//Checks the features keys
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MUTATION_TYPE))
			throw new FeaturesException(SimulationFeaturesConstants.MUTATION_TYPE + " key must be specified.");
		
		this.mutationType = simulationFeatures.getProperty(SimulationFeaturesConstants.MUTATION_TYPE);
		
		if(this.mutationType.equals(SimulationFeaturesConstants.FLIP_MUTATIONS) ||
			this.mutationType.equals(SimulationFeaturesConstants.TEMPORARY_MUTATIONS)){
			
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB))
				throw new FeaturesException(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB + " key must be specified.");
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB))
				throw new FeaturesException(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB + " key must be specified.");
			
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB))
				throw new FeaturesException(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB + " key must be specified.");
			//Gets the parameters
			int minTimes = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB).toString());
			int maxTimes = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB).toString());
			int nodesToPerturb = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB).toString());
			this.mutation = new BinaryMutation(graphManager, samplingManager.getAttractorFinder(), this.mutationType, nodesToPerturb, minTimes, maxTimes);
			
		}else if(this.mutationType.equals(SimulationFeaturesConstants.KNOCKIN_KNOCKOUT_MUTATIONS)){
			//Param checking
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.KNOCKIN_NODES))
				throw new FeaturesException(SimulationFeaturesConstants.KNOCKIN_NODES + " key must be specified.");
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.KNOCKOUT_NODES))
				throw new FeaturesException(SimulationFeaturesConstants.KNOCKOUT_NODES + " key must be specified.");
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MIN_KNOCKIN_DURATION))
				throw new FeaturesException(SimulationFeaturesConstants.MIN_KNOCKIN_DURATION + " key must be specified.");
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MAX_KNOCKIN_DURATION))
				throw new FeaturesException(SimulationFeaturesConstants.MAX_KNOCKIN_DURATION + " key must be specified.");
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MIN_KNOCKOUT_DURATION))
				throw new FeaturesException(SimulationFeaturesConstants.MIN_KNOCKOUT_DURATION + " key must be specified.");
			if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MAX_KNOCKOUT_DURATION))
				throw new FeaturesException(SimulationFeaturesConstants.MAX_KNOCKOUT_DURATION + " key must be specified.");
			//Gets the parameters
			int knockInNodes = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.KNOCKIN_NODES).toString());
			int knockOutNodes = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.KNOCKOUT_NODES).toString());
			int minKnockInDuration = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MIN_KNOCKIN_DURATION).toString());
			int maxKnockInDuration = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MAX_KNOCKIN_DURATION).toString());
			int minKnockOutDuration = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MIN_KNOCKOUT_DURATION).toString());
			int maxKnockOutDuration = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MAX_KNOCKOUT_DURATION).toString());
			
			this.mutation = new BinaryMutation(graphManager, samplingManager.getAttractorFinder(), this.mutationType, 
					knockInNodes, knockOutNodes, minKnockInDuration, maxKnockInDuration, minKnockOutDuration, maxKnockOutDuration);
			
		}
	}
	
	/**
	 * Returns the mutation object
	 * @return the mutation object
	 */
	public Mutation getMutation(){
		return this.mutation;
	}

	
}
