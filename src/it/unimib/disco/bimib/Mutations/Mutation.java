/**
 * This class defines the Mutation interface
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

//GRNSim imports
import it.unimib.disco.bimib.Atms.Atm;
import it.unimib.disco.bimib.Exceptions.*;
import it.unimib.disco.bimib.Networks.GraphManager;
import it.unimib.disco.bimib.Sampling.AttractorsFinder;


public interface Mutation {
	
	/**
	 * This method returns the mutated atm
	 * @return atm
	 */
	public Atm getMutatedAtm();

	/**
	 * This method allows to mutated a generic group of node in a specific times during the simulation
	 * @param mutatedNodesNumber: numbers of node to be mutated
	 * @param times: mutation's duration
	 * @param mutationRate: rate of the mutation in attractors
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws NotExistingAttractorsException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void mutationGenerator(int mutatedNodesNumber, int times, double mutationRate) throws Exception;
	
	/**
	 * This method allows to mutate temporally a set of nodes after the simulation
	 * @param graphManager
	 * @param mutationRate: rate of the mutation in attractors
	 * @param mutatedNodesSet: set of nodes to be mutated
	 * @param perpetualType: rate of permanent mutation
	 * @param times: mutation's duration
	 * @param knockInRate: rate of the node's activation
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws NotExistingAttractorsException
	 * @throws AttractorNotFoundException 
	 */
	public void mutationGenerator(GraphManager graphManager, double mutationRate, 
			ArrayList<Integer> mutatedNodesSet, double perpetualType, int times, 
			double knockInRate) throws ParamDefinitionException, 
			NotExistingNodeException, InputTypeException, NotExistingAttractorsException, AttractorNotFoundException;
	
	/**
	 * This method allows to mutate permanently a set of nodes after the simulation
	 * @param graphManager
	 * @param mutationRate: rate of the mutation in attractors
	 * @param perpertualMutatedNodes:
	 * @param notPerpertualMutatedNodes:
	 * @param notPerpertualMutatedTimes:
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 * @throws NotExistingAttractorsException
	 * @throws AttractorNotFoundException 
	 */
	public void mutationGenerator(GraphManager graphManager, double mutationRate, HashMap<Integer, Boolean> perpetualMutatedNodes, 
			HashMap<Integer, Boolean> notPerpetualMutatedNodes, HashMap<Integer, Integer> notPerpetualMutationsTimes) 
					throws ParamDefinitionException, NotExistingNodeException, InputTypeException, 
					NotExistingAttractorsException, AttractorNotFoundException;

	/**
	 * This method allows to mutate a specific state
	 * @param times: mutation's duration		
	 * @param mutations: specified mutations that will be done
	 * @param state: specified state where will be the mutation
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public Object doMutation(ArrayList<Integer> mutatedNodes, int times, Object state) 
			throws ParamDefinitionException, ArrayIndexOutOfBoundsException, 
			NotExistingNodeException, InputTypeException;

	/**
	 * This method returns the state mutated.
	 * The mutations are only flips and the nodes are randomly selected.
	 * @return the mutated state
	 * @throws ParamDefinitionException
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	public Object doFlips(int nodesForFlip, Object state, int times) throws ParamDefinitionException, NotExistingNodeException, InputTypeException;
	
	/**
	 * This method allows to make a random perpetually mutation
	 * @param nodes: specified the number of nodes that will be mutated
	 * @param knockInRate: rate of the nodes activation 
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 */
	public void randomPerpetuallyMutation(int nodes, double knockInRate) throws ParamDefinitionException, NotExistingNodeException;
	
	/**
	 * This method allows to make a perpetually mutation in a specific node
	 * @param nodes: specified the node that will be mutated
	 * @param knockIn: specified if is a knockOut or a knockIn
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 */
	public void perpetuallyMutation(int node, boolean knockIn) throws ParamDefinitionException, NotExistingNodeException;
	
	/**
	 * This method returns all attractors mutated
	 * @return sampling
	 */
	public AttractorsFinder getMutatedAttractorFinder();
	
	/**
	 * This method returns the avalanche of a simulation
	 * @return avalanche
	 */
	public ArrayList<Integer> getAvalanches();
	
	/**
	 * This method returns the sensitivity of the network considered
	 * @return sensitivity
	 */
	public int[] getSensitivity();

}
