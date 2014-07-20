package it.unimib.disco.bimib.Threads;

public class TasksExecutor extends Thread{

	private  TasksQueue tasksQueue;
	
	public TasksExecutor(TasksQueue tasksQueue, String threadName){
		super(threadName);
		if(tasksQueue == null)
			throw new NullPointerException("The tasks queue object must be not null.");
		this.tasksQueue = tasksQueue;
	}
	
	@Override
	public void run() {
		while(!this.tasksQueue.isFinished()){
			try {
				Task task = tasksQueue.take();
				task.doTask();
				System.out.println(this.getName() + " does the task");
				this.tasksQueue.newMatch();
				System.out.println("Remaining: " + this.tasksQueue.getRemaining());
			} catch (InterruptedException e) {
				System.out.println(this.getName() + " Thread closed");
			} catch (Exception e) {
				Thread.currentThread().interrupt();	
			}
		}	
	}

}