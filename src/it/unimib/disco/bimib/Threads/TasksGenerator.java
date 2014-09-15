package it.unimib.disco.bimib.Threads;

/**
 * This class is the task generator.
 * It is a thread that creates new tasks and stores them in the tasks queue.
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @group BIMIB @ DISCo (Department of Information Technology, Systems and Communication) of Milan University - Bicocca 
 * @year 2014
 */

//System imports
import java.util.concurrent.ExecutorService;

public class TasksGenerator extends Thread {

	private Task task;
	private TasksQueue tasksQueue;
	private ExecutorService threadsPool;
	
	public TasksGenerator(TasksQueue tasksQueue, Task task, String threadName, ExecutorService threadsPool){
		super(threadName);
		//Params checking
		if(task == null)
			throw new NullPointerException("The task must be not null");
		if(tasksQueue == null)
			throw new NullPointerException("The task queue must be not null");
		//Params assignments
		this.task = task;
		this.tasksQueue = tasksQueue;
		this.threadsPool = threadsPool;
	}
	
	@Override
	public void run() {		int i = 0;
		while(!this.tasksQueue.isFinished()){
	
			try {
				//Puts a new task in the queue
				this.tasksQueue.push(this.task);
				System.out.println(i++);
			} catch (InterruptedException ex) {
				//Displays the exception message
				if(ex.getMessage() == null || ex.getMessage().equalsIgnoreCase(""))
					System.out.println(super.getName() + " thread forced closed");
				else
					System.out.println(ex.getMessage());
			}
		}
		//Closes all the active threads
		this.threadsPool.shutdownNow();

	}

}
