package edu.gsu.psych.sosa.experiment;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;

import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.image.Texture;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.TextureState;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAArdorCanvas;
import edu.gsu.psych.sosa.main.SOSAMain;

public class ExperimentCreatorPreviewCanvasImpl extends SOSAArdorCanvas{

	private SOSAWindowExperiment iexp;
	private Stimulus stim;
	private ImageStim board;
	private Texture boardFace;
	private boolean relativeView = false;

	public ExperimentCreatorPreviewCanvasImpl(final SwtCanvas canvas, SOSAWindowExperiment iexp) {
		super(canvas);
		this.iexp = iexp;
	}
	
	public void extraSetup(){
		attachBoard();
	}
	
	protected void positionCamera(Camera cam){
		if(cam != null){
			cam.setLocation(new Vector3(0, -10, 10));
			cam.lookAt(Vector3.ZERO, Vector3.UNIT_Z);
			//Sets cam position
		}
	}
	
	protected void extraUpdates(double tpf){
		updatePeg(tpf);	//updates the attached stim if necessary
		updatePegLabel(tpf);	//updates the label if necessary
		updateBoard(tpf);
		
	}
	
	private Stimulus getSelectedPeg(){
		if(iexp.getExperiment() != null)
			return iexp.getExperiment().getSelectedPeg();
		return null;
	}
	
	private void attachPeg(Stimulus stim){
		this.stim = stim;
		if(stim instanceof ImageStim)
			((ImageStim)stim).setCamera(getCamera());
		rootNode.attachChild(stim.getRender());
		attachPegLabel(stim);
		//Attaches Pegs to Board
		if(SOSAMain.DEBUG)
			stim.logStatus();
	}
	
	protected void attachBoard(){
		board = new ImageStim();
		board.setSize(94f);
		board.setCamera(getCamera());		
		board.getRender().setTranslation(125, 90, -90);
		board.setRenderID("board");
		rootNode.attachChild(board.getRender());
		//Attaches Board to Window
		
//		if(SOSAMain.DEBUG)
//			board.logStatus();
	}
	
	public void showRelativeStim(boolean on) {
		relativeView = on;
	}
	
	private void attachPegLabel(Stimulus stim){
		stim.getRenderLabel().setTranslation(getCamera().getScreenCoordinates(stim.getRender().getWorldTranslation()).addLocal(centerLabel(stim)));
//		stim.getRenderLabel().setTranslation(stim.getRender().getWorldTranslation().add(centerLabel(stim),null));
		rootNode.attachChild(stim.getRenderLabel());
	}
	
	private Vector3 centerLabel(Stimulus stim){
		return new Vector3(-stim.getRenderLabel().getWidth()/2f, 50, 0);
		//Renders label 
	}
	
	private void updatePeg(double tpf){
		if(getSelectedPeg() != null){
			if(stim == null){
				attachPeg(getSelectedPeg());
			}else if(stim != getSelectedPeg()){
				stim.resetRender();
				attachPeg(getSelectedPeg());
			}else if(stim.isRenderNull()){
				attachPeg(getSelectedPeg());
			}else{
	//			stim.getRender().updateGeometricState(tpf);
				stim.updatePegColor();
			}
			if(relativeView)
				getSelectedPeg().getRender().setTranslation(0, 90, -90);
			else
				getSelectedPeg().getRender().setTranslation(0, 0, 0);
			//Updates Peg color
		}
	}
	
	private void updatePegLabel(double tpf){
		if(stim != null){
			if(stim.isRenderLabelNull())
				attachPegLabel(stim);
	//		stim.getRenderLabel().updateGeometricState(tpf);
			stim.updatePegLabelColor();
			//Updates Peglabel
		}
	}
	
	private void updateBoard(double tpf){
		if(iexp.getExperiment().getBoardFace() != null){
			if(iexp.getExperiment().getBoardFace() != boardFace){
				boardFace = iexp.getExperiment().getBoardFace();
				TextureState ts = new TextureState();
				ts.setTexture(boardFace);
				board.getRender().setRenderState(ts);
			}
		} else {
			boardFace = null;
			TextureState ts = new TextureState();
			board.getRender().setRenderState(ts);
			//Updates the boardface
		}
	}
	
	public ControlListener newResizeHandler(final SwtCanvas swtCanvas, final CanvasRenderer canvasRenderer) {
        final ControlListener retVal = new ControlListener() {
            public void controlMoved(final ControlEvent e) {}

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
