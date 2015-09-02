package edu.gsu.psych.sosa.experiment;


import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.scenegraph.Spatial;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAArdorCanvas;
import edu.gsu.psych.sosa.main.SOSAMain;

public class ExperimentPreviewCanvasImpl extends SOSAArdorCanvas{
	
	//tell whether to show the back ground or not for the preview window
	public static boolean showBackground = true;

	public ExperimentPreviewCanvasImpl(SwtCanvas canvas) {
		super(canvas);
	}
	
	public void positionCamera(Camera cam){
		if(cam != null){
			cam.setLocation(new Vector3(0,0,132));
			cam.lookAt(Vector3.ZERO, Vector3.UNIT_Y);
		}
	}//Sets location of the Camera
	
	public void extraSetup(){
		generatePreview();
	}
	
	public void generatePreview(){
		SOSAMain.experiment.revertAllRenders();
		
		attachBoard();
		attachStims();
		
		//hide the background for the subject if needed to
		if(showBackground == false)
		{
			TextureState ts = new TextureState();
			ts.setTexture(null);
			getBoard().setRenderState(ts);
		}
	}

	public void extraUpdates(double tpf){
		SOSAMain.experiment.updateLabelPositions(rootNode, getCamera());
		//Updates the label position
	}

	public void updateBoardColor() {
		setBoardColor(SOSAMain.experiment.boardColor);
		//Updates the board
	}
	
	public void updateBGColor(){
		setBackgroundColor(SOSAMain.experiment.bgColor);
		//updates the board
	}

	private void attachStims(){
		for (int i = 0; i < SOSAMain.experiment.getStimList().size(); i++) {
			Stimulus temp = SOSAMain.experiment.getStimList().get(i);
			Spatial s = null;
			if(temp instanceof ImageStim)
				((ImageStim) temp).setCamera(getCamera());
			s = temp.getRender();
			temp.faceUp();
			rootNode.attachChild(setStimPosition(s,i,SOSAMain.experiment.getStimList().size()));
			//attaches stims to StimList
		}
	}

	private Spatial setStimPosition(Spatial s, int i, int total) {
		if(total <= 3){
			switch (i) {
			case 0:
				s.setTranslation(-24, 0, 0);
				break;
			case 1:
				s.setTranslation(0, 0, 0);
				break;
			case 2:
				s.setTranslation(24, 0, 0);
				break;
			default:
				return null;
			}
		}else if(total <= 9){
			switch (i) {
			case 0:
				s.setTranslation(-24, 24, 0);
				break;
			case 1:
				s.setTranslation(0, 24, 0);
				break;
			case 2:
				s.setTranslation(24, 24, 0);
				break;
			case 3:
				s.setTranslation(-24, 0, 0);
				break;
			case 4:
				s.setTranslation(0, 0, 0);
				break;
			case 5:
				s.setTranslation(24, 0, 0);
				break;
			case 6:
				s.setTranslation(-24, -24, 0);
				break;
			case 7:
				s.setTranslation(0, -24, 0);
				break;
			case 8:
				s.setTranslation(24, -24, 0);
				break;
			default:
				return null;
			}
		}else{
			switch (i){
			case 0:
				s.setTranslation(-28.8f, 28.8f, 0);
				break;
			case 1:
				s.setTranslation(-9.6f, 28.8f, 0);
				break;
			case 2:
				s.setTranslation(9.6f, 28.8f, 0);
				break;
			case 3:
				s.setTranslation(28.8f, 28.8f, 0);
				break;
			case 4:
				s.setTranslation(-28.8f, 9.6f, 0);
				break;
			case 5:
				s.setTranslation(-9.6f, 9.6f, 0);
				break;
			case 6:
				s.setTranslation(9.6f, 9.6f, 0);
				break;
			case 7:
				s.setTranslation(28.8f, 9.6f, 0);
				break;
			case 8:
				s.setTranslation(-28.8f, -9.6f, 0);
				break;
			case 9:
				s.setTranslation(-9.6f, -9.6f, 0);
				break;
			case 10:
				s.setTranslation(9.6f, -9.6f, 0);
				break;
			case 11:
				s.setTranslation(28.8f, -9.6f, 0);
				break;
			case 12:
				s.setTranslation(-28.8f, -28.8f, 0);
				break;
			case 13:
				s.setTranslation(-9.6f, -28.8f, 0);
				break;
			case 14:
				s.setTranslation(9.6f, -28.8f, 0);
				break;
			case 15:
				s.setTranslation(28.8f, -28.8f, 0);
				break;
			default:
				return null;
			}
		}//Sets Spatial Translation based on a case basis
		return s;
	}
	
}
