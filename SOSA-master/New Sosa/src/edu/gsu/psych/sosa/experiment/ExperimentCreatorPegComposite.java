package edu.gsu.psych.sosa.experiment;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

import com.ardor3d.math.ColorRGBA;

import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSASimpleUpdatable;

public class ExperimentCreatorPegComposite extends Composite implements SOSASimpleUpdatable{

	private Group pegAppearanceGroup;
	private Label colorValueLabel;
	private Label defaultColorLabel;
	private Label labelColorLabel;
	private Button labelColorDefaultCheckBox;
	private SOSAWindowExperiment parentWindow;
	private Label pegColorLabel;
	private Button pegColorDefaultCheckBox;
	private Label sizeLabel;
	private Combo stimSizeCombo;
	private Composite labelColorSliderComposite;
	private Label labelRedSliderLabel;
	private Slider labelRedSlider;
	private Label labelRedValueLabel;
	private Label labelGreenSliderLabel;
	private Slider labelGreenSlider;
	private Label labelGreenValueLabel;
	private Label labelBlueSliderLabel;
	private Slider labelBlueSlider;
	private Label labelBlueValueLabel;
	private Composite colorSliderComposite;
	private Label redSliderLabel;
	private Slider redSlider;
	private Label redValueLabel;
	private Label greenSliderLabel;
	private Slider greenSlider;
	private Label greenValueLabel;
	private Label blueSliderLabel;
	private Slider blueSlider;
	private Label blueValueLabel;

	public ExperimentCreatorPegComposite(Composite parent, int style, SOSAWindowExperiment parentWindow) {
		super(parent, style);
		this.parentWindow = parentWindow;
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		createPegAppearanceGroup();
		this.setLayout(gridLayout);
	}
	
