package it.unimib.disco.bimib.threads;

/**
 * This interface defines the methods of the notifying threads
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

public interface ThreadListener {
	
	/**
	 * This method notifies that the thread is started
	 */
	public void threadStarted(String task, int threadId);
	
	/**
	 * This method notifies that the thread is finished
	 */
	public void threadFinished(String task, int threadId);
	
	/**
	 * This method notifies that the thread is stopped
	 */
	public void threadStopped(String task, int threadId);
	
}
