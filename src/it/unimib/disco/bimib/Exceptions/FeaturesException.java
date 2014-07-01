/**
 * FeaturesException
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2013
 * 
 */

package it.unimib.disco.bimib.Exceptions;

public class FeaturesException extends Exception {

	private static final long serialVersionUID = 1L;

	public FeaturesException() {
		this("Missing feature");
	}

	public FeaturesException(String msg) {
		super(msg);
	}

}
