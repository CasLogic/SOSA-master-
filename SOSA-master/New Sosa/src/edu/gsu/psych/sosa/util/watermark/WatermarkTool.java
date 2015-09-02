package edu.gsu.psych.sosa.util.watermark;

import java.io.File;
import java.util.concurrent.Executors;

import edu.gsu.psych.sosa.main.SOSAWindow;
import edu.gsu.psych.sosa.util.background.BackgroundExecutor;
import edu.gsu.psych.sosa.util.background.OperationStatus;
import edu.gsu.psych.sosa.util.watermark.distro.Distributable;



public class WatermarkTool {

	private WatermarkFile watermark;
	private WatermarkGUI gui;
	private BackgroundExecutor executor;
	private OperationStatus status;
	
	private Distributable distroFile;
	
	private boolean shuttingDown = false;
	
    /**
     * Creates a new WatermarkTool
     * 
     * Sets up a background process for
     * logging and creates a new WatermarkGUI.
     *
     * @param None
     * @return An instance of the WatermarkTool class
     */
	public WatermarkTool(){
		status = new OperationStatus();
		executor = new BackgroundExecutor(status);
		gui = new WatermarkGUI(this, status);
	}
	
    /**
     * Checks files that are watermarked
     *
     * Reads in the file passed to it, and runs a
     * background execution task to read it as a watermark file
     *
     * @param fileString Path to the file used for distributing
     * @return None
     */
	public void readWatermark(String fileString){
		distroFile = new Distributable(fileString);
		executor.submit(new ReadWatermarkOperation(distroFile));		
	}
	
    /**
     * Checks if the distributable file is loaded
     *
     * Checks that the file actually exists, and if
     * it does, if it successfully loaded
     *
     * @param None
     * @return Boolean value
     */
	public boolean distroFileLoaded(){
		return distroFile != null && distroFile.distroFileLoaded();
	}
	
    /**
     * Saves files used for watermarking
     *
     * If the watermark file doesn't exist, creates a new one.
     * Gets the input lines from the GUI and then executes
     * a save operation in the background.
     *
     * @param saveFile Path to save
     * @return None
     */
	public void saveWatermark(File saveFile){
		if(watermark == null)
			watermark = new WatermarkFile();
		gui.getInputLines(watermark);
		executor.submit(new SaveWatermarkOperation(distroFile, saveFile, watermark));
	}

    /**
     * Reutns the instantiated SOSAWindow GUI
     *
     * @param None
     * @return SOSAWindow GUI instance
     */
	public SOSAWindow getGUI() {
		return gui;
	}
	
    /**
     * Checks if the program is shutting down
     *
     * @param None
     * @return Boolean value
     */
	public boolean isShuttingDown(){
		return shuttingDown;
	}

    /**
     * Cleans up processes and shuts down
     *
     * Creates a new thread and begins shutting down
     * background processes.  States it is shutting down.
     *
     * @param None
     * @return None
     */
	public void cleanupAndShutdown() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			public void run() {
				status.logEntry.add("Beginning Shutdown and Cleanup (Please do not terminate this program prematurely)");
								
				executor.shutdownNow();
				
				status.logEntry.add("Removing temporary files...");
//				removeTempDir();
				status.logEntry.add("All temporary files removed");
				
				status.logEntry.add("Shutting down...");
				shuttingDown = true;
			}
		});
	}
    
    /**
     * Shuts down the background executor.
     *
     * @param None
     * @return None
     */
	public void shutdown() {
		executor.shutdownNow();
	}
}
