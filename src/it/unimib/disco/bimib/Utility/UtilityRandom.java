/**
 * This class implements the common random functions.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 * 
 */
package it.unimib.disco.bimib.Utility;

//System imports
import java.util.ArrayList;
import java.util.Collections;
//GRNSim imports
import it.unimib.disco.bimib.Exceptions.ParamDefinitionException;

public class UtilityRandom {

	/**
	 * This methods returns a boolean value with p probability
	 * @param p is the probability of true
	 * @return a boolean random value 
	 */
	public static boolean randomBooleanChoice(double p){
		return Math.random() <= p;	
	}


	/**
	 * This methods returns an element from an array with 
	 * a uniform distribution probability
	 * @param elements is an array of elements
	 * @return an element from the array with an uniform distribution.
	 */
	public static int randomUniformChoice(int[] elements){
		int n = elements.length;
		//The index is the smallest integer before n * U 
		int index = (int)Math.floor(n * Math.random());
		return elements[index];
	}
	
	/**
	 * This methods returns an element from an array list with 
	 * a uniform distribution probability
	 * @param elements is an array list of integer elements
	 * @return an element from the array list chosen with an uniform distribution.
	 */
	public static Integer randomUniformChoice(ArrayList<Integer> elements){
		int n = elements.size();
		//The index is the smallest integer before n * U 
		int index = (int)Math.floor(n * Math.random());
		return elements.get(index);
	}


	/**
	 * This method return a random integer number between min and max - 1.
	 * @Return a random integer number
	 */
	public static int randomUniform(int min, int max){
		int n = (int)Math.floor((max - min) * Math.random());
		return n + min;
	}


	/**
	 * This method returns an elements from an array with
	 * a discrete distribution probability
	 * @param probabilities : array of probabilities
	 * @param elements : array elements
	 * @return an element from the array 
	 */
	public static int randomDiscreteDistribuitedChoice(double[]probabilities, int[] elements){

		double u = Math.random();
		int choice = 0;
		double probAcc = 0;
		while(choice < probabilities.length && u > (probAcc + probabilities[choice])){
			probAcc += probabilities[choice];
			choice++;

		}
		return choice == 0 ? elements[0] : elements[choice - 1];	
	}

	/**
	 * This method returns not negative integer with
	 * a discrete distribution probability
	 * @param probabilities : array of probabilities
	 * @return an integer following the passed discrete distribution 
	 */
	public static int randomIntegerDiscreteDistribuitedChoice(double[]probabilities){
		double u = Math.random();
		int choice = 0;
		double probAcc = 0;
		while(choice < probabilities.length && u > probAcc + probabilities[choice]){
			probAcc += probabilities[choice];
			choice++;
		}
		return choice;
	}

	/**
	 * This method return a random n values binary sequence. 
	 * Each value is independent and uniformly distributed with probability 'probability' 
	 * @param n: sequence length
	 * @param probability: Probability to have a True value
	 * @return: returns a binary n value sequence
	 */
	public static String createRandomBinarySequence(int n, double probability){
		String sequence = "";
		//Generates the Binary sequence. Each value is uniformly distributed
		for(int i = 0; i < n; i++){
			sequence += (Math.random() <= probability ? "1" : "0");
		}
		return sequence;
	}

	/**
	 * This method returns a integer subset on the integer set {0, ... ,elementsNumber - 1}
	 * The subset dimension is randomly chosen
	 * @param elements: The original set
	 * @return integer subset.
	 */
	public static int[] randomSubset(Integer[] elements){
		double u = Math.random();
		int choose;
		int[] subset;
		//Selects the subset dimension
		int dimension = (int)(Math.ceil(elements.length * u));
		ArrayList<Integer> added = new ArrayList<Integer>();
		//Creates the random subset
		for(int i = 0; i < dimension; i++){
			do{
				u = Math.random();
				choose = elements[(int)(Math.floor(elements.length * u))];
			}while(added.contains(choose));
			added.add(choose);	
		}
		Collections.sort(added);
		subset = new int[dimension];
		for(int i = 0; i < dimension; i++){
			subset[i] = added.get(i);
		}
		return subset;
	}

