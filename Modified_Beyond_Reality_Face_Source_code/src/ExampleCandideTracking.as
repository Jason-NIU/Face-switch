package  {
	import com.tastenkunst.as3.brf.nxt.BRFFaceShape;
	import com.tastenkunst.as3.brf.nxt.BRFMode;
	import com.tastenkunst.as3.brf.nxt.BRFState;
	import com.tastenkunst.as3.brf.nxt.utils.DrawingUtils;

	import flash.events.Event;
	import flash.geom.Rectangle;

	/**
	 * Uses super class ExampleBase to init BRF, Camera and GUI.
	 * 
	 * Sets tracking mode BRFMode.FACE_TRACKING and its params.
	 * Does update the candide properties (see onReadyBRF).
	 * 
	 * (And please, don't hide the BRF logo. If you need a 
	 * version without logo, just email us. Thanks!)
	 * 
	 * @author Marcel Klammer, Tastenkunst GmbH, 2014
	 */
	public class ExampleCandideTracking extends ExampleBase {
		
		/**
		 * We use the Rectangles that are preselected in ExampleBase.
		 */
		public function ExampleCandideTracking(
				cameraResolution : Rectangle = null,
				brfResolution : Rectangle = null,
				brfRoi : Rectangle = null,
				faceDetectionRoi : Rectangle = null,
				screenRect : Rectangle = null,
				maskContainer : Boolean = true,
				webcamInput : Boolean = true) {

			super(cameraResolution, brfResolution, brfRoi, 
				faceDetectionRoi, screenRect, maskContainer, webcamInput);
		}
		
		/**
		 * When BRF is ready, we can set its params and BRFMode.
		 * 
		 * In this example we want to do face tracking, 
		 * so we set tracking mode to BRFMode.FACE_TRACKING.
		 */
		override public function onReadyBRF(event : Event) : void {
			
			// The following settings are completely optional.
			// BRF is by default set up to do the complete tracking
			// (including candide and its actionunits).
			_brfManager.setFaceDetectionVars(5.0, 1.0, 14.0, 0.06, 6, false);
			_brfManager.setFaceDetectionROI(_faceDetectionRoi.x, _faceDetectionRoi.y, 
					_faceDetectionRoi.width, _faceDetectionRoi.height);
			_brfManager.setFaceTrackingVars(80, 500, 1);
			
			// If you don't need 3d engine support or don't want to use
			// the candide vertices, you can turn that feature off, 
			// which saves CPU cycles.
			// But actually we want to show this feature in this example:
			_brfManager.candideEnabled = true;
			_brfManager.candideActionUnitsEnabled = true;
			
			// Face Tracking? Face Tracking!
			_brfManager.mode = BRFMode.FACE_TRACKING;

			super.onReadyBRF(event);
		}
		
		/**
		 * We don't need to overwrite the updateInput and updateBRF, but we
		 * need to draw the results for BRFMode.FACE_TRACKING.
		 */
		override public function updateGUI() : void {
			
			_draw.clear();
			
			// Get the current BRFState and faceShape.
			var state : String = _brfManager.state;
			var faceShape : BRFFaceShape = _brfManager.faceShape;
			
			// Draw BRFs region of interest, that got analysed:
			DrawingUtils.drawRect(_draw, _brfRoi, false, 2, 0xacfeff, 1.0);
	
			if(state == BRFState.FACE_DETECTION) {
				// Last update was face detection only, 
				// draw the face detection roi and lastDetectedFace:
				DrawingUtils.drawRect(_draw, _faceDetectionRoi, false, 1, 0xffff00, 1);
				
				var rect : Rectangle = _brfManager.lastDetectedFace;
				if(rect != null && rect.width != 0) {
					DrawingUtils.drawRect(_draw, rect, false, 1, 0xff7900, 1);
				}
			} else if(state == BRFState.FACE_TRACKING_START) {
				// FACE_TRACKING_START does not update the candide properties,
				// we only get faceShape vertices and bounds here.
				
				// The found face rectangle got analysed in detail
				// draw the faceShape and its bounds:

				DrawingUtils.drawTriangles(_draw, faceShape.faceShapeVertices, faceShape.faceShapeTriangles);
//				DrawingUtils.drawTrianglesAsPoints(_draw, faceShape.faceShapeVertices);
				DrawingUtils.drawRect(_draw, faceShape.bounds);
			} else if(state == BRFState.FACE_TRACKING) {
				// FACE_TRACKING does update the candide properties.
				DrawingUtils.drawTriangles(_draw, faceShape.candideShapeVertices, faceShape.candideShapeTriangles);
				DrawingUtils.drawTrianglesAsPoints(_draw, faceShape.candideShapeVertices);
			}
		}
	}
}