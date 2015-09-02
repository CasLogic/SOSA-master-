package edu.gsu.psych.sosa.experiment.stimulus;

import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.ui.text.BMText;

/**load object **/
public interface SOSARenderObject {
	
	Spatial getRender();
	BMText	getRenderLabel();
	
	void resetRender();
	void resetLabelRender();
	
	void setRenderID(String ID);

}
