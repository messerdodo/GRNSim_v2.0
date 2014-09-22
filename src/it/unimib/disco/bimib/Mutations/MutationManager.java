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

//System imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

//GRNSim imports
import it.unimib.disco.bimib.Atms.*;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Utility.*;

public class MutationManager {

	private Mutation mutation;
	private int minTimes;
	private int maxTimes;
	private double mutationRate;

	@SuppressWarnings("unused")
	private String mutationType;
	private double perpetualType;
	private int mutatedNodes;
	private double knockInRate;
	private GraphManager graphManager;

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
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB))
			throw new FeaturesException("Minimum mutation times feature missed");
		
		if(!simulationFeatures.containsKey(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB))
			throw new FeaturesException("Maximim mutation times feature missed");
		
		//Mettere qua il controllo mutation type

		//Set the class parameters
		this.minTimes = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB).toString());
		this.maxTimes = Integer.parseInt(simulationFeatures.get(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB).toString());

		this.mutation = new BinaryMutation(graphManager, samplingManager.getAttractorFinder());
	}


	@SuppressWarnings("unchecked")
	public MutationManager(GraphManager graphManager, SamplingManager samplingManager, 
			AtmManager atmManager, HashMap<String, Object> newSimulationFeatures) 
					throws FeaturesException, ParamDefinitionException, 
					NotExistingNodeException, InputTypeException, 
					NotExistingAttractorsException, AttractorNotFoundException {

		if(atmManager == null)
			throw new NullPointerException("Atm Manager must be not null");
		if(samplingManager == null)
			throw new NullPointerException("Sampling manager must be not null");
		if(graphManager == null)
			throw new NullPointerException("Graph manager must be not null");
		
		this.graphManager = graphManager;
		this.perpetualType = 0;
		
		//Checks the features keys
		if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB))
			throw new FeaturesException(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB +" key must be specified!");
		else
			this.mutationRate = Double.parseDouble((String)newSimulationFeatures.get(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB));

		if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.MUTATION_TYPE)){
			throw new FeaturesException(SimulationFeaturesConstants.MUTATION_TYPE + " key must be specified!");
		}else{
			//Random mutation
			if(newSimulationFeatures.get(SimulationFeaturesConstants.MUTATION_TYPE).equals(SimulationFeaturesConstants.RANDOM_MUTATION)){
				this.mutationType = SimulationFeaturesConstants.RANDOM_MUTATION;

				if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.PERPETUAL_TYPE)){
					throw new FeaturesException(SimulationFeaturesConstants.PERPETUAL_TYPE + " key and value must be specified");
				}else{

					//Gets the number of nodes to be mutated.
					if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB)){
						throw new FeaturesException(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB + " key must be specified");
					}else{
						this.mutatedNodes = Integer.parseInt((String)newSimulationFeatures.get(SimulationFeaturesConstants.HOW_MANY_NODES_TO_PERTURB));
						if(this.mutatedNodes > graphManager.getNodesNumber() || this.mutatedNodes < 0)
							throw new FeaturesException("The number of mutated nodes must be between 0 " +
									"and the number of nodes in the network");
					}

					this.perpetualType = Double.parseDouble((String)newSimulationFeatures.get(SimulationFeaturesConstants.PERPETUAL_TYPE));

					if(this.perpetualType > 1 || this.perpetualType < 0)
						throw new FeaturesException("The perpetual type rate must be in [0, 1]");

					//There are not perpetual mutations
					if(this.perpetualType != 1){
						if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB))
							throw new FeaturesException("Minimum mutation times feature missed");
						if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB))
							throw new FeaturesException("Maximim mutation times feature missed");

						//Set the class parameters
						this.minTimes = Integer.parseInt((String)newSimulationFeatures.get(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB));
						this.maxTimes = Integer.parseInt((String)newSimulationFeatures.get(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB));
					}

					//There is at least one perpetual mutation 
					if(this.perpetualType != 0){
						//Gets the knock-in/knock-out rate
						if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.KNOCKIN_RATE)){
							throw new FeaturesException(SimulationFeaturesConstants.KNOCKIN_RATE + " key must be specified");
						}
						this.knockInRate = Double.parseDouble((String)newSimulationFeatures.get(SimulationFeaturesConstants.KNOCKIN_RATE));
						//KnockIn rate value checking
						if(this.knockInRate < 0 || this.knockInRate > 1)
							throw new FeaturesException("The knockIn rate value must be in [0, 1]");
					}
				}
				
				//Chooses the mutated nodes
				ArrayList<Integer> mutatedNodesSet = UtilityRandom.randomSubset(graphManager.getNodesNumber(), this.mutatedNodes);
				int times = UtilityRandom.randomUniform(this.minTimes, this.maxTimes + 1);
				this.mutation = new BinaryMutation(this.graphManager, samplingManager.getAttractorFinder().copy());
				this.mutation.mutationGenerator(this.graphManager, this.mutationRate, mutatedNodesSet, this.perpetualType, times, knockInRate);
				
			}else if(newSimulationFeatures.get(SimulationFeaturesConstants.MUTATION_TYPE).equals(SimulationFeaturesConstants.CUSTOM_MUTATION)){
				//Custom mutation
				this.mutationType = SimulationFeaturesConstants.CUSTOM_MUTATION;
				int nodeNumber;

				HashMap<Integer, Boolean> perpetualMutatedNodes = new HashMap<Integer, Boolean>();
				HashMap<Integer, Boolean> notPerpetualMutatedNodes = new HashMap<Integer, Boolean>();
				HashMap<Integer, Integer> notPerpetualMutationsTimes = new HashMap<Integer, Integer>();
				int mutationTimes;

				if(!newSimulationFeatures.containsKey(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB))
					throw new FeaturesException(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB +" key must be specified!");
				else
					this.mutationRate = Double.parseDouble((String)newSimulationFeatures.get(SimulationFeaturesConstants.RATIO_OF_STATES_TO_PERTURB));


				HashMap<String, String> specificMutations;

				for(String nodeName : newSimulationFeatures.keySet()){
					//Nodes features
					if(newSimulationFeatures.get(nodeName) instanceof HashMap<?, ?>){
						specificMutations = (HashMap<String, String>) newSimulationFeatures.get(nodeName);
						nodeNumber = this.graphManager.getGraph().getNodeNumber(nodeName);
						if(!specificMutations.containsKey(SimulationFeaturesConstants.MUTATION_STRENGTH)){
							throw new FeaturesException(SimulationFeaturesConstants.MUTATION_STRENGTH + " key must be specified!");
						}

						//KnockIn/KnockOut feature must be specified
						if(!specificMutations.containsKey(SimulationFeaturesConstants.MUTATION_EFFECT)){
							throw new FeaturesException("In node " + nodeName + " the " + 
									SimulationFeaturesConstants.MUTATION_EFFECT + " key must be specified");
						}
						if((!specificMutations.get(SimulationFeaturesConstants.MUTATION_EFFECT).equals(SimulationFeaturesConstants.KNOCK_IN)) && 
								(!specificMutations.get(SimulationFeaturesConstants.MUTATION_EFFECT).equals(SimulationFeaturesConstants.KNOCK_OUT))){
							throw new FeaturesException("In node " + nodeName + " the " + SimulationFeaturesConstants.MUTATION_EFFECT + 
									" value must be " + SimulationFeaturesConstants.KNOCK_IN + " or " + SimulationFeaturesConstants.KNOCK_OUT);
						}

						if(specificMutations.get(SimulationFeaturesConstants.MUTATION_STRENGTH).equals(SimulationFeaturesConstants.PERPETUAL_MUTATIONS)){

							perpetualMutatedNodes.put(nodeNumber,
									specificMutations.get(SimulationFeaturesConstants.MUTATION_EFFECT).equals(SimulationFeaturesConstants.KNOCK_IN) ? Boolean.TRUE : Boolean.FALSE);


						}else if(specificMutations.get(SimulationFeaturesConstants.MUTATION_STRENGTH).equals(SimulationFeaturesConstants.TEMPORARY_MUTATIONS)){
							//KnockIn/KnockOut feature must be specified
							if(!specificMutations.containsKey(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB) || !specificMutations.containsKey(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB)){
								throw new FeaturesException("In node " + nodeName + " the " + SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB + 
										" key or the " + SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB + " key must be specified");
							}

							mutationTimes = UtilityRandom.randomUniform(
									Integer.parseInt(specificMutations.get(SimulationFeaturesConstants.MIN_DURATION_OF_PERTURB)), 
									Integer.parseInt(specificMutations.get(SimulationFeaturesConstants.MAX_DURATION_OF_PERTURB)));
							if(mutationTimes <= 0)
								throw new FeaturesException("The duration of the perturbation must be not less than 0");

							notPerpetualMutatedNodes.put(nodeNumber,
									specificMutations.get(SimulationFeaturesConstants.MUTATION_EFFECT).equals(SimulationFeaturesConstants.KNOCK_IN) ? 
											Boolean.TRUE : Boolean.FALSE);
							notPerpetualMutationsTimes.put(nodeNumber, mutationTimes);

						}else{
							throw new FeaturesException("In node " + nodeName + " the " + 
									SimulationFeaturesConstants.MUTATION_STRENGTH + " specified value is not supported yet");
						}
					}
				}

				//Recalls the mutation manager
				this.mutation = new BinaryMutation(this.graphManager, samplingManager.getAttractorFinder().copy());

				this.mutation.mutationGenerator(this.graphManager, this.mutationRate, 
						perpetualMutatedNodes, notPerpetualMutatedNodes, 
						notPerpetualMutationsTimes); 
			}else{
				throw new FeaturesException("The mutation type is not supported yet.");
			}
		}
	}

	/**
	 * This method returns the atm mutated
	 * @return the atm matrix
	 */
	public Atm getMutatedAtm(){

		return this.mutation.getMutatedAtm();
	}

	public Mutation getMutation(){
		return this.mutation;
	}






}
