package test;
import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.framework.Scene;
import com.ardor3d.framework.Updater;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.light.DirectionalLight;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.ContextManager;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.GameTaskQueue;
import com.ardor3d.util.GameTaskQueueManager;
import com.ardor3d.util.ReadOnlyTimer;

public class ExperimentCanvasBase implements Updater, Scene {

	private final Canvas canvas;
	private final Node root;
	private final LogicalLayer logicalLayer;

	private boolean inited = false;

	private Box board;
	
	public ExperimentCanvasBase(final Canvas canvas, final LogicalLayer logicalLayer){
		this.canvas = canvas;
		this.logicalLayer = logicalLayer;
		root = new Node("root");
	}

    @MainThread
    public boolean renderUnto(final Renderer renderer) {
        // Execute renderQueue item
        GameTaskQueueManager.getManager(ContextManager.getCurrentContext()).getQueue(GameTaskQueue.RENDER).execute(
                renderer);

        renderer.draw(root);
        return true;
    }

    public PickResults doPick(final Ray3 pickRay) {
        // does nothing.
        return null;
    }

	@Override
	public void init() {
		if (inited)
			return;

		final ZBufferState buf = new ZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		root.setRenderState(buf);

		final DirectionalLight light = new DirectionalLight();

		light.setDiffuse(ColorRGBA.WHITE);
		light.setAmbient(ColorRGBA.WHITE);
		light.setDirection(0, 0.4, -1);
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		final LightState lightState = new LightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		root.setRenderState(lightState);

		attachBoard();

		revertCamera();

		inited = true;
	}

	private void attachBoard() {

		Vector3 max = new Vector3(48, 48, 0);
		Vector3 min = new Vector3(-48, -48, -2);
		board = new Box("the board", min, max);
		
//		board.setRenderState(getLight(SOSAMain.experiment != null ? SOSAMain.experiment.boardColor : ColorRGBA.white));
//		board.setRenderQueueMode(Renderer.QUEUE_INHERIT);	//was having a strange issue of the board painting on top of everything... this forces it to paint first (so always in the bg even when things are behind it)
//		board.setDefaultColor(SOSAMain.experiment != null ? SOSAMain.experiment.boardColor : ColorRGBA.white);
//		board.setLightCombineMode(LightCombineMode.Inherit);
		
		root.attachChild(board);
	}

	public void revertCamera() {
		canvas.getCanvasRenderer().getCamera().setLocation(0, -96, 96);
		canvas.getCanvasRenderer().getCamera().lookAt(Vector3.ZERO,	Vector3.UNIT_Z);
	}

	@Override
	public void update(final ReadOnlyTimer timer) {
		final double tpf = timer.getTimePerFrame();

		logicalLayer.checkTriggers(tpf);

		// rotate
//		angle += tpf * CUBE_ROTATE_SPEED * rotationSign;
//
//		rotation.fromAngleAxis(angle, rotationAxis);
//		board.setRotation(rotation);

		board.updateGeometricState(tpf, true);
	}

}
