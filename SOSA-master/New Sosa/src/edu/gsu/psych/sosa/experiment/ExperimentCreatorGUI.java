package edu.gsu.psych.sosa.experiment;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ardor3d.framework.swt.SwtCanvas;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAFileHandler;
import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.main.SOSASimpleUpdatable;
import edu.gsu.psych.sosa.main.SOSATexture;

public class ExperimentCreatorGUI implements SOSAWindowExperiment, SOSAUpdatable{

	private Shell sShell = null;
	private Composite experimentSettingsComposite = null;
	private Button newExperimentButton = null;
	private Composite canvasComposite = null;
	private Composite pegAreaComposite = null;
	private Combo pegArrangementCombo = null;
	private Composite pegSettingsAreaComposite = null;
	private Composite pegListComposite = null;
	private List pegList = null;
	private Button openFileButton = null;
	private Button saveSetButton = null;
	private Label fileLoadedLabel = null;
	private Label fileLoadedNameLabel = null;
	private Group group = null;
	private Label experimentNameLabel = null;
	private Text experimentNameText = null;
	private Label experimentVersionLabel = null;
	private Text experimentVersionText = null;
	private Group pegSettingsGroup = null;
	private Label labelLabel = null;
	private Text labelText = null;
	private Label procIDLabel = null;
	private Text processingIDText = null;
	private Group fileGroup = null;
	private Button loadButton = null;
	private Button saveButton = null;
	private Group typeGroup = null;
	private Button moveUpButton = null;
	private Button moveDownButton = null;
	private SwtCanvas creatorPreviewCanvas;
	private ExperimentCreatorPreviewCanvasImpl creatorPreviewCanvasImpl;
	private Composite orderButtonsComposite = null;
	private Button newArrangementButton = null;
	private Button removeArrangementButton = null;
	private Composite addRemovePegButtonsComposite = null;
	private Button addPegButton = null;
	private Button removePegButton = null;
	
	private Experiment experiment;
	private Composite pegListCompositeTop = null;
	private Button typePegRadioButton = null;
	private Button typeImageRadioButton = null;
	private Composite appearanceComposite = null;
	private ExperimentCreatorPegComposite appearanceCompositePeg;
	private ExperimentCreatorImageComposite appearanceCompositeImage;
	private SOSASimpleUpdatable currentAppearanceComposite;
	private Label boardTextureFileName;
	private Button btnShowRelativeStimulus;
	
	private ExperimentCreatorGUI thisWindow = this;
	private Label lblwindowSizeDisplay;
	
	public ExperimentCreatorGUI(){
		createSShell();
		
		sShell.pack();
		sShell.setMinimumSize(sShell.getSize());
		sShell.open();
	}
	
	private void createNewExperiment(){
		experiment = new Experiment();
		experiment.setUpdateInterface(this);
		
		experiment.name = experimentNameText.getText();
		experiment.version = experimentVersionText.getText();
	}
	
	private void loadNewExperiment(Experiment experimentIn) {
		if(experimentIn != null){
			experiment = experimentIn;
			experimentNameText.setText(experiment.name);
			experimentVersionText.setText(experiment.version);			
			experiment.setUpdateInterface(this);
			experiment.initLabels();
		}
	}
	
