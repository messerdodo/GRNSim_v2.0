package it.unimib.disco.bimib.Exceptions;

/**
 * This class defines the missed param exceptions
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca  
 *
 */

public class MissedParamException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public MissedParamException() {
		super("Missed parameter");
	}

	/**
	 * Generic constructor
	 * @param msg: Exception message to display 
	 */
	public MissedParamException(String msg) {
		super(msg);

	}


}
