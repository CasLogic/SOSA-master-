package edu.gsu.psych.sosa.util.background;


public abstract class OperationRunnable implements Runnable{
	protected Operation operation;
	
	protected void initOperation(OperationStatus status){
		operation.setStatus(status);
		//Sets the operation status 
		
	}
	
	@Override
	public void run() {
		operation.begin();
		task();
		operation.finish();
	}
	protected abstract void task();
	
	/**
	 * Convenience extension that handles the common scenario
	 * of needing to queue something for execution without making
	 * it visible to the GUI
	 */
	public static abstract class Basic extends OperationRunnable{
		@Override
		protected void initOperation(OperationStatus status){
			operation = Operation.NoOperation;
			super.initOperation(status);
		}
	}
	
	/**
	 * Extension on the runnable operation that includes a prerequisite
	 * function.  If this function returns false then the normal operation
	 * will not occur.
	 */
	public static abstract class WithPrereq extends OperationRunnable{
		protected abstract boolean prereqTask();
		
		@Override
		public void run(){
			if(prereqTask())
				super.run();
		}
	}
}
