package edu.gsu.psych.sosa.util.watermark.distro;

import java.util.ArrayList;
import java.util.List;

public class ManifestEntry{
	private final static String OPTIONAL_TAG = " <<optional>>";
	private final static String NODISTRO_TAG = " <<noDistro>>";
	
	private String entry;
	private boolean optional = false;
	private boolean notForDistribution = false;
	public ManifestEntry(String entryLine) {
		parse(entryLine);
	}
	
	private void parse(String input){
		if(input.indexOf(OPTIONAL_TAG) > -1){
			input = input.replaceFirst(OPTIONAL_TAG, "");
			optional = true;
		}
		if(input.indexOf(NODISTRO_TAG) > -1){
			input = input.replaceFirst(NODISTRO_TAG, "");
			notForDistribution = true;
		}
		entry = input;
	}
	
	public String getEntry(){
		return entry;
	}
	
	public boolean isOptional(){
		return optional;
	}
	
	public boolean isNotForDistribution(){
		return notForDistribution;
	}
	
	public String toString(){
		return entry + (optional ? OPTIONAL_TAG : "") + (notForDistribution ? NODISTRO_TAG : "");
	}

	public static List<ManifestEntry> buildList(List<String> entryListing) {
		List<ManifestEntry> output = new ArrayList<ManifestEntry>(entryListing.size());
		for (String entryString : entryListing)
			output.add(new ManifestEntry(entryString));
		return output;
	}
}