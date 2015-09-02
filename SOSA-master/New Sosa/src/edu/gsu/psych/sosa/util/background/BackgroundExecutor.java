package edu.gsu.psych.sosa.util.background;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import edu.gsu.psych.sosa.main.SOSAMain;


public class BackgroundExecutor {
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private ExecutorService debugExecutor;
	private OperationStatus status;
	private List<Future<?>> tasks = new ArrayList<Future<?>>();
	
	public BackgroundExecutor(OperationStatus status){
		this.status = status;
		if(SOSAMain.DEBUG)
			createMonitor();
		//assigns OperatoinStatus and creates a Monitor if debug is True
	}
	
	public void submit(OperationRunnable task){
		task.initOperation(status);
		tasks.add(executor.submit(task));
		//Sets status and adds to the arraylist thread

	}

	public void shutdownNow(){
		executor.shutdown();
		//cancels all operations (doesn't interrupt the current one)
		for (Future<?> task : tasks)
			task.cancel(false);
		
		status.stopOperation();	//asks the current operation to stop (nicely)
								//blocks until it does
		
		try {
			executor.awaitTermination(5000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdownNow();	//kills the executor completely
		
		if(debugExecutor != null)	//kills the debug executor
			debugExecutor.shutdownNow();
	}
	
	private void createMonitor(){
		//Creates a Debug version of the ExecutorService
		debugExecutor = Executors.newSingleThreadExecutor();
		debugExecutor.submit(new Runnable() {
			@Override
			public void run() {
				boolean go = true;
				while(go){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						//when this thread is interrupted let's just die gracefully
						go = false;
					}
					System.out.println(status.getCurrentOperation());
				}
			}
		});
		
	}
}
