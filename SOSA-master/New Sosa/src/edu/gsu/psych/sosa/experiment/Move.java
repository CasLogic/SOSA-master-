package edu.gsu.psych.sosa.experiment;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAPoint2D;

public class Move {
	
	static SimpleDateFormat SOSADateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	
	private String timestamp;
	public Stimulus stim;
	public SOSAPoint2D origin;
	public SOSAPoint2D destination;
	
	public Move(Stimulus stim, SOSAPoint2D origin, SOSAPoint2D destination){
		this.stim = stim;
		this.origin = origin;
		this.destination = destination;
		this.timestamp = SOSADateFormat.format(new Date()); 
		//Sets Constructor of Move to set the Stimulus, the origin, destination and the timestamp
	}

	public String[] getLogArray() {
		String[] output = new String[5];
		output[0] = timestamp;
		output[1] = stim.processingID;
		output[2] = stim.getLabel();
		output[3] = origin.toString();
		output[4] = destination.toString();
		return output;
		//Returns an array that holds the timestamp, the Stimulus ID , the Stimulus Label,  the Origin
		// and the Destination
	}
}
