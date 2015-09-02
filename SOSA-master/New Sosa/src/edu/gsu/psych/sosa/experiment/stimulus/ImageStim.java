package edu.gsu.psych.sosa.experiment.stimulus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.image.Texture;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.MaterialState;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.TextureManager;
import com.ardor3d.util.export.binary.BinaryExporter;
import com.ardor3d.util.export.binary.BinaryImporter;
import com.ardor3d.util.resource.MultiFormatResourceLocator;
import com.ardor3d.util.resource.ResourceSource;

import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.main.SOSATexture;


public class ImageStim extends Stimulus{

	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 7427210107039392412L;
	private byte[] imageData = null;
	private SOSATexture texture = null;
	
	private transient Texture face = null;	
	private transient String filePath = null;
	private transient String fileName = null;
	private transient Node pivot;
	private transient Camera cam = null;	
	/**Prints available sizes**/
	public String[] getAvailableTypes() {
		return new String[]{"small","medium","large","larger","largest"};
	}
	/**sets typeIdex to size**/
	public void setSize(float size){
		if(size > 0){
			typeIndex = size;
			resetRender();
		}
	}
	/**Returns typeIndex**/
	public float getTypeIndex(){
		return typeIndex;
	}
	
	public Spatial getRender(){
		if(render == null || pivot == null || anchor == null){
			float side = SIZE + typeIndex;
			
			//render is the actual visible stim
			//its model bound allows it to be picked and dragged by the user
			render = new Box(renderID, Vector3.ZERO, side/2, 0.01f, side/2);
			Quaternion rot = new Quaternion();
			rot.fromAngleNormalAxis(-90f * MathUtils.DEG_TO_RAD, Vector3.UNIT_Y);
			render.setRotation(rot);
//			render.setRenderQueueMode(Renderer.QUEUE_INHERIT);
			render.setModelBound(new BoundingBox());
			render.updateModelBound();
			
			MaterialState ms = new MaterialState();
			ms.setEmissive(ColorRGBA.WHITE);
			render.setRenderState(ms);
			
			try {
				setTexture();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//pivot is what rotates the stim to face the way we want
			pivot = new Node();
			pivot.attachChild(render);
			
			faceCamera();
			
			//anchor obscures the adjustments above so translations in the world are on a predictable plane
			anchor = new Node();
			anchor.attachChild(pivot);
			
			translationHeight = Float.NaN;	//force recalculation
			
//			if(SOSAMain.DEBUG)
//				logStatus();
		}
		return anchor;
	}
	/**sets Camera to face forward**/
	public void resetRender(){
		super.resetRender();
		cam = null;
		face = null;
	}
	/**reset Camera **/
	public void setCamera(Camera cam){
		this.cam = cam;
	}
	/**takes the clone of the image selected and makes it the stim **/
	public void setStim(Stimulus stim){
		super.setStim(stim);
		if(stim instanceof ImageStim)
			this.imageData = ((ImageStim)stim).imageData.clone();
	}
	/**recalibrates the image to face the camera**/
	public boolean faceCamera(){
		if(cam != null && pivot != null){
			Quaternion q = new Quaternion();
			Quaternion r = new Quaternion();
			q.lookAt(cam.getDirection().negate(null), Vector3.UNIT_Z);
			r.fromAngleNormalAxis(-90f * MathUtils.DEG_TO_RAD, Vector3.UNIT_X);
			q.multiplyLocal(r);
			pivot.setRotation(q);
			return true;
		}
		return false;
	}
	/**recalibrates the image to face up**/
	public boolean faceUp(){
		if(cam != null && pivot != null){
			Quaternion q = new Quaternion();
			Quaternion r = new Quaternion();
			q.lookAt(cam.getDirection().negate(null), Vector3.UNIT_Y);	//aligning to the Z axis causes huge rounding errors to skew the result
			r.fromAngleNormalAxis(-90f * MathUtils.DEG_TO_RAD, Vector3.UNIT_X);
			q.multiplyLocal(r);
			pivot.setRotation(q);
			return true;
		}
		return false;
	}
	/**returns file path**/
	public String getFilePath(){
		String temp = "";
		if(filePath != null)
			temp = filePath;
		return temp;
	}
	/**returns the file name**/
	public String getFileName(){
		String temp = "";
		if(fileName != null)
			temp = fileName;
		return temp;
	}
	/**sets the stim image**/
	public void setImage(ImageStim stim) {
		super.setStim(stim);
	}
	/** sets background to file image**/
	public void setTexture(File file) throws IOException{
		this.filePath = file.getParent();
		this.fileName = file.getName();
		
		getImageDataFromFile(file);
		setTexture();
	}
	/**sets background and updates file name and path**/
	public void setTexture(SOSATexture texture) throws IOException {
		this.texture = texture;
		this.imageData = null;
		this.filePath = texture.getParentPath();
		this.fileName = texture.getFileName();
		
		getImageDataFromTexture();
		setTexture();
	}
	/**gets image, texture from file **/
	private void getImageDataFromFile(File file) throws IOException{
		try {
			MultiFormatResourceLocator mfrl = new MultiFormatResourceLocator(file.getParentFile().toURI(), ".png", ".jpg");
			ResourceSource image = mfrl.locateResource(file.getName());
			face = TextureManager.load(image, Texture.MinificationFilter.Trilinear, true);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//extract the byte array version of this texture (for saving purposes)
		BinaryExporter exporter = new BinaryExporter();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.save(face, os);
		
		imageData = os.toByteArray();
	}
	/**gets image from the texture **/
	private void getImageDataFromTexture() {
		face = TextureManager.loadFromImage(texture.getArdorImage(true), Texture.MinificationFilter.Trilinear);		
	}
	/**load from imageData,
	-if texture is null get image data from texture
	-render the new texture  **/
	private void setTexture() throws IOException{
		if(face == null){
			if(imageData != null){
				//load texture from imageData
				BinaryImporter importer = new BinaryImporter();
				face = (Texture)importer.load(imageData);
			} else if (texture != null) {
				getImageDataFromTexture();
			} else
				return;
		}
		
		TextureState ts = new TextureState();
		ts.setTexture(face);
		render.setRenderState(ts);
		
//		MaterialState ms = new MaterialState();
//		ms.setColorMaterial(ColorMaterial.Diffuse);
//		ms.setEmissive(new ColorRGBA(255, 255, 255, 1));
//		render.setRenderState(ms);
//		render.updateRenderState();
	}

	/**
	 * Ants stand at attention after wondering outside the hive.
	 * 
	 * @param filePath The way back home.
	 * @return The hive, one by one.
	 * @throws java.io.IOException The hive crushed, the ants loose their way.
	 */
//	private byte[] getImageDataFromFile(String filePath) throws IOException{
//		DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath));
//		if((new File(filePath)).length() > Integer.MAX_VALUE)
//			throw new IOException("File size is too large.");
//		imageData = new byte[(int) (new File(filePath)).length()];
//		//dataInputStream.readFully(imageData);
//		int offset = 0;
//        int numRead = 0;
//        while ( (offset < imageData.length)
//                &&
//                ( (numRead=dataInputStream.read(imageData, offset, imageData.length-offset)) >= 0) ) {
//
//            offset += numRead;
//        }
//        if (offset < imageData.length) {
//            throw new IOException("Could not completely read file " + filePath);
//        }
//		dataInputStream.close();
//		return imageData;
//	}
	/**log the pivot and anchor **/
	public void logStatus(){
		super.logStatus();
		
		logStatus("pivot",pivot);
		logStatus("anchor",anchor);
	}

}
