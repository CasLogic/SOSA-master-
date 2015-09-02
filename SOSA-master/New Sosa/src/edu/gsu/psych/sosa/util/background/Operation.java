package edu.gsu.psych.sosa.util.background;

import java.util.List;


public enum Operation {
	OpenWatermark("Opening:","Looking for SOSA Watermark...", "", "Watermark Found", "Watermark Not Found... Create New One", "","", true),
	SaveWatermark("Saving:","Saving SOSA Watermark...", "",	"Saving: done", "Save Not Successful... See Log for Details", "", "", false),
	Extract ("Extracting:", "Expanding SOSA Archive...",	"",	"Expansion: done",	"",	"Stopping unzip procedure...","Successfully stopped", false),
	Compress("Compressing:","Compressing SOSA Archive...",	"",	"Compression: done","",	"Stopping rezip procedure...","Successfully stopped", false),
	CleanUp ("Cleaning Up:","Cleaning up temporary files...","","Finished Cleaning up!","","","", false),
	NoOperation ();
	
	/**
	 * Special constructor for NoOperation
	 */
	private Operation(){
		initProgress(0, 0, 1);	//the progress bar should remain at 0 for this type of operation
		finished = true;		//if 'stop' is called, should end immediately
	}
	
	private Operation(String shortLabel, String longLabel, String description,
			String completionStatementSuccess, String completionStatementFailure, String beginInterrupt, String endInterrupt, boolean indeterminate){
		this.shortLabel = shortLabel;
		this.longLabel = longLabel;
		this.description = description;
		this.completionStatementSuccess = completionStatementSuccess;
		this.completionStatementFailure = completionStatementFailure;
		this.beginInterrupt = beginInterrupt;
		this.endInterrupt = endInterrupt;
		this.progress.indeterminate = indeterminate;
		//Creates an Operation Constructor that takes and Assigns values for shortlabel, longLabel, description, completionStatementSuccess, completionStatementFailure, beginInterrupt, endInterrupt
		//and indeterminate
	}
	
	private OperationStatus status = null;
	
	private boolean stop = false;
	private boolean finished = false;
	private boolean success = true;
	
	private String description = "";
	private String longLabel = "";	
	private String shortLabel = "";	
	private String completionStatementSuccess = "";
	private String completionStatementFailure = "";
	private String completionStatementFailureAlt = null;
	private String beginInterrupt = "";
	private String endInterrupt = "";
	
	private String subOperationLabel = "";
//	private SOSAOperationProgress subOperationProgress;		//TODO: not yet implemented
	
	public SOSAOperationProgress progress = new SOSAOperationProgress();

	public class SOSAOperationProgress{
		public boolean indeterminate = true;
		public int start, end, current;
		public String toString(){
			return "("+start+","+current+"/"+end+")";
		}
	}
	
	public void expandProgressSize(int size) {
		progress.end = progress.end + size;
		//Increases the Progress by size
	}
	
	public void report(String message){
		if(status != null)
			status.logEntry.offer(message);
		//Returns a boolean value for status if the status isn't null
	}
	
	public void reportError(String type, String message) {
		report(type + " Error: " + message);
		//Creates a report with type of error and a message
	}
	
	public void presentResults(List<String> results) {
		if(status != null)
			status.setFormResults(results);
		//Creates a Object that takes assigns a setFormResults of result
	}
	
	/**
	 * Flags this operation as unsuccessful, and adopts the default
	 * failure statement.
	 * 
	 * <p>This is identical to calling <code>setUnsuccessful(null)</code>.</p>
	 */
	public void setUnsuccessful(){
		setUnsuccessful(null);
	}
	
	/**
	 * Flags this operation as unsuccessful, and additionally 
	 * sets a special alternate failure statement to be displayed
	 * when the default failure statement is not complete enough
	 * or otherwise does not apply.  If <code>null</code> is passed
	 * as the argument then this operation will fall back to the default
	 * failure statement. 
	 * @param customFailureStatement the string to display rather than the default failure statment for this operation
	 */
	public void setUnsuccessful(String customFailureStatement){
		completionStatementFailureAlt = customFailureStatement;
		success = false;
		//Sets as unsuccessful
	}
	
	public void setStatus(OperationStatus status){
		this.status = status;
		//sets status
	}
	
	public void begin(){
		//reinit the progress flags as this may not be the first use of this operation object
		stop = false;
		finished = false;
		success = true;
		
		if(status != null)
			status.setCurrentOperation(this);
	}
	
	public void advance(){
		if(!progress.indeterminate)
			progress.current++;
		//Advance through the progres
	}
	
	/**
	 * Flags this operation to stop as soon as possible.  Waits
	 * until the 'finished' flag is set.
	 * <p>WARNING: only sets a flag. The underlying process must 
	 * recognize this and shutdown on its own.</p>
	 */
	public void stop(){
		stop = true;
		while(!finished);
	}
	
	public boolean isStop(){
		return stop;
	}
	
	/**
	 * Flags that this operation has finished.
	 */
	public void finish(){
		finished = true;		
	}
	
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Initializes the progress indicator
	 * @param start
	 * @param current
	 * @param end
	 */
	public void initProgress(int start, int current, int end) {
		progress.indeterminate = false;
		progress.start = start;
		progress.current = current;
		progress.end = end;
	}
	//This section sets labels and get the various descritions
	public void setSubOperationLabel(String label){
		subOperationLabel = label;
	}
	
	public String getSubOperationLabel(){
		return subOperationLabel;
	}
	
	public String getDescription() {
		return description;
	}

	public String getLongLabel() {
		return longLabel;
	}

	public String getShortLabel() {
		return shortLabel;
	}

	public String getCompletionStatement() {
		if(success)
			return completionStatementSuccess;
		else if(completionStatementFailureAlt != null)
			return completionStatementFailureAlt;
		else
			return completionStatementFailure;
	}

	public String getBeginInterrupt() {
		return beginInterrupt;
	}

	public String getEndInterrupt() {
		return endInterrupt;
	}
	
	public String toString(){
		return super.toString() + "(ID=" + this.hashCode() + ") progress: " + progress + " " + subOperationLabel;	
		//Returns a to string of a enum outlined, the progress and the suboperation label
	}
}
