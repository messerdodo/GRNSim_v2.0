package it.unimib.disco.bimib.Statistics;

//System imports
import java.util.ArrayList;
import java.util.HashMap;

//import java.util.ArrayList;
//import java.util.HashMap;

//GRNSim imports
//import it.unimib.disco.bimib.Mutations.*;

public class DynamicPerturbationsStatistics {
	
	private ArrayList<Integer> avalanches;
	private int[] sensitivity;
	private int nodes;
	
	/**
	 * Default constructor
	 * @param nodes: Number of the nodes of the network
	 */
	public DynamicPerturbationsStatistics(int nodes){
		//Data structures initialization
		this.nodes = nodes;
		this.avalanches = new ArrayList<Integer>();
		this.sensitivity = new int[this.nodes];
		//Sets the sensitivity to 0 for each node.
		for(int i = 0; i < this.nodes; i++)
			this.sensitivity[i] = 0;
	}
	
	/**
	 * This method computes the avalanches and the sensitivity contribution
	 * performing all the possible state shifts in the two passed attractors. 
	 * @param oldAttractor : the first attractor. It must be a String array
	 * @param newAttractor : the second attractor. It must be a String array.
	 */
	public void avalanchesAndSensitivityComputation(Object[] oldAttractor, Object[] newAttractor){

		int minAvalanche = this.nodes + 1; //The avalanche must be less or equal than the number of the genes
		int i, j, avalanche;
		ArrayList<Integer> positions;
		int localSensitivity[], minSensitivity[] = null;
		
		//Performs all the possible attractor states shifts.
		for(int shift = 0; shift < newAttractor.length; shift++){

			avalanche = 0;
			i = 0;
			j = shift;
			//Initializes the unchanged genes arraylist and the local sensitivity
			//for the current shifting test
			positions = new ArrayList<Integer>();
			localSensitivity = new int[this.nodes];
			for(int gene = 0; gene < this.nodes; gene++){
				positions.add(gene);
				localSensitivity[gene] = 0;
			}
			
			for(int k = 0; k < lcm(oldAttractor.length, newAttractor.length); k++){
				String stateInOldAttractor = (String) oldAttractor[i];
				String stateInNewAttractor = (String) newAttractor[j];
				
				//Compares gene by gene the two states of the attractors
				for(int gene = 0; gene < positions.size(); gene++){
					if(stateInOldAttractor.charAt(positions.get(gene)) != stateInNewAttractor.charAt(positions.get(gene))){
						localSensitivity[positions.get(gene)] += 1;
						avalanche ++;
						positions.remove(gene);
					}	
				}
				//Define the new state index for both the attractors
				i = (i + 1) % oldAttractor.length;
				j = (j + 1) % newAttractor.length;
			}

			//Checks if this shift has produced the minimum avalanche
			if(avalanche < minAvalanche){
				//Updates the minimum avalanche and the corresponding sensitivity configuration
				minAvalanche = avalanche;
				minSensitivity = localSensitivity;
			}
		}
		
		//Saves the minimum avalanche calculated.
		this.avalanches.add(minAvalanche);
		//Saves the sensitivity associated to the minimum avalanche
		for(int gene = 0; gene < this.nodes; gene++){
			this.sensitivity[gene] += minSensitivity[gene];
		}

	}
	
	/**
	 * This method returns the least common multiple (lcm) between two values.
	 * @param x The first integer and positive number
	 * @param y The second integer and positive number
	 * @return the lcm of x and y
	 */
	private int lcm (int x, int y){
		return (x * y)/gcd(x, y);
	}
	
	/**
	 * This method returns the greatest common divisor (GCD) between two values 
	 * using the Euclid's algorithm.
	 * @param x The first integer and positive number
	 * @param y The second integer and positive number
	 * @return The GCD of x and y
	 */
	private int gcd(int x, int y){
		if(y == 0)
			return x;
		else
			return gcd(y, x % y);
	}
	
	/**
	 * This method returns the avalanche distribution
	 * @return avalanche distribution
	 */
	public HashMap<Integer, Integer>  getAvalanche(){
		
		HashMap<Integer, Integer> distribution = new HashMap<Integer, Integer>();
		
		for(Integer value : this.avalanches){
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
	 * This method returns the sensitivity of each node.
	 * @return sensitivity array.
	 */
	public int[]  getSensitivity(){
		return this.sensitivity;
	}
	
	
	
	


	
}
