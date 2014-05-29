/**
 * BIMIB @ Milan University - Bicocca 
 * 2013
 */

/**
 * InputTypeException. 
 * This exception will lift up when a input type isn't correct for the function
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * 
 */

package it.unimib.disco.bimib.Exceptions;

public class InputTypeException extends Exception {

	static final long serialVersionUID = 1L;

	public InputTypeException() {
		super("Wrong input type");
	}

	public InputTypeException(String msg) {
		super(msg);
	}

}
