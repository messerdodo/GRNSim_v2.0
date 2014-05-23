/**
 * BIMIB @ Milan University - Bicocca 
 * 2013
 */

/**
 * NotExistingNodeException class.
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

package GRNException;


public class NotExistingNodeException extends Exception {


	private static final long serialVersionUID = 1L;

	public NotExistingNodeException() {
		super("The node doesn't exist");

	}

	public NotExistingNodeException(String exception) {
		super(exception);
	}



}

