package it.unimib.disco.bimib.Tes;

//System imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//GRNSim imports
import it.unimib.disco.bimib.Exceptions.TesTreeException;
import it.unimib.disco.bimib.Sampling.AttractorsFinder;
import it.unimib.disco.bimib.Sampling.SamplingManager;
import it.unimib.disco.bimib.Atms.Atm;
import it.unimib.disco.bimib.Atms.AtmManager;

public class TesManager {

	private Atm atm;
	private AttractorsFinder attractorsFinder;
	private TesTree createdTree;

	/**
	 * Default constructor 
	 * @param atm
	 * @param attractorsFinder
	 * @throws Exception
	 */
	public TesManager(AtmManager atm, SamplingManager sampling)throws Exception {

		this.atm = atm.getAtm();
		this.attractorsFinder = sampling.getAttractorFinder();	
		this.setCreatedTree(null);
	}

	/**
	 * @return the createdTree
	 */
	public TesTree getCreatedTree() {
		return createdTree;
	}

	/**
	 * @param createdTree the createdTree to set
	 */
	private void setCreatedTree(TesTree createdTree) {
		this.createdTree = createdTree;
	}

	/**
	 * This method tries to create a TES tree with the specified network. 
	 * @param givenTree: The required differentiation tree
	 * @return this method returns the created TES tree which is equal to the specified one. 
	 * If it's impossible to create the given tree, the method returns the null value. 
	 * @throws TesTreeException: An error occurred during the tree creation
	 */
	public double[] findCorrectTesTree(TesTree givenTree) throws TesTreeException {
		int k = this.attractorsFinder.getAttractors().length;
		int givenTreeDeepness = givenTree.getTreeDeppness();
		double[] correctDelta = null;
		//Checks if the number of attractor is enough
		if(k < givenTreeDeepness){
		return null;}

		//Searches every possible combination of delta in the matrix 
		ArrayList<double[]> combinations = deltaCombinations(this.atm.copyAtm(), this.attractorsFinder.getAttractors(), givenTreeDeepness);		

		TesTree createdTree = null;
		//Creates a new TES tree for each delta values combination
		for(double[] deltas : combinations){
			createdTree = new TesTree(deltas, this.atm.copyAtm(), this.attractorsFinder.getAttractors());
			//Checks if the created tree is equal to the given differentiation tree.
			//createdTree.print();
			if(createdTree.tesTreeCompare(givenTree)){
				correctDelta = deltas;
				break;
			}		
		}

		//Returns the correct delta values
		return correctDelta;
	}

	/**
	 * This method tries to create a TES tree with the specified network. 
	 * @param givenTree: The required differentiation tree
	 * @return this method returns the created TES tree which is equal to the specified one. 
	 * If it's impossible to create the given tree, the method returns the null value. 
	 * @throws TesTreeException: An error occurred during the tree creation
	 */
	public int findMinDistanceTesTree(TesTree givenTree) throws TesTreeException {
		int k = this.attractorsFinder.getAttractors().length;
		int givenTreeDeepness = givenTree.getTreeDeppness();
		
		//Checks if the number of attractor is enough
		if(k < givenTreeDeepness){
			return -1;
		}

		//Searches every possible combination of delta in the matrix 
		ArrayList<double[]> combinations = deltaCombinations(this.atm.copyAtm(), this.attractorsFinder.getAttractors(), givenTreeDeepness);		

		TesTree createdTree = null;
		int minDistance = -1, currentDistance;
		//Creates a new TES tree for each delta values combination
		for(double[] deltas : combinations){
			createdTree = new TesTree(deltas, this.atm.copyAtm(), this.attractorsFinder.getAttractors());
			//Calculates the min distance between the created tree and the given tree
			currentDistance = createdTree.tesTreeDistance(givenTree);
			if(currentDistance == 0){
				minDistance = currentDistance;
				break;
			}
			//Calculates the min distance.
			minDistance = (minDistance == -1 ? currentDistance : Math.min(currentDistance, minDistance));	
		}

		//Returns the min distance between the given tree and the created tree with the selected deltas
		return minDistance;
	}

