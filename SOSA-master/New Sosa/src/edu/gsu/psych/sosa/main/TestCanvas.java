package edu.gsu.psych.sosa.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.Camera.ProjectionMode;
import com.ardor3d.scenegraph.Line;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Group;

public class TestCanvas {
	private Text text;
	private Text positionX;
	private Text positionY;
	private Text positionZ;
	private TestCanvasImpl testCanvasImpl;
	private Text cameraPosX;
	private Text cameraPosY;
	private Text cameraPosZ;
	private Text cameraDirX;
	private Text cameraDirY;
	private Text cameraDirZ;
	private Button objectAddButton;
	private Text cameraFrusTop;
	private Text cameraFrusLeft;
	private Text cameraFrusRight;
	private Text cameraFrusBottom;
	private Text cameraFrusNear;
	private Text cameraFrusFar;
	private Text boardPosX;
	private Text boardPosY;
	private Text boardPosZ;
	private Text boardRotX;
	private Text boardRotY;
	private Text boardRotZ;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestCanvas window = new TestCanvas();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		/*
		 * Create a display and shell and set size and text for shell
		 */
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(700, 650);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(4, false));
		shell.setMinimumSize(shell.getSize());
//		shell.setVisible(true);
		
		/*
		 * Create a new composite and set Layout and LayoutData
		 */
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		/*
		 * Create a GLData and set depthSize and doubleBuffer
		 */
		final GLData data = new GLData();
		data.depthSize = 8;
		data.doubleBuffer = true;
		
		/*
		 * Create an swtCanvas and set testCanvasImpl to new TestCanvasImpl using swtCanvas
		 */
		SwtCanvas swtCanvas = new SwtCanvas(composite, SWT.NONE, data);
		testCanvasImpl = new TestCanvasImpl(swtCanvas);
		
		/*
		 * Create a new group for stim controls
		 */
		Group grpNewObject = new Group(shell, SWT.NONE);
		grpNewObject.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 2));
		grpNewObject.setText("Stim");
		grpNewObject.setLayout(new GridLayout(8, false));
		
		/*
		 * Create a new label for stim label label
		 */
		Label lblLabel = new Label(grpNewObject, SWT.NONE);
		lblLabel.setSize(25, 13);
		lblLabel.setText("Label");
		
		/*
		 * Create a new text box for stim label input
		 */
		text = new Text(grpNewObject, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 7, 1));
		text.setSize(282, 19);
		
		/*
		 * Create a new label for stim position label
		 */
		Label lblPosition = new Label(grpNewObject, SWT.NONE);
		lblPosition.setSize(37, 13);
		lblPosition.setText("Position");
		
		/*
		 * Create a new label for stim x position label
		 */
		Label lblX = new Label(grpNewObject, SWT.NONE);
		lblX.setSize(10, 13);
		lblX.setText("X:");
		
		/*
		 * Create a new text box for stim x position input
		 */
		positionX = new Text(grpNewObject, SWT.BORDER);
		positionX.setText("0");
		GridData gd_positionX = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_positionX.widthHint = 12;
		positionX.setLayoutData(gd_positionX);
		positionX.setSize(103, 19);
		
		/*
		 * Create a new label for stim y position label
		 */
		Label lblY = new Label(grpNewObject, SWT.NONE);
		lblY.setSize(10, 13);
		lblY.setText("Y:");
		
		/*
		 * Create a new text box for stim y position input
		 */
		positionY = new Text(grpNewObject, SWT.BORDER);
		positionY.setText("0");
		GridData gd_positionY = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_positionY.widthHint = 12;
		positionY.setLayoutData(gd_positionY);
		positionY.setSize(142, 19);
		
		/*
		 * Create a new label for stim z position label
		 */
		Label lblZ = new Label(grpNewObject, SWT.NONE);
		lblZ.setSize(10, 13);
		lblZ.setText("Z:");
		
		/*
		 * Create a new text box for stim z position input
		 */
		positionZ = new Text(grpNewObject, SWT.BORDER);
		positionZ.setText("0");
		GridData gd_positionZ = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_positionZ.widthHint = 13;
		positionZ.setLayoutData(gd_positionZ);
		positionZ.setSize(258, 19);
		
		/*
		 * Create new button for adding a new stim
		 */
		objectAddButton = new Button(grpNewObject, SWT.NONE);
		objectAddButton.setSize(31, 23);
		objectAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testCanvasImpl.attachPeg("stim1",new Stimulus(),
						Double.parseDouble(positionX.getText()),
						Double.parseDouble(positionY.getText()),
						Double.parseDouble(positionZ.getText()));
			}
		});
		objectAddButton.setText("Add");
		
		/*
		 * Create a new button for showing and hiding axis lines
		 */
		Button btnShowAxisLines = new Button(shell, SWT.NONE);
		btnShowAxisLines.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnShowAxisLines.setSize(88, 23);
		btnShowAxisLines.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testCanvasImpl.showAllAxis();
			}
		});
		btnShowAxisLines.setText("Show Axis Lines");
		
		/*
		 * Create a new button for changing the camera perspective
		 */
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testCanvasImpl.toggleCameraPerspective();
			}
		});
		btnNewButton_1.setText("Camera Perspective");
		
		/*
		 * Create a new group for camera controls
		 */
		Group grpCamera = new Group(shell, SWT.NONE);
		GridData gd_grpCamera = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 2);
		gd_grpCamera.widthHint = 100;
		grpCamera.setLayoutData(gd_grpCamera);
		grpCamera.setText("Camera");
		grpCamera.setLayout(new GridLayout(8, false));
		
		/*
		 * Create new labels for camera position labels
		 */
		Label lblPosition_1 = new Label(grpCamera, SWT.NONE);
		lblPosition_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPosition_1.setText("Position");
		
		Label lblDirection = new Label(grpCamera, SWT.NONE);
		lblDirection.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblDirection.setText("Direction");
		
		Label lblFrustrum = new Label(grpCamera, SWT.NONE);
		lblFrustrum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		lblFrustrum.setText("Frustum");
		
		Label lblX_1 = new Label(grpCamera, SWT.NONE);
		lblX_1.setText("X:");		
		cameraPosX = new Text(grpCamera, SWT.BORDER);
		cameraPosX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cameraPosX.setText("0");
		
		Label lblX_2 = new Label(grpCamera, SWT.NONE);
		lblX_2.setText("X:");		
		cameraDirX = new Text(grpCamera, SWT.BORDER);
		cameraDirX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cameraDirX.setText("0");
		
		Label lblX_3 = new Label(grpCamera, SWT.NONE);
		lblX_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblX_3.setText("T:");		
		cameraFrusTop = new Text(grpCamera, SWT.BORDER);
		cameraFrusTop.setText("0");
		cameraFrusTop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(grpCamera, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("B:");		
		cameraFrusBottom = new Text(grpCamera, SWT.BORDER);
		cameraFrusBottom.setText("0");
		cameraFrusBottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblY_1 = new Label(grpCamera, SWT.NONE);
		lblY_1.setText("Y:");		
		cameraPosY = new Text(grpCamera, SWT.BORDER);
		cameraPosY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cameraPosY.setText("0");
		
		Label lblY_2 = new Label(grpCamera, SWT.NONE);
		lblY_2.setText("Y:");		
		cameraDirY = new Text(grpCamera, SWT.BORDER);
		cameraDirY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cameraDirY.setText("0");
		
		Label lblY_3 = new Label(grpCamera, SWT.NONE);
		lblY_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblY_3.setText("L:");		
		cameraFrusLeft = new Text(grpCamera, SWT.BORDER);
		cameraFrusLeft.setText("0");
		cameraFrusLeft.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(grpCamera, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("N:");		
		cameraFrusNear = new Text(grpCamera, SWT.BORDER);
		cameraFrusNear.setText("0");
		cameraFrusNear.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblZ_1 = new Label(grpCamera, SWT.NONE);
		lblZ_1.setText("Z:");		
		cameraPosZ = new Text(grpCamera, SWT.BORDER);
		cameraPosZ.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cameraPosZ.setText("0");
		
		Label lblZ_2 = new Label(grpCamera, SWT.NONE);
		lblZ_2.setText("Z:");		
		cameraDirZ = new Text(grpCamera, SWT.BORDER);
		cameraDirZ.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cameraDirZ.setText("0");
		
		Label lblZ_3 = new Label(grpCamera, SWT.NONE);
		lblZ_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblZ_3.setText("R:");		
		cameraFrusRight = new Text(grpCamera, SWT.BORDER);
		cameraFrusRight.setText("0");
		cameraFrusRight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(grpCamera, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("F:");		
		cameraFrusFar = new Text(grpCamera, SWT.BORDER);
		cameraFrusFar.setText("0");
		cameraFrusFar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		/*
		 * Create a button to refresh the camera display controls
		 */
		Button btnRefresh = new Button(grpCamera, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateCameraDisplay();
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnRefresh.setText("Refresh");
		new Label(grpCamera, SWT.NONE);
		
		/*
		 * Create a new button to set the desired camera position
		 */
		Button btnNewButton = new Button(grpCamera, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testCanvasImpl.moveCamera(
						new Vector3(
								Double.parseDouble(cameraPosX.getText()),
								Double.parseDouble(cameraPosY.getText()),
								Double.parseDouble(cameraPosZ.getText())),
						new Vector3(
								Double.parseDouble(cameraDirX.getText()),
								Double.parseDouble(cameraDirY.getText()),
								Double.parseDouble(cameraDirZ.getText())));
				testCanvasImpl.setCameraFrustum(
						Double.parseDouble(cameraFrusTop.getText()),
						Double.parseDouble(cameraFrusBottom.getText()),
						Double.parseDouble(cameraFrusLeft.getText()),
						Double.parseDouble(cameraFrusRight.getText()),
						Double.parseDouble(cameraFrusNear.getText()),
						Double.parseDouble(cameraFrusFar.getText()));
				updateCameraDisplay();
			}
		});
		btnNewButton.setText("Set");
		
		/*
		 * Create a button to revert the camera position inputs
		 */
		Button btnRevert = new Button(grpCamera, SWT.NONE);
		btnRevert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testCanvasImpl.revertCamera();
				updateCameraDisplay();
			}
		});
		btnRevert.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		btnRevert.setText("Revert");
		
		/*
		 * Create a grouping for the board
		 */
		Group grpBoard = new Group(shell, SWT.NONE);
		grpBoard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpBoard.setText("Board");
		grpBoard.setLayout(new GridLayout(4, false));
		
		/*
		 * Create labels for the board position information
		 */
		Label lblPosition_2 = new Label(grpBoard, SWT.NONE);
		lblPosition_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPosition_2.setText("Position");
		
		Label lblRotation = new Label(grpBoard, SWT.NONE);
		lblRotation.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblRotation.setText("Rotation");
		
		Label lblX_4 = new Label(grpBoard, SWT.NONE);
		lblX_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblX_4.setText("X:");		
		boardPosX = new Text(grpBoard, SWT.BORDER);
		boardPosX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		boardPosX.setText("100");
		
		Label lblX_5 = new Label(grpBoard, SWT.NONE);
		lblX_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblX_5.setText("X:");		
		boardRotX = new Text(grpBoard, SWT.BORDER);
		boardRotX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblY_4 = new Label(grpBoard, SWT.NONE);
		lblY_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblY_4.setText("Y:");		
		boardPosY = new Text(grpBoard, SWT.BORDER);
		boardPosY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		boardPosY.setText("100");
		
		Label lblY_5 = new Label(grpBoard, SWT.NONE);
		lblY_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblY_5.setText("Y:");		
		boardRotY = new Text(grpBoard, SWT.BORDER);
		boardRotY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblZ_4 = new Label(grpBoard, SWT.NONE);
		lblZ_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblZ_4.setText("Z:");		
		boardPosZ = new Text(grpBoard, SWT.BORDER);
		boardPosZ.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		boardPosZ.setText("-100");
		
		Label lblZ_5 = new Label(grpBoard, SWT.NONE);
		lblZ_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblZ_5.setText("Z:");		
		boardRotZ = new Text(grpBoard, SWT.BORDER);
		boardRotZ.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		/*
		 * Create a button for adding the board to the display
		 */
		Button btnAdd = new Button(grpBoard, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImageStim i = new ImageStim();
				i.setSize(94f);
				testCanvasImpl.attachPeg("board", i,
						Double.parseDouble(boardPosX.getText()),
						Double.parseDouble(boardPosY.getText()),
						Double.parseDouble(boardPosZ.getText()));
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnAdd.setText("Add");
		
		/*
		 * Create a button for auto rotating the board
		 */
		Button btnAutorotate = new Button(grpBoard, SWT.NONE);
		btnAutorotate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnAutorotate.setText("AutoRotate");
		
		/*
		 * Open the shell
		 */
		shell.open();
		shell.layout();
		
		/*
		 * While shell is running, update detials
		 */
		while (!shell.isDisposed()) {
			testCanvasImpl.getFrameHandler().updateFrame();
			updateDetails();
			display.readAndDispatch();
			Thread.yield();
		}
	}
	
	/**
	 * This updates the details for the given position inputs
	 */
	private void updateDetails() {
		//updateCameraDisplay();
		
		if(!positionX.getText().equals("") && !positionY.getText().equals("") && !positionZ.getText().equals(""))
			objectAddButton.setEnabled(true);
		else
			objectAddButton.setEnabled(false);
	}
	
	/**
	 * Updates the camera display
	 * Not actually used - I think
	 */
	public void updateCameraDisplay(){
		cameraPosX.setText(String.valueOf(testCanvasImpl.getCamera().getLocation().getX()));
		cameraPosY.setText(String.valueOf(testCanvasImpl.getCamera().getLocation().getY()));
		cameraPosZ.setText(String.valueOf(testCanvasImpl.getCamera().getLocation().getZ()));
		cameraDirX.setText(String.valueOf(testCanvasImpl.getCamera().getDirection().getX()));
		cameraDirY.setText(String.valueOf(testCanvasImpl.getCamera().getDirection().getY()));
		cameraDirZ.setText(String.valueOf(testCanvasImpl.getCamera().getDirection().getZ()));
		cameraFrusBottom.setText(String.valueOf((testCanvasImpl.getCamera().getFrustumBottom())));
		cameraFrusRight.setText(String.valueOf(testCanvasImpl.getCamera().getFrustumRight()));
		cameraFrusLeft.setText(String.valueOf(testCanvasImpl.getCamera().getFrustumLeft()));
		cameraFrusTop.setText(String.valueOf(testCanvasImpl.getCamera().getFrustumTop()));
		cameraFrusNear.setText(String.valueOf(testCanvasImpl.getCamera().getFrustumNear()));
		cameraFrusFar.setText(String.valueOf(testCanvasImpl.getCamera().getFrustumFar()));
	}

	class TestCanvasImpl extends SOSAArdorCanvas{		
		boolean xAxisVisable = false;
		boolean yAxisVisable = false;
		boolean zAxisVisable = false;

		/**
		 * Creates a test canvas
		 * @param canvas
		 */
		public TestCanvasImpl(SwtCanvas canvas) {
			super(canvas);
	//		attachPeg(new Stimulus());
		}

		/**
		 * Shows the each individual axis
		 */
		public void showAllAxis(){
			showAxis('x');
			showAxis('y');
			showAxis('z');
		}
		
		/**
		 * Displays the vectors for the axis passed in through axis
		 * @param axis
		 */
		public void showAxis(char axis){
			Vector3[] points = new Vector3[2];
			String label = "";
			boolean makeLine = false;
			switch (axis) {
			case 'x':
				label = "X Axis";
				if(xAxisVisable){
					rootNode.getChild(label).removeFromParent();
					xAxisVisable = false;
				}else{
					points[0] = new Vector3(-10,0,0);
					points[1] = new Vector3(10,0,0);
					makeLine = true;
					xAxisVisable = true;
				}
				break;
			case 'y':
				label = "Y Axis";
				if(yAxisVisable){
					rootNode.getChild(label).removeFromParent();
					yAxisVisable = false;
				}else{
					points[0] = new Vector3(0,-10,0);
					points[1] = new Vector3(0,10,0);
					makeLine = true;
					yAxisVisable = true;
				}
				break;
			case 'z':
				label = "Z Axis";
				if(zAxisVisable){
					rootNode.getChild(label).removeFromParent();
					zAxisVisable = false;
				}else{
					points[0] = new Vector3(0,0,-10);
					points[1] = new Vector3(0,0,10);
					makeLine = true;
					zAxisVisable = true;
				}
				break;
			default:
				break;
			}
			if(makeLine){
				Line axisLine = new Line(label,points,null,null,null);
				axisLine.setLineWidth(1);
				rootNode.attachChild(axisLine);
			}
		}
		
		/**
		 * Attaches a peg with the given id and stim object at the given position
		 * @param ID
		 * @param stim
		 * @param posX
		 * @param posY
		 * @param posZ
		 */
		private void attachPeg(String ID, Stimulus stim, double posX, double posY, double posZ){
//			this.stim = stim;
			if(stim instanceof ImageStim)
				((ImageStim)stim).setCamera(getCamera());
			stim.getRender().setTranslation(posX, posY, posZ);
			stim.setRenderID(ID);
			rootNode.attachChild(stim.getRender());
			
//			attachPegLabel(stim);
			if(SOSAMain.DEBUG)
				stim.logStatus();
		}

		/**
		 * Positions the camera as long as it is not null
		 * @param cam
		 */
		@Override
		protected void positionCamera(Camera cam) {
			if(cam != null){
				cam.setLocation(new Vector3(0, -10, 10));
				cam.lookAt(Vector3.ZERO, Vector3.UNIT_Z);
			}
		}
		
		/**
		 * Positions the camera to original
		 */
		public void revertCamera() {
			positionCamera(getCamera());
		}
		
		/**
		 * Moves the camera based on position and direction
		 * @param pos
		 * @param dir
		 */
		public void moveCamera(ReadOnlyVector3 pos, ReadOnlyVector3 dir){
			getCamera().setLocation(pos);
			getCamera().setDirection(dir);
		}
		
		/**
		 * Changes camera mode between perspective and parallel
		 */
		public void toggleCameraPerspective() {
			if(getCamera().getProjectionMode() == ProjectionMode.Perspective)
				getCamera().setProjectionMode(ProjectionMode.Parallel);
			else
				getCamera().setProjectionMode(ProjectionMode.Perspective);
			getCamera().update();
		}
		
		/**
		 * Sets the camera frustrum given the inputs
		 * @param top
		 * @param bottom
		 * @param left
		 * @param right
		 * @param near
		 * @param far
		 */
		public void setCameraFrustum(double top, double bottom, double left, double right, double near, double far) {
			getCamera().setFrustum(near, far, left, right, top, bottom);
		}
		
		@Override
		protected void extraSetup() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void extraUpdates(double tpf) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * Parent function for controlMoved and controlResized
		 */
	    public ControlListener newResizeHandler(final SwtCanvas swtCanvas, final CanvasRenderer canvasRenderer) {
	        final ControlListener retVal = new ControlListener() {
	            public void controlMoved(final ControlEvent e) {}

	            /**
	             * Sets canvas size and distances camera accordingly
	             * @param ControlEvent event
	             */
	            public void controlResized(final ControlEvent event) {
	                final Rectangle size = swtCanvas.getClientArea();
	                if ((size.width == 0) && (size.height == 0)) {
	                    return;
	                }
	                final float aspect = (float) size.width / (float) size.height;
	                final Camera camera = canvasRenderer.getCamera();
	                if (camera != null) {
	                    final float fovY = 45; // XXX no camera.getFov()
	                    final double near = camera.getFrustumNear();
	                    final double far = camera.getFrustumFar();
	                    // camera.setFrustumPerspective(fovY, aspect, near, far);
	                    
	                    final double h = Math.tan(fovY * MathUtils.DEG_TO_RAD * .5) * near;
	                    final double w = h * aspect;
	                    final double lateralskew = w/2;
	                    final double left = -w + lateralskew;
	                    final double right = w + lateralskew;
	                    final double bottom = -h;
	                    final double top = h;
	                    
	                    camera.setFrustum(near, far, left, right, top, bottom);                    
	                    camera.resize(size.width, size.height);
	                }
	            }
	        };
	        return retVal;
	    }
	}
}
