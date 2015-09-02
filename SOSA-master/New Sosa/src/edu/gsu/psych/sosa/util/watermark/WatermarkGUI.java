package edu.gsu.psych.sosa.util.watermark;

import java.io.File;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.gsu.psych.sosa.main.SOSAWindow;
import edu.gsu.psych.sosa.util.background.OperationStatus;
import edu.gsu.psych.sosa.util.background.OperationStatusGUI;
import edu.gsu.psych.sosa.util.background.OperationStatusGUIUpdater;
import edu.gsu.psych.sosa.util.background.Operation.SOSAOperationProgress;


public class WatermarkGUI implements SOSAWindow, OperationStatusGUI {

	private Shell sShell = null;
	private Composite southComposite;
	private Button saveButton;
	private Button doneButton;
	private Label operationTypeLabel;
	private Label currentOperationLabel;
	private Button openJarButton;
	private Label sosaFileStatusLabel;
	private Label WatermarkLine1;
	private Text uniqueIdentifier;
	private Button GenerateCodeButton;
	private Label WatermarkLine2;
	private Text additionalLine1;
	private Label optionalLabel;
	private Text additionalLine2;
	private Label currentMajorOperationLabel;
	private List statusLogList;
	
	private WatermarkTool tool;
//	private UpdateGUIDuringZipProcess zipUpdater;
	private Composite progressBarComposite = null;
	private ProgressBar progressBar1 = null;
	private OperationStatusGUIUpdater statusUpdater;
	
    
    /**
     * Instantiates the WatermarkGUI
     *
     * @param WatermarkTool, OperationStatus
     * @return WatermarkGUI instance
     */
	public WatermarkGUI(WatermarkTool tool, OperationStatus status){
		this.tool = tool;
		statusUpdater = new OperationStatusGUIUpdater(status, this);
		createSShell();
		sShell.open();
	}
    
