package it.unimib.disco.bimib.Threads;

/**
 * This class is the task queue.
 * The queue stores the tasks with the FIFO policy.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */


//GRNSim imports
import it.unimib.disco.bimib.Exceptions.TaskCompletedException;

public class TaskScheduler {
	
	private Task taskToPerform;
	private int remaining;
	
	public TaskScheduler(Task taskToPerform, int required){
		this.taskToPerform = taskToPerform;
		this.remaining = required > 0 ? required : 0;
	}
	
	public synchronized Task take() throws TaskCompletedException{
		if(this.remaining == 0)
			throw new TaskCompletedException();
		else
			return this.taskToPerform;
	}
	
	public synchronized void newMatch(){
		this.remaining = this.remaining == 0 ? 0 : this.remaining - 1;
	}
	
	public synchronized void setRequiredMatches(int reqMatches){
		this.remaining = reqMatches;
	}
	
	public synchronized boolean isFinished(){
		return (this.remaining == 0);
	}
	
	public int getRemaining(){
		return this.remaining;
	}
}
