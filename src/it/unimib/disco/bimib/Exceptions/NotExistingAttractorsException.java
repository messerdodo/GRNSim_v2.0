/**
 * NotExistingAttractorsException.
 * This exception will lift up when the type of attractors isn't correct
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Exceptions;

public class NotExistingAttractorsException extends Exception{

	private static final long serialVersionUID = 1L;

	public NotExistingAttractorsException() {
		super("The type of attractors isn't correct");
	}

	public NotExistingAttractorsException(String message) {
		super(message);
	}

}
