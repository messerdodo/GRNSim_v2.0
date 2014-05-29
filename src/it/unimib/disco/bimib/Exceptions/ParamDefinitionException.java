/**
 * BIMIB @ Milan University - Bicocca 
 * 2013
 */

/**
 * ParamDefinitionException. 
 * This exception will lift up when a parameter isn't correct
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

package it.unimib.disco.bimib.Exceptions;

public class ParamDefinitionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParamDefinitionException() {
		super("The parameter isn't correct");
	}

	public ParamDefinitionException(String message) {
		super(message);
	}

}
