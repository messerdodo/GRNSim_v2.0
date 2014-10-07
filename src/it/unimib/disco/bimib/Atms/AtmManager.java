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
	
	/**
	 * AtmManager constructor with atm specified.
	 * @param atm: a double matrix that represents the atm 
	 * @param samplingManager: The sampling Manager
	 * @param mutationManager: The mutation Manager
	 * @param nodes: Number of nodes of the network.
	 * @throws MissingFeaturesException
	 * @throws ParamDefinitionException
	 */
	public AtmManager(double[][] atm, SamplingManager samplingManager, MutationManager mutationManager, int nodes) throws MissingFeaturesException, ParamDefinitionException{
		if(samplingManager == null)
			throw new MissingFeaturesException("The sampling manager must be not null");
		if(nodes < 0)
			throw new ParamDefinitionException("The nodes number must be greater then 0");
		//Creates the ATM
		this.atm = new Atm(samplingManager.getAttractorFinder(), mutationManager.getMutation(), atm);
	}
	
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
		int perturbExperiments;

		if(simulationFeatures == null)
			throw new MissingFeaturesException("Features must be not null");
		if(samplingManager == null)
			throw new MissingFeaturesException("The sampling manager must be not null");
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB)) 
			throw new MissingFeaturesException("Features must contain the mutation rate value");
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

		//Gets the perturb experiments
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.HOW_MANY_PERTURB_EXP)) 
			throw new MissingFeaturesException("Features must contain the " + SimulationFeaturesConstants.HOW_MANY_PERTURB_EXP + " key");
		perturbExperiments = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.HOW_MANY_PERTURB_EXP).toString());
		
		this.atm.createAtm(samplingManager.getAttractorFinder().getAttractors(), perturbExperiments, mutationRate);
		
	}

	/**
	 * This method returns the atm matrix
	 * @return
	 */
	public Atm getAtm(){
		return this.atm; 
	}


}
