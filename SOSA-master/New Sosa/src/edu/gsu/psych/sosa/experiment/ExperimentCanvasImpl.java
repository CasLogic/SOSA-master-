package edu.gsu.psych.sosa.experiment;

import java.util.ArrayList;

import com.ardor3d.framework.Canvas;
import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.input.Key;
import com.ardor3d.input.MouseButton;
import com.ardor3d.input.logical.AnyKeyCondition;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyHeldCondition;
import com.ardor3d.input.logical.MouseButtonClickedCondition;
import com.ardor3d.input.logical.MouseButtonReleasedCondition;
import com.ardor3d.input.logical.MouseWheelMovedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TriggerConditions;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.intersection.Pickable;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Plane;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.extension.CameraNode;
import com.ardor3d.util.Timer;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAArdorCanvas;
import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.util.background.Delegate;

public class ExperimentCanvasImpl extends SOSAArdorCanvas{

	long startTime = 0;
	long fps = 0;

	private java.util.List<Stimulus> attachedStims = new ArrayList<Stimulus>();
	private Spatial selectedStim = null;
	private Stimulus selectedStimObject = null;
	private int stimUpdateCounter = 0;
	private Node stimNode;	//this will be set after init() below creates it, so can't be null here 
	private float camX = 0;
	private float camY = 0;
	private Node pivotCameraNode;
	private Node panCameraNode;
	private CameraNode camNode;
	private boolean settingSC = false;

//	public ExperimentCanvasImpl(int width, int height, SosaAbsoluteMouse mouse) {
//		super(width, height);
//		localSize = new Point(width, height);
//		originalLocalWidth = localSize.x;
//		originalLocalHeight = mouseHeight = localSize.y;
//		ratio = ((float)width) / ((float)height);
//		mouseWrapper = mouse;
//	}
	
	public ExperimentCanvasImpl(final SwtCanvas canvas){
		super(canvas);
	}
	
	public ExperimentCanvasImpl(final SwtCanvas canvas, boolean aspectRatioLocked){
		super(canvas, aspectRatioLocked);
	}
	
	public ExperimentCanvasImpl(final SwtCanvas canvas, boolean aspectRatioLocked, int ratioX, int ratioY){
		super(canvas, aspectRatioLocked, ratioX, ratioY);
	}
	
//	public void resize(){
//		resizeTrigger2  = true;
//		resizeTriggerTime = System.currentTimeMillis();
//	}

//	public void resize(int newWidth, int newHeight){
//		localSize.x = newWidth;
//		localSize.y = newHeight;
//		mouseHeight  = localSize.y;
//		preserveRatio(localSize, ratio);
//		resizeTrigger = true;	//tells the update thread to resize next pass
//	}
	
	protected void positionCamera(Camera cam){
		if(cam != null){
			getCameraPivot().setRotation(new Quaternion());
			panCameraNode.setTranslation(Vector3.ZERO);
			camX = camY = 0;
		}
	}
	
	public void extraSetup() {
		setupStimNode();
		attachBoard();

		attachMouse();
		attachKeyboard();
	}
	
	private void setupStimNode(){
		stimNode = new Node("stimNode");
		rootNode.attachChild(stimNode);
	}
	
	public void extraUpdates(double tpf) {
//		rootNode.updateWorldTransform(true);
//		rootNode.updateGeometricState(tpf);
		
		updateImageStimFacing();
		
		for(Stimulus stim : attachedStims){
			stim.getRender().updateGeometricState(tpf);
			stim.getRenderLabel().updateGeometricState(tpf);
		}
		
		if(!settingSC ){
			getCameraPivot().updateWorldTransform(true);
			getCamera().lookAt(panCameraNode.getWorldTranslation(), Vector3.UNIT_Z);
		}

		if(SOSAMain.experiment != null)
			SOSAMain.experiment.updateLabelPositions(rootNode, getCamera());
	}
	
	protected void showStatus(){
		if(statusTimer == null)
			statusTimer = new Timer();
		
		if(statusTimer.getTimeInSeconds() > statusNextCheck){
			statusNextCheck += statusCheckInterval;
			
			for(Stimulus stim : attachedStims)
				stim.logStatus();
			
			System.out.println(getCamera());
		}		
	}
	
	private void updateImageStimFacing(){
		for(Stimulus stim : attachedStims)
			if(stim instanceof ImageStim){
				if(SOSAMain.endOfExperiment)
					stim.faceUp();
				else
					((ImageStim)stim).faceCamera();
			}
	}
	
