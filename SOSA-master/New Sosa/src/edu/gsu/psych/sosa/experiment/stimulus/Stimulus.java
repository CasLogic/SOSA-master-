package edu.gsu.psych.sosa.experiment.stimulus;

import java.io.Serializable;


import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.state.MaterialState;
import com.ardor3d.renderer.state.MaterialState.ColorMaterial;
import com.ardor3d.renderer.state.MaterialState.MaterialFace;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Cylinder;
import com.ardor3d.ui.text.BMText;
import com.ardor3d.ui.text.BasicText;

import edu.gsu.psych.sosa.main.SOSAMain;

public class Stimulus implements SOSARenderObject, Serializable{
	
	public static final int PEG 	= 1;
	public static final int IMAGE 	= 2;
	
	protected final static float SIZE = 2f;

	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 3763668064372534145L;
	
	private String label = "unlabeled";
	public String processingID = "0";
	public float typeIndex = 0;
	
	
	public ColorRGBA pegColorDefault = new ColorRGBA(88f/255f,88f/255,1,1);				//light blue
	public ColorRGBA pegColor = new ColorRGBA(pegColorDefault);
	public ColorRGBA labelColorDefault = new ColorRGBA(155f/255f,155f/255f, 1, 1);		//lighter blue
	public ColorRGBA labelColor = new ColorRGBA(labelColorDefault);
	
	private ColorRGBA startColor;
	
	
	protected transient Mesh render;
	private transient BMText renderLabel;
	private transient float renderLabelScale = 1f;
	protected transient String renderID = "stim x";
	protected transient float translationHeight = Float.NaN;
	protected transient Node anchor = null;
	/**Returns the available sizes	**/
	public String[] getAvailableTypes() {
		return new String[]{"small","medium","large"};
	}
	/**If render is null make new cylinder, set color, bound, and material color **/
	public Spatial getRender(){
		if(render == null){
			render = new Cylinder(renderID, 5, 20, 1, SIZE+typeIndex, true);
			//render.setRenderQueueMode(Renderer.QUEUE_INHERIT);
			render.setDefaultColor(pegColor);
			render.setModelBound(new BoundingBox());
			render.updateModelBound();
//			render.updateWorldBound(false);
			MaterialState ms = new MaterialState();
			ms.setColorMaterial(ColorMaterial.Diffuse);
			ms.setColorMaterialFace(MaterialFace.FrontAndBack);
			render.setRenderState(ms);
			translationHeight = Float.NaN;	//force recalculation
		}
		return render;
	}
	/**returns label text **/
	public BMText getRenderLabel(){
		if(renderLabel == null){
	//		renderLabel = new BMText("label", this.label, BMFontLoader.defaultFont());
			
//			try {
//				BMFont font = new BMFont(new URLResourceSource(ResourceLocatorTool.getClassPathResource(getClass(), "fonts/ArialOutline32px.fnt")), true);
//				renderLabel = new BMText("label", this.label, font);
//				renderLabel.setAutoRotate(true);
//				renderLabel.setTextColor(labelColor);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			renderLabel = new BasicText("label", this.label, BasicText.DEFAULT_FONT, BasicText.DEFAULT_FONT_SIZE);
		//	renderLabel.setLocalScale(new Vector3f(50f, 50f, 0.01f));
		//	BMText.resetDefaultFontTextureState();
		//	renderLabel.setRenderState(BMText.getDefaultFontTextureState());
		//	renderLabel.setRenderState(BMText.getFontBlend());
			renderLabel.setTextColor(labelColor);
			renderLabel.setRotation(new Matrix3().fromAngles(MathUtils.HALF_PI, 0, 0));
			setLabelSize(renderLabelScale);
		//	renderLabel.setLightCombineMode(LightCombineMode.Off);
		//	renderLabel.updateRenderState();
		}
		return renderLabel;
	}
	/**returns label**/
	public String getLabel(){
		return label;
	}
	/**resets label size **/
	public void resetLabelSize(){
		renderLabelScale = 1f;
	}
	/**If render isn’t null set it to null and call the label to be reset **/
	public void resetRender(){
		if(render != null){
			render.removeFromParent();
			render = null;
		}
		resetLabelRender();
	}
	/**if renderLabel isn’t null set it to null **/
	public void resetLabelRender(){
		if(renderLabel != null){
			renderLabel.removeFromParent();
			renderLabel = null;			
		}
	}
	
	/**
	 * Util function.  If the render is null then it needs to be updated. 
	 * @return true if the render is null
	 */
	public boolean isRenderNull() {
		return render == null;
	}
	
