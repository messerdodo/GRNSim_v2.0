package it.unimib.disco.bimib.Threads;

/**
 * This class is the task executor.
 * It is a thread that executes the tasks stored in the tasks queue.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

import it.unimib.disco.bimib.Exceptions.TaskCompletedException;



//System imports

public class TasksExecutor extends Thread{

	private  TaskScheduler scheduler;
	
	public TasksExecutor(TaskScheduler scheduler, String threadName){
		super(threadName);
		if(scheduler == null)
			throw new NullPointerException("The tasks queue object must be not null.");
		this.scheduler = scheduler;
	}
	
	@Override
	public void run() {
		while(!this.scheduler.isFinished()){
			try {
				Task taskToPerform = this.scheduler.take();
				boolean match = taskToPerform.doTask();
				//Checks if the task produced a match, in this case the number of match will be incremented 
				if(match)
					this.scheduler.newMatch();
				
			}catch(TaskCompletedException e){
				//Thread.currentThread().interrupt();	
			} catch (Exception e) {
				//Displays the exception message
				if(e.getMessage().equalsIgnoreCase(""))
					System.out.println(e);
				else
					System.out.println(e.getMessage());
			}
		}	
		//Simulation completed, so all the threads must be closed.
		Thread.currentThread().interrupt();	
	}

}