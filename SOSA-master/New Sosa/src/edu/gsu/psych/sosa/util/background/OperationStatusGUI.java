package edu.gsu.psych.sosa.util.background;

import java.util.List;

import edu.gsu.psych.sosa.util.background.Operation.SOSAOperationProgress;

public interface OperationStatusGUI {
	
	void updateOperationProgress(SOSAOperationProgress progress);
	
	void setOperationShortLabel(String shortLabel);
	void setOperationLongLabel(String longLabel);
	void setOperationDescription(String description);
	void setSubOperationLabel(String subOperationLabel);
	
	void addLogLine(String logLine);

	void populate(List<String> formResults);	
}
