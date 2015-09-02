package edu.gsu.psych.sosa.experiment;

import java.awt.AWTException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ardor3d.framework.swt.SwtCanvas;

import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.main.SOSAWindow;

public class ExperimentPreviewSubjectGUI implements SOSAWindow{

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="213,32"
	private Button beginButton = null;
	private Text subjectIDText = null;
	private Label subjectIDLabel = null;
	private Composite previewExperimentCanvasComposite = null;
	private SwtCanvas previewExperimentCanvas = null;
	private ExperimentPreviewCanvasImpl previewExperimentCanvasImpl = null;
	private ScyhMod Sych;
	public String[]  Hotkey;
	public ExperimentPreviewSubjectGUI(){
		createSShell();
		sShell.pack();
		sShell.setMinimumSize(sShell.getSize());
		sShell.setLocation(0,0);
		sShell.open();
		Hotkey = Sych.setHotKey();
		SOSAMain.experiment.logInitialPresentation();
	}

	/**
	 * This method initializes sShell	
	 *
	 */
	private void createSShell() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.END;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		sShell = new Shell();
		sShell.setText("Experiment Preview");
		sShell.setLayout(gridLayout);
		createPreviewExperimentCanvasComposite();
		subjectIDLabel = new Label(sShell, SWT.NONE);
		subjectIDLabel.setText("Enter ID:");
		subjectIDLabel.setLayoutData(gridData1);
		subjectIDText = new Text(sShell, SWT.BORDER);
		subjectIDText.setLayoutData(gridData);
		beginButton = new Button(sShell, SWT.NONE);
		beginButton.setText("Begin Experiment");
		beginButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(!subjectIDText.getText().equals("")){
					SOSAMain.experiment.subject = subjectIDText.getText();
					SOSAMain.setNewActiveWindow(SOSAMain.EXPERIMENT_MAIN);
					try {
						
						Sych.pressHotKey(Hotkey);
					} catch (AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					sShell.dispose();
				}					
			}
		});
	}

	/**
	 * This method initializes previewExperimentCanvasComposite	
	 *
	 */
	private void createPreviewExperimentCanvasComposite() {
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 3;
		gridData2.verticalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.heightHint = 600;
		gridData2.widthHint = 600;
		gridData2.horizontalAlignment = GridData.FILL;
		previewExperimentCanvasComposite = new Composite(sShell, SWT.NONE);
		previewExperimentCanvasComposite.setLayout(new FillLayout());
		previewExperimentCanvasComposite.setLayoutData(gridData2);
		createPreviewCanvas();
		//Sets the layout of the canvas Preview
	}
	
	private void createPreviewCanvas(){
		final GLData data = new GLData();
		data.depthSize = 8;
		data.doubleBuffer = true;
		
		previewExperimentCanvas = new SwtCanvas(previewExperimentCanvasComposite, SWT.NONE, data);
		previewExperimentCanvasImpl = new ExperimentPreviewCanvasImpl(previewExperimentCanvas);
		//Creates the Experiment Canvas preview
	}

	@Override
	public Shell getMainShell() {
		return sShell;
		//returns the "MainShell
	}
	
	@Override
	public void updateDetails() {
		previewExperimentCanvasImpl.getFrameHandler().updateFrame();
		//Updates the previewExperimentCanvasImpl
	}
}
