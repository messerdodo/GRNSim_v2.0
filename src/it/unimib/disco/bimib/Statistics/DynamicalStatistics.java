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
	
	private SamplingManager samplingManager;
	
	public DynamicalStatistics(SamplingManager samplingManager){
		this.attractorsLengths = new ArrayList<Integer>();
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
			//Returns the previous version of the lengths array.
			return this.attractorsLengths;
		}else{
			Object[] attractors;
			this.attractorsLengths = new ArrayList<Integer>();
			//Gets the array
			attractors = this.samplingManager.getAttractorFinder().getAttractors();
			for(Object attractor : attractors){
				//Gets the attractor length
				this.attractorsLengths.add(this.samplingManager.getAttractorFinder().getAttractorLength(attractor));
			}
			return this.attractorsLengths;
		}
	}
	
}
