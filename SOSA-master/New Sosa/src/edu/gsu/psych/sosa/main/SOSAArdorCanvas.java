package edu.gsu.psych.sosa.main;

import java.io.File;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.LWJGLException;

import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.framework.Scene;
import com.ardor3d.framework.Updater;
import com.ardor3d.framework.lwjgl.LwjglCanvasCallback;
import com.ardor3d.framework.lwjgl.LwjglCanvasRenderer;
import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.image.util.ScreenShotImageExporter;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.swt.SwtFocusWrapper;
import com.ardor3d.input.swt.SwtKeyboardWrapper;
import com.ardor3d.input.swt.SwtMouseWrapper;
import com.ardor3d.intersection.PickData;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.intersection.PickingUtil;
import com.ardor3d.intersection.PrimitivePickResults;
import com.ardor3d.light.DirectionalLight;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.ContextManager;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.renderer.state.MaterialState;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.renderer.state.MaterialState.ColorMaterial;
import com.ardor3d.renderer.state.MaterialState.MaterialFace;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.ContextGarbageCollector;
import com.ardor3d.util.GameTaskQueue;
import com.ardor3d.util.GameTaskQueueManager;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.TextureManager;
import com.ardor3d.util.Timer;
import com.ardor3d.util.screen.ScreenExporter;

public abstract class SOSAArdorCanvas implements Updater, Scene{
	
	private boolean inited = false;
	
	protected final SwtCanvas canvas;
	protected final Node rootNode;
	protected final LogicalLayer logicalLayer;
	protected final FrameHandler frameHandler;
	private Camera cam;
	
	protected Timer statusTimer = null;
	protected int statusCheckInterval = 5;
	protected int statusNextCheck = 5;
	
	private boolean forceAspectRatio;
	private int ratioX;
	private int ratioY;

	private static Box board;
//	private UUID resizeActionID = null;
//	private boolean stopCascade = false;
//	private UUID stopCascadeID = null;
	
	/**
	 * Initiates a canvas with the aspect ration locked
	 * @param SwtCanvas canvas
	 */
	public SOSAArdorCanvas(final SwtCanvas canvas){
		this(canvas, false);
	}
	
	/**
	 * initiates a canvas with the possibility of locking the aspect ratio
	 * sets a size of 5x4
	 * @param SetCanvas canvas
	 * @param boolean aspectRatioLocked
	 */
	public SOSAArdorCanvas(final SwtCanvas canvas, boolean aspectRatioLocked) {
		this(canvas, aspectRatioLocked, 5, 4);
	}

	/**
	 * Main SOSAArdorCanvas that gets initialized when a canvas is created
	 * Creates the main parts of the canvas and makes the frame
	 * @param SwtCanvas canvas
	 * @param boolean aspectRatioLocked
	 * @param int ratioX
	 * @param int ratioY
	 */
	public SOSAArdorCanvas(final SwtCanvas canvas, boolean aspectRatioLocked, int ratioX, int ratioY) {
		this.forceAspectRatio = aspectRatioLocked;
		this.ratioX = ratioX;
		this.ratioY = ratioY;		
		this.canvas = canvas;
		
		this.logicalLayer = new LogicalLayer();
		rootNode = new Node("root");
		
		Timer timer = new Timer();
		frameHandler = new FrameHandler(timer);
		
		setupRenderer(canvas, this, frameHandler);
		
		frameHandler.init();
	}

	/**
	 * This assigns keyboard, mouse and focus inputs and creates a new physical
	 * layer and passes in the give inputs
	 * Registers inputs with the logical layer
	 */
	public void registerInput(){
		final SwtKeyboardWrapper kw = new SwtKeyboardWrapper(canvas);
		final SwtMouseWrapper mw = new SwtMouseWrapper(canvas);
		final SwtFocusWrapper fw = new SwtFocusWrapper(canvas);
		
		final PhysicalLayer pl = new PhysicalLayer(kw, mw, fw);
		logicalLayer.registerInput(this.canvas, pl);
	}
	
	public void cleanup(){
		// Done, do cleanup
		// Sets canvas as the current context
		canvas.getCanvasRenderer().makeCurrentContext();
		// Passes canvas into garbage collector
		ContextGarbageCollector.doFinalCleanup(canvas.getCanvasRenderer().getRenderer());
		canvas.dispose();
	}
	
	public void cleanTextures(){
		// sets canvas as current context
		canvas.getCanvasRenderer().makeCurrentContext();
		TextureManager.cleanAllTextures(canvas.getCanvasRenderer().getRenderer());
	}
	