	private void attachKeyboard(){
		logicalLayer.registerTrigger(new InputTrigger(new KeyHeldCondition(Key.UP), new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				getCamNode().setTranslation(getCamNode().getTranslation().multiply(0.99d, null));
			}
		}));
		logicalLayer.registerTrigger(new InputTrigger(new KeyHeldCondition(Key.DOWN), new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				getCamNode().setTranslation(getCamNode().getTranslation().multiply(1.01d, null));
			}
		}));
		
		if(SOSAMain.DEBUG){
		    logicalLayer.registerTrigger(new InputTrigger(new AnyKeyCondition(), new TriggerAction() {
	            public void perform(final Canvas source, final TwoInputStates inputState, final double tpf) {
	                System.out.println("Key character pressed: "
	                        + inputState.getCurrent().getKeyboardState().getKeyEvent().getKeyChar());
	            }
	        }));
		}
	}
	
	private void attachMouse(){
		//pick and drag stims
		final Predicate<TwoInputStates> mouseMovedAndLeftButtonPressed = Predicates.and(TriggerConditions.mouseMoved(), TriggerConditions.leftButtonDown());
		logicalLayer.registerTrigger(new InputTrigger(mouseMovedAndLeftButtonPressed, new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				pickDragStim(inputState.getCurrent().getMouseState().getX(),inputState.getCurrent().getMouseState().getY());
			}
		}));

		//release stims
		logicalLayer.registerTrigger(new InputTrigger(new MouseButtonReleasedCondition(MouseButton.LEFT), new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				if(selectedStim != null){
					SOSAMain.experiment.logMoveDestination(selectedStim.getWorldTranslation().getXf()/4f, selectedStim.getWorldTranslation().getYf()/4f);	//div by 4 to get inches
					selectedStim = null;
				}
			}
		}));
		
		//rotate board
		final Predicate<TwoInputStates> mouseMovedAndRightButtonPressed = Predicates.and(TriggerConditions.mouseMoved(), TriggerConditions.rightButtonDown());
		logicalLayer.registerTrigger(new InputTrigger(mouseMovedAndRightButtonPressed, new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				float speed = 50f;
				int xDelta = inputState.getCurrent().getMouseState().getDx();
				int yDelta = inputState.getCurrent().getMouseState().getDy();
				camX += xDelta*tpf*speed;
				camY += yDelta*tpf*speed;
				camX = limit(30,camX);		//limit the rotation of the board
				camY = limit(30,camY);
				
				//inverted X to feel more natural
				getCameraPivot().setRotation(new Matrix3().fromAngles(camY*MathUtils.DEG_TO_RAD, 0, -camX*MathUtils.DEG_TO_RAD));
//				getCameraPivot().updateWorldTransform(true);
				
				if(SOSAMain.DEBUG){
					System.out.println("Xd: " + xDelta + " Yd: " + yDelta);
					System.out.println("X:  " + camX + " Y:  " + camY);
					SOSAArdorCanvas.logStatus("Pivot", getCameraPivot());
					SOSAArdorCanvas.logStatus("camera", getCamNode());
					System.out.println(getCamera());
				}
				
			}
		}));
		
		//zoom camera
		logicalLayer.registerTrigger(new InputTrigger(new MouseWheelMovedCondition(), new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				float dir = 1f;
				int delta = inputState.getCurrent().getMouseState().getDwheel();
				if(delta > 0)
					dir = 0.99f;
				else if(delta < 0)
					dir = 1.01f;
				getCamNode().setTranslation(getCamNode().getTranslation().multiply(dir,null));
			}
		}));
		
		//pan camera
		final Predicate<TwoInputStates> mouseMovedAndMiddleButtonPressed = Predicates.and(TriggerConditions.mouseMoved(), TriggerConditions.middleButtonDown());
		logicalLayer.registerTrigger(new InputTrigger(mouseMovedAndMiddleButtonPressed, new TriggerAction() {
			public void perform(Canvas source, TwoInputStates inputState, double tpf) {
				double xDelta = inputState.getCurrent().getMouseState().getDx();
				double yDelta = inputState.getCurrent().getMouseState().getDy();
				xDelta /= 2;
				yDelta /= 2;
				
				panCameraNode.setTranslation(panCameraNode.getTranslation().add(xDelta, yDelta, 0, null));
			}
		}));
		
		if(SOSAMain.DEBUG){
	        final Predicate<TwoInputStates> clickLeftOrRight = Predicates.or(new MouseButtonClickedCondition(
	                MouseButton.LEFT), new MouseButtonClickedCondition(MouseButton.RIGHT));	
	        logicalLayer.registerTrigger(new InputTrigger(clickLeftOrRight, new TriggerAction() {
	            public void perform(final Canvas source, final TwoInputStates inputStates, final double tpf) {
	                System.err.println("clicked: " + inputStates.getCurrent().getMouseState().getClickCounts());
	            }
	        }));
		}
	}
	
	private float limit(float limit, float test){
		if(test > limit)
			return limit;
		if(test < -limit)
			return -limit;
		return test;
	}
	
