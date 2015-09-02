package edu.gsu.psych.sosa.util.background;

public class ScheduledPerformance {
	public Delegate perform;
	public long schedule;
	
	public ScheduledPerformance(long performAtTimestamp, Delegate perform){
		this.schedule = performAtTimestamp;
		this.perform = perform;
		//Sets the ScheduledPerformance arguments 
	}

	public boolean isTime() {
		return System.currentTimeMillis() >= schedule;
		//returns a boolean of the schedule being equal to current schedule
	}

	public void perform() {
		perform.perform();
		//Uses the perform from the Delegate interface
		
	}
}
