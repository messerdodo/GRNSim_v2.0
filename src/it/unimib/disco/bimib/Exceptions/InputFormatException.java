package it.unimib.disco.bimib.Exceptions;

/**
 * This class defines the input format exceptions
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca  
 *
 */

public class InputFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor
	 */
	public InputFormatException(){
		super("Input Format Exception");
	}
	
	/**
	 * Generic constructor
	 * @param msg: Exception message to display 
	 */
	public InputFormatException(String msg){
		super(msg);
	}
	

}
