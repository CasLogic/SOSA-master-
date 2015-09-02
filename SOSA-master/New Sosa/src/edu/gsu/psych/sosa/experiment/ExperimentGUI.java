package edu.gsu.psych.sosa.experiment;

import java.io.IOException;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import com.ardor3d.framework.swt.SwtCanvas;

import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.main.SOSAWindow;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

public class ExperimentGUI implements SOSAWindow, SOSAUpdatable {

	private Shell sShell = null;
	private Composite canvasComposite = null;
	private SwtCanvas experimentCanvas = null;
	private ExperimentCanvasImpl experimentCanvasImpl = null;
	private Composite panelComposite = null;
	private Label stimuliLabel = null;
	private List stimuliList = null;
	private Button finishExperimentButton = null;
	private Button revertCameraButton = null;
	
	private boolean creatorMode = false;
	private ExperimentCreatorGUI creatorWindow = null;
	private Label lblSizeIndicator;
	private boolean canvasAspectRatioLocked = true;
	
	/**
	 * @wbp.parser.constructor
	 */
	public ExperimentGUI(){
		createSShell();
		sShell.pack();
		sShell.setMinimumSize(sShell.getSize());
		sShell.setLocation(0, 0);
		sShell.open();
		
		if(SOSAMain.experiment.isWindowSizeSet)
			sShell.setSize(SOSAMain.experiment.getWindowSizeX(), SOSAMain.experiment.getWindowSizeY());
		
		init();
		
		SOSAMain.experiment.logExperimentStart();
	}
	
	public ExperimentGUI(ExperimentCreatorGUI thisWindow) {
		this.creatorMode = true;
		this.creatorWindow = thisWindow;
		Experiment experiment = thisWindow.getExperiment();
		
		createSShell();
		if(experiment.isWindowSizeSet)
			sShell.setSize(experiment.getWindowSizeX(), experiment.getWindowSizeY());
		else {
			sShell.setSize(600,450);
		}
		sShell.open();
		
		init();
	}

	private void init(){
		if(SOSAMain.experiment != null){
			SOSAMain.experiment.revertAllRenders();		//clears the rotational adjustments made for the preview
			for (Stimulus stim : SOSAMain.experiment.getStimList())
				stimuliList.add(stim.getLabel());
			SOSAMain.experiment.setUpdateInterface(this);	//subscribes to the realtime updates that matter to this interface
		}
	}
	
	@Override
	public Shell getMainShell() {
		return sShell;
	}
	