	/**
	 * This method returns a integer subset on the integer set passed.
	 * The subset dimension is passed.
	 * @param elements: The original set
	 * @param size: The size of the subset.
	 * @return integer subset.
	 * @throws ParamDefinitionException 
	 */
	public static ArrayList<Integer> randomSubset(ArrayList<Integer> elements, int size) throws ParamDefinitionException{

		//Parameters checking
		if(elements == null)
			throw new ParamDefinitionException("The original set must be not null");
		if(size > elements.size())
			throw new ParamDefinitionException("The subset size must be smaller than the original set size.");
		if(size < 0)
			return null;

		int choose;
		double u;

		ArrayList<Integer> subset = new ArrayList<Integer>();
		//Creates the random subset
		for(int i = 0; i < size; i++){
			do{
				u = Math.random();
				choose = elements.get((int)(Math.floor(elements.size() * u)));
			}while(subset.contains(choose));
			subset.add(choose);	
		}

		return subset;
	}

	/**
	 * This method returns a integer subset on the integer set {0, ... ,elementsNumber - 1}
	 * The subset dimension is specified
	 * @param elements: the original set
	 * @param size: the subset size
	 * @return integer subset.
	 * @throws ParamDefinitionException 
	 */
	public static ArrayList<Integer> randomSubset(int elementsNumber, int size) throws ParamDefinitionException{

		//Param checking
		if(elementsNumber < 0)
			throw new ParamDefinitionException("The original set must be not null");
		if(size > elementsNumber)
			throw new ParamDefinitionException("The subset size must be smaller than the original set size.");
		if(size < 0)
			return null;

		int choose;
		ArrayList<Integer> subset = new ArrayList<Integer>();
		//Creates the random subset
		for(int i = 0; i < size; i++){
			do{
				choose = UtilityRandom.randomUniform(0, elementsNumber);
			}while(subset.contains(choose));
			subset.add(choose);	
		}
		//Sorts the subset
		Collections.sort(subset);
		return subset;
	}
	
	/**
	 * This method returns a integer subset on the integer set {0, ... ,elementsNumber - 1}
	 * The subset dimension is specified
	 * @param elements: the original set
	 * @param size: the subset size
	 * @param exclude: list of elements to exclude from the choice.
	 * @return integer subset.
	 * @throws ParamDefinitionException 
	 */
	public static ArrayList<Integer> randomSubset(int elementsNumber, int size, ArrayList<Integer> exclude) throws ParamDefinitionException{

		//Param checking
		if(elementsNumber < 0)
			throw new ParamDefinitionException("The original set must be not null");
		if(size > elementsNumber)
			throw new ParamDefinitionException("The subset size must be smaller than the original set size.");
		if(size < 0)
			return null;

		int choose;
		ArrayList<Integer> subset = new ArrayList<Integer>();
		//Creates the random subset
		for(int i = 0; i < size; i++){
			do{
				choose = UtilityRandom.randomUniform(0, elementsNumber);
			}while(subset.contains(choose) || exclude.contains(choose));
			subset.add(choose);	
		}
		//Sorts the subset
		Collections.sort(subset);
		return subset;
	}
	
	/**
	 * This method returns a permutation of the passed array.
	 * @param array: An object array
	 * @return  permutation of the passed array.
	 */
	public static Object[] randomPermutation(Object[] array){
		if(array == null)
			throw new NullPointerException("The array must be not null");
		
		Object swap;
		int index;
		//Performs a permutation of the array
		for(int i = 0; i < array.length - 1; i++){
			index = randomUniform(i + 1, array.length - 1);
			swap = array[i];
			array[i] = array[index];
			array[index] = swap;
		}
		return array;
	}
	
	/**
	 * This method returns a permutation of the passed array.
	 * @param array: An object array list
	 * @return  permutation of the passed array.
	 */
	public static ArrayList<Integer> randomPermutation(ArrayList<Integer> array){
		if(array == null)
			throw new NullPointerException("The array must be not null");
		
		Integer swap;
		int index;
		//Performs a permutation of the array
		for(int i = 0; i < array.size() - 1; i++){
			index = randomUniform(i + 1, array.size() - 1);
			swap = array.get(i);
			array.set(i, array.get(index));
			array.set(index, swap);
		}
		return array;
	}
}
