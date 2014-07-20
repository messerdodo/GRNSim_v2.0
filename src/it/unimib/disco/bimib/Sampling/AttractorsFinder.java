/**
 * AttractorsFinder interface. 
 * This interface specifies all the methods which must be implemented in a sampling class
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Sampling;

import java.util.ArrayList;
import java.util.HashMap;

import it.unimib.disco.bimib.Exceptions.*;

public interface AttractorsFinder {

	/**
	 * This method returns the attractor of a given network status.
	 * The status must be a binary string; it's value must be between 0 and the 2^n - 1 
	 * where n is the number of the nodes in the network.
	 * @param status: The string value representing the network status.
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 * @ParamDefinitionException: The passed status isn't correct, it doesn't follow 
	 * the previous rules.
	 */
	public Object getAttractor(Object status) throws ParamDefinitionException, NotExistingNodeException, InputTypeException, AttractorNotFoundException;


	/**
	 * This method return an array with all the calculated attractors.
	 * @return an array with the attractors is returned.
	 */
	public Object[] getAttractors();


	/**
	 * This method returns the array of states in the given attractor
	 * @param The attractor
	 * @return The array of status
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 */
	public Object[] getStatesInAttractor(Object attractor) throws ParamDefinitionException, NotExistingNodeException, InputTypeException;

	/**
	 * This method returns the passed attractor length
	 * @param attractor: the attractor 
	 * @return the attractor length
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 */
	public int getAttractorLength(Object attractor) throws ParamDefinitionException, NotExistingNodeException, InputTypeException;
	
	/**
	 * This method returns the transient's length
	 * @param attractors
	 * @return the length
	 * @throws ParamDefinitionException
	 */
	public ArrayList<Integer> getTransientLength();

	/**
	 * This method returns the basin of attractions of each attractors
	 * @return HashMap(attractors,#transient)
	 */
	public ArrayList<Integer> getBasinOfAttraction();

	/**
	 * This method returns for each attractor the percentage of nodes oscillating
	 * @return HashMap<attractor,percentage>
	 */
	public double getOscillatingNodesRatio();


	/**
	 * This method rewires the attractor finder
	 * @throws ParamDefinitionException 
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws AttractorNotFoundException 
	 */
	public void rewiredAttractorFinder() throws ParamDefinitionException, NotExistingNodeException, InputTypeException, AttractorNotFoundException;

	/**
	 * This method returns all the old attractor when is made a permanent perturbation
	 * @return all the old attractors
	 */
	public Object[] getOldAttractors();

	/**
	 * This method saves the old state of the sampling object.
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public void clearAndStore() throws ParamDefinitionException, NotExistingNodeException, InputTypeException;

	/**
	 * This method returns the stored attractors.
	 * The returned HashMap has the attractor id as key an an object array, representing the
	 * states in the attractor, as value.
	 * @return The stored attractors
	 */
	public HashMap<Object, Object[]> getStoredAttractors();

	/**
	 * This method returns a copy of itself.
	 * @return a copy of itself.
	 * @throws InputTypeException 
	 * @throws NotExistingNodeException 
	 * @throws ParamDefinitionException 
	 * @throws NullPointerException 
	 */
	public AttractorsFinder copy() throws NullPointerException, ParamDefinitionException, NotExistingNodeException, InputTypeException;

	/**
	 * This method returns the number of the attractors.
	 * @return the number of the attractors
	 */
	public int getAttractorsNumber();
	
	/**
	 * This method returns the number of attractors not found
	 * @return the number of not found attractors
	 */
	public int getAttractorsNotFound();

}