	/**
	 * Util function.  If the label is null then it needs to be updated. 
	 * @return true if the render label is null
	 */
	public boolean isRenderLabelNull() {
		return renderLabel == null;
	}
	
	/**
	 * Convenience function to return the proper translation height based on peg size
	 * @return float value representing the distance from the middle of the peg to the bottom edge
	 */
	public float getLevelHeight(){
		if(Float.isNaN(translationHeight)){
			translationHeight = (SIZE+((float)typeIndex))/2f;
			if(SOSAMain.DEBUG)
				System.out.println("Set Translation Height: " + label + ": " + translationHeight);
		}
		return translationHeight;
		
	}
	/**set typeIndex to size and reset render **/
	public void setSize(int size){
		if(size >= 0 && size <= 2){
			typeIndex = size;
			resetRender();	//nuke the render it so that updates with the new size 
		}
	}
	/**return label **/
	public String toString(){
		return label;
	}
	/**Resets peg color to the default color **/
	public void revertPegColor() {
		pegColor.set(pegColorDefault);
	}
	/**Resets label color to default color **/
	public void revertLabelColor() {
		labelColor.set(labelColorDefault);
	}
	/**Changes the default peg color to the current peg color **/
	public void updatePegColor() {
		if(render != null)
			render.setDefaultColor(pegColor);
	}
	/**Changes the label text color to label color **/
	public void updatePegLabelColor(){
		if(renderLabel != null)
			renderLabel.setTextColor(labelColor);
	}
	/**Sets the label, processing ID, peg default 
	 color, color, label default color and label color.
	 * */
	public void setStim(Stimulus stim) {
		this.label = new String(stim.label);
		this.processingID = new String(stim.processingID);
		setSize((int)stim.typeIndex);
		
		pegColorDefault.set(stim.pegColorDefault);
		pegColor.set(stim.pegColor);
		labelColorDefault.set(stim.labelColorDefault);
		labelColor.set(stim.labelColor);
		
		renderLabelScale = 1;	//set default
	}
	/**sets render ID to name and sets render name **/
	public void setRenderID(String name) {
		this.renderID = name;
		if(render != null){
			render.setName(name);
		}
	}
	/**sets label string **/
	public void setLabel(String label){
		this.label = label;
	}
	/**returns renderID **/
	public String getRenderID() {
		return renderID;
	}
	/**darkens and updates level **/
	public void darkenLabel(float level) {
		if(startColor == null)
			startColor = labelColor.clone();	//the original color gets saved
		labelColor.lerpLocal(ColorRGBA.BLACK, startColor, level);
		updatePegLabelColor();
	}
	/**sets renderLabelScale to size **/
	public void setLabelSize(float size) {
		getRenderLabel().setScale(size);
		renderLabelScale = size;
	}
	/**Sets stim to from  **/
	public void convertFrom(Stimulus from) {
		if(from instanceof ImageStim){
			setStim(from);
			this.pegColor = new ColorRGBA(pegColorDefault);
		} else
			setStim(from);
	}

	public void logStatus() {
//		if(render.getWorldBound() != null) show the world bound		
	
	}
	/**  **/
	protected void logStatus(String name, Spatial show){
		logStatus(name,show,false);
	}
	/**logs status of object **/
	protected void logStatus(String name, Spatial show, boolean showBound){
		double a[] = show.getWorldRotation().toAngles(null);
		double b[] = show.getRotation().toAngles(null);
		
		System.out.println("\t" + name);
		
		if(showBound)
			System.out.println("\t\tBound:               " + show.getWorldBound().getCenter());
		System.out.println("\t\tTranslation (world): " + show.getWorldTranslation());
		System.out.println("\t\tTranslation (local): " + show.getTranslation());
		System.out.println("\t\tRotation    (world): " + "(as angles) " + a[0] + " " + a[1] + " " + a[2]);
		System.out.println("\t\tRotation    (local): " + "(as angles) " + b[0] + " " + b[1] + " " + b[2]);
//		System.out.println("\t\tRotation    (world): " + show.getWorldRotation());
//		System.out.println("\t\tRotation    (local): " + show.getRotation());
	}
	/**rotates image up **/
	public boolean faceUp() {
		if(render != null){
			Quaternion q = new Quaternion();
			q.fromAngleAxis(MathUtils.DEG_TO_RAD * 90, Vector3.UNIT_X);
			render.setRotation(q);
	//		render.getLocalRotation().fromAngleAxis(FastMath.DEG_TO_RAD * 90, Vector3f.UNIT_X);
			return true;
		}
		return false;		
	}
}
