package edu.gsu.psych.sosa.util.background;

public class OperationStatusGUIUpdater{
	OperationStatus status;
	OperationStatusGUI gui;
	Operation currentOperation;
	
	public OperationStatusGUIUpdater(OperationStatus status, OperationStatusGUI gui){
		this.status = status;
		this.gui = gui;
		//Takes in arguments of OperationStatus and OperationStatusGUI and sets the value
	}
	
	public void update(){
		currentOperation = status.getCurrentOperation();
		if(status.expireNew())
			initGUI();
		
		gui.addLogLine(status.logEntry.poll());
				
		if(status.hasFormResults())
			gui.populate(status.getFormResults());
		
		if(currentOperation != Operation.NoOperation){
			gui.updateOperationProgress(currentOperation.progress);
			gui.setSubOperationLabel(currentOperation.getSubOperationLabel());
			if(currentOperation.isFinished()){
				gui.setOperationLongLabel(currentOperation.getCompletionStatement());
				status.setCurrentOperation(Operation.NoOperation);
			}
		}
	}//Updates the currentOperation and logs the changes an update causes

	private void initGUI() {
		gui.updateOperationProgress(currentOperation.progress);
		gui.setOperationShortLabel(currentOperation.getShortLabel());
		gui.setOperationLongLabel(currentOperation.getLongLabel());
		gui.setOperationDescription(currentOperation.getDescription());
		gui.setSubOperationLabel(currentOperation.getSubOperationLabel());
		// Calls the UpdateOperationProgress and sets the shortLabel, longLabel, Description and the SubOperationLabel
	}
}
