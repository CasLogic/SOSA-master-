package edu.gsu.psych.sosa.experiment;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.renderer.state.TextureState;

import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.main.SOSAWindow;

public class ExperimentPreviewGUI implements SOSAWindow {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="13,4"
	private SwtCanvas previewExperimentCanvas = null;
	private Button launchExperimentButton = null;
	private Group chooseStimulusVisabilityGroup = null;
	private Composite navButtonsComposite = null;
	private Group appearanceGroup = null;
	@SuppressWarnings("unused")
	private Label filler = null;
	private Label colorValueLabel = null;
	private Label defaultColorLabel = null;
	private Label boardColorLabel = null;
	private Composite boardColorSliderComposite = null;
	private Label boardSliderRLabel = null;
	private Slider boardSliderR = null;
	private Label boardValueRLabel = null;
	private Label boardSliderGLabel = null;
	private Slider boardSliderG = null;
	private Label boardValueGLabel = null;
	private Label boardSliderBLabel = null;
	private Slider boardSliderB = null;
	private Label boardValueBLabel = null;
	private Button boardColorDefaultCheckBox = null;
	private Label bgColorLabel = null;
	private Composite bgColorSliderComposite = null;
	private Label bgSliderRLabel = null;
	private Slider bgSliderR = null;
	private Label bgValueRLabel = null;
	private Label bgSliderGLabel = null;
	private Slider bgSliderG = null;
	private Label bgValueGLabel = null;
	private Label bgSliderBLabel = null;
	private Slider bgSliderB = null;
	private Label bgValueBLabel = null;
	private Button bgColorDefaultCheckBox = null;
	private Composite previewExperimentCanvasComposite;
	private ExperimentPreviewCanvasImpl previewExperimentCanvasImpl = null;  //  @jve:decl-index=0:
	private Button showLabelsCheckBox = null;
	private Group logFileGroup = null;
	private Button logLocationChooseButton = null;
	private Label logOutputLocationLabel = null;
	private Composite leftSideComposite = null;
	private Label orderLabel = null;
	private List orderList = null;
	private Label pegLabel = null;
	private List pegList = null;
	private Slider labelsShadeSlider = null;
	private Slider labelsPositionSlider = null;
	private Slider labelsSizeSlider = null;
	private Label labelsSizeLabel = null;
	private Label labelsPositionLabel = null;
	private Label labelsShadeLabel = null;
	private Label defaultlabel = null;
	private Label labelsPositionValueLabel = null;
	private Label labelsSizeValueLabel = null;
	private Label labelsShadeValueLabel = null;
	private Button labelsDefaultCheckBox = null;
	private Button displayBoard = null;
	public ExperimentPreviewGUI() {
		createSShell();
		sShell.pack();
		sShell.setMinimumSize(sShell.getSize());
		sShell.open();
		
		setBoardColorSliders(SOSAMain.experiment.defaultBoardColor);
		setBGColorSliders(SOSAMain.experiment.defaultBGColor);
		setLabelSliders(60,100,75);
		boardColorDefaultCheckBox.setSelection(true);
		bgColorDefaultCheckBox.setSelection(true);
		
		populateOrderList();
		updatePegList();
		
		SOSAMain.experiment.initLabels();
		SOSAMain.experiment.setLabelValues(60, 100, 75);
		
		showLabelsCheckBox.setSelection(true);
		labelsDefaultCheckBox.setSelection(true);
		//Sets the Default Preview of the Experiment
	}

	@Override
	public Shell getMainShell(){
		return sShell;
		//Returns Shell 
	}
	
