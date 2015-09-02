package edu.gsu.psych.sosa.experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAPoint2D;

public class ExperimentLog {

	private Experiment experiment;
	private int lineWidth;	//the number of cells in a row
	private int numLines = 0;
	private StringBuffer log;
	private String outputLocation;
	private List<Move> moves = new ArrayList<Move>();
	private Map<Stimulus, SOSAPoint2D> finalPositions = new HashMap<Stimulus, SOSAPoint2D>();
	
	private List<List<Float>> distanceArray = new ArrayList<List<Float>>();;	
	private String notApplicableValue = "N/A";
	
	private String subjectInitialPresentationTimestamp = "";
	private String subjectExperimentStartTimestamp = "";

	public ExperimentLog(Experiment experiment) {
		this.experiment = experiment;
		lineWidth = Math.max(5, experiment.getStimListUnordered().size() + 1);
		//Logs the Unordered stimList 
	}
	
	public void addMove(Move move){
		moves.add(move);
		finalPositions.put(move.stim, move.destination);	//update the latest position of this stimulus
	}
	
	public void logInitialPresentation() {
		subjectInitialPresentationTimestamp = Move.SOSADateFormat.format(new Date());
		//Sets the Initial timestamp of the of the opening of the Experiment
	}
	
	public void logExperimentStart() {
		subjectExperimentStartTimestamp = Move.SOSADateFormat.format(new Date());
		//Sets the start of the Experiment
	}
	
	public void calcDistances(){
		for (Stimulus first : experiment.getStimListUnordered()) {
			List<Float> distances = new ArrayList<Float>();
			for (Stimulus second : experiment.getStimListUnordered())
				if(first != second && finalPositions.containsKey(first) && finalPositions.containsKey(second))
					distances.add((float)finalPositions.get(first).distance(finalPositions.get(second)));
				else
					distances.add(Float.NaN);
			distanceArray.add(distances);
			//Calculates the position of the Pegs
		}
	}
	
	private void makeLine(String... cells){
		int padding = lineWidth;
		if(cells != null){		//if no cells argument, then make a blank line
			padding -= cells.length;
			if(padding < 0)
				throw new IllegalArgumentException("Too many cells in the given row. Log line: " + numLines);
			for (String cell : cells)
				log.append(clean(cell) + ",");
		}
		while(padding-- > 0)	//number of commas to add to the end	
			log.append(",");
		log.append(System.getProperty("line.separator"));		//end of line
	}
	
	private String clean(String cell){
		if(cell.contains(","))
			return "\"" + cell + "\"";
		return cell;
	}
	
	public String getLog(){
		log = new StringBuffer();
		outputHeader();
		outputOrder();
		outputPresentationTimes();
		outputActions();
		outputFinalPositions();
		outputDistances();
		return log.toString();
		// Outputs the log of the experiment
	}

	private void outputHeader(){
		makeLine("Experiment:",	experiment.name);
		makeLine("Version:",	experiment.version);
		makeLine("Subject:",	experiment.subject);
		makeLine("Date:", 		experiment.date);
		makeLine();
		//Creates the lines of the outputHeader
	}
	
	private void outputOrder(){
		makeLine("Stimuli Presentation Order:");
		makeLine("ID", "Label");
		for(Stimulus stim : experiment.getStimList())
			makeLine(stim.processingID, stim.getLabel());
		makeLine();
		//Creates lines for the output order
	}
	
	private void outputPresentationTimes() {
		makeLine("Subject Initial Preview:", subjectInitialPresentationTimestamp);
		makeLine("Subject Experiment Start:", subjectExperimentStartTimestamp);
		makeLine();
		//Outputs the Initial and Experiment Timestamps
	}
	
	private void outputActions(){
		makeLine("Subject Actions:");
		makeLine("Time", "ID", "Label", "From", "To");
		for(Move move : moves)
			makeLine(move.getLogArray());
		makeLine();
		//Sets the output positions
	}
	
	private void outputFinalPositions(){
		makeLine("Final Positions:");
		makeLine("ID","Label","Position");
		for(Stimulus stim : experiment.getStimListUnordered())
			if(finalPositions.containsKey(stim))
				makeLine(stim.processingID,stim.getLabel(),finalPositions.get(stim).toString());
			else
				makeLine(stim.processingID,stim.getLabel(),notApplicableValue);		//handles the case when the stim was not even added to the board
		makeLine();
		//Outputs the final positions of the pegs/images
	}
	
	private void outputDistances(){
		calcDistances();
		List<String> orderedLabels = new ArrayList<String>(); 
		for(Stimulus stim : experiment.getStimListUnordered())
			orderedLabels.add(stim.getLabel());
		List<String> sectionHeading = new ArrayList<String>();
		sectionHeading.add("Label");
		sectionHeading.addAll(orderedLabels);
		
		makeLine("Distances:");
		makeLine(sectionHeading.toArray(new String[0]));
		for(int i = 0; i < experiment.getStimListUnordered().size(); i++)
			makeLine(outputDistanceRow(i, experiment.getStimListUnordered().get(i).getLabel()));
		makeLine();
		//Outputs the distances of the pegs/images
	}
	
	private String[] outputDistanceRow(int row, String label){
		List<String> rowOutput = new LinkedList<String>();
		for(Float value : distanceArray.get(row))
			if(!value.equals(Float.NaN))
				rowOutput.add(value.toString());
			else
				rowOutput.add(notApplicableValue);
		rowOutput.add(0, label);
		
		return rowOutput.toArray(new String[0]);
	}

	public void setOutputLocation(String file) {
		outputLocation = file;
		//Sets output location
	}

	public String getOuputLocation() {
		return outputLocation;	
		//Returns outputLocation
	}
}
