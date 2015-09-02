package edu.gsu.psych.sosa.experiment;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

import com.ardor3d.math.ColorRGBA;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.main.SOSAFileHandler;
import edu.gsu.psych.sosa.main.SOSASimpleUpdatable;
import edu.gsu.psych.sosa.main.SOSATexture;

public class ExperimentCreatorImageComposite extends Composite implements SOSASimpleUpdatable{

	private Group appearanceGroup = null;
	private Label filePathLabel = null;
	private Composite labelColorSliderComposite = null;
	private Label labelRedSliderLabel = null;
	private Slider labelRedSlider = null;
	private Label labelRedValueLabel = null;
	private Label labelGreenSliderLabel = null;
	private Slider labelGreenSlider = null;
	private Label labelGreenValueLabel = null;
	private Label labelBlueSliderLabel = null;
	private Slider labelBlueSlider = null;
	private Label labelBlueValueLabel = null;
	private Label labelColorLabel = null;
	private Label valueLabel = null;
	private Label defaultLabel = null;
	private Button labelColorDefaultCheckBox = null;
	private Combo stimSizeCombo = null;
	private Label SizeLabel = null;
	private Button browseFilePathButton = null;
	private SOSAWindowExperiment parentWindow;
	private Composite fileNameComposite = null;
	private Label filePathTextlabel = null;
	private Slider imageSizeSlider;
	private Label imageSizeLabel;
	public ExperimentCreatorImageComposite(Composite parent, int style, SOSAWindowExperiment parentWindow){
		super(parent, style);
		this.parentWindow = parentWindow;
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 0;
		gridLayout1.marginWidth = 0;
		createAppearanceGroup();
		this.setLayout(gridLayout1);
	}

