package it.unimib.disco.bimib.Statistics;

//System imports
import java.util.ArrayList;
import java.util.HashMap;

//GRNSim imports
import it.unimib.disco.bimib.Mutations.*;

public class DynamicPerturbation {
	
	/**
	 * This method returns the avalanche of a specific mutation experiments
	 * @param mutationManager
	 * @return avalanche distribution
	 */
	public static HashMap<Integer, Integer>  getAvalanche(MutationManager mutationManager){
		Mutation mutation = mutationManager.getMutation();
		
		ArrayList<Integer> result = mutation.getAvalanches();
		HashMap<Integer, Integer> distribution = new HashMap<Integer, Integer>();
		
		for(Integer value : result){
			if(distribution.containsKey(value)){
				distribution.put(value,
						distribution.get(value) + 1);
			}else{
				distribution.put(value, 1);
			}
		}
		return distribution;
	}
	
	/**
	 * This method returns the sensitivity of a specific mutation experiments
	 * @param mutationManager
	 * @return sensitivity distribution
	 */
	public static int[]  getSensitivity(MutationManager mutationManager){
		Mutation mutation = mutationManager.getMutation();
		return mutation.getSensitivity();
	}
	
	


	
}