	/**
	 * This method tries to create a TES tree with the specified network. 
	 * @param givenTree: The required differentiation tree
	 * @return this method returns the created TES tree which is equal to the specified one. 
	 * If it's impossible to create the given tree, the method returns the null value. 
	 * @throws TesTreeException: An error occurred during the tree creation
	 */
	public int findMinHistogramDistanceTesTree(TesTree givenTree) throws TesTreeException {
		int k = this.attractorsFinder.getAttractors().length;
		int givenTreeDeepness = givenTree.getTreeDeppness();
		
		System.out.println("k " + k + " deepness " + givenTreeDeepness);
		//Checks if the number of attractor is enough
		if(k < givenTreeDeepness){
			return -1;
		}

		//Searches every possible combination of delta in the matrix 
		ArrayList<double[]> combinations = deltaCombinations(givenTreeDeepness);		
		TesTree createdTree = null;
		int minDistance = -1, currentDistance;
		//Creates a new TES tree for each delta values combination
		double deltas[];
		int i = 0;
		while(minDistance != 0 && i < combinations.size()){
			deltas = combinations.get(i);

			try{
				createdTree = new TesTree(deltas, this.atm.copyAtm(), this.attractorsFinder.getAttractors());
			}catch(TesTreeException e){
				return -1;
			}
			//Calculates the min distance between the created tree and the given tree
			currentDistance = createdTree.tesTreeHistogramComparison(givenTree);
			if(currentDistance == 0){
				minDistance = currentDistance;
				System.out.println("Min Distance for: " + Arrays.toString(deltas) + " is " + minDistance);

			}else{
				//Calculates the min distance.
				minDistance = (minDistance == -1 ? currentDistance : Math.min(currentDistance, minDistance));	
				System.out.println("Min Distance for: " + Arrays.toString(deltas) + " is " + minDistance + " current distance " + currentDistance);
			}
			i++;
		}

		//Returns the min distance between the given tree and the created tree with the selected deltas
		return minDistance;
	}
	
	
	
	public ArrayList<double[]> deltaCombinations(int requiredTreeDeepness)throws TesTreeException{
		//Initializes the ArraList
				ArrayList<Double> values = new ArrayList<Double>();
				ArrayList<double[]> combinations = new ArrayList<double[]>();
				for(double delta = 0.01; delta < 0.15; delta = delta + 0.01){
					values.add(delta);
				}
				
				//Sorts all the value in a crescent order
				Collections.sort(values); 
				
				if(requiredTreeDeepness > values.size())
					throw new TesTreeException("k ("+requiredTreeDeepness+") is bigger then the threshold's values ("+values.size() +")");
				else{
					//Gets the unstructured combinations 
					ArrayList<Double> e = combinationCreator(values, requiredTreeDeepness);
					for(int i = 0; i < e.size(); i += requiredTreeDeepness){
						double[] singleCombination = new double[requiredTreeDeepness + 1];
						singleCombination[0] = 0.0;
						for(int j = 1; j < requiredTreeDeepness + 1; j++){
							singleCombination[j] = e.get(i+j-1);
						}
						//Adds the combination in the array list "Combinations"
						combinations.add(singleCombination);
					}
				}
				//Returns all k-sized combinations 
				return combinations;
	}
	
	/**
	 * This method return all the possible delta values combinations. Each combination is ascending sorted. 
	 * @param atm: The attractors threshold matrix.
	 * @param attractors: The attractors set.
	 * @param requiredTreeDeepness: The number of elements in each combination.
	 * @return an arrayList which contains an array with the delta values for each combination.
	 * @throws TesTreeException: An error occurred during the combinations calculation.
	 */
	public ArrayList<double[]> deltaCombinations(double[][] atm, Object[] attractors, int requiredTreeDeepness) throws TesTreeException{	
		//Initializes the ArraList
		ArrayList<Double> values = new ArrayList<Double>();
		ArrayList<double[]> combinations = new ArrayList<double[]>();
		//Puts into the ArrayList all the distinct values of the Atm matrix
		for(int line = 0; line < atm.length; line++){
			for(int pillar = 0; pillar < atm.length; pillar++){
				if(values.contains(atm[line][pillar]) == false)
					values.add(atm[line][pillar]);	
			}
		}
		
		//Sorts all the value in a crescent order
		Collections.sort(values); 
		//Removes the zero if it is present.
		if(values.contains(0.0))
			values.remove(new Double(0.0));
		
		if(requiredTreeDeepness > values.size())
			throw new TesTreeException("k ("+requiredTreeDeepness+") is bigger then the threshold's values ("+values.size() +")");
		else{
			//Gets the unstructured combinations 
			ArrayList<Double> e = combinationCreator(values, requiredTreeDeepness);
			for(int i = 0; i < e.size(); i += requiredTreeDeepness){
				double[] singleCombination = new double[requiredTreeDeepness + 1];
				singleCombination[0] = 0.0;
				for(int j = 1; j < requiredTreeDeepness + 1; j++){
					singleCombination[j] = e.get(i+j-1);
				}
				//Adds the combination in the array list "Combinations"
				combinations.add(singleCombination);
			}
		}
		//Returns all k-sized combinations 
		return combinations;

	}