	protected void showStatus(){
		// if timer is null, make a new timer
		if(statusTimer == null)
			statusTimer = new Timer();
		
		// If timer is greater than check, add interval to check
		if(statusTimer.getTimeInSeconds() > statusNextCheck){
			statusNextCheck += statusCheckInterval;
			
			// For each stim in root node's children, log stim status and name
			for(Spatial stim : rootNode.getChildren())
				logStatus(stim.getName(),stim);
			
			System.out.println(cam);
		}
	}
	
	public FrameHandler getFrameHandler(){
		return frameHandler;
	}
	
	/**
	 * Initializer for SOSAArdorCanvas
	 */
	@Override
	public void init() {
		cleanTextures(); // Clean Textures
		getCamera();	// Create a camera
		
		// If already initiated, return
		if (inited)
			return;

		// Create buffer and set root node render state
		final ZBufferState buf = new ZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		rootNode.setRenderState(buf);

		// Create light
		final DirectionalLight light = new DirectionalLight();
		light.setDiffuse(ColorRGBA.WHITE);
		light.setAmbient(ColorRGBA.WHITE);
		light.setDirection(0, 0.4, -1);
		light.setEnabled(true);

		// Attach the light to a lightState and the lightState to rootNode
		final LightState lightState = new LightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		rootNode.setRenderState(lightState);
		
		setBackgroundColor(SOSAMain.experiment != null ? SOSAMain.experiment.bgColor : ColorRGBA.BLACK);
		
		extraSetup();
		
		inited = true;
	}
	
	protected abstract void positionCamera(Camera cam);
	protected abstract void extraSetup();
	
	/**
	 * if camera is null, get camera from canvas and position it
	 * @return Camera cam
	 */
	protected Camera getCamera(){
		if(cam == null){
			cam = canvas.getCanvasRenderer().getCamera();
			positionCamera(cam);
		}
		return cam;
	}
	
	/**
	 * Gets time per frame and check it against triggers in the logical layer,
	 * calls any extra updates on the tpf ad updates the Geometric state for the 
	 * root node
	 * @param ReadOnlyTimer timer
	 */
	@Override
	public void update(ReadOnlyTimer timer) {
		final double tpf = timer.getTimePerFrame();
		
		logicalLayer.checkTriggers(tpf);
		extraUpdates(tpf);
		
		rootNode.updateGeometricState(tpf,true);
		
//		if(SOSAMain.DEBUG)
//			showStatus();
	}
	
	protected abstract void extraUpdates(double tpf);
	
	protected void setBoardColor(ReadOnlyColorRGBA color){
		if(board != null)
			board.setDefaultColor(color);
	}
	
	protected void setBackgroundColor(ReadOnlyColorRGBA color){
		//if you don't do this first line, the world explodes... great documentation guys
		canvas.getCanvasRenderer().makeCurrentContext();
		canvas.getCanvasRenderer().getRenderer().setBackgroundColor(color);
	}
	
	/**
	 * Creates a board and attaches it to the root node
	 */
	protected void attachBoard(){
		// sets board as a new box with a min and max vector ((-48,-48,-2) and (48,48,0) respectively)
		Vector3 max = new Vector3(48, 48, 0);
		Vector3 min = new Vector3(-48, -48, -2);
		board = new Box("board", min, max);
		
		// if experiment and board are not null
		if(SOSAMain.experiment != null && SOSAMain.experiment.getBoardFace() != null){
			// Make a new texture state with the texture of the board face
			TextureState ts = new TextureState();
			ts.setTexture(SOSAMain.experiment.getBoardFace());
			// renders the texture state on the board
			board.setRenderState(ts);
		}
		
		// sets the board color to the experiment’s board color, or white
		board.setDefaultColor(SOSAMain.experiment != null ? SOSAMain.experiment.boardColor : ColorRGBA.WHITE);
		
		MaterialState ms = new MaterialState();
		ms.setColorMaterial(ColorMaterial.Diffuse);
		ms.setColorMaterialFace(MaterialFace.FrontAndBack);
		board.setRenderState(ms);
		
		rootNode.attachChild(board);
	}
	
	public void takeScreenShot(){
		String path = SOSAMain.experiment.getLogOutputLocation();
		if(path.indexOf(".csv")> -1)
			path = path.substring(0, path.length()-4);
		
		// Create a new file at the path
		File f = new File(path);
		
		// creates a new screen shot exporter and sets the directory and the name of the file
		ScreenShotImageExporter screenShotExp = new ScreenShotImageExporter();
		screenShotExp.setDirectory(new File(f.getParent()));
		screenShotExp.setPrepend(f.getName());
		
		// force any waiting scene elements to be renderer.
		canvas.getCanvasRenderer().makeCurrentContext();
		canvas.getCanvasRenderer().getRenderer().renderBuckets();
        
		// exports current screen to the screenshot exporter
		ScreenExporter.exportCurrentScreen(canvas.getCanvasRenderer().getRenderer(), screenShotExp);
	}