//	private void resizeMouse() {
//		am.setLimit(localSize.x, localSize.y);	//resizes the mouse bounds
//		
//		am.getXUpdateAction().setSpeed(((float) originalLocalWidth)  / ((float)localSize.x)  );	//compensates for stretched world
//	    am.getYUpdateAction().setSpeed(((float) originalLocalHeight) / ((float)localSize.y) );
//	    
////	    am.setLocalTranslation(new Vector3f(localWidth / 2, localHeight / 2, 0));	//puts cursor in the middle of the screen
//	    
//	    mouseWrapper.setCanvasHeight(mouseHeight);	//set new height so mouse inversion behaves properly
//	}
	
	/**
	 * Attaches the stim to the rootNode of this canvas (displaying it).
	 * A check is performed to be sure this stim is not already rendered,
	 * so it is safe to call this function any number of times.  The stim
	 * will only be rendered once.
	 */
	public void attachStim(){
		Stimulus s = SOSAMain.experiment.getSelectedPeg();
		String name = "stim"+SOSAMain.experiment.getCurrentPegIndex();
		if(stimNode.getChild(name) == null){
			if(s instanceof ImageStim){
				s.resetRender();	//re-init texture
				((ImageStim) s).setCamera(getCamera());
			}
			Spatial node = s.getRender();
			s.setRenderID(name);
			node.setTranslation(-56, 56, s.getLevelHeight());
			
			stimNode.attachChild(node);
			attachedStims.add(s);
			
		}
	}
	
	public void setScreenShotCamera(){
	//	revertCamera();
		
		settingSC = true;	//to stop normal camera updates that fuxor everything
		getCameraPivot().setRotation(new Quaternion());
		panCameraNode.setWorldTranslation(Vector3.ZERO);
		getCamNode().setTranslation(0,0,117);
		getCameraPivot().updateWorldTransform(true);
		getCamera().lookAt(Vector3.ZERO, Vector3.UNIT_Y);
	}
	
	public void makeScreenShot() {
		setScreenShotCamera();
		SOSAMain.performAfterWait(200, new Delegate() {
			@Override
			public void perform() {
				takeScreenShot();
				SOSAMain.shutdownReady = true;
			}//Takes Screenshot
		});
	}
	
//	public void makeScreenShot2(){
//		setScreenShotCamera();
//		
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
////				while(renderer.isProcessingQueue());
//				SOSAMain.cameraReady = true;
//			}
//		}).start();
//	}
	
	private void pickDragStim(int mouseX, int mouseY){	
		//set up a mouseRay
		final Vector2 pos = Vector2.fetchTempInstance().set(mouseX, mouseY);
		final Ray3 mouseRay = Ray3.fetchTempInstance();
		canvas.getCanvasRenderer().getCamera().getPickRay(pos, false, mouseRay);
		Vector2.releaseTempInstance(pos);		
		
		if(selectedStim == null) {		//if no stim is selected yet				
            //check if that ray intersects with anything interesting
			PickResults pr = doPick(mouseRay);
			
			Mesh temp;
			Pickable pick;
			for (int i = 0; i < pr.getNumber(); i++) {
				pick = pr.getPickData(i).getTarget();
				if(pick instanceof Mesh){
					temp = (Mesh)pick;
					for (Stimulus stim : attachedStims) {
						if(temp.getName().equals(stim.getRenderID())){
							selectedStim = stim.getRender();	//temp //found a stim !!!
							if(SOSAMain.DEBUG){
								selectedStimObject = stim;
								stimUpdateCounter = 0;
							}
							SOSAMain.experiment.selectPeg(stim);
							SOSAMain.experiment.logMoveOrigin(selectedStim.getWorldTranslation().getXf()/4f, selectedStim.getWorldTranslation().getYf()/4f);	// div by 4 to get inches
							i = pr.getNumber();		//break outer loop
							break;					//break inner loop
						}
					}
				}
			}
		}else{			//if there is already a selected stim
			Plane p = new Plane(Vector3.UNIT_Z, SOSAMain.experiment.getSelectedPeg().getLevelHeight());
			Vector3 location = new Vector3();
			if(mouseRay.intersects(p, location)){
				selectedStim.setTranslation(location);	//put the stim where the mouse is
				if(SOSAMain.DEBUG){
					System.out.println("pass " + ++stimUpdateCounter + ":");
					System.out.println("plane:        " + p);
					System.out.println("intersection: " + location);
					selectedStimObject.logStatus();
				}
			}
		}
		Ray3.releaseTempInstance(mouseRay);
	}
	
	private CameraNode getCamNode(){
		if(camNode == null){
			camNode = new CameraNode("camNode", getCamera());
			camNode.setTranslation(0, -96, 96);
		}
		return camNode;
		// Returns CamNode
	}
	
	private Node getCameraPivot(){
		if(pivotCameraNode == null){
			// create a pivot Node which will be rotated by the mouse input
			pivotCameraNode = new Node("pivot");
			pivotCameraNode.attachChild(getCamNode());
			panCameraNode = new Node("pan");
			panCameraNode.attachChild(pivotCameraNode);
			rootNode.attachChild(panCameraNode);
		}
		return pivotCameraNode;
	}

	public void revertCamera() {
		settingSC = false;
		positionCamera(getCamera());
		getCamNode().setTranslation(0, -96, 96);
		//Sets position to postition of (0, -96, 96)
	}
}
