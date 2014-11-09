/**
 * This class manages the mutations
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

//GRNSim imports
import it.unimib.disco.bimib.Atms.Atm;
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.AttractorsFinder;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Utility.SimulationFeaturesConstants;
import it.unimib.disco.bimib.Utility.UtilityRandom;

public class BinaryMutation implements Mutation {

	private GraphManager graphManager;
	//private AttractorsFinder sampling;

	//Used only for random mutation or flips
	private int numebrOfnodesToPerturb;
	private int minPerturbDuration;
	private int maxPerturbDuration;
	//Used only for static mutations
	private int knockInNodesNumber;
	private int knockOutNodesNumber;
	private int minKnockInDuration;
	private int maxKnockInDuration;
	private int minKnockOutDuration;
	private int maxKnockOutDuration;

	//Used for specific mutation definitions
	private ArrayList<Integer> knockInNodes;
	private ArrayList<Integer> knockOutNodes;
	private ArrayList<Integer> perturbNodes;

	private String mutationType;


	/**
	 * This is the constructor for the mutation during the simulation
	 * @param graphManager The graph manager
	 * @param attractorFinder: The attractor finder object
	 * @param mutationType: The selected mutation type
	 * @param nodesToPerturb: The number of nodes to perturb in each experiment
	 * @param minPerturbDuration: The minimum duration of a perturb
	 * @param maxPerturbDuration: The maximum duration of a perturb
	 * @param perturbNodes: The list of the specific nodes to flip.
	 * @throws FeaturesException 
	 */
	public BinaryMutation(GraphManager graphManager, AttractorsFinder attractorFinder, 
			String mutationType, int nodesToPerturb, int minPerturbDuration, int maxPerturbDuration, 
			ArrayList<Integer> perturbNodes) throws FeaturesException {

		if(attractorFinder == null)
			throw new NullPointerException("Sampling method object must be not null");
		if(graphManager == null)
			throw new NullPointerException("Graph manager must be not null");
		if(nodesToPerturb < 0 || nodesToPerturb > graphManager.getNodesNumber())
			throw new FeaturesException("The number of nodes to be mutated must be between 0 and n (number of nodes of the network)");

		//this.sampling = attractorFinder;
		this.graphManager = graphManager;
		this.mutationType = mutationType;

		//Param value checking
		int swap;
		if (minPerturbDuration > maxPerturbDuration){
			swap = minPerturbDuration;
			minPerturbDuration = maxPerturbDuration;
			maxPerturbDuration = swap;
		}

		this.minPerturbDuration = minPerturbDuration;
		this.maxPerturbDuration = maxPerturbDuration;

		this.perturbNodes = perturbNodes;
		//Sets the number of nodes to perturb
		if(this.perturbNodes != null)
			this.numebrOfnodesToPerturb = Math.max(this.perturbNodes.size(), nodesToPerturb);
		else
			this.numebrOfnodesToPerturb = nodesToPerturb;

	}

	/**
	 * This is the constructor for the mutation during the simulation
	 * @param graphManager The graph manager
	 * @param attractorFinder: The attractor finder object
	 * @param mutationType: The selected mutation type
	 * @param nodesToPerturb: The number of nodes to perturb in each experiment
	 * @param minPerturbDuration: The minimum duration of a perturb
	 * @param maxPerturbDuration: The maximum duration of a perturb
	 * @throws FeaturesException 
	 */
	public BinaryMutation(GraphManager graphManager, AttractorsFinder attractorFinder, 
			String mutationType, int nodesToPerturb, int minPerturbDuration, int maxPerturbDuration) 
					throws FeaturesException {
		this(graphManager, attractorFinder, mutationType, nodesToPerturb, minPerturbDuration, maxPerturbDuration, null);
	}

	/**
	 * This is the constructor for the mutation during the simulation
	 * @param graphManager The graph manager
	 * @param attractorFinder: The attractor finder object
	 * @param mutationType: The selected mutation type
	 * @param knockInNodesNumber: The number of nodes to knock-in in each experiment
	 * @param knockOutNodesNumber: The number of nodes to knock-out in each experiment
	 * @param minKnockInDuration: The minimum duration of a knock-in.
	 * @param maxKnockInDuration: The maximum duration of a knock-in.
	 * @param minKnockOutDuration: The minimum duration of a knock-out.
	 * @param maxKnockOutDuration: The maximum duration of a knock-out.
	 * @param knockInNodes: The list of the specific nodes to knock-in.
	 * @param knockOutNodes: The list of the specific nodes to knock-out.
	 * @throws FeaturesException 
	 */
	public BinaryMutation(GraphManager graphManager, AttractorsFinder attractorFinder, 
			String mutationType, int knockInNodesNumber, int knockOutNodesNumber, int minKnockInDuration, 
			int maxKnockInDuration, int minKnockOutDuration, int maxKnockOutDuration, ArrayList<Integer> knockInNodes, ArrayList<Integer> knockOutNodes) throws FeaturesException {

		if(attractorFinder == null)
			throw new NullPointerException("Sampling method object must be not null");
		if(graphManager == null)
			throw new NullPointerException("Graph manager must be not null");
		if(knockInNodesNumber + knockOutNodesNumber > graphManager.getNodesNumber())
			throw new FeaturesException("The number of nodes to perturb must be less or equal than the number of the nodes in the network");

		//Param value checking
		knockInNodesNumber = knockInNodesNumber < 0 ? 0 : knockInNodesNumber;
		knockOutNodesNumber = knockOutNodesNumber < 0 ? 0 : knockOutNodesNumber;
		int swap;
		if (minKnockInDuration > maxKnockInDuration){
			swap = minKnockInDuration;
			minKnockInDuration = maxKnockInDuration;
			maxKnockInDuration = swap;
		}
		if(minKnockOutDuration > maxKnockOutDuration){
			swap = minKnockOutDuration;
			minKnockOutDuration = maxKnockOutDuration;
			maxKnockOutDuration = swap;
		}

		//this.sampling = attractorFinder;
		this.graphManager = graphManager;
		this.mutationType = mutationType;


		this.knockOutNodesNumber = knockOutNodesNumber;
		this.minKnockInDuration = minKnockInDuration;
		this.maxKnockInDuration = maxKnockInDuration;
		this.minKnockOutDuration = minKnockOutDuration;
		this.maxKnockOutDuration = maxKnockOutDuration;

		//Sets the specific knock-in nodes
		this.knockInNodes = knockInNodes;
		//Sets the number of nodes to knock-in
		if(this.knockInNodes != null)
			this.knockInNodesNumber = Math.max(knockInNodes.size(), knockInNodesNumber);
		else
			this.knockInNodesNumber = knockInNodesNumber;
		//Sets the specific knock-out nodes
		this.knockOutNodes = knockOutNodes;
		//Sets the number of nodes to knock-out
		if(this.knockOutNodes != null)
			this.knockOutNodesNumber = Math.max(knockOutNodes.size(), knockOutNodesNumber);
		else
			this.knockOutNodesNumber = knockOutNodesNumber;

	}

	/**
	 * This is the constructor for the mutation during the simulation
	 * @param graphManager The graph manager
	 * @param attractorFinder: The attractor finder object
	 * @param mutationType: The selected mutation type
	 * @param knockInNodesNumber: The number of nodes to knock-in in each experiment
	 * @param knockOutNodesNumber: The number of nodes to knock-out in each experiment
	 * @param minKnockInDuration: The minimum duration of a knock-in.
	 * @param maxKnockInDuration: The maximum duration of a knock-in.
	 * @param minKnockOutDuration: The minimum duration of a knock-out.
	 * @param maxKnockOutDuration: The maximum duration of a knock-out.
	 * @throws FeaturesException 
	 */
	public BinaryMutation(GraphManager graphManager, AttractorsFinder attractorFinder, 
			String mutationType, int knockInNodesNumber, int knockOutNodesNumber, int minKnockInDuration, 
			int maxKnockInDuration, int minKnockOutDuration, int maxKnockOutDuration) throws FeaturesException {
		this(graphManager, attractorFinder, mutationType, knockInNodesNumber, knockOutNodesNumber, minKnockInDuration,
				maxKnockInDuration, minKnockOutDuration, maxKnockOutDuration, null, null);
	}


	/**
	 * This method allows to mutate a specific state
	 * @param times: mutation's duration		
	 * @param mutations: specified mutations that will be done
	 * @param state: specified state where will be the mutation
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public Object doMutation(Object state) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		int times, knockInDuration, knockOutDuration;
		ArrayList<Integer> perturbSubset;
		Boolean[] mutatedValues;
		if(this.mutationType.equals(SimulationFeaturesConstants.FLIP_MUTATIONS)){
			//Generates the duration of the perturb
			times = UtilityRandom.randomUniform(this.minPerturbDuration, this.maxPerturbDuration);
			//Generates the subset of nodes to perturb
			if(this.perturbNodes != null){
				perturbSubset = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), this.numebrOfnodesToPerturb - this.perturbNodes.size(), this.perturbNodes);
				//Adds the specific nodes to flip in the list
				perturbSubset.addAll(perturbNodes);
			}else{
				perturbSubset = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), this.numebrOfnodesToPerturb);
			}
			return doFlip(state, perturbSubset, times);

		}else if(this.mutationType.equals(SimulationFeaturesConstants.TEMPORARY_MUTATIONS)){
			//Generates the duration of the perturb
			times = UtilityRandom.randomUniform(this.minPerturbDuration, this.maxPerturbDuration);
			//Generates the subset of nodes to perturb
			if(this.perturbNodes != null){
				perturbSubset = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), this.numebrOfnodesToPerturb - this.perturbNodes.size(), this.perturbNodes);
				//Adds the specific nodes to flip in the list
				perturbSubset.addAll(perturbNodes);
			}else{
				perturbSubset = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), this.numebrOfnodesToPerturb);
			}
			//Generates the new values for the mutate nodes
			mutatedValues = new Boolean[this.numebrOfnodesToPerturb];
			for(int i = 0; i < this.numebrOfnodesToPerturb; i++)
				mutatedValues[i] = UtilityRandom.randomBooleanChoice(0.5);
			return doRandomMutations(state, perturbSubset, mutatedValues, times);

		}else if(this.mutationType.equals(SimulationFeaturesConstants.KNOCKIN_KNOCKOUT_MUTATIONS)){
			//Generates the duration of the perturbations
			knockInDuration = UtilityRandom.randomUniform(this.minKnockInDuration, this.maxKnockInDuration);
			knockOutDuration = UtilityRandom.randomUniform(this.minKnockOutDuration, this.maxKnockOutDuration);

			ArrayList<Integer> randomNodes, knockInSubset, knockOutSubset;
			//Generates two nodes subsets, one for the knockIn and one for the knockOut
			int subsetDim = 0;
			if(this.knockInNodes!= null || this.knockOutNodes != null){
				ArrayList<Integer> exclude = new ArrayList<Integer>();
				int specKnockIn = 0, specKnockOut = 0;
				if(this.knockInNodes != null){
					exclude.addAll(this.knockInNodes);
					specKnockIn = this.knockInNodes.size();
				}
				if(this.knockOutNodes != null){
					exclude.addAll(this.knockOutNodes);
					specKnockOut = this.knockOutNodes.size();
				}
				
				subsetDim = this.knockInNodesNumber + this.knockOutNodesNumber - specKnockIn - specKnockOut;
				randomNodes = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), subsetDim, exclude);
				knockInSubset = UtilityRandom.randomSubset(randomNodes, this.knockInNodesNumber - specKnockIn);
				if(this.knockInNodes != null)
					knockInSubset.addAll(this.knockInNodes);
				//The knock out subset is the complementary set of the knock in subset.
				knockOutSubset = new ArrayList<Integer>();
				for(Integer node : randomNodes)
					if(!knockInSubset.contains(node))
						knockOutSubset.add(node);
				if(this.knockOutNodes != null)
					knockOutSubset.addAll(knockOutNodes);

			}else{
				subsetDim = this.knockInNodesNumber + this.knockOutNodesNumber;
				randomNodes = UtilityRandom.randomSubset(this.graphManager.getNodesNumber(), subsetDim);
				knockInSubset = UtilityRandom.randomSubset(randomNodes, this.knockInNodesNumber);
				//The knock out subset is the complementary set of the knock in subset.
				knockOutSubset = new ArrayList<Integer>();
				for(Integer node : randomNodes)
					if(!knockInSubset.contains(node))
						knockOutSubset.add(node);
			}

			return doStaticMutations(state, knockInSubset, knockOutSubset, knockInDuration, knockOutDuration);
		}else{
			throw new ParamDefinitionException("The " + SimulationFeaturesConstants.MUTATION_TYPE +" value is not correct.");
		}

	}

	/**
	 * This method performs flips in the state for the given number of time steps.
	 * @param initialState: The initial state of the network
	 * @param perturbSubset: The subset of nodes to perturb
	 * @param times: the duration of the flips (number of time steps)
	 * @return the new state after the required flips events
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	private Object doFlip(Object initialState, ArrayList<Integer> perturbSubset, int times) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{

		String stringState = (String)initialState;
		Boolean[] state =  new Boolean[stringState.length()];
		Boolean[] newState;

		//Converts the string state in a boolean array
		for(int j = 0; j < state.length; j++){
			state[j] = stringState.charAt(j) == '1' ? Boolean.TRUE : Boolean.FALSE;
		}

		//Performs the flips for times time steps
		for(int i = 0; i < times; i++){
			//Performs the flips
			for(Integer node : perturbSubset){
				state[node] = state[node] ? Boolean.FALSE : Boolean.TRUE;
			}
			//Obtains the new state from the network
			newState = this.graphManager.getNewState(state);
			state = newState;
		}
		//Converts the Boolean array in a binary string
		stringState = "";
		for(int j = 0; j < state.length; j++)
			stringState = stringState + (state[j] ? "1" : "0");
		return stringState;
	}

	/**
	 * This method performs random mutations in the state for the given number of time steps.
	 * @param initialState: The initial state of the network
	 * @param nodesToPerturb: The subset of nodes to perturb
	 * @param newValues: The set of new values for the mutated nodes
	 * @param times: the duration of the flips (number of time steps)
	 * @return the new state after the required flips events
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	private Object doRandomMutations(Object initialState, ArrayList<Integer> nodesToPerturb, Boolean[] newValues, int times) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{

		String stringState = (String)initialState;
		Boolean[] state =  new Boolean[stringState.length()];
		Boolean[] newState;

		//Converts the string state in a boolean array
		for(int j = 0; j < state.length; j++){
			state[j] = stringState.charAt(j) == '1' ? Boolean.TRUE : Boolean.FALSE;
		}

		//Performs the flips for times time steps
		for(int i = 0; i < times; i++){
			//Performs the flips
			for(int j = 0; j < nodesToPerturb.size(); j++){
				state[nodesToPerturb.get(j)] = newValues[j];
			}
			//Obtains the new state from the network
			newState = this.graphManager.getNewState(state);
			state = newState;
		}
		//Converts the Boolean array in a binary string
		stringState = "";
		for(int j = 0; j < state.length; j++)
			stringState = stringState + (state[j] ? "1" : "0");
		return stringState;
	}

	/**
	 * This method performs knock-in and knock-out mutations as given.
	 * @param initialState: The initial state
	 * @param knockInNodes: Subset of nodes to be set active
	 * @param knockOutNodes: Subset of nodes to be set deactive
	 * @param knockInTimes: Number of time steps for the knock-in mutations
	 * @param knockOutTimes: Number of time steps for the knock-out mutations
	 * @return The new state after the mutations
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	private Object doStaticMutations(Object initialState, ArrayList<Integer> knockInNodes, ArrayList<Integer> knockOutNodes, int knockInTimes, int knockOutTimes) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{

		String stringState = (String)initialState;
		Boolean[] state =  new Boolean[stringState.length()];
		Boolean[] newState;

		//Converts the string state in a boolean array
		for(int j = 0; j < state.length; j++){
			state[j] = stringState.charAt(j) == '1' ? Boolean.TRUE : Boolean.FALSE;
		}

		//Performs the flips for times time steps
		for(int t = 0; t < Math.max(knockInTimes, knockOutTimes); t++){
			//Performs the knock-in (if possible)
			if(t < knockInTimes){
				for(Integer node : knockInNodes){
					state[node] = Boolean.TRUE;
				}
			}
			//Performs the knock-out (if possible)
			if(t < knockOutTimes){
				for(Integer node : knockOutNodes){
					state[node] = Boolean.FALSE;
				}
			}
			//Obtains the new state from the network
			newState = this.graphManager.getNewState(state);
			state = newState;
		}

		//Converts the Boolean array in a binary string
		stringState = "";
		for(int j = 0; j < state.length; j++)
			stringState = stringState + (state[j] ? "1" : "0");
		return stringState;
	}

	@Override
	public Atm getMutatedAtm() {
		// TODO Auto-generated method stub
		return null;
	}
}