	/**
	 * Override original doPick function
	 * @param Ray3 pickRay
	 * @return PickResults pickResults
	 */
	@Override
	public PickResults doPick(final Ray3 pickRay) {
        final PrimitivePickResults pickResults = new PrimitivePickResults();
        pickResults.setCheckDistance(true);
        PickingUtil.findPick(rootNode, pickRay, pickResults);
        if(SOSAMain.DEBUG)
        	processPicks(pickResults);
        return pickResults;
    }
    
	/**
	 * Processes stimulus pick
	 * @param PrimitivePickResults pickResults
	 */
    protected void processPicks(final PrimitivePickResults pickResults) {
        int i = 0;
        // While loop does nothing
        while (pickResults.getNumber() > 0
                && pickResults.getPickData(i).getIntersectionRecord().getNumberOfIntersections() == 0
                && ++i < pickResults.getNumber()) {
        }
        if (pickResults.getNumber() > i) {
        	// process pick and print pick result
            final PickData pick = pickResults.getPickData(i);
            System.err.println("picked: " + pick.getTarget() + " at: "
                    + pick.getIntersectionRecord().getIntersectionPoint(0));
        } else {
            System.err.println("picked: nothing");
        }
    }

    /**
     * Executes renderQueue item and draws the root node
     * @param Renderer renderer
     * @return true
     */
	@Override
	public boolean renderUnto(Renderer renderer) {
		// Execute renderQueue item
        GameTaskQueueManager.getManager(ContextManager.getCurrentContext())
        					.getQueue(GameTaskQueue.RENDER).execute(renderer);

        renderer.draw(rootNode);
        return true;
	}
	
	/**
	 * Parent function for makeCurrent and releaseContext
	 * @param SwtCanvas canvas
	 * @param Renderer renderer
	 */
    public static void addCallback(final SwtCanvas canvas, final LwjglCanvasRenderer renderer) {
        renderer.setCanvasCallback(new LwjglCanvasCallback() {
        	/**
        	 * Sets the current canvas or throws LWJGLException
        	 */
            @Override
            public void makeCurrent() throws LWJGLException {
                canvas.setCurrent();
            }
            
            /**
             * throws LWJGLException
             */
            @Override
            public void releaseContext() throws LWJGLException {
                ; // do nothing?
            }
        });
    }

    /**
	 * Due to a crashing issue in LWJGL the buffer size for the canvas
	 * MUST be divisible by 32 when a screenshot is rendered
	 * @param Point input
	 * @param Point max
	 * @return Point out
	 */
	private Point makeRect32(Point input, Point max) {
		Point out = /*advanced32Fixup(input.x, input.y);*/ null;
		if(out == null)
			out = simple32Fixup(input);	//revert to simple resize if advanced fails
		
		return out;
	}	
	
//	private Point advanced32Fixup(int x, int y){
//		for(int searchDepth=0; searchDepth < 32; searchDepth++){ 
//			for(int i=0; i < searchDepth; i++){
//				if(check32(x-i,y-searchDepth))
//					return new Point(x-i,y-searchDepth);
//				if(check32(x-searchDepth,y-i))
//					return new Point(x-searchDepth,y-i);
//			}
//			if(check32(x-searchDepth,y-searchDepth))
//				return new Point(x-searchDepth,y-searchDepth);
//		}
//		return null;
//	}
	
//	private boolean check32(int x, int y){
//		if(SOSAMain.DEBUG){
//			boolean test = (((x*y)%32)==0);
//			if(test)
//				System.out.println("trying: (" + x + "x" + y + ")%32 :: " + test);
//			else
//				System.out.println("trying: (" + x + "x" + y + ")%32 :: " + test);
//		}		
//		return ((x*y)%32)==0;
//	}
	
//	private boolean check32(Point test){
//		return check32(test.x, test.y);
//	}
	
	/**
	 * creates a new point based on the difference between x and y mod 32
	 * @param Point input
	 * @return Point out
	 */
	private Point simple32Fixup(Point input){
		int newX = input.x;
		int newY = input.y;
		int xDiff = input.x % 32;
		int yDiff = input.y % 32;
		
		//change the one that will have the lesser impact
		if(xDiff < yDiff) 
			newX -= xDiff;
		else
			newY -= yDiff;
		
		Point out = new Point(newX, newY);
		System.out.print(" canvas2: " + out + "\n");
		return out;
	}
	
