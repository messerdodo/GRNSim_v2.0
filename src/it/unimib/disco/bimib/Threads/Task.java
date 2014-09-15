package it.unimib.disco.bimib.Threads;

/**
 * This is the Task interface. 
 * It defines only the doTask method.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

public interface Task {
	
	public boolean doTask() throws Exception;

}
