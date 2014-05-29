/**
 * BIMIB @ Milan University - Bicocca 
 * 2013
 */

/**
 * This class is the Function interface that specifies 
 * all the methods that the node's functions must have.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

package it.unimib.disco.bimib.Functions;

// System imports
import java.util.ArrayList;

// GRNSim imports
import it.unimib.disco.bimib.Exceptions.*;

public interface Function {

	/**
	 * This method evaluates the function with the passed inputs.
	 * @param inputs: Is a Object array with the function inputs. 
	 * In this case they must be Objects
	 * @return Returns a Object value
	 * throws InputTypeException: This exception will lift up when the 
	 * inputs won't be Objects.
	 * @throws ParamDefinitionException 
	 */
	public Object eval(Object inputs[]) throws InputTypeException, ParamDefinitionException;

	/**
	 * This method returns the function xml representation.
	 * @return: the xml function representation
	 */
	public String toGRNML();

	/**
	 * This method returns the number of input for calculate the function of a node
	 * @return the number of input
	 */
	public ArrayList<Integer> getInputs();

	/**
	 * This method return the bias of the function
	 * @return bias
	 */
	public double getBias();

	/**
	 * This method activates the perpetually mutation
	 * @param knockIn: It is true if the gene will be always active, false if it will be always off.
	 */
	public void perpetuallyMutationActivation(boolean knockIn);

	/**
	 * This method deactivates the perpetually mutation
	 */
	public void perpetuallyMutationDeactivation();

	/**
	 * This method adds a link to the function
	 * @param inputLink: The new input way
	 * @throws ParamDefinitionException
	 */
	public void addLink(int inputLink) throws ParamDefinitionException;

	/**
	 * This method returns a copy of the current function.
	 * @return a copy of the function.
	 * @throws ParamDefinitionException 
	 */
	public Function copy() throws ParamDefinitionException;
}
