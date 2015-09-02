package edu.gsu.psych.sosa.util.watermark;

import java.io.File;
import java.io.IOException;

import edu.gsu.psych.sosa.util.background.Operation;
import edu.gsu.psych.sosa.util.background.OperationRunnable;
import edu.gsu.psych.sosa.util.watermark.distro.Distributable;

public class SaveWatermarkOperation extends OperationRunnable {
	
	private Distributable distroFile;
	private WatermarkFile watermark;
	private File saveFile;
	
	public SaveWatermarkOperation(Distributable distroFile, File saveFile, WatermarkFile watermark){
		this.distroFile = distroFile;
		
		this.saveFile = saveFile;
		this.watermark = watermark;
		operation = Operation.SaveWatermark;
		operation.initProgress(0, 0, 1975);	//already determined to be 1975 steps in this process
											//this avoids the 'pointless progress bar' issue that seems
											//always full since the process can't be estimated internally
		this.distroFile.growOperationProgress = false;  //this flag causes the progress bar to not be resized internally
								//if this were set to 'true' and the above set to (0,0,0) then it'd behave as before
	}

	@Override
	protected void task() {
		try {
			distroFile.saveWatermark(saveFile,watermark,operation);
		} catch (IOException e) {
			e.printStackTrace();
			operation.reportError("IO",e.getMessage());
			operation.setUnsuccessful();
		}

	}

}
