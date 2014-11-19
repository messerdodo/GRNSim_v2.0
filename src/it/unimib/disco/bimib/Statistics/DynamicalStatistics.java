package it.unimib.disco.bimib.Statistics;

//GESTODifferent imports
import it.unimib.disco.bimib.Exceptions.InputTypeException;
import it.unimib.disco.bimib.Exceptions.NotExistingNodeException;
import it.unimib.disco.bimib.Exceptions.ParamDefinitionException;
import it.unimib.disco.bimib.Sampling.SamplingManager;

//System imports
import java.util.ArrayList;

public class DynamicalStatistics {
	
	private ArrayList<Integer> attractorsLengths;
	private ArrayList<Integer> basinOfAttraction;
	
	private SamplingManager samplingManager;
	
	public DynamicalStatistics(SamplingManager samplingManager){
		this.attractorsLengths = new ArrayList<Integer>();
		this.basinOfAttraction = new ArrayList<Integer>();
		this.samplingManager = samplingManager;
	}

	/**
	 * This method returns an Array List with all the attractors lengths.
	 * The array is not computed when the method is invoked, but is returned the stored version.
	 * @return all the attractors lengths.
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public ArrayList<Integer> getAttractorsLength() throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		return this.getAttractorsLength(false);
	}
	
	/**
	 * This method returns an Array List with all the attractors lengths.
	 * If the compute param is false, the array is not computed when the method is invoked, but is returned the stored version.
	 * Otherwise if the param compute is true, the array is computed and it will be updated.
	 * @param compute: It used to specify if the attractor lengths must be computed.
	 * @return all the attractors lengths.
	 * @throws ParamDefinitionException
	 * @throws NotExistingNodeException
	 * @throws InputTypeException
	 */
	public ArrayList<Integer> getAttractorsLength(boolean compute) throws ParamDefinitionException, NotExistingNodeException, InputTypeException{
		if(!compute){
			//Computes a new version of the attractors lenght
			Object[] attractors;
			this.attractorsLengths = new ArrayList<Integer>();
			//Gets the array
			attractors = this.samplingManager.getAttractorFinder().getAttractors();
			for(Object attractor : attractors){
				//Gets the attractor length
				this.attractorsLengths.add(this.samplingManager.getAttractorFinder().getAttractorLength(attractor));
			}
		}
		//Returns the attractors lengths
		return this.attractorsLengths;
	}
	
	/**
	 * This method returns the basin of attraction of each attractor of the network.
	 * The basin of attraction is not computed when the method is invoked.
	 * @return The basin of attraction of each attractor of the network.
	 */
	public ArrayList<Integer> getBasinOfAttraction(){
		return this.getBasinOfAttraction(false);
	}
	
	/**
	 * This method returns the basin of attraction of each attractor of the network.
	 * If the parameter compute is true, the method compute the basin of attraction when it is invoked.
	 * Otherwise, if compute is false, the method returns the stored array, so it is not update.
	 * @param compute: It is used in order to specify id the basin of attraction must be compute.
	 * @return an array with the basin of attraction of each attractor of the network.
	 */
	public ArrayList<Integer> getBasinOfAttraction(boolean compute){
		if(compute){
			//Gets the updated basin of attraction of each attractor
			this.basinOfAttraction = this.samplingManager.getAttractorFinder().getBasinOfAttraction();
		}
		return this.basinOfAttraction;
	}
	
	/**
	 * This method returns the ratio of oscillating nodes. 
	 * An oscillating node of a network is a node that change it's value
	 * in the states of an attractor.
	 * @return
	 */
	public double getOscillatingNodesRatio(){
		return this.samplingManager.getAttractorFinder().getOscillatingNodesRatio();
	}
	
}