	@Override
	public void updateDetails() {
		experimentCanvasImpl.getFrameHandler().updateFrame();
	}
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		
		sShell = new Shell();
		if(creatorMode) {
			sShell.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					showCurrentWindowSize();
				}
			});
		}
		if(!creatorMode && SOSAMain.experiment.isWindowSizeSet){
			sShell = new Shell(SWT.CLOSE | SWT.TITLE);
		}	//this causes a crash in windows 7 when capturing the screenshot
		
		sShell.setText("SOSA Modeling Experiment");
		sShell.setLayout(gridLayout2);
		createPanelComposite();
		createCanvasComposite();
	}

	/**
	 * This method initializes experimentCanvas	
	 *
	 */
	private void createExperimentCanvas() {
		final GLData data = new GLData();
		data.depthSize = 8;
		data.doubleBuffer = true;
		
		experimentCanvas = new SwtCanvas(canvasComposite, SWT.NONE, data);
		experimentCanvasImpl = new ExperimentCanvasImpl(experimentCanvas, canvasAspectRatioLocked);
		
		experimentCanvasImpl.registerInput();
	}
	
	private void createSizeIndicator() {
		Composite composite = new Composite(canvasComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		lblSizeIndicator = new Label(composite, SWT.CENTER);
		lblSizeIndicator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSizeIndicator.setFont(new Font(Display.getDefault(), "Tahoma", 14, SWT.NORMAL));
		showCurrentWindowSize();
		//Creates an indicator for the size 
	}
	
	private void showCurrentWindowSize() {
		int x = sShell.getSize().x;
		int y = sShell.getSize().y;
		lblSizeIndicator.setText(x+"x"+y);
	}

	/**
	 * This method initializes canvasComposite	
	 *
	 */
	private void createCanvasComposite() {
		GridData gridData = new GridData();
		gridData.verticalSpan = 1;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 800/8*6;
		gridData.heightHint = 600/8*6;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		canvasComposite = new Composite(sShell, SWT.NONE);
		canvasComposite.setLayout(new FillLayout());
		canvasComposite.setLayoutData(gridData);
		
		if(creatorMode) {
			createSizeIndicator();
		} else {
			createExperimentCanvas();
		}
	}

	/**
	 * This method initializes panelComposite	
	 *
	 */
	private void createPanelComposite() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gridData3 = new GridData();
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = GridData.FILL;
		gridData3.widthHint = 100;
		gridData3.horizontalAlignment = GridData.BEGINNING;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.verticalAlignment = GridData.FILL;
		panelComposite = new Composite(getMainShell(), SWT.NONE);
		panelComposite.setLayoutData(gridData1);
		panelComposite.setLayout(gridLayout);
		stimuliLabel = new Label(panelComposite, SWT.NONE);
		stimuliLabel.setFont(new Font(Display.getDefault(), "Tahoma", 14, SWT.NORMAL));
		stimuliLabel.setLayoutData(gridData2);
		stimuliLabel.setText("Stimuli");
		stimuliList = new List(panelComposite, SWT.NONE);
		stimuliList.setLayoutData(gridData3);
		revertCameraButton = new Button(panelComposite, SWT.NONE);
		revertCameraButton.setText("Revert Camera");
		revertCameraButton.setLayoutData(gridData4);
		revertCameraButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				experimentCanvasImpl.revertCamera();
			}
		});
		stimuliList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				SOSAMain.experiment.selectPeg(stimuliList.getSelectionIndex());
				experimentCanvasImpl.attachStim();
			}
		});
		finishExperimentButton = new Button(panelComposite, SWT.NONE);
		finishExperimentButton.setText("Finish Test");
		finishExperimentButton.setLayoutData(gridData7);
		finishExperimentButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(creatorMode){
					creatorWindow.setExperimentWindowSize(sShell.getSize().x, sShell.getSize().y);
					sShell.dispose();
					return;
				}
				try 
				{
					//Set the location (file name) where to write too
					SOSAMain.experiment.setLogOutputLocation(SOSAMain.experiment.getDirectoryLocation() + "\\" + SOSAMain.experiment.subject + ".csv");
					SOSAMain.experiment.writeLog();     //write the log
					//  sShell.pack();
					//Take a screen shot
					experimentCanvasImpl.makeScreenShot();
					SOSAMain.endOfExperiment = true;    //begin end procedures
				} catch (IOException e1)
				{
					FileDialog file = new FileDialog(sShell, SWT.SAVE);
					file.setFilterExtensions(new String[] {"*.csv"});
					file.setText("Unable to Write Log, Please Select a Valid Location");
					String fileName = file.open();
					if(fileName != null){
						SOSAMain.experiment.setLogOutputLocation(fileName);
						widgetSelected(e);  //try again
					}
				}
			}
		});
	}

	@Override
	public void updateList() {
//		stimuliList.removeAll();
//		for (Stimulus stim : SOSAMain.experiment.getStimList())
//			stimuliList.add(stim.label);
		stimuliList.select(SOSAMain.experiment.getCurrentPegIndex());		
	}

	@Override
	public void updateListActivePeg() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOrderDropdown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sets the cursor on the canvas composite to the requested cursor.
	 * The accepted cursor options are:
	 * <pre>     ExperimentCanvasImpl.CURSOR_NORMAL
	 *     ExperimentCanvasImpl.CURSOR_GRABBER
	 *     ExperimentCanvasImpl.CURSOR_GRABBING</pre>
	 * All other given variables will throw an Illegal Argument Exception.
	 * 
	 * @param cusorType The requested cursor.
	 * @throws IllegalArgumentException If the given cursor does not match the available cursor choices.
	 */
	@Override
	public void updateCursor(int cusorType) throws IllegalArgumentException{
		// Initialize the variable to contain the new cursor.
		Cursor cursor = null;
		// Set the cursor with the requested image.
		switch(cusorType){
		case SOSAUpdatable.CURSOR_NORMAL:
			cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_ARROW);
			break;
		case SOSAUpdatable.CURSOR_GRABBER:
			cursor = new Cursor(Display.getCurrent(), new ImageData(getClass().getClassLoader().getResourceAsStream("edu/gsu/psych/sosa/images/grab.gif" )), 20, 15);
			break;
		case SOSAUpdatable.CURSOR_GRABBING:
			cursor = new Cursor(Display.getCurrent(), new ImageData(getClass().getClassLoader().getResourceAsStream("edu/gsu/psych/sosa/images/grabbing.gif" )), 20, 15);
			break;
		default: 
			throw new IllegalArgumentException("Unrecognized cursor type.");
		}
		
		canvasComposite.setCursor(cursor);		  // Set the cursor in the canvas.
	}
}
