/**
 * AttractorNotFoundException
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 */
package it.unimib.disco.bimib.Exceptions;

public class AttractorNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AttractorNotFoundException() {
		this("Attractor not found");
	}

	public AttractorNotFoundException(String msg) {
		super(msg);
	}

}