	private void createPegAppearanceGroup() {
		//header row
		GridLayout gridLayout21 = new GridLayout();
		gridLayout21.numColumns = 3;		
		pegAppearanceGroup = new Group(this, SWT.NONE);
		pegAppearanceGroup.setText("Appearance");		
		pegAppearanceGroup.setLayout(gridLayout21);		
		GridData gridData90 = new GridData();
		gridData90.horizontalAlignment = GridData.CENTER;
		@SuppressWarnings("unused")
		Label filler6 = new Label(pegAppearanceGroup, SWT.NONE);
		colorValueLabel = new Label(pegAppearanceGroup, SWT.NONE);
		colorValueLabel.setText("Value");
		colorValueLabel.setLayoutData(gridData90);
		defaultColorLabel = new Label(pegAppearanceGroup, SWT.NONE);
		defaultColorLabel.setText("Default");
		
		//label color row
		labelColorLabel = new Label(pegAppearanceGroup, SWT.NONE);
		labelColorLabel.setText("Label Color");
		createLabelColorSliderComposite();
		GridData gridData50 = new GridData();
		gridData50.horizontalAlignment = GridData.CENTER;
		gridData50.verticalAlignment = GridData.CENTER;
		labelColorDefaultCheckBox = new Button(pegAppearanceGroup, SWT.CHECK);
		labelColorDefaultCheckBox.setLayoutData(gridData50);
		labelColorDefaultCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(labelColorDefaultCheckBox.getSelection()){
					setLabelColorToDefault();
				}
			}
		});
		
		//peg color row
		pegColorLabel = new Label(pegAppearanceGroup, SWT.NONE);
		pegColorLabel.setText("Peg Color");		
		createColorSliderComposite();
		GridData gridData51 = new GridData();
		gridData51.horizontalAlignment = GridData.CENTER;
		gridData51.verticalAlignment = GridData.CENTER;
		pegColorDefaultCheckBox = new Button(pegAppearanceGroup, SWT.CHECK);
		pegColorDefaultCheckBox.setLayoutData(gridData51);
		pegColorDefaultCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(pegColorDefaultCheckBox.getSelection()){
					setPegColorToDefault();
				}
			}
		});

		//stim size / type row
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 2;
		gridData2.widthHint = 80;
		sizeLabel = new Label(pegAppearanceGroup, SWT.NONE);
		sizeLabel.setText("Size / Type");
		stimSizeCombo = new Combo(pegAppearanceGroup, SWT.NONE);
		stimSizeCombo.setLayoutData(gridData2);
		stimSizeCombo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null)
					parentWindow.getExperiment().getSelectedPeg().setSize(stimSizeCombo.getSelectionIndex());
			}
		});
	}
	
	private void createLabelColorSliderComposite(){
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
		labelColorSliderComposite = new Composite(pegAppearanceGroup, SWT.NONE);
		labelColorSliderComposite.setLayout(gridLayout6);
		labelRedSliderLabel = new Label(labelColorSliderComposite, SWT.NONE);
		labelRedSliderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		labelRedSliderLabel.setText("R");
		labelRedSlider = new Slider(labelColorSliderComposite, SWT.NONE);
		labelRedSlider.setMaximum(256);
		labelRedSlider.setThumb(1);
		labelRedSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					labelRedValueLabel.setText(""+labelRedSlider.getSelection());
					labelColorDefaultCheckBox.setSelection(false);
					parentWindow.getExperiment().getSelectedPeg().labelColor.setRed(((float)labelRedSlider.getSelection()) / 255f);
				}
			}
		});
		labelRedValueLabel = new Label(labelColorSliderComposite, SWT.NONE);
		labelRedValueLabel.setText("255");
		labelRedValueLabel.setLayoutData(gridData141);
		labelGreenSliderLabel = new Label(labelColorSliderComposite, SWT.NONE);
		labelGreenSliderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		labelGreenSliderLabel.setText("G");
		labelGreenSlider = new Slider(labelColorSliderComposite, SWT.NONE);
		labelGreenSlider.setMaximum(256);
		labelGreenSlider.setThumb(1);
		labelGreenSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					labelGreenValueLabel.setText(""+labelGreenSlider.getSelection());
					labelColorDefaultCheckBox.setSelection(false);
					parentWindow.getExperiment().getSelectedPeg().labelColor.setGreen(((float)labelGreenSlider.getSelection()) / 255f);
				}
			}
		});
		labelGreenValueLabel = new Label(labelColorSliderComposite, SWT.NONE);
		labelGreenValueLabel.setText("255");
		labelGreenValueLabel.setLayoutData(gridData151);
		labelBlueSliderLabel = new Label(labelColorSliderComposite, SWT.NONE);
		labelBlueSliderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		labelBlueSliderLabel.setText("B");
		labelBlueSlider = new Slider(labelColorSliderComposite, SWT.NONE);
		labelBlueSlider.setMaximum(256);
		labelBlueSlider.setThumb(1);
		labelBlueSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					labelBlueValueLabel.setText(""+labelBlueSlider.getSelection());
					labelColorDefaultCheckBox.setSelection(false);
					parentWindow.getExperiment().getSelectedPeg().labelColor.setBlue(((float)labelBlueSlider.getSelection()) / 255f);
				}
			}
		});
		labelBlueValueLabel = new Label(labelColorSliderComposite, SWT.NONE);
		labelBlueValueLabel.setText("255");
		labelBlueValueLabel.setLayoutData(gridData161);
		//Sets the Label Composite 
	}
	
	private void createColorSliderComposite() {
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
		colorSliderComposite = new Composite(pegAppearanceGroup, SWT.NONE);
		colorSliderComposite.setLayout(gridLayout11);
		redSliderLabel = new Label(colorSliderComposite, SWT.NONE);
		redSliderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		redSliderLabel.setText("R");
		redSlider = new Slider(colorSliderComposite, SWT.NONE);
		redSlider.setMaximum(256);
		redSlider.setThumb(1);
		redSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					redValueLabel.setText(""+redSlider.getSelection());
					pegColorDefaultCheckBox.setSelection(false);
					parentWindow.getExperiment().getSelectedPeg().pegColor.setRed(((float)redSlider.getSelection()) / 255f);
				}
			}
		});
		redValueLabel = new Label(colorSliderComposite, SWT.NONE);
		redValueLabel.setText("255");
		redValueLabel.setLayoutData(gridData111);
		greenSliderLabel = new Label(colorSliderComposite, SWT.NONE);
		greenSliderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		greenSliderLabel.setText("G");
		greenSlider = new Slider(colorSliderComposite, SWT.NONE);
		greenSlider.setMaximum(256);
		greenSlider.setThumb(1);
		greenSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					greenValueLabel.setText(""+greenSlider.getSelection());
					pegColorDefaultCheckBox.setSelection(false);
					parentWindow.getExperiment().getSelectedPeg().pegColor.setGreen(((float)greenSlider.getSelection()) / 255f);
				}
			}
		});
		greenValueLabel = new Label(colorSliderComposite, SWT.NONE);
		greenValueLabel.setText("255");
		greenValueLabel.setLayoutData(gridData121);
		blueSliderLabel = new Label(colorSliderComposite, SWT.NONE);
		blueSliderLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		blueSliderLabel.setText("B");
		blueSlider = new Slider(colorSliderComposite, SWT.NONE);
		blueSlider.setMaximum(256);
		blueSlider.setThumb(1);
		blueSlider.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					blueValueLabel.setText(""+blueSlider.getSelection());
					pegColorDefaultCheckBox.setSelection(false);
					parentWindow.getExperiment().getSelectedPeg().pegColor.setBlue(((float)blueSlider.getSelection()) / 255f);
				}
			}
		});
		blueValueLabel = new Label(colorSliderComposite, SWT.NONE);
		blueValueLabel.setText("255");
		blueValueLabel.setLayoutData(gridData131);
		//Creates the Composite for the color slider
	}
	
	private void setPegColorSliders(ColorRGBA rgba) {
		redSlider.setSelection((int)(rgba.getRed()*255f));
		greenSlider.setSelection((int)(rgba.getGreen()*255f));
		blueSlider.setSelection((int)(rgba.getBlue()*255f));
		
		redValueLabel.setText(""+(int)(rgba.getRed()*255f));
		greenValueLabel.setText(""+(int)(rgba.getGreen()*255f));
		blueValueLabel.setText(""+(int)(rgba.getBlue()*255f));	
		//Sets up the slider for the Peg color
	}
	
	private void setLabelColorSliders(ColorRGBA rgba){
		labelRedSlider.setSelection((int)(rgba.getRed()*255f));
		labelGreenSlider.setSelection((int)(rgba.getGreen()*255f));
		labelBlueSlider.setSelection((int)(rgba.getBlue()*255f));
		
		labelRedValueLabel.setText(""+(int)(rgba.getRed()*255f));
		labelGreenValueLabel.setText(""+(int)(rgba.getGreen()*255f));
		labelBlueValueLabel.setText(""+(int)(rgba.getBlue()*255f));
		//Sets up the slider for Label Color
	}
	
	private void setPegColorToDefault(){
		if(parentWindow.getExperiment() != null && parentWindow.getExperiment().getSelectedPeg() != null){
			setPegColorSliders(parentWindow.getExperiment().getSelectedPeg().pegColorDefault);
			parentWindow.getExperiment().getSelectedPeg().revertPegColor();
			pegColorDefaultCheckBox.setSelection(true);
			//Sets the default peg color
		}
	}
	
	private void setLabelColorToDefault(){
		if(parentWindow.getExperiment() != null && parentWindow.getExperiment().getSelectedPeg() != null){
			setLabelColorSliders(parentWindow.getExperiment().getSelectedPeg().labelColorDefault);
			parentWindow.getExperiment().getSelectedPeg().revertLabelColor();
			labelColorDefaultCheckBox.setSelection(true);
			//Sets the default color for label
		}
	}
	
	private void updatePegSettingsArea(){
		Stimulus peg = parentWindow.getExperiment().getSelectedPeg();
		if(peg != null){
			
			setPegColorSliders(peg.pegColor);
			
			if(peg.pegColorDefault.equals(peg.pegColor))
				pegColorDefaultCheckBox.setSelection(true);
			else
				pegColorDefaultCheckBox.setSelection(false);
			
			setLabelColorSliders(peg.labelColor);
			
			if(peg.labelColorDefault.equals(peg.labelColor))
				labelColorDefaultCheckBox.setSelection(true);
			else
				labelColorDefaultCheckBox.setSelection(false);
			
			stimSizeCombo.removeAll();
			for (String size : peg.getAvailableTypes())
				stimSizeCombo.add(size);
			stimSizeCombo.select((int)peg.typeIndex);
		}//Updates the Pegs color and labels
	}

	@Override
	public void updateDetails() {
		updatePegSettingsArea();
		//Updates the PegSettingArea
	}
}