	/**
	 * calls overloaded forceRatioPoint with input split into x and y
	 * @param Point input
	 * @param Point max
	 * @return forceRatioPoint(input.x, input.y, max)
	 */
	private Point forceRatioPoint(Point input, Point max){
		return forceRatioPoint(input.x, input.y, max);
	}
	
	/**
	 * creates a new point based on ratios of x and y
	 * @param int x
	 * @param int y
	 * @param Point max
	 * @return Point out
	 */
	private Point forceRatioPoint(int x, int y, Point max){
		int pX = getRatioX(y);
		int pY = getRatioY(x);
		if(x > pX)
			x = pX;
		else
			y = pY;
		
		Point out = new Point(x,y);
		System.out.print("composite: " + max + "canvas: " + out);
//		if(x > max.x || y > max.y)
//			System.out.println("ALERT!!!!!!!!!!!!!!!!: " + out);
//			return forceRatioPoint(max.x, y, max);
//		else if(y > max.y)
//			return forceRatioPoint(x, max.y, max);
//		else 	
		return out;
	}
	
	/**
	 * computes ratio of x and y compared to y
	 * @param int y
	 * @return int y/ratioY*ratioX
	 */
	private int getRatioX(int y){
		return y/ratioY*ratioX;
	}
	
	/**
	 * computes ratio of y and x compared to x
	 * @param int x
	 * @return int x/ratioX*ratioY
	 */
	private int getRatioY(int x){
		return x/ratioX*ratioY;
	}
	
	/**
	 * Parent function for controlMoved and controlResized
	 * @param SwtCanvas swtCanvas
	 * @param CanvasRenderer canvasRenderer
	 * @return newResizeHandler retVal
	 */
    public ControlListener newResizeHandler(final SwtCanvas swtCanvas, final CanvasRenderer canvasRenderer) {
        final ControlListener retVal = new ControlListener() {
            public void controlMoved(final ControlEvent e) {}

            /**
             * Sets canvas size and distances camera accordingly
             * @param ControlEvent event
             */
            public void controlResized(final ControlEvent event) {
            	if(forceAspectRatio)
	        		swtCanvas.setSize(forceRatioPoint(swtCanvas.getSize(), swtCanvas.getParent().getSize()));
            	
            	swtCanvas.setSize(makeRect32(swtCanvas.getSize(), swtCanvas.getParent().getSize()));
            	
//            	if(stopCascade) {
//            		stopCascadeID = SOSAMain.performAfterWait2(500, new Delegate() {
//						@Override
//						public void perform() {
//							stopCascade = false;								
//						}
//					}, stopCascadeID);
//            	} else {
//            		stopCascade = true;
//	            	if(!check32(swtCanvas.getSize())){
//		            	resizeActionID = SOSAMain.performAfterWait2(500, new Delegate() {
//							@Override
//							public void perform() {
//								swtCanvas.setSize(makeRect32(swtCanvas.getSize(), swtCanvas.getParent().getSize()));								
//							}
//						}, resizeActionID);
//	            	}
//            	}
	
				final Rectangle size = swtCanvas.getClientArea();
				// if canvas size is 0, do nothing
				if ((size.width == 0) && (size.height == 0)) {
					return;
				}
				final float aspect = (float) size.width / (float) size.height;
				final Camera camera = canvasRenderer.getCamera();
				if (camera != null) {
					final float fovY = 45; // XXX no camera.getFov()
					final double near = camera.getFrustumNear();
					final double far = camera.getFrustumFar();
					camera.setFrustumPerspective(fovY, aspect, near, far);
					camera.resize(size.width, size.height);
				}
            	
            }
        };
        return retVal;
    }
    
    /**
     * Sets up a canvas renderer
     * @param SwtCanvas canvas
     * @param SOSAArdorCanvas impl
     * @param FrameHandler fh
     */
    public void setupRenderer(SwtCanvas canvas, SOSAArdorCanvas impl, FrameHandler fh){
    	LwjglCanvasRenderer renderer = new LwjglCanvasRenderer(impl);
		addCallback(canvas, renderer);
		canvas.setCanvasRenderer(renderer);
		fh.addCanvas(canvas);
		fh.addUpdater(impl);
		canvas.addControlListener(newResizeHandler(canvas, renderer));		
    }
    
    /**
     * Logs status with showBound as false
     * @param String name
     * @param Spatial show
     */
    protected static void logStatus(String name, Spatial show){
		logStatus(name,show,false);
	}
	
    /**
     * Logs stimulus status with local and world positions
     * @param String name
     * @param Spatial show
     * @param boolean showBound
     */
	protected static void logStatus(String name, Spatial show, boolean showBound){
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
	
	//Gets the board
	public Box getBoard()
	{
		return board;
	}

}
