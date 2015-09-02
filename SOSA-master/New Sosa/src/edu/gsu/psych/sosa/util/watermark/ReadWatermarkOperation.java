package edu.gsu.psych.sosa.util.watermark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.gsu.psych.sosa.util.background.Operation;
import edu.gsu.psych.sosa.util.background.OperationRunnable;
import edu.gsu.psych.sosa.util.watermark.distro.Distributable;

/**
 * Background task that validates the given file as a SOSA
 * Redistributable (via internal manifest) before looking for
 * a watermark file embedded in the sosa.jar executable.
 * 
 * Reports any failures, and offers the name of the given distroFile
 * along with the contents of the watermark file as results.
 */
public class ReadWatermarkOperation extends OperationRunnable {
	
	private Distributable distroFile;
	private WatermarkFile watermark;
	private List<String> results = new ArrayList<String>();
	
	public ReadWatermarkOperation(Distributable distroFile){
		this.distroFile = distroFile;
		operation = Operation.OpenWatermark;
		results.add(this.distroFile.getName());
	}

	@Override
	protected void task(){
		if(distroFile.validate()){
			try {
				watermark = distroFile.getWatermark();
			} catch (IOException e) {
				e.printStackTrace();
				operation.reportError("IO",e.getMessage());
			}
			if(watermark == null){
				operation.setUnsuccessful();
				results.addAll((new WatermarkFile()).toStringList());
				operation.presentResults(results);
			}else{
				results.addAll(watermark.toStringList());
				operation.presentResults(results);
			}
		}else{
			operation.setUnsuccessful("Not a valid SOSA archive, file not opened");
			operation.presentResults(null);
		}
	}
}