	/**
	 * This method initializes appearanceGroup	
	 *
	 */
	private void createAppearanceGroup() {
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.CENTER;
		gridData6.verticalAlignment = GridData.CENTER;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.CENTER;
		gridData5.verticalAlignment = GridData.CENTER;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.CENTER;
		gridData4.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.horizontalAlignment = GridData.END;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessVerticalSpace = false;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.horizontalSpacing = 5;
		appearanceGroup = new Group(this, SWT.NONE);
		appearanceGroup.setText("Appearance");
		appearanceGroup.setLayoutData(gridData1);
		appearanceGroup.setLayout(gridLayout);
		@SuppressWarnings("unused")
		Label filler2 = new Label(appearanceGroup, SWT.NONE);
		valueLabel = new Label(appearanceGroup, SWT.NONE);
		valueLabel.setText("Value");
		valueLabel.setLayoutData(gridData4);
		defaultLabel = new Label(appearanceGroup, SWT.NONE);
		defaultLabel.setText("Default");
		defaultLabel.setLayoutData(gridData5);
		labelColorLabel = new Label(appearanceGroup, SWT.NONE);
		labelColorLabel.setText("Label Color");
		createLabelColorSliderComposite();
		labelColorDefaultCheckBox = new Button(appearanceGroup, SWT.CHECK);
		labelColorDefaultCheckBox.setLayoutData(gridData6);
		labelColorDefaultCheckBox
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						setLabelColorToDefault();
					}
				});
		filePathLabel = new Label(appearanceGroup, SWT.NONE);
		filePathLabel.setText("File Name:");
		createFileNameComposite();
		SizeLabel = new Label(appearanceGroup, SWT.NONE);
		SizeLabel.setText("Size / Type");
		
		Composite composite = new Composite(appearanceGroup, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		imageSizeSlider = new Slider(composite, SWT.NONE);
		imageSizeSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(parentWindow.getExperiment().getSelectedPeg() != null){
					float temp = 2f + (((float)imageSizeSlider.getSelection())/10f);
					imageSizeLabel.setText(""+temp);
					((ImageStim)parentWindow.getExperiment().getSelectedPeg()).setSize(temp-2);
				}
			}
		});
		imageSizeSlider.setMaximum(80);
		imageSizeSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		imageSizeSlider.setThumb(1);
		imageSizeSlider.setPageIncrement(1);
		
		imageSizeLabel = new Label(composite, SWT.NONE);
		imageSizeLabel.setText("2.0");
	}

	/**
	 * This method initializes labelColorSliderComposite	
	 *
	 */
	private void createLabelColorSliderComposite() {
		GridData gridData1611 = new GridData();
		gridData1611.horizontalAlignment = GridData.FILL;
		gridData1611.verticalAlignment = GridData.CENTER;
		GridData gridData1511 = new GridData();
		gridData1511.horizontalAlignment = GridData.FILL;
		gridData1511.verticalAlignment = GridData.CENTER;
		GridData gridData1411 = new GridData();
		gridData1411.horizontalAlignment = GridData.FILL;
		gridData1411.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout61 = new GridLayout();
		gridLayout61.numColumns = 3;
		gridLayout61.marginHeight = 0;
		gridLayout61.marginWidth = 2;
		gridLayout61.verticalSpacing = 0;
		gridLayout61.horizontalSpacing = 0;
		labelColorSliderComposite = new Composite(appearanceGroup, SWT.NONE);
		labelColorSliderComposite.setLayout(gridLayout61);
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
		labelRedValueLabel.setLayoutData(gridData1411);
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
		labelGreenValueLabel.setLayoutData(gridData1511);
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
		labelBlueValueLabel.setLayoutData(gridData1611);
		//This sets the sliders of the experiment set up
		
	}

	/**
	 * This method initializes fileNameComposite	
	 *
	 */
	private void createFileNameComposite() {
		GridData gridData15 = new GridData();
		gridData15.horizontalAlignment = GridData.FILL;
		gridData15.grabExcessHorizontalSpace = true;
		gridData15.verticalAlignment = GridData.CENTER;
		GridData gridData14 = new GridData();
		gridData14.horizontalSpan = 2;
		gridData14.verticalAlignment = GridData.CENTER;
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gridLayout3.marginHeight = 0;
		gridLayout3.marginWidth = 0;
		GridData gridData2 = new GridData();
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.horizontalAlignment = GridData.END;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = false;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		gridLayout2.verticalSpacing = 5;
		gridLayout2.marginHeight = 5;
		gridLayout2.marginWidth = 0;
		fileNameComposite = new Composite(appearanceGroup, SWT.NONE);
		fileNameComposite.setLayout(gridLayout3);
		fileNameComposite.setLayoutData(gridData14);
		filePathTextlabel = new Label(fileNameComposite, SWT.NONE);
		filePathTextlabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		filePathTextlabel.setAlignment(SWT.RIGHT);
		filePathTextlabel.setLayoutData(gridData15);
		filePathTextlabel.setText("No File Selected");
		browseFilePathButton = new Button(fileNameComposite, SWT.NONE);
		browseFilePathButton.setText("Browse");
		browseFilePathButton.setLayoutData(gridData2);
		//Sets up the Experiment set up
		browseFilePathButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent selectionEvent){
				if(parentWindow.getExperiment() != null && parentWindow.getExperiment().getSelectedPeg() != null){
					FileDialog fileDialog = new FileDialog(parentWindow.getMainShell());
					fileDialog.setText("Select an image file");
					fileDialog.setFilterExtensions(new String[]{"*.jpg;*.png"});
					String temp = fileDialog.open();
//					if(temp != null){
//						File file = new File(temp);
//						filePathTextlabel.setText(file.getName());
//						try {
//							((edu.gsu.psych.sosa.experiment.stimulus.ImageStim) parentWindow.getExperiment().getSelectedPeg()).setTexture(file);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
					if(temp != null) {
						try {
							SOSATexture texture = SOSAFileHandler.readTextureFile(temp);
							filePathTextlabel.setText(texture.getPath());
							((ImageStim) parentWindow.getExperiment().getSelectedPeg()).setTexture(texture);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public void setLabelColorToDefault(){
		if(parentWindow.getExperiment() != null && parentWindow.getExperiment().getSelectedPeg() != null){
			setLabelColorSliders(parentWindow.getExperiment().getSelectedPeg().labelColorDefault);
			parentWindow.getExperiment().getSelectedPeg().revertLabelColor();
			labelColorDefaultCheckBox.setSelection(true);
			//Sets default color
		}
	}
	
	private void setLabelColorSliders(ColorRGBA rgba){
		labelRedSlider.setSelection((int)(rgba.getRed()*255f));
		labelGreenSlider.setSelection((int)(rgba.getGreen()*255f));
		labelBlueSlider.setSelection((int)(rgba.getBlue()*255f));
		
		labelRedValueLabel.setText(""+(int)(rgba.getRed()*255f));
		labelGreenValueLabel.setText(""+(int)(rgba.getGreen()*255f));
		labelBlueValueLabel.setText(""+(int)(rgba.getBlue()*255f));
		//Sets the colors of the slider
	}
	
	private void updateImageSettingsArea() {
		if(!(parentWindow.getExperiment().getSelectedPeg() instanceof ImageStim))
			return;
		ImageStim image = (ImageStim) parentWindow.getExperiment().getSelectedPeg();
		if(image != null){
			setLabelColorSliders(image.labelColor);
			
			if(image.labelColorDefault.equals(image.labelColor))
				labelColorDefaultCheckBox.setSelection(true);
			else
				labelColorDefaultCheckBox.setSelection(false);
			
			filePathTextlabel.setText(image.getFileName());
			
			imageSizeSlider.setSelection((int)(image.getTypeIndex()*10));
			imageSizeLabel.setText(""+((float)image.getTypeIndex()+2));
			//Updates the setting of the experiment
		}
	}

	@Override
	public void updateDetails() {
		updateImageSettingsArea();
	}
}