	@Override
	public void updateDetails() {
		previewExperimentCanvasImpl.getFrameHandler().updateFrame();
		//Updates the preview Experiment
	}
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.makeColumnsEqualWidth = false;
		sShell = new Shell();
		sShell.setText("Preview Experiment");
		createLeftSideComposite();
		sShell.setLayout(gridLayout);
		createPreviewExperimentCanvasComposite();
		createAppearanceGroup();
		createChooseStimulusVisabilityGroup();
		createLogFileGroup();
		createNavButtonsComposite();
	}

	/**
	 * This method initializes previewExperimentCanvas	
	 *
	 */
	private void createPreviewExperimentCanvasComposite() {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 500;
		gridData.widthHint = 500;
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = GridData.FILL;
		previewExperimentCanvasComposite = new Composite(sShell, SWT.NONE);
		previewExperimentCanvasComposite.setLayout(new FillLayout());
		previewExperimentCanvasComposite.setLayoutData(gridData);
		createPreviewCanvas();
	}

	/**
	 * This method initializes chooseStimulusVisabilityGroup	
	 *
	 */
	private void createChooseStimulusVisabilityGroup() {		
		GridData gridData22 = new GridData();
		gridData22.horizontalAlignment = GridData.CENTER;
		gridData22.verticalAlignment = GridData.CENTER;
		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.CENTER;
		gridData21.verticalAlignment = GridData.CENTER;
		GridData gridData20 = new GridData();
		gridData20.horizontalAlignment = GridData.CENTER;
		gridData20.verticalSpan = 3;
		gridData20.verticalAlignment = GridData.CENTER;
		GridData gridData19 = new GridData();
		GridData gridData18 = new GridData();
		gridData18.horizontalAlignment = GridData.END;
		gridData18.verticalAlignment = GridData.CENTER;
		GridData gridData17 = new GridData();
		gridData17.verticalAlignment = GridData.CENTER;
		gridData17.horizontalAlignment = GridData.END;
		GridData gridData16 = new GridData();
		gridData16.verticalAlignment = GridData.CENTER;
		gridData16.horizontalAlignment = GridData.END;
		GridData gridData15 = new GridData();
		gridData15.horizontalAlignment = GridData.END;
		gridData15.heightHint = -1;
		gridData15.verticalAlignment = GridData.CENTER;
		GridData gridData14 = new GridData();
		gridData14.verticalAlignment = GridData.CENTER;
		gridData14.heightHint = -1;
		gridData14.horizontalAlignment = GridData.END;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = GridData.END;
		gridData13.verticalAlignment = GridData.CENTER;
		gridData13.heightHint = -1;
		gridData13.grabExcessVerticalSpace = false;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
		gridLayout3.horizontalSpacing = 2;
		gridLayout3.marginWidth = 5;
		gridLayout3.verticalSpacing = 0;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.BEGINNING;
		gridData3.grabExcessVerticalSpace = false;
		gridData3.verticalSpan = 2;
		gridData3.verticalAlignment = GridData.BEGINNING;
		chooseStimulusVisabilityGroup = new Group(sShell, SWT.NONE);
		chooseStimulusVisabilityGroup.setText("Labels");
		chooseStimulusVisabilityGroup.setLayout(gridLayout3);
		chooseStimulusVisabilityGroup.setLayoutData(gridData3);
		showLabelsCheckBox = new Button(chooseStimulusVisabilityGroup, SWT.CHECK);
		showLabelsCheckBox.setText("Show");
		showLabelsCheckBox.setLayoutData(gridData19);
		@SuppressWarnings("unused")
		Label filler2 = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler3 = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		defaultlabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		defaultlabel.setText("Default");
		showLabelsCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				SOSAMain.experiment.showLabels = showLabelsCheckBox.getSelection();
				SOSAMain.experiment.revertAllLabelRenders();
			}
		});
		labelsPositionLabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		labelsPositionLabel.setText("Position");
		labelsPositionLabel.setLayoutData(gridData17);
		labelsPositionSlider = new Slider(chooseStimulusVisabilityGroup, SWT.HORIZONTAL);
		labelsPositionSlider.setThumb(1);
		labelsPositionSlider.setLayoutData(gridData14);
		labelsPositionSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				labelsPositionValueLabel.setText("" + (labelsPositionSlider.getSelection() + 1));
				labelsDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.setLablePosition(labelsPositionSlider.getSelection());
			}
		});
		labelsPositionValueLabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		labelsPositionValueLabel.setText("100");
		labelsDefaultCheckBox = new Button(chooseStimulusVisabilityGroup, SWT.CHECK);
		labelsDefaultCheckBox.setLayoutData(gridData20);
		labelsDefaultCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(labelsDefaultCheckBox.getSelection()){
					setLabelSliders(60, 100, 75);
					SOSAMain.experiment.setLabelValues(60, 100, 75);
				}
			}
		});
		labelsShadeLabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		labelsShadeLabel.setText("Shade");
		labelsShadeLabel.setLayoutData(gridData18);
		labelsShadeSlider = new Slider(chooseStimulusVisabilityGroup, SWT.HORIZONTAL);
		labelsShadeSlider.setThumb(1);
		labelsShadeSlider.setLayoutData(gridData15);
		labelsShadeSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				labelsShadeValueLabel.setText(""+(labelsShadeSlider.getSelection() + 1));
				labelsDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.setLabelShade(labelsShadeSlider.getSelection());
			}
		});
		labelsShadeValueLabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		labelsShadeValueLabel.setText("100");
		labelsSizeLabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		labelsSizeLabel.setText("Size");
		labelsSizeLabel.setLayoutData(gridData16);
		labelsSizeSlider = new Slider(chooseStimulusVisabilityGroup, SWT.HORIZONTAL);
		labelsSizeSlider.setThumb(1);
		labelsSizeSlider.setLayoutData(gridData13);
		labelsSizeSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				labelsSizeValueLabel.setText(""+(labelsSizeSlider.getSelection() + 1));
				labelsDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.setLabelSize(labelsSizeSlider.getSelection() + 1);
			}
		});
		labelsSizeValueLabel = new Label(chooseStimulusVisabilityGroup, SWT.NONE);
		labelsSizeValueLabel.setText("100");
	}

	/**
	 * This method initializes navButtonsComposite	
	 *
	 */
	private void createNavButtonsComposite() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.END;
		gridData2.verticalAlignment = GridData.END;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.horizontalSpan = 2;
		gridData1.widthHint = 150;
		gridData1.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		navButtonsComposite = new Composite(sShell, SWT.NONE);
		navButtonsComposite.setLayout(gridLayout2);
		navButtonsComposite.setLayoutData(gridData2);
		launchExperimentButton = new Button(navButtonsComposite, SWT.NONE);
		launchExperimentButton.setText("Launch Experiment");
		launchExperimentButton.setLayoutData(gridData1);
		launchExperimentButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
			{
				//Check if the directory location was chosen
				if(SOSAMain.experiment.getDirectoryLocation() != null)
				{
					SOSAMain.setNewActiveWindow(SOSAMain.EXPERIMENT_PREVIEW_SUBJECT_PROMPT);
					sShell.dispose();
				}
				else
				{
					//SHow the error message box if no directory was chosen
					MessageBox mb = new MessageBox(sShell, SWT.NONE);
					mb.setMessage("You must choose a file directory location to save the log file to.\nPlease choose a valid file directory location to continue");
					mb.setText("No file directory Chosen");
					mb.open();
				}
			}
		});
	}
	
	private void createPreviewCanvas(){
		final GLData data = new GLData();
		data.depthSize = 8;
		data.doubleBuffer = true;
		
		previewExperimentCanvas = new SwtCanvas(previewExperimentCanvasComposite, SWT.NONE, data);
		previewExperimentCanvasImpl = new ExperimentPreviewCanvasImpl(previewExperimentCanvas);
		//Creates a Preview of the Experiment canvas
	}

	/**
	 * This method initializes appearanceGroup	
	 *
	 */
	private void createAppearanceGroup() {
		GridData gridData4 = new GridData();
		gridData4.verticalSpan = 2;
		gridData4.horizontalAlignment = GridData.BEGINNING;
		gridData4.verticalAlignment = GridData.BEGINNING;
		gridData4.horizontalSpan = 2;
		GridData gridData61 = new GridData();
		gridData61.horizontalAlignment = GridData.CENTER;
		gridData61.verticalAlignment = GridData.CENTER;
		GridData gridData51 = new GridData();
		gridData51.horizontalAlignment = GridData.CENTER;
		gridData51.verticalAlignment = GridData.CENTER;
		GridData gridData41 = new GridData();
		gridData41.horizontalAlignment = GridData.CENTER;
		gridData41.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout21 = new GridLayout();
		gridLayout21.numColumns = 3;
		appearanceGroup = new Group(sShell, SWT.NONE);
		appearanceGroup.setText("Appearance");
		appearanceGroup.setLayoutData(gridData4);
		appearanceGroup.setLayout(gridLayout21);
		filler = new Label(appearanceGroup, SWT.NONE);
		colorValueLabel = new Label(appearanceGroup, SWT.NONE);
		colorValueLabel.setText("Value");
		colorValueLabel.setLayoutData(gridData41);
		defaultColorLabel = new Label(appearanceGroup, SWT.NONE);
		defaultColorLabel.setText("Default");
		boardColorLabel = new Label(appearanceGroup, SWT.NONE);
		boardColorLabel.setText("Board");
		createBoardColorSliderComposite();
		boardColorDefaultCheckBox = new Button(appearanceGroup, SWT.CHECK);
		boardColorDefaultCheckBox.setLayoutData(gridData51);
		boardColorDefaultCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(boardColorDefaultCheckBox.getSelection()){
					setBoardColorSliders(SOSAMain.experiment.defaultBoardColor);
					SOSAMain.experiment.revertBoardColor();
					previewExperimentCanvasImpl.updateBoardColor();
				}
			}
		});
		bgColorLabel = new Label(appearanceGroup, SWT.NONE);
		bgColorLabel.setText("Background");
		createBGColorSliderComposite();
		bgColorDefaultCheckBox = new Button(appearanceGroup, SWT.CHECK);
		bgColorDefaultCheckBox.setLayoutData(gridData61);
		bgColorDefaultCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(bgColorDefaultCheckBox.getSelection()){
					setBGColorSliders(SOSAMain.experiment.defaultBGColor);
					SOSAMain.experiment.revertBGColor();
					previewExperimentCanvasImpl.updateBGColor();
				}
			}
		});
		
		//Create the grid data for the show / hide background button
		GridData gridDataB = new GridData();
		gridDataB.grabExcessHorizontalSpace = true;
		gridDataB.grabExcessVerticalSpace = true;
		gridDataB.horizontalAlignment = GridData.FILL;
		gridDataB.verticalAlignment = GridData.FILL;
	
		//create the button to show/hide the board's background
		displayBoard = new Button(appearanceGroup, SWT.NONE);
		displayBoard.setText("Hide board background");
		
		//Give the button a action listener
		displayBoard.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
			{
				if(displayBoard.getText() == "Hide board background")
				{
					if(SOSAMain.experiment != null && SOSAMain.experiment.getBoardFace() != null)
					{
						TextureState ts = new TextureState();
						ts.setTexture(null);
						previewExperimentCanvasImpl.showBackground = false;
						previewExperimentCanvasImpl.getBoard().setRenderState(ts);
						displayBoard.setText("Show board background");
					}
				}
				else
				{
					if(SOSAMain.experiment != null && SOSAMain.experiment.getBoardFace() != null){
						TextureState ts = new TextureState();
						ts.setTexture(SOSAMain.experiment.getBoardFace());
						previewExperimentCanvasImpl.showBackground = true;
						previewExperimentCanvasImpl.getBoard().setRenderState(ts);
						displayBoard.setText("Hide board background");
					}
				}
			}
		});
	}

	private void setBoardColorSliders(ReadOnlyColorRGBA color) {
		boardSliderR.setSelection((int)(color.getRed()*255f));
		boardSliderG.setSelection((int)(color.getGreen()*255f));
		boardSliderB.setSelection((int)(color.getBlue()*255f));
		
		boardValueRLabel.setText(""+(int)(color.getRed()*255f));
		boardValueGLabel.setText(""+(int)(color.getGreen()*255f));
		boardValueBLabel.setText(""+(int)(color.getBlue()*255f));
		//Sets the color of the sliders of the board
	}
	
	private void setBGColorSliders(ReadOnlyColorRGBA color) {
		bgSliderR.setSelection((int)(color.getRed()*255f));
		bgSliderG.setSelection((int)(color.getGreen()*255f));
		bgSliderB.setSelection((int)(color.getBlue()*255f));
		
		bgValueRLabel.setText(""+(int)(color.getRed()*255f));
		bgValueGLabel.setText(""+(int)(color.getGreen()*255f));
		bgValueBLabel.setText(""+(int)(color.getBlue()*255f));
		//Sets the color of the background
	}
	
	private void setLabelSliders(int position, int shade, int size) {
		position = position < 1 ? 1 : position;
		shade = shade < 1 ? 1 : shade;
		size = size < 1 ? 1 : size;
		
		labelsPositionSlider.setSelection(position-1);
		labelsShadeSlider.setSelection(shade-1);
		labelsSizeSlider.setSelection(size-1);
		
		labelsPositionValueLabel.setText(""+position);
		labelsShadeValueLabel.setText(""+shade);
		labelsSizeValueLabel.setText(""+size);
		//Set the label sliders
	}
	
	private void populateOrderList(){
		for (int i = 1; i <= SOSAMain.experiment.getOrderCount(); i++)
			orderList.add("Order " + i);
		orderList.select(SOSAMain.experiment.getCurrentOrderIndex());
		//returns the OrderList as needed
	}
	
	private void updatePegList(){
		pegList.removeAll();
		for (Stimulus stim : SOSAMain.experiment.getStimList()) {
			pegList.add(stim.getLabel());
		}
		//Updates Peglist
	}

	/**
	 * This method initializes colorSliderComposite	
	 *
	 */
	private void createBoardColorSliderComposite() {
		GridData gridData131 = new GridData();
		gridData131.horizontalAlignment = GridData.FILL;
		gridData131.verticalAlignment = GridData.CENTER;
		GridData gridData121 = new GridData();
		gridData121.horizontalAlignment = GridData.FILL;
		gridData121.verticalAlignment = GridData.CENTER;
		GridData gridData111 = new GridData();
		gridData111.horizontalAlignment = GridData.FILL;
		gridData111.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout11 = new GridLayout();
		gridLayout11.numColumns = 3;
		gridLayout11.marginHeight = 0;
		gridLayout11.marginWidth = 2;
		gridLayout11.verticalSpacing = 0;
		gridLayout11.horizontalSpacing = 0;
		boardColorSliderComposite = new Composite(appearanceGroup, SWT.NONE);
		boardColorSliderComposite.setLayout(gridLayout11);
		boardSliderRLabel = new Label(boardColorSliderComposite, SWT.NONE);
		boardSliderRLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		boardSliderRLabel.setText("R");
		boardSliderR = new Slider(boardColorSliderComposite, SWT.NONE);
		boardSliderR.setMaximum(256);
		boardSliderR.setThumb(1);
		boardSliderR.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				boardValueRLabel.setText(""+boardSliderR.getSelection());
				boardColorDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.boardColor.setRed(((float)boardSliderR.getSelection()) / 255f);
				previewExperimentCanvasImpl.updateBoardColor();
			}
		});
		boardValueRLabel = new Label(boardColorSliderComposite, SWT.NONE);
		boardValueRLabel.setText("255");
		boardValueRLabel.setLayoutData(gridData111);
		boardSliderGLabel = new Label(boardColorSliderComposite, SWT.NONE);
		boardSliderGLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		boardSliderGLabel.setText("G");
		boardSliderG = new Slider(boardColorSliderComposite, SWT.NONE);
		boardSliderG.setMaximum(256);
		boardSliderG.setThumb(1);
		boardSliderG.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				boardValueGLabel.setText(""+boardSliderG.getSelection());
				boardColorDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.boardColor.setGreen(((float)boardSliderG.getSelection()) / 255f);
				previewExperimentCanvasImpl.updateBoardColor();
			}
		});
		boardValueGLabel = new Label(boardColorSliderComposite, SWT.NONE);
		boardValueGLabel.setText("255");
		boardValueGLabel.setLayoutData(gridData121);
		boardSliderBLabel = new Label(boardColorSliderComposite, SWT.NONE);
		boardSliderBLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		boardSliderBLabel.setText("B");
		boardSliderB = new Slider(boardColorSliderComposite, SWT.NONE);
		boardSliderB.setMaximum(256);
		boardSliderB.setThumb(1);
		boardSliderB.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				boardValueBLabel.setText(""+boardSliderB.getSelection());
				boardColorDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.boardColor.setBlue(((float)boardSliderB.getSelection()) / 255f);
				previewExperimentCanvasImpl.updateBoardColor();
			}
		});
		boardValueBLabel = new Label(boardColorSliderComposite, SWT.NONE);
		boardValueBLabel.setText("255");
		boardValueBLabel.setLayoutData(gridData131);
	}

	/**
	 * This method initializes labelColorSliderComposite	
	 *
	 */
	private void createBGColorSliderComposite() {
		GridData gridData161 = new GridData();
		gridData161.horizontalAlignment = GridData.FILL;
		gridData161.verticalAlignment = GridData.CENTER;
		GridData gridData151 = new GridData();
		gridData151.horizontalAlignment = GridData.FILL;
		gridData151.verticalAlignment = GridData.CENTER;
		GridData gridData141 = new GridData();
		gridData141.horizontalAlignment = GridData.FILL;
		gridData141.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 3;
		gridLayout6.marginHeight = 0;
		gridLayout6.marginWidth = 2;
		gridLayout6.verticalSpacing = 0;
		gridLayout6.horizontalSpacing = 0;
		bgColorSliderComposite = new Composite(appearanceGroup, SWT.NONE);
		bgColorSliderComposite.setLayout(gridLayout6);
		bgSliderRLabel = new Label(bgColorSliderComposite, SWT.NONE);
		bgSliderRLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		bgSliderRLabel.setText("R");
		bgSliderR = new Slider(bgColorSliderComposite, SWT.NONE);
		bgSliderR.setMaximum(256);
		bgSliderR.setThumb(1);
		bgSliderR.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				bgValueRLabel.setText(""+bgSliderR.getSelection());
				bgColorDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.bgColor.setRed(((float)bgSliderR.getSelection()) / 255f);
				previewExperimentCanvasImpl.updateBGColor();
			}
		});
		bgValueRLabel = new Label(bgColorSliderComposite, SWT.NONE);
		bgValueRLabel.setText("255");
		bgValueRLabel.setLayoutData(gridData141);
		bgSliderGLabel = new Label(bgColorSliderComposite, SWT.NONE);
		bgSliderGLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		bgSliderGLabel.setText("G");
		bgSliderG = new Slider(bgColorSliderComposite, SWT.NONE);
		bgSliderG.setMaximum(256);
		bgSliderG.setThumb(1);
		bgSliderG.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				bgValueGLabel.setText(""+bgSliderG.getSelection());
				bgColorDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.bgColor.setGreen(((float)bgSliderG.getSelection()) / 255f);
				previewExperimentCanvasImpl.updateBGColor();
			}
		});
		bgValueGLabel = new Label(bgColorSliderComposite, SWT.NONE);
		bgValueGLabel.setText("255");
		bgValueGLabel.setLayoutData(gridData151);
		bgSliderBLabel = new Label(bgColorSliderComposite, SWT.NONE);
		bgSliderBLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		bgSliderBLabel.setText("B");
		bgSliderB = new Slider(bgColorSliderComposite, SWT.NONE);
		bgSliderB.setMaximum(256);
		bgSliderB.setThumb(1);
		bgSliderB.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				bgValueBLabel.setText(""+bgSliderB.getSelection());
				bgColorDefaultCheckBox.setSelection(false);
				SOSAMain.experiment.bgColor.setBlue(((float)bgSliderB.getSelection()) / 255f);
				previewExperimentCanvasImpl.updateBGColor();
			}
		});
		bgValueBLabel = new Label(bgColorSliderComposite, SWT.NONE);
		bgValueBLabel.setText("255");
		bgValueBLabel.setLayoutData(gridData161);
	}

	/**
	 * This method initializes logFileGroup	
	 *
	 */
	private void createLogFileGroup() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.FILL;
		gridData11.verticalAlignment = GridData.CENTER;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = GridData.CENTER;
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.CENTER;
		logFileGroup = new Group(getMainShell(), SWT.NONE);
		logFileGroup.setLayout(new GridLayout());
		logFileGroup.setLayoutData(gridData7);
		logFileGroup.setText(" Log Output Location");
		logOutputLocationLabel = new Label(logFileGroup, SWT.CENTER);
		logOutputLocationLabel.setText("no location chosen...");
		logOutputLocationLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		logOutputLocationLabel.setLayoutData(gridData11);
		logLocationChooseButton = new Button(logFileGroup, SWT.NONE);
		logLocationChooseButton.setText("Choose...");
		logLocationChooseButton.setLayoutData(gridData10);
		logLocationChooseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
			{
				DirectoryDialog direct = new DirectoryDialog(sShell);
				direct.setFilterPath("c:\\");
				String directLocal = direct.open();
				
				if(directLocal != null)
				{
					SOSAMain.experiment.setDirectoryLocation(directLocal);
					logOutputLocationLabel.setText(directLocal);
				}
			}//Handles the File Output location
		});
	}

	/**
	 * This method initializes leftSideComposite	
	 *
	 */
	private void createLeftSideComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.CENTER;
		gridData12.verticalAlignment = GridData.CENTER;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.CENTER;
		gridData9.verticalAlignment = GridData.CENTER;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.heightHint = 120;
		gridData8.widthHint = 120;
		gridData8.verticalAlignment = GridData.CENTER;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.verticalAlignment = GridData.FILL;
		GridData gridData5 = new GridData();
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.horizontalAlignment = GridData.BEGINNING;
		leftSideComposite = new Composite(getMainShell(), SWT.NONE);
		leftSideComposite.setLayoutData(gridData5);
		leftSideComposite.setLayout(gridLayout1);
		orderLabel = new Label(leftSideComposite, SWT.LEFT);
		orderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		orderLabel.setLayoutData(gridData12);
		orderLabel.setText("Presentation Order");
		orderList = new List(leftSideComposite, SWT.NONE);
		orderList.setLayoutData(gridData8);
		orderList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				SOSAMain.experiment.setOrder(orderList.getSelectionIndex());
				updatePegList();
				previewExperimentCanvasImpl.generatePreview();	//update the preview
			}
		});
		pegLabel = new Label(leftSideComposite, SWT.NONE);
		pegLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		pegLabel.setLayoutData(gridData9);
		pegLabel.setText("Stimuli");
		pegList = new List(leftSideComposite, SWT.NONE);
		pegList.setLayoutData(gridData6);
		//Layout the Experiment Preview's Left side
	}
}
