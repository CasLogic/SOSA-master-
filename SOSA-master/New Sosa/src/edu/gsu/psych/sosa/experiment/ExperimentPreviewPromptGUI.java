package edu.gsu.psych.sosa.experiment;

import org.eclipse.swt.widgets.Shell;

import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.main.SOSAWindow;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ExperimentPreviewPromptGUI implements SOSAWindow {

	protected Shell sShell;

	public ExperimentPreviewPromptGUI() {
		createShell();
		sShell.open();
		sShell.layout();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createShell() {
		sShell = new Shell();
		sShell.setSize(300, 200);
		sShell.setText("Prompt");
		sShell.setLayout(new GridLayout(1, false));
		
		Button btnNewButton = new Button(sShell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SOSAMain.setNewActiveWindow(SOSAMain.EXPERIMENT_PREVIEW_SUBJECT);
				sShell.dispose();
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_btnNewButton.heightHint = 75;
		gd_btnNewButton.widthHint = 200;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("View Stimuli");
		//Creates the View Stimuli window
	}

	@Override
	public Shell getMainShell() {
		return sShell;
		//Returns the shell
	}

	@Override
	public void updateDetails() {
		// TODO Auto-generated method stub
		
	}
}
