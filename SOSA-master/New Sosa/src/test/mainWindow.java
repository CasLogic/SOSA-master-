package test;

import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;

import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.framework.Scene;
import com.ardor3d.framework.lwjgl.LwjglCanvasCallback;
import com.ardor3d.framework.lwjgl.LwjglCanvasRenderer;
import com.ardor3d.framework.swt.SwtCanvas;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.renderer.Camera;
import com.ardor3d.util.Timer;

import org.lwjgl.LWJGLException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;

public class mainWindow {

	private Shell sShell = null;
	private SwtCanvas canvas = null;
	private Button button = null;
	private Button button1 = null;
	
	private FrameHandler framework = null;
	private MyExit exit = null;
	
	public mainWindow(){
		createSShell();		//this is first so that canvas is properly init before passing the reference to ExperimentUpdater
		
		final Timer timer = new Timer();
		framework = new FrameHandler(timer);
		final LogicalLayer logicalLayer = new LogicalLayer();
		
		exit = new MyExit();
//		final RotatingCubeGame game = new RotatingCubeGame(scene, exit, logicalLayer, Key.T);
		final ExperimentCanvasBase game = new ExperimentCanvasBase(canvas, logicalLayer);
		
		framework.addUpdater(game);
		
		setupCanvas(game, framework);
		
		sShell.open();
		
//		game.init();
		framework.init();
	}
	
	private void update(){
		framework.updateFrame();
	}
	
	private boolean isExit(){
		return exit.isExit();
	}

	/**
	 * This method initializes canvas	
	 *
	 */
	private void createCanvas() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		final GLData data = new GLData();
        data.depthSize = 8;
        data.doubleBuffer = true;
		canvas = new SwtCanvas(sShell, SWT.NONE, data);		
		canvas.setLayoutData(gridData);
	}
	
	private void setupCanvas(Scene scene, FrameHandler framework){
		LwjglCanvasRenderer lwjglCanvasRenderer = new LwjglCanvasRenderer(scene);
		addCallback(canvas, lwjglCanvasRenderer);
		canvas.setCanvasRenderer(lwjglCanvasRenderer);
		framework.addCanvas(canvas);
		canvas.addControlListener(newResizeHandler(canvas, lwjglCanvasRenderer));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		
//		AWTImageLoader.registerLoader();
//
//        try {
//            final SimpleResourceLocator srl = new SimpleResourceLocator(ResourceLocatorTool.getClassPathResource(
//                    LwjglSwtExample.class, "com/ardor3d/example/media/"));
//            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, srl);
//        } catch (final URISyntaxException ex) {
//            ex.printStackTrace();
//        }
		
		Display display = Display.getDefault();
		mainWindow thisClass = new mainWindow();
		
		while(!thisClass.sShell.isDisposed() && !thisClass.isExit()){
			display.readAndDispatch();
			thisClass.update();
			Thread.yield();
		}
		
//		while (!thisClass.sShell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//			else
//				framework.updateFrame();
//		}
		
		display.dispose();
		System.exit(0);
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.horizontalAlignment = GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("Test Box");
		createCanvas();
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(300, 200));
		button = new Button(sShell, SWT.NONE);
		button.setText("test 1");
		button.setLayoutData(gridData1);
		button1 = new Button(sShell, SWT.NONE);
		button1.setText("test 2");
		button1.setLayoutData(gridData2);
	}
	
    private static void addCallback(final SwtCanvas canvas, final LwjglCanvasRenderer renderer) {
        renderer.setCanvasCallback(new LwjglCanvasCallback() {
            @Override
            public void makeCurrent() throws LWJGLException {
                canvas.setCurrent();
            }

            @Override
            public void releaseContext() throws LWJGLException {
                ; // do nothing?
            }
        });
    }
    
    static ControlListener newResizeHandler(final SwtCanvas swtCanvas, final CanvasRenderer canvasRenderer) {
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
                    camera.setFrustumPerspective(fovY, aspect, near, far);
                    camera.resize(size.width, size.height);
                }
            }
        };
        return retVal;
    }
	
    private static class MyExit implements Exit {
        private volatile boolean exit = false;

        public void exit() {
            exit = true;
        }

        public boolean isExit() {
            return exit;
        }
    }

}
