package edu.gsu.psych.sosa.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.eclipse.swt.widgets.Display;

import com.ardor3d.image.util.AWTImageLoader;

import edu.gsu.psych.sosa.experiment.Experiment;
import edu.gsu.psych.sosa.experiment.ExperimentCreatorGUI;
import edu.gsu.psych.sosa.experiment.ExperimentGUI;
import edu.gsu.psych.sosa.experiment.ExperimentPreviewGUI;
import edu.gsu.psych.sosa.experiment.ExperimentPreviewPromptGUI;
import edu.gsu.psych.sosa.experiment.ExperimentPreviewSubjectGUI;
import edu.gsu.psych.sosa.util.background.BackgroundWait;
import edu.gsu.psych.sosa.util.background.Delegate;
import edu.gsu.psych.sosa.util.watermark.WatermarkTool;

public enum SOSAMain {
	LAUNCHER,
	EXPERIMENT_CREATOR,
	EXPERIMENT_PREVIEW,
	EXPERIMENT_PREVIEW_SUBJECT_PROMPT,
	EXPERIMENT_PREVIEW_SUBJECT,
	EXPERIMENT_MAIN,
	WATERMARK;
	
	public static final boolean DEBUG = false;  

	private static Display display = Display.getDefault();
	private static SOSAWindow activeWindow;
	public static Experiment experiment;
	
	private static WatermarkTool tool;
	
	public static boolean endOfExperiment = false;
	public static boolean cameraReady	= false;
	public static boolean shutdownReady = false;
	
	private static Map<Future<?>, Delegate> waitingTasks = new HashMap<Future<?>, Delegate>();
//	private static Map<UUID, ScheduledPerformance> waitingTasks2 = new HashMap<UUID, ScheduledPerformance>();
	
	public static void main(String[] args) {
		File swtJar = new File(LibraryLoader.getArchSwt());
		System.out.println(swtJar.toString());
		AWTImageLoader.registerLoader();

		if(args.length > 0 && args[0].equals("watermark"))
			setNewActiveWindow(WATERMARK);
		else
			setNewActiveWindow(LAUNCHER);
		
		while (!activeWindow.getMainShell().isDisposed()) {
			activeWindow.updateDetails();
			
			display.readAndDispatch();
			
			if(!waitingTasks.isEmpty())
				performReadyTasks();
//			if(!waitingTasks2.isEmpty())
//				performReadyTasks2();
			
			if (endOfExperiment){
				if(shutdownReady)	//end program
					activeWindow.getMainShell().dispose();
			}
			Thread.yield();
		}
		display.dispose();
		
		if(tool != null)
			tool.shutdown();
		
		System.exit(0);
	}
	
	/**
	 * Sets a future (key) to a background task that waits the time of milli
	 * Puts the pair (key, perform) in a waitingTasks Map
	 * @param long milli
	 * @param Delagate perform
	 * @return Future<?> key
	 */
	public static Future<?> performAfterWait(long milli, Delegate perform ) {
		Future<?> key = BackgroundWait.Wait(milli);
		waitingTasks.put(key, perform);
		return key;
	}
	
//	public static UUID performAfterWait2(long milli, Delegate perform){
//		long currentTime = System.currentTimeMillis();
//		long scheduleTime = currentTime + milli;
//		UUID key = UUID.randomUUID();
//		waitingTasks2.put(key, new ScheduledPerformance(scheduleTime, perform));
//		return key;
//	}
	
	/**
	 * Interrupts the task for interruptIfExists and removes interruptIfExists from task Map
	 * @param long milli
	 * @param Delagate perform
	 * @param Future<?> interruptIfExists
	 * @return performAfterWait(milli, perform)
	 */
	public static Future<?> performAfterWait(long milli, Delegate perform, Future<?> interruptIfExists){
		if(DEBUG)
			System.out.println("interrupting task...");
		waitingTasks.remove(interruptIfExists);
		return performAfterWait(milli, perform);
	}
	
//	public static UUID performAfterWait2(long milli, Delegate perform, UUID interruptIfExists){
//		if(DEBUG)
//			System.out.println("interrupting task... " + interruptIfExists);
//		waitingTasks2.remove(interruptIfExists);
//		return performAfterWait2(milli, perform);
//	}
	
	/**
	 * Makes a list of Future<?> and sets to array list and performs done tasks
	 */
	private static void performReadyTasks() {
		List<Future<?>> doneTasks = new ArrayList<Future<?>>();
		// For each Map entry task in waitingTasks, if task is done, add to doneTasks
		for ( Map.Entry<Future<?>, Delegate> task : waitingTasks.entrySet()) {
			if(task.getKey().isDone()) {
				doneTasks.add(task.getKey());
			}
		}
		
		// For each Future<?> in doneTasks, perform the future and remove it from waitingTasks
		for (Future<?> future : doneTasks) {
			waitingTasks.get(future).perform();
			waitingTasks.remove(future);
		}
	}
	
//	private static void performReadyTasks2(){
//		List<UUID> doneTasks = new ArrayList<UUID>();
//		for ( Map.Entry<UUID, ScheduledPerformance> task : waitingTasks2.entrySet()) {
//			if(task.getValue().isTime()) {
//				doneTasks.add(task.getKey());
//			}
//		}
//		
//		for (UUID id : doneTasks) {
//			ScheduledPerformance temp = waitingTasks2.get(id);
//			if(temp != null)
//				temp.perform();
//			else
//				System.out.println("race condition detected...");
//			waitingTasks2.remove(id);
//		}
//	}
	
	/**
	 * This sets the new active window based on which window is chosen
	 * @param SOSAMain nextWindow
	 * @param args
	 */
	public static void setNewActiveWindow(SOSAMain nextWindow, Object ...args){
		switch (nextWindow) {
		case LAUNCHER:
			activeWindow = new LauncherGUI();
			break;
		case EXPERIMENT_CREATOR:
			activeWindow = new ExperimentCreatorGUI();
			break;
		case EXPERIMENT_MAIN:
			activeWindow = new ExperimentGUI();
			break;
		case EXPERIMENT_PREVIEW:
			activeWindow = new ExperimentPreviewGUI();
			break;
		case EXPERIMENT_PREVIEW_SUBJECT_PROMPT:
			activeWindow = new ExperimentPreviewPromptGUI();
			break;
		case EXPERIMENT_PREVIEW_SUBJECT:
			activeWindow = new ExperimentPreviewSubjectGUI();
			break;
		case WATERMARK:
			activeWindow = getTool().getGUI();
			break;
		default:
			break;
		}
	}
	 /**
	  * If tool is null, set to new watermark tool
	  * @return WatermarkTool tool
	  */
	private static WatermarkTool getTool(){
		if(tool == null)
			tool = new WatermarkTool();
		return tool;
	}
}