	/**
	 * This is a service method for the combinations generation.
	 * @param values: The set with all the values that must be used in the combinations generation.
	 * @param k: The number of values in each combination
	 * @return Returns an arrayList with the unformatted combinations. 
	 * Each set of k values should be explained as a single combination.
	 */
	private ArrayList<Double> combinationCreator(ArrayList<Double> values, int k) {
		//Initializes all the ArrayList needed
		ArrayList<Double> head = new ArrayList<Double>();
		ArrayList<Double> temp =new ArrayList<Double>();
		ArrayList<Double> tailcombs = new ArrayList<Double>();
		ArrayList<Double> combs = new ArrayList<Double>();
		int j = 0;

		//If k is equal to the length of the ArrayList returns its
		if (k == values.size()) {
			return values;
		}
		//If k is equal to 1 adds all the values in the ArrayList combs
		if (k == 1) {
			for (int i = 0; i < values.size() ; i++) {
				combs.add(values.get(i));
			}
			return combs;
		}
		//Searches the k combinations
		for (int i = 0; i < values.size() - k  + 1; i++) {
			//Adds the first element of the ArrayList values into head
			head.add(values.get(i));

			//Adds all the remain values in a temporary ArrayList
			for(int index = i + 1; index < values.size(); index++)
				temp.add(values.get(index));

			//If the temporary AraayList isn't empty makes the recursive call
			if(temp.isEmpty() != true)
				tailcombs = combinationCreator(temp, k - 1);

			//Pass all the element of the ArrayList tailcombs
			while ( j < tailcombs.size()) {
				//Checks if the specified values of the ArrayList is different from the ith head's element
				//and if it is bigger.
				if(tailcombs.get(j) != head.get(0) && tailcombs.get(j) > head.get(i)){
					//Adds the ith head's element in the ArraList combs
					combs.add(head.get(i));
					//Adds all the k-element in the ArraList combs
					for(int valueK = 0 ; valueK < k - 1; valueK ++){
						combs.add(tailcombs.get(j));
						j++;}
				}

			}
			//Resets the index and the temporary ArrayList
			j = 0;
			temp.clear();
		}
		return combs;
	}

	/**
	 * Creates the given tree
	 * @param rootId: root node id
	 * @throws TesTreeException 
	 * @throws NumberFormatException 
	 */
	public static TesTree createTesTreeFromFile(ArrayList<String[]> tree) throws NumberFormatException, TesTreeException{
		TesTree givenTree;

		if(!tree.get(0)[0].equals("0"))
			throw new TesTreeException("Root not specified");

		givenTree = new TesTree(Integer.parseInt(tree.get(0)[1]));

		for(int i = 1; i < tree.size(); i++){
			addNodeManually(Integer.parseInt(tree.get(i)[1]),
					Integer.parseInt(tree.get(i)[0]),
					Integer.parseInt(tree.get(i)[2]),
					givenTree);
		}

		return givenTree;
	}

	/**
	 * Adds a new node in the given tree
	 * @param nodeId: The new node id
	 * @param level: The new node level
	 * @param parentId: The new node parent id
	 * @throws TesTreeException: An error occurred during the research
	 */
	private static void addNodeManually(int nodeId, int level, int parentId, TesTree tree) throws TesTreeException{
		tree.addNodeManually(nodeId, level, parentId);
	}


}
