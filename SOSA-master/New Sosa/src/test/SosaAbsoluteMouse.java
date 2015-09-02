package test;//package util;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.widgets.Composite;
//
//import com.jme.input.MouseInput;
//import com.jmex.swt.input.SWTMouseInput;
//
///**
// * <p>This class compensates for the inverted absolute mouse
// * positioning encountered when using SWT to capture and
// * feeding those movements directly to openGL.</p>
// *
// * <p>The problem is a result of differing origin (0,0) positions
// * between the two systems (top left for SWT, bottom left for GL).</p>
// *
// * <p>By subtracting the captured y coord from the known canvas height
// * the coordinate is inverted allowing the cursor to be properly
// * rendered in the GL system.</p>
// * @author smurray
// *
// */
//public class SosaAbsoluteMouse extends SWTMouseInput{
//
//	int canvasHeight = 0;
//
//	@Override
//	public void mouseMove(MouseEvent e) {
//		if(canvasHeight > e.y )
//			e.y = canvasHeight - e.y;
//		super.mouseMove(e);
//	}
//
//	public void setCanvasHeight(int canvasHeight){
//		this.canvasHeight = canvasHeight;
//	}
//
//    /**
//     * Set up a canvas to fire mouse events via the input system.
//     * @param Composite canvas that should be listened to
//     * @param dragOnly true to enable mouse input to jME only when the mouse is dragged
//     * @param canvasHeight the height of the canvas being listened to
//     */
//    public static SosaAbsoluteMouse setup( Composite canvas, boolean dragOnly, int canvasHeight) {
//    	if (!MouseInput.isInited()) {
//    		setProvider(SosaAbsoluteMouse.class.getCanonicalName());
//    	}
//    	SosaAbsoluteMouse sosaAbsMouse = ( (SosaAbsoluteMouse) get() );
//        sosaAbsMouse.setDragOnly( dragOnly );
//        sosaAbsMouse.setCanvasHeight(canvasHeight);
//        canvas.addMouseListener((SosaAbsoluteMouse) MouseInput.get());
//        canvas.addMouseMoveListener((SosaAbsoluteMouse) MouseInput.get());
//        canvas.addDragDetectListener((SosaAbsoluteMouse) MouseInput.get());
//        canvas.addListener(SWT.MouseWheel, (SosaAbsoluteMouse) MouseInput.get());
//
//        return sosaAbsMouse;
//    }
//}