package edu.gsu.psych.sosa.main;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;

public class LauncherGUI implements SOSAWindow {

	private Shell sShell = null;
	private Label welcomeLabel = null;
	private Label runExperimentLabel = null;
	private Label runCreatorLabel = null;
	private Button runExperimentButton = null;
	private Button runCreatorButton = null;
	private List loadedStimulousList = null;
	private Button loadStimulusSetButton = null;
	private Label label = null;
	
	/**
	 * Creates and opens a new sShell
	 */
	public LauncherGUI(){
		createSShell();
		sShell.open();
	}
	
	@Override
	public Shell getMainShell(){
		return sShell;
	}
	
	@Override
	public void updateDetails() {
		//nothing to update		
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData9 = new GridData();
		gridData9.horizontalSpan = 3;
		gridData9.horizontalIndent = 50;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.CENTER;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.CENTER;
		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = false;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.horizontalAlignment = GridData.FILL;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.END;
		gridData3.heightHint = -1;
		gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.END;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.verticalSpan = 2;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.verticalAlignment = GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalSpan = 3;
		gridData.horizontalIndent = 50;
		gridData.horizontalAlignment = GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		sShell = new Shell();
		sShell.setText("SOSA Data Modeler");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(547, 246));
		welcomeLabel = new Label(sShell, SWT.NONE);
		welcomeLabel.setText("Welcome to the SOSA data modeling software");
		welcomeLabel.setLayoutData(gridData);
		label = new Label(sShell, SWT.NONE);
		label.setText("You may now choose your mode, click the appropriate button for the mode you desire.");
		label.setLayoutData(gridData9);
		runExperimentLabel = new Label(sShell, SWT.NONE);
		runExperimentLabel.setText("Click here to run experiment");
		runExperimentLabel.setLayoutData(gridData2);
		runExperimentButton = new Button(sShell, SWT.NONE);
		runExperimentButton.setText("Run Experiment");
		runExperimentButton.setLayoutData(gridData5);
		runExperimentButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// check if experiment is loaded when user clicks run experiment,
				if(SOSAMain.experiment != null){
					SOSAMain.setNewActiveWindow(SOSAMain.EXPERIMENT_PREVIEW);
					sShell.dispose();
				}
				// if not, displays prompt saying so
				else{
				//	getLoadErrorPopup().open();
					MessageBox mb = new MessageBox(sShell, SWT.NONE);
					mb.setMessage("There is no experiment loaded\nPlease load an experiment file to continue");
					mb.setText("No Experiment Loaded");
					mb.open();
				}
			}
		});
		loadStimulusSetButton = new Button(sShell, SWT.NONE);
		loadStimulusSetButton.setText("Load Stimulus Set");
		loadStimulusSetButton.setLayoutData(gridData4);
		loadStimulusSetButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// Updates the stimulus list once the user loads a .sosa file and displays in stimulus list box
				FileDialog file = new FileDialog(sShell, SWT.OPEN);
				file.setFilterExtensions(new String[] {"*.sosa"});
				try {
					SOSAMain.experiment = SOSAFileHandler.readExperimentFile(file.open());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				if(SOSAMain.experiment != null)
					updateStimList();
			}
		});
		runCreatorLabel = new Label(sShell, SWT.NONE);
		runCreatorLabel.setText("Click here to create a stimulus set");
		runCreatorLabel.setLayoutData(gridData3);
		runCreatorButton = new Button(sShell, SWT.NONE);
		runCreatorButton.setText("Create Stimulus Set");
		runCreatorButton.setLayoutData(gridData6);
		runCreatorButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						// dispose of sShell once the user clicks run experiment
						SOSAMain.setNewActiveWindow(SOSAMain.EXPERIMENT_CREATOR);
						sShell.dispose();
					}
				});
		loadedStimulousList = new List(sShell, SWT.NONE);
		loadedStimulousList.setLayoutData(gridData1);
	}
	
	/**
	 * Removes all currently loaded stimuli and adds their name to loaded list
	 */
	private void updateStimList() {
		// removes all stimuli in loadedStimulousSet
		loadedStimulousList.removeAll();
		// for each stimulus in loaded experiment, ads stimuli name to stim list
		for (Stimulus stim : SOSAMain.experiment.getStimList())
			loadedStimulousList.add(stim.getLabel());	
	}
}