    /*
     * This method initialized progressBarComposite
     *
     * @param None
     * @return None
     */
	private void createProgressBarComposite() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 5;
		gridLayout2.marginHeight = 0;
		gridLayout2.marginWidth = 0;
		GridData gridData6 = new GridData();
		gridData6.horizontalSpan = 2;
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = true;
		progressBarComposite = new Composite(southComposite, SWT.NONE);
		progressBarComposite.setLayoutData(gridData6);
		progressBarComposite.setLayout(gridLayout2);
		makeNormalProgressBar();
	}
	
    
    /**
     * Creates a new progress bar from 0 to 100
     *
     * @param None
     * @return None
     */
	public void makeNormalProgressBar(){
		makeNormalProgressBar(0, 100);
	}
	
    /*
     * Creates new progress bar
     *
     * If progress bar doesn't exist, or is indeterminate, create
     * a new one with a starting position and maximum length
     *
     * @param Starting position, Process length
     * @return None
     */
	private void makeNormalProgressBar(int processOrigin, int processSize) {
		if(progressBar1 == null || (progressBar1.getStyle() & SWT.INDETERMINATE) != 0)	//if indeterminate style bit is on
			createProgressBar(true, processOrigin, processSize);
		else if(progressBar1.getMaximum() != processSize)
			progressBar1.setMaximum(processSize);
	}
	
    
    /**
     * Create indeterminate progress bar
     *
     * @param None
     * @return None
     */
	public void makeInfiniteProgressBar(){
		if(progressBar1 == null || (progressBar1.getStyle() & SWT.INDETERMINATE) == 0)	//if indeterminate style bit is off
			createProgressBar(false, 0, 100);
	}
	
    /*
     * Checks files that are watermarked
     *
     * Creates a new progress bar of either determinate or
     * indeterminate length based on user input, starting
     * position, and maximum length
     *
     * @param Boolean normal or indeterminate, starting position, max length
     * @return None
     */
	private void createProgressBar(boolean normal,int processOrigin, int processSize){
		if(processOrigin > processSize){	//swap origin and size if they were given backwards
			int temp = processSize;
			processSize = processOrigin;
			processOrigin = temp;
		}
		
		GridData gridData14 = new GridData();
		gridData14.horizontalAlignment = GridData.FILL;
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.verticalAlignment = GridData.CENTER;
		
		if(progressBar1 != null)
			progressBar1.dispose();
		
		int style = SWT.INDETERMINATE;
		if(normal)
			style = SWT.NONE;
		
		progressBar1 = new ProgressBar(progressBarComposite, style);
		progressBar1.setMinimum(processOrigin);
		progressBar1.setMaximum(processSize);
		progressBar1.setLayoutData(gridData14);
		progressBarComposite.layout();
	}

	/**
	 * This method initializes southComposite	
	 *
	 */
	private void createSouthComposite() {
		GridData gridData13 = new GridData();
		gridData13.horizontalSpan = 4;
		gridData13.verticalAlignment = GridData.CENTER;
		gridData13.grabExcessHorizontalSpace = false;
		gridData13.horizontalAlignment = GridData.FILL;
		GridData gridData9 = new GridData();
		gridData9.verticalSpan = 2;
		gridData9.verticalAlignment = GridData.FILL;
		gridData9.horizontalAlignment = GridData.BEGINNING;
		GridData gridData5 = new GridData();
		gridData5.verticalSpan = 2;
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.horizontalAlignment = GridData.BEGINNING;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.horizontalSpacing = 5;
		southComposite = new Composite(sShell, SWT.NONE);
		southComposite.setLayout(gridLayout1);
		createProgressBarComposite();
		southComposite.setLayoutData(gridData13);
		
		saveButton = new Button(southComposite, SWT.NONE);
		saveButton.setText("Save");
		saveButton.setEnabled(false);
		saveButton.setLayoutData(gridData9);
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog fd = new FileDialog(sShell, SWT.SAVE);
				fd.setFilterExtensions(new String[]{"*.zip"});	//"*.jar;*.zip"
				String savePath;
				if((savePath = fd.open()) != null)
					tool.saveWatermark(new File(savePath));					
			}
		});
		doneButton = new Button(southComposite, SWT.NONE);
		doneButton.setText("Done");
		doneButton.setLayoutData(gridData5);
		operationTypeLabel = new Label(southComposite, SWT.NONE);
		operationTypeLabel.setText("Compressing:");
		operationTypeLabel.setVisible(false);
		doneButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				MessageBox mb = new MessageBox(sShell, SWT.YES | SWT.NO);
				mb.setMessage("Cancel all current operations and Quit?");
				mb.setText("Finished?");
				if(mb.open() == SWT.YES){
					makeInfiniteProgressBar();
					tool.cleanupAndShutdown();
				}
			}
		});
		operationTypeLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		operationTypeLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		currentOperationLabel = new Label(southComposite, SWT.CENTER);
		currentOperationLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		currentOperationLabel.setText("");
		currentOperationLabel.setLayoutData(gridData8);
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData12 = new GridData();
		gridData12.horizontalSpan = 4;
		gridData12.verticalAlignment = GridData.CENTER;
		gridData12.heightHint = -1;
		gridData12.horizontalAlignment = GridData.FILL;
		GridData gridData10 = new GridData();
		gridData10.horizontalSpan = 4;
		gridData10.verticalAlignment = GridData.FILL;
		gridData10.grabExcessVerticalSpace = true;
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.horizontalAlignment = GridData.FILL;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.horizontalSpan = 2;
		gridData7.verticalAlignment = GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.END;
		gridData4.horizontalSpan = 2;
		gridData4.verticalAlignment = GridData.CENTER;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.CENTER;
		gridData3.horizontalSpan = 2;
		gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData21 = new GridData();
		gridData21.horizontalIndent = 0;
		gridData21.horizontalAlignment = GridData.CENTER;
		gridData21.verticalAlignment = GridData.CENTER;
		gridData21.horizontalSpan = 2;
		gridData21.heightHint = -1;
		GridData gridData11 = new GridData();
		gridData11.verticalAlignment = GridData.CENTER;
		gridData11.horizontalSpan = 2;
		gridData11.horizontalAlignment = GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessVerticalSpace = false;
		gridData2.heightHint = 30;
		gridData2.horizontalSpan = 2;
		gridData2.verticalAlignment = GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.horizontalSpan = 2;
		gridData1.verticalAlignment = GridData.END;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 5;
		sShell = new Shell();
		sShell.setText("SOSA Watermark Tool");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(525, 309));
		sShell.setMinimumSize(sShell.getSize());
		openJarButton = new Button(sShell, SWT.NONE);
		openJarButton.setText("Open Distributable");
		openJarButton.setLayoutData(gridData7);
		sosaFileStatusLabel = new Label(sShell, SWT.CENTER | SWT.WRAP);
		openJarButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog fd = new FileDialog(sShell);
			//	fd.setFilterNames(new String[]{"SOSA Redistributable"});
				fd.setFilterExtensions(new String[]{"*.zip"}); //"*.jar;*.zip"
				String filePath = fd.open();
				if(filePath != null){
					tool.readWatermark(filePath);
					//	sosaFileStatusLabel.setText(filePath);
					//else
					//	sosaFileStatusLabel.setText("<no file open>");
				}						
			}
		});
		sosaFileStatusLabel.setText("<no file open>");
		sosaFileStatusLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		sosaFileStatusLabel.setLayoutData(gridData2);
		sosaFileStatusLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		WatermarkLine1 = new Label(sShell, SWT.NONE);
		WatermarkLine1.setText("Unique Identifier:");
		WatermarkLine1.setLayoutData(gridData4);
		uniqueIdentifier = new Text(sShell, SWT.BORDER);
		uniqueIdentifier.setLayoutData(gridData);
		GenerateCodeButton = new Button(sShell, SWT.NONE);
		GenerateCodeButton.setText("Generate");
		GenerateCodeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//UUID code = new UUID();
				//uniqueIdentifier.setText(code.toString());
				uniqueIdentifier.setText(UUID.randomUUID().toString());
			}
		});
		WatermarkLine2 = new Label(sShell, SWT.NONE);
		WatermarkLine2.setText("Additional Information:");
		WatermarkLine2.setLayoutData(gridData21);
		additionalLine1 = new Text(sShell, SWT.BORDER);
		additionalLine1.setLayoutData(gridData1);
		optionalLabel = new Label(sShell, SWT.NONE);
		optionalLabel.setText("(optional)");
		optionalLabel.setLayoutData(gridData3);
		additionalLine2 = new Text(sShell, SWT.BORDER);
		additionalLine2.setLayoutData(gridData11);
		currentMajorOperationLabel = new Label(sShell, SWT.CENTER);
		currentMajorOperationLabel.setText("");
		currentMajorOperationLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		currentMajorOperationLabel.setLayoutData(gridData12);
		statusLogList = new List(sShell, SWT.BORDER | SWT.V_SCROLL);
		statusLogList.setLayoutData(gridData10);
		createSouthComposite();

		sShell.addListener(SWT.CLOSE, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				event.doit = false;
				MessageBox mb = new MessageBox(sShell, SWT.YES | SWT.NO);
				mb.setMessage("Cancel all current operations and Quit?");
				mb.setText("Finished?");
				if(mb.open() == SWT.YES)
					tool.cleanupAndShutdown();
			}
		});
	}
    
    /**
     * Updates the status bar in the window
     *
     * Also adds it to the log file
     *
     * @param Status string
     * @return None
     */
	public void setStatusLine(String status){
		if(status != null && !status.equals("")){
			currentMajorOperationLabel.setText(status);
			addLogLine(status);			
		}
	}
	
    
    /**
     * Gets the input lines for the watermark file
     *
     * @param None
     * @return WatermarkFile with input lines
     */
	public WatermarkFile getInputLines(){
		return getInputLines(new WatermarkFile());
	}
    
    /**
     * Sets the watermark lines on the WatermarkFile target
     *
     * @param WatermarkFile target path
     * @return WatermarkFile
     */
	public WatermarkFile getInputLines(WatermarkFile target){
		target.set(uniqueIdentifier.getText(), additionalLine1.getText(), additionalLine2.getText());
		return target;
	}
    
    /**
     * Sets the text for the watermark file
     *
     * If the Watermark file contains text, set
     * the parameters.  Otherwise, create blank lines.
     *
     * @param WatermarkFile
     * @return None
     */
	public void setInputLines(WatermarkFile watermark){
		if(watermark != null){
			uniqueIdentifier.setText(watermark.getUUID());
			additionalLine1.setText(watermark.getAdditionalLine1());
			additionalLine2.setText(watermark.getAdditionalLine2());
		}else{
			uniqueIdentifier.setText("");
			additionalLine1.setText("");
			additionalLine2.setText("");
		}
	}
	
    
    /**
     * Returns the main shell of the watermark
     *
     * @param None
     * @return Main shell
     */
	@Override
	public Shell getMainShell() {
		return sShell;
	}
	
    
    /**
     * Updates the details of the watermark
     *
     * Updates the status, if there is information to watermark, enable
     * the save button, and dispose of the shell if the program is shutting down
     *
     * @param None
     * @return None
     */
	@Override
	public void updateDetails() {
		if(statusUpdater != null)
			statusUpdater.update();
		if(!uniqueIdentifier.getText().equals("") && !additionalLine1.getText().equals("") && tool.distroFileLoaded())
			saveButton.setEnabled(true);
		else
			saveButton.setEnabled(false);
		if(tool.isShuttingDown())
			sShell.dispose();
	}
	
    
    /**
     * Allow the save button to be used
     *
     * @param None
     * @return None
     */
	public void enableSave(){
		saveButton.setEnabled(true);
	}

    
    /**
     * Adds a new log line to the status list
     *
     * @param Log string
     * @return None
     */
	@Override
	public void addLogLine(String logLine) {
		if(logLine != null){
			statusLogList.add(logLine);
			statusLogList.setTopIndex(statusLogList.getItemCount());
		}
	}

	@Override
	public void setOperationDescription(String description) {
		//TODO do something with this		
	}

    /**
     * Sets the long label for the operation
     *
     * @param String long label
     * @return None
     */
	@Override
	public void setOperationLongLabel(String longLabel) {
		setStatusLine(longLabel);
	}
    
    /**
     * Set the short label for the operation
     *
     * @param String short label
     * @return None
     */
	@Override
	public void setOperationShortLabel(String shortLabel) {
		if(!operationTypeLabel.isVisible())
			operationTypeLabel.setVisible(true);
		operationTypeLabel.setText(shortLabel);
	}
    
    /**
     * Sets the subtext label
     *
     * @param String sub label
     * @return None
     */
	@Override
	public void setSubOperationLabel(String subOperationLabel){
		currentOperationLabel.setText(subOperationLabel);
	}
    
    /**
     * Update the progress bar and its type
     *
     * Create either an indeterminate or determinate
     * progress bar, and update its start and end points
     *
     * @param SOSA progress type
     * @return None
     */
	@Override
	public void updateOperationProgress(SOSAOperationProgress progress){
		if(progress.indeterminate)
			makeInfiniteProgressBar();
		else{
			makeNormalProgressBar(progress.start, progress.end);
			progressBar1.setSelection(progress.current);
		}
	}
    
    /**
     * Populates the list with watermark parameters
     *
     * Item 1: SOSA File Status Label
     * Item 2: UUID
     * Item 3: Additional Line 1
     * Item 4: Additional Line 2
     *
     * @param Form Results
     * @return None
     */
	@Override
	public void populate(java.util.List<String> formResults) {
		if(formResults != null && formResults.size() == 4){
			sosaFileStatusLabel.setText(formResults.get(0));
			uniqueIdentifier.setText(formResults.get(1));
			additionalLine1.setText(formResults.get(2));
			additionalLine2.setText(formResults.get(3));
		}else{
			sosaFileStatusLabel.setText("<no file open>");
			uniqueIdentifier.setText("");
			additionalLine1.setText("");
			additionalLine2.setText("");
		}
	}
}
