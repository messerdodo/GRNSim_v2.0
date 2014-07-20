package it.unimib.disco.bimib.Threads;

public class TasksGenerator extends Thread {

	private Task task;
	private TasksQueue tasksQueue;
	
	public TasksGenerator(TasksQueue tasksQueue, Task task, String threadName){
		super(threadName);
		//Params checking
		if(task == null)
			throw new NullPointerException("The task must be not null");
		if(tasksQueue == null)
			throw new NullPointerException("The task queue must be not null");
		//Params assignments
		this.task = task;
		this.tasksQueue = tasksQueue;
	}
	
	@Override
	public void run() {
		while(!this.tasksQueue.isFinished()){
			try {
				//Puts a new task in the queue
				this.tasksQueue.push(this.task);
			} catch (InterruptedException ex) {
				//Displays the exception message
				if(ex.getMessage() == null || ex.getMessage().equalsIgnoreCase(""))
					System.out.println(ex);
				else
					System.out.println(ex.getMessage());
			}
		}
		System.out.println("Thread closed");

	}

}
