package edu.gsu.psych.sosa.util.background;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class OperationStatus {
	public Queue<String> logEntry = new ConcurrentLinkedQueue<String>();
//	private boolean hasNewLogEntry = false;
	private Operation currentOperation = Operation.NoOperation;
	private boolean isNewOperation = false;
	private List<String> formResults;
	private boolean haveFormResults = false;
	
	public boolean isNewOperation(){
		return isNewOperation;
	}
	
	/**
	 * Marks the current operation as not new and returns whether
	 * this was necessary or not (old value of isNewLogEntry)
	 * @return true if the current operation was marked as new, false otherwise
	 */
	public boolean expireNew(){
		if(isNewOperation){
			isNewOperation = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Obtains the current operation.
	 * <p>NOTE: does not mark as read (or not 'new').
	 * For that functionality use:
	 * <br>{@code getCurrentOperation(SOSAOperation operation)}</br></p>
	 * @return the current operation
	 */
	public Operation getCurrentOperation() {
		return currentOperation;
	}
	
	/**
	 * Sets the current operation and flags it as 'new' (never been read externally)
	 * @param newOperation the operation to set as current
	 */
	public void setCurrentOperation(Operation newOperation){
		currentOperation = newOperation;
		isNewOperation = true;
		
	}

	public void stopOperation() {
		currentOperation.stop();
	}
	
	public boolean hasFormResults(){
		return haveFormResults;
		//Returns True or false based on the haveFormResult
	}
	
	public List<String> getFormResults(){
		haveFormResults = false;	//indicate calling process not to call again
		return formResults;
	}
	
	public void setFormResults(List<String> results){
		formResults = results;
		haveFormResults = true;
		//sets results as formResults and haveFormResults to true
	}
}