	/**
	 * This method initializes sShell	
	 *
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 2;
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginWidth = 6;		//set abnormally high on purpose
		gridLayout.makeColumnsEqualWidth = false;
		sShell = new Shell();
		sShell.setText("Experiment Creator");		
		sShell.setSize(new Point(676, 488));
		sShell.setLayout(gridLayout);
		createExperimentSettingsComposite();
		
		createCanvasComposite();
		createPegSettingsAreaComposite();
		
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				SOSAMain.setNewActiveWindow(SOSAMain.LAUNCHER);
			}
		});
		
	}

	/**
	 * This method initializes experimentSettingsComposite	
	 *
	 */
	private void createExperimentSettingsComposite() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.FILL;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.widthHint = 120;
		gridData2.horizontalAlignment = GridData.END;
		GridData gridData11 = new GridData();
		gridData11.verticalSpan = 2;
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.horizontalSpan = 1;
		gridData11.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 3;
		gridLayout3.marginHeight = 2;
		gridLayout3.verticalSpacing = 2;
		experimentSettingsComposite = new Composite(sShell, SWT.NONE);
		experimentSettingsComposite.setLayout(gridLayout3);
		experimentSettingsComposite.setLayoutData(gridData11);
		createGroup();
		newExperimentButton = new Button(experimentSettingsComposite, SWT.NONE);
		newExperimentButton.setText("New");
		newExperimentButton.setLayoutData(gridData3);
		newExperimentButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createNewExperiment();
			}
		});
		openFileButton = new Button(experimentSettingsComposite, SWT.NONE);
		openFileButton.setText("Load");
		openFileButton.setLayoutData(gridData4);
		openFileButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog file = new FileDialog(sShell, SWT.OPEN);
				file.setFilterExtensions(new String[] {"*.sosa"});
				try {
					loadNewExperiment(SOSAFileHandler.readExperimentFile(file.open()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(file.getFileName().length() > 0)
					fileLoadedNameLabel.setText(file.getFileName());
			}
		});
		fileLoadedLabel = new Label(experimentSettingsComposite, SWT.NONE);
		fileLoadedLabel.setText("Current Open File:");
		fileLoadedNameLabel = new Label(experimentSettingsComposite, SWT.NONE);
		fileLoadedNameLabel.setText("No File Selected");
		fileLoadedNameLabel.setAlignment(SWT.RIGHT);
		fileLoadedNameLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		fileLoadedNameLabel.setLayoutData(gridData2);
		saveSetButton = new Button(experimentSettingsComposite, SWT.NONE);
		saveSetButton.setText("Save");
		saveSetButton.setLayoutData(gridData5);
		saveSetButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog file = new FileDialog(sShell, SWT.SAVE);
				file.setFilterExtensions(new String[] {"*.sosa"});
				try {
					SOSAFileHandler.writeFile(experiment, file.open());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * This method initializes canvasComposite	
	 *
	 */
	private void createCanvasComposite() {
		
		Group grpBoardSettings = new Group(sShell, SWT.NONE);
		grpBoardSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpBoardSettings.setLayout(new GridLayout(3, false));
		grpBoardSettings.setText("Board Settings");
		
		Label lblFileName = new Label(grpBoardSettings, SWT.NONE);
		lblFileName.setText("File Name:");
		
		boardTextureFileName = new Label(grpBoardSettings, SWT.NONE);
		boardTextureFileName.setAlignment(SWT.RIGHT);
		boardTextureFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		boardTextureFileName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		boardTextureFileName.setText("No File Selected");
		
		Button btnBrowse = new Button(grpBoardSettings, SWT.NONE);
		btnBrowse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(sShell);
				fd.setText("Select texture file for Experiment Board");
				fd.setFilterExtensions(new String[]{"*.jpg;*.png"});
				String temp = fd.open();
				if(temp != null){
					File file = new File(temp);
					boardTextureFileName.setText(file.getName());
					//getExperiment().setBoardTexture(file);
					try {
						getExperiment().setBoardTexture(new SOSATexture(file));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnBrowse.setText("Browse");
		
		btnShowRelativeStimulus = new Button(grpBoardSettings, SWT.CHECK);
		btnShowRelativeStimulus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				creatorPreviewCanvasImpl.showRelativeStim(btnShowRelativeStimulus.getSelection());
			}
		});
		btnShowRelativeStimulus.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 2, 1));
		btnShowRelativeStimulus.setText("Show Stimulus at relative size");
		
		Button btnRevert = new Button(grpBoardSettings, SWT.NONE);
		btnRevert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boardTextureFileName.setText("No File Selected");
				//getExperiment().setBoardTexture(null);
				getExperiment().resetBoardTexture();
			}
		});
		btnRevert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRevert.setText("Revert");
		
		Group grpDisplay = new Group(sShell, SWT.NONE);
		grpDisplay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpDisplay.setText("Display");
		grpDisplay.setLayout(new GridLayout(3, false));
		
		Label lblWindowSizeLabel = new Label(grpDisplay, SWT.NONE);
		lblWindowSizeLabel.setText("Window Size:");
		
		lblwindowSizeDisplay = new Label(grpDisplay, SWT.NONE);
		GridData gd_lblwindowSizeDisplay = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblwindowSizeDisplay.minimumWidth = 200;
		lblwindowSizeDisplay.setLayoutData(gd_lblwindowSizeDisplay);
		lblwindowSizeDisplay.setText("600x450");
		
		Button btnSet = new Button(grpDisplay, SWT.NONE);
		btnSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ExperimentGUI(thisWindow);
			}
		});
		btnSet.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSet.setText("Set");
		GridData gridData10 = new GridData();
		gridData10.horizontalSpan = 2;
		gridData10.heightHint = 300;		
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.horizontalAlignment = GridData.FILL;
		canvasComposite = new Composite(sShell, SWT.NONE);
		canvasComposite.setLayoutData(gridData10);		
		createPreviewCanvas();
		canvasComposite.setLayout(new FillLayout());
		
	}
	
	private void createPreviewCanvas(){
		final GLData data = new GLData();
		data.depthSize = 8;
		data.doubleBuffer = true;
		
		creatorPreviewCanvas = new SwtCanvas(canvasComposite, SWT.NONE, data);
		creatorPreviewCanvasImpl = new ExperimentCreatorPreviewCanvasImpl(creatorPreviewCanvas,this);
	}
	
	/**
	 * This method initializes pegSettingsAreaComposite	
	 *
	 */
	private void createPegSettingsAreaComposite() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData12 = new GridData();
		gridData12.horizontalSpan = 2;
		gridData12.verticalAlignment = GridData.FILL;
		gridData12.grabExcessVerticalSpace = true;
		gridData12.horizontalAlignment = GridData.FILL;
		pegAreaComposite = new Composite(sShell, SWT.NONE);
		createPegListComposite();
		pegAreaComposite.setLayout(gridLayout4);
		pegAreaComposite.setLayoutData(gridData12);
		createPegSettingsComposite();
	}
	
	@Override
	public void updateList() {
		pegList.removeAll();
		for (Stimulus stim : getExperiment().getStimList())
			pegList.add(stim.getLabel());
		updatePegSettingsArea();
		pegList.select(getExperiment().getCurrentPegIndex());
	}
	
	@Override
	public void updateListActivePeg() {
		pegList.setItem(getExperiment().getCurrentPegIndex(), getExperiment().getSelectedPeg().getLabel());
		getExperiment().getSelectedPeg().resetLabelRender();	//force the label to update
	}

	/**
	 * This method initializes pegArrangementCombo	
	 *
	 */
	private void createPegArrangementCombo() {
		GridData gridData17 = new GridData();
		gridData17.horizontalAlignment = GridData.FILL;
		gridData17.grabExcessHorizontalSpace = true;
		gridData17.widthHint = 80;
		gridData17.verticalAlignment = GridData.CENTER;
		pegArrangementCombo = new Combo(pegListCompositeTop, SWT.DROP_DOWN);
		pegArrangementCombo.setLayoutData(gridData17);
		pegArrangementCombo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().setOrder(pegArrangementCombo.getSelectionIndex());
			}
		});
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createPegSettingsComposite() {
		GridData gridData14 = new GridData();
		gridData14.horizontalAlignment = GridData.FILL;
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.grabExcessVerticalSpace = true;
		gridData14.verticalAlignment = GridData.FILL;
		pegSettingsAreaComposite = new Composite(pegAreaComposite, SWT.NONE);
		pegSettingsAreaComposite.setLayout(new GridLayout());
		createPegSettingsGroup();
		pegSettingsAreaComposite.setLayoutData(gridData14);
	}

	/**
	 * This method initializes pegListComposite	
	 *
	 */
	private void createPegListComposite() {
		GridData gridData25 = new GridData();
		gridData25.horizontalAlignment = GridData.CENTER;
		gridData25.grabExcessVerticalSpace = true;
		gridData25.verticalAlignment = GridData.BEGINNING;
		GridData gridData24 = new GridData();
		gridData24.horizontalAlignment = GridData.CENTER;
		gridData24.grabExcessVerticalSpace = true;
		gridData24.verticalAlignment = GridData.END;
		GridData gridData15 = new GridData();
		gridData15.grabExcessVerticalSpace = true;
		gridData15.verticalAlignment = GridData.FILL;
		gridData15.horizontalAlignment = GridData.FILL;
		GridData gridData13 = new GridData();
		gridData13.verticalAlignment = GridData.FILL;
		gridData13.grabExcessVerticalSpace = true;
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.verticalSpan = 2;
		gridData13.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 2;
		gridLayout5.verticalSpacing = 5;
		gridLayout5.marginWidth = 0;
		gridLayout5.marginHeight = 0;
		gridLayout5.horizontalSpacing = 2;
		pegListComposite = new Composite(pegAreaComposite, SWT.NONE);
		pegListComposite.setLayoutData(gridData15);
		pegListComposite.setLayout(gridLayout5);
		createPegListCompositeTop();
		pegList = new List(pegListComposite, SWT.V_SCROLL);		
		pegList.setLayoutData(gridData13);
		pegList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().selectPeg(pegList.getSelectionIndex());
			}
		});
		moveUpButton = new Button(pegListComposite, SWT.NONE);
		moveUpButton.setImage(new Image(Display.getCurrent(), new Image(Display.getCurrent(),
				new ImageData(getClass().getClassLoader().getResourceAsStream("edu/gsu/psych/sosa/images/arrow-up.png"))).getImageData().scaledTo(10, 12)));
		moveUpButton.setLayoutData(gridData24);
		moveUpButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().movePegUp();
			}
		});
		moveDownButton = new Button(pegListComposite, SWT.NONE);
		moveDownButton.setImage(new Image(Display.getCurrent(), new Image(Display.getCurrent(),
				new ImageData(getClass().getClassLoader().getResourceAsStream("edu/gsu/psych/sosa/images/arrow-down.png"))).getImageData().scaledTo(10, 12)));
		moveDownButton.setLayoutData(gridData25);
		moveDownButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().movePegDown();
				
			}
		});		
		createAddRemovePegButtonsComposite();
		//Creates a PegList Composite
	}

	private void createPegListCompositeTop(){
		GridData gridData27 = new GridData();
		gridData27.horizontalSpan = 2;
		GridLayout gridLayout9 = new GridLayout();
		gridLayout9.numColumns = 2;
		gridLayout9.marginWidth = 0;
		gridLayout9.marginHeight = 0;
		gridLayout9.horizontalSpacing = 5;
		gridLayout9.verticalSpacing = 5;
		pegListCompositeTop = new Composite(pegListComposite, SWT.NONE);
		pegListCompositeTop.setLayout(gridLayout9);
		createPegArrangementCombo();
		pegListCompositeTop.setLayoutData(gridData27);
		createOrderButtonsComposite();
	}

	@Override
	public void updateOrderDropdown() {
		pegArrangementCombo.removeAll();
		for (int i = 1; i <= experiment.getOrderCount(); i++)
			pegArrangementCombo.add("Order " + i);
		pegArrangementCombo.select(experiment.getCurrentOrderIndex());
	}
	
	public void updatePegSettingsArea(){
		Stimulus stim = experiment.getSelectedPeg();
		if(stim != null){
			labelText.setText(stim.getLabel());
			processingIDText.setText(stim.processingID);
			if(stim instanceof ImageStim){
				typePegRadioButton.setSelection(false);
				typeImageRadioButton.setSelection(true);				
				exchangeAppearenceControls(Stimulus.IMAGE);
			} else {
				typePegRadioButton.setSelection(true);
				typeImageRadioButton.setSelection(false);
				exchangeAppearenceControls(Stimulus.PEG);
			}			
			
			currentAppearanceComposite.updateDetails();
			//Updates the labels and determine between RadioButtons (peg or Image)
		}
	}
	
	@Override
	public Shell getMainShell() {
		return sShell;
	}
	
	@Override
	public void updateDetails(){
		creatorPreviewCanvasImpl.getFrameHandler().updateFrame();
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.END;
		gridData8.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.END;
		gridData7.verticalAlignment = GridData.CENTER;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 2;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.verticalSpan = 2;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		gridLayout1.marginHeight = 0;
		gridLayout1.horizontalSpacing = 2;
		gridLayout1.marginWidth = 2;
		gridLayout1.verticalSpacing = 2;
		group = new Group(experimentSettingsComposite, SWT.NONE);
		group.setText("Experiment");
		group.setLayout(gridLayout1);
		group.setLayoutData(gridData1);
		experimentNameLabel = new Label(group, SWT.NONE);
		experimentNameLabel.setText("Name:");
		experimentNameLabel.setLayoutData(gridData7);
		experimentNameText = new Text(group, SWT.BORDER | SWT.RIGHT);
		experimentNameText.setLayoutData(gridData);
		experimentNameText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				getExperiment().name = experimentNameText.getText();
			}
		});
		experimentVersionLabel = new Label(group, SWT.NONE);
		experimentVersionLabel.setText("Version:");
		experimentVersionLabel.setLayoutData(gridData8);
		experimentVersionText = new Text(group, SWT.BORDER | SWT.RIGHT);
		experimentVersionText.setLayoutData(gridData6);
		experimentVersionText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				getExperiment().version = experimentVersionText.getText();
			}
		});
	}

	/**
	 * This method initializes pegSettingsGroup	
	 *
	 */
	private void createPegSettingsGroup() {
		GridData gridData411 = new GridData();
		gridData411.widthHint = 50;
		GridData gridData21 = new GridData();
		gridData21.horizontalSpan = 2;
		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = GridData.FILL;
		gridData31.horizontalSpan = 2;
		gridData31.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
		pegSettingsGroup = new Group(pegSettingsAreaComposite, SWT.NONE);
		pegSettingsGroup.setText("Peg Settings");
		pegSettingsGroup.setLayout(gridLayout2);
		labelLabel = new Label(pegSettingsGroup, SWT.NONE);
		labelLabel.setText("Label");
		labelText = new Text(pegSettingsGroup, SWT.BORDER);
		labelText.setLayoutData(gridData31);
		createAppearanceComposite();
		labelText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(getExperiment().getSelectedPeg() != null){
					getExperiment().getSelectedPeg().setLabel(labelText.getText());
					getExperiment().updatePegEntry();
				}
			}
		});
		procIDLabel = new Label(pegSettingsGroup, SWT.NONE);
		procIDLabel.setText("Processing ID");
		procIDLabel.setLayoutData(gridData21);
		processingIDText = new Text(pegSettingsGroup, SWT.BORDER);
		processingIDText.setLayoutData(gridData411);
		processingIDText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(getExperiment().getSelectedPeg() != null)
					getExperiment().getSelectedPeg().processingID = processingIDText.getText();
			}
		});
		createFileGroup();
		createTypeGroup();
	}
	
	/**
	 * This method initializes appearanceComposite	
	 *
	 */
	private void createAppearanceComposite() {
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.horizontalSpacing = 0;
		gridLayout6.marginWidth = 0;
		gridLayout6.marginHeight = 0;
		gridLayout6.verticalSpacing = 0;
		GridData gridData18 = new GridData();
		gridData18.verticalSpan = 4;
		gridData18.horizontalAlignment = GridData.FILL;
		gridData18.verticalAlignment = GridData.FILL;
		appearanceComposite = new Composite(pegSettingsGroup, SWT.NONE);
		appearanceComposite.setLayoutData(gridData18);
		appearanceComposite.setLayout(gridLayout6);
		appearanceCompositePeg = new ExperimentCreatorPegComposite(appearanceComposite, SWT.NONE, this);
		currentAppearanceComposite = appearanceCompositePeg;
	}

	/**
	 * This method initializes fileGroup	
	 *
	 */
	private void createFileGroup() {
		GridLayout gridLayout31 = new GridLayout();
		gridLayout31.numColumns = 2;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.FILL;
		gridData9.horizontalSpan = 3;
		gridData9.verticalAlignment = GridData.CENTER;
		fileGroup = new Group(pegSettingsGroup, SWT.NONE);
		fileGroup.setText("File");
		fileGroup.setLayoutData(gridData9);
		fileGroup.setLayout(gridLayout31);
		loadButton = new Button(fileGroup, SWT.NONE);
		loadButton.setText("Load");
		loadButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(getExperiment().getSelectedPeg() == null)
					getExperiment().addPeg();
				FileDialog file = new FileDialog(sShell, SWT.OPEN);
				file.setFilterExtensions(new String[] {"*.stim"});
				try {
					getExperiment().setSelectedPeg(SOSAFileHandler.readStimFile(file.open()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		saveButton = new Button(fileGroup, SWT.NONE);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(experiment.getSelectedPeg() != null){
					FileDialog file = new FileDialog(sShell, SWT.SAVE);
					file.setFilterExtensions(new String[] {"*.stim"});
					try {
						SOSAFileHandler.writeFile(experiment.getSelectedPeg(),file.open());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * This method initializes typeGroup	
	 *
	 */
	private void createTypeGroup() {
		GridLayout gridLayout10 = new GridLayout();
		gridLayout10.numColumns = 1;
		GridData gridData81 = new GridData();
		gridData81.horizontalAlignment = GridData.FILL;
		gridData81.verticalAlignment = GridData.CENTER;
		GridData gridData71 = new GridData();
		gridData71.horizontalAlignment = GridData.FILL;
		gridData71.horizontalSpan = 3;
		gridData71.grabExcessVerticalSpace = false;
		gridData71.verticalAlignment = GridData.END;
		typeGroup = new Group(pegSettingsGroup, SWT.NONE);
		typeGroup.setText("Type");
		typeGroup.setLayout(gridLayout10);
		typeGroup.setLayoutData(gridData71);
		typePegRadioButton = new Button(typeGroup, SWT.RADIO);
		typePegRadioButton.setText("Peg");
		typeImageRadioButton = new Button(typeGroup, SWT.RADIO);
		typeImageRadioButton.setText("Image");
		typePegRadioButton.setSelection(true);
		typePegRadioButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						if(typePegRadioButton.getSelection()){
							exchangeAppearenceControls(Stimulus.PEG);
							if((getExperiment().getSelectedPeg() instanceof ImageStim))
								getExperiment().convertCurrentStimTo(Stimulus.PEG);
							currentAppearanceComposite.updateDetails();
						}
					}
				});
		typeImageRadioButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(typeImageRadioButton.getSelection()){
					exchangeAppearenceControls(Stimulus.IMAGE);
					if(!(getExperiment().getSelectedPeg() instanceof ImageStim))
						getExperiment().convertCurrentStimTo(Stimulus.IMAGE);
					currentAppearanceComposite.updateDetails();
				}
			}
		});
	}
	
	/**
	 * Changes out the settings panels from the current set to the specified.
	 * 
	 * @param type They type of stimulus to create settings for.
	 * @param setDefaults <strong>true</strong> if the panel should be set to default for the currently selected stimulus.
	 */
	private void exchangeAppearenceControls(int type){
		if(type == Stimulus.PEG){
			if(appearanceCompositeImage != null && !appearanceCompositeImage.isDisposed())
				appearanceCompositeImage.dispose();
			if(appearanceCompositePeg == null || appearanceCompositePeg.isDisposed())
				appearanceCompositePeg  = new ExperimentCreatorPegComposite(appearanceComposite, SWT.NONE, this);
			currentAppearanceComposite = appearanceCompositePeg;
		} else if(type == Stimulus.IMAGE){
			if((appearanceCompositePeg != null) && (!appearanceCompositePeg.isDisposed()))
				appearanceCompositePeg.dispose();
			if(appearanceCompositeImage == null || appearanceCompositeImage.isDisposed())
				appearanceCompositeImage = new ExperimentCreatorImageComposite(appearanceComposite, SWT.NONE, this);
			currentAppearanceComposite = appearanceCompositeImage;
		}
		appearanceComposite.layout();
	}

	/**
	 * This method initializes orderButtonsComposite	
	 *
	 */
	private void createOrderButtonsComposite() {
		GridData gridData23 = new GridData();
		gridData23.horizontalAlignment = GridData.END;
		gridData23.verticalAlignment = GridData.CENTER;
		GridData gridData20 = new GridData();
		gridData20.horizontalAlignment = GridData.FILL;
		gridData20.verticalAlignment = GridData.CENTER;
		GridData gridData19 = new GridData();
		gridData19.horizontalAlignment = GridData.FILL;
		gridData19.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout7 = new GridLayout();
		gridLayout7.numColumns = 2;
		gridLayout7.horizontalSpacing = 2;
		gridLayout7.verticalSpacing = 0;
		gridLayout7.marginWidth = 0;
		gridLayout7.marginHeight = 0;
		gridLayout7.makeColumnsEqualWidth = true;
		orderButtonsComposite = new Composite(pegListCompositeTop, SWT.NONE);
		orderButtonsComposite.setLayout(gridLayout7);
		orderButtonsComposite.setLayoutData(gridData23);
		newArrangementButton = new Button(orderButtonsComposite, SWT.NONE);
		newArrangementButton.setText("+");
		newArrangementButton.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		newArrangementButton.setLayoutData(gridData19);
		newArrangementButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().addPegOrder();
			}
		});
		removeArrangementButton = new Button(orderButtonsComposite, SWT.NONE);
		removeArrangementButton.setText("-");
		removeArrangementButton.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		removeArrangementButton.setLayoutData(gridData20);
		removeArrangementButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().removePegOrder();
				//Creates Order for the Experiment
			}
		});
	}

	/**
	 * This method initializes addRemovePegButtonsComposite	
	 *
	 */
	private void createAddRemovePegButtonsComposite() {
		GridData gridData26 = new GridData();
		gridData26.grabExcessHorizontalSpace = true;
		gridData26.verticalAlignment = GridData.CENTER;
		gridData26.horizontalAlignment = GridData.FILL;
		GridData gridData22 = new GridData();
		gridData22.horizontalAlignment = GridData.FILL;
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.verticalAlignment = GridData.CENTER;
		GridData gridData16 = new GridData();
		gridData16.verticalAlignment = GridData.CENTER;
		gridData16.horizontalSpan = 2;
		gridData16.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout8 = new GridLayout();
		gridLayout8.numColumns = 2;
		gridLayout8.verticalSpacing = 0;
		gridLayout8.marginWidth = 0;
		gridLayout8.marginHeight = 0;
		gridLayout8.horizontalSpacing = 3;
		addRemovePegButtonsComposite = new Composite(pegListComposite, SWT.NONE);
		addRemovePegButtonsComposite.setLayout(gridLayout8);
		addRemovePegButtonsComposite.setLayoutData(gridData16);
		addPegButton = new Button(addRemovePegButtonsComposite, SWT.NONE);
		addPegButton.setText("Add Stim");
		addPegButton.setLayoutData(gridData22);
		addPegButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().addPeg();
			}
		});
		removePegButton = new Button(addRemovePegButtonsComposite, SWT.NONE);
		removePegButton.setText("Remove Stim");
		removePegButton.setLayoutData(gridData26);
		removePegButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getExperiment().removePeg();
			}
		});//Creates a Add and Remove Composite
	}

	@Override
	public Experiment getExperiment(){
		if(experiment == null)
			createNewExperiment();
		return experiment;
		//Gets Experiment and Creates one if null
	}

	@Override
	public void updateCursor(int cursorType) {
		// TODO Auto-generated method stub
	}

	public void setExperimentWindowSize(int x, int y) {
		getExperiment().setWindowSize(x, y);
		lblwindowSizeDisplay.setText(getExperiment().getWindowSizeText());
		//Sets the Size of the Experiment window
	}
}
