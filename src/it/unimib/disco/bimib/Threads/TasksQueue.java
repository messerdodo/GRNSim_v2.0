package it.unimib.disco.bimib.Threads;

/**
 * This class is the task queue.
 * The queue stores the tasks with the FIFO policy.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

//System imports
import java.util.ArrayList;

public class TasksQueue {
	
	private ArrayList<Task> tasksQueue;
	private int remaining;
	
	public TasksQueue(int required){
		this.tasksQueue = new ArrayList<Task>();
		this.remaining = required > 0 ? required : 0;
	}
	
	public synchronized void push(Task task) throws InterruptedException{
		if(task != null && this.remaining > 0){
			if(this.tasksQueue.size() >= this.remaining){
				wait();
			}
			this.tasksQueue.add(task);
			notifyAll();
		}	
	}
	
	public synchronized Task take() throws InterruptedException{
		if(this.tasksQueue.isEmpty() || this.isFinished())
			wait();
		Task task = this.tasksQueue.get(0);
		this.tasksQueue.remove(0);
		notifyAll();
		return task;
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
