package  {
	import com.tastenkunst.as3.brf.nxt.BRFMode;
	import com.tastenkunst.as3.brf.nxt.BRFState;
	import com.tastenkunst.as3.brf.nxt.utils.DrawingUtils;

	import flash.events.Event;
	import flash.geom.Rectangle;

	/**
	 * Uses super class ExampleBase to init BRF, Camera and GUI.
	 * 
	 * Sets tracking mode BRFMode.FACE_DETECTION and its params.
	 * In this BRFMode the _brfManager does not update 
	 * the faceShape properties.
	 * 
	 * (And please, don't hide the BRF logo. If you need a 
	 * version without logo, just email us. Thanks!)
	 * 
	 * @author Marcel Klammer, Tastenkunst GmbH, 2014
	 */
	public class ExampleFaceDetection extends ExampleBase {
		
		/**
		 * We use the Rectangles that are preselected in ExampleBase.
		 */
		public function ExampleFaceDetection(
				cameraResolution : Rectangle = null,
				brfResolution : Rectangle = null,
				brfRoi : Rectangle = null,
				faceDetectionRoi : Rectangle = null,
				screenRect : Rectangle = null,
				maskContainer : Boolean = true,
				webcamInput : Boolean = true) {
			
			// Or do the face detection on the whole image, not just a smaller roi/part.
			
//			brfResolution		= brfResolution		|| new Rectangle(0, 0, 640, 480);
//			brfRoi				= brfRoi			|| new Rectangle(0, 0, 640, 480);
//			faceDetectionRoi	= faceDetectionRoi	|| new Rectangle(0, 0, 640, 480);
			
			super(cameraResolution, brfResolution, brfRoi, 
				faceDetectionRoi, screenRect, maskContainer, webcamInput);
		}
		
		/**
		 * When BRF is ready, you can set its params and BRFMode.
		 * 
		 * In this example we want to do just face detection, a simple rectangle
		 * around a found face, so we set tracking mode to BRFMode.FACE_DETECTION.
		 */
		override public function onReadyBRF(event : Event) : void {
			
			// Please look into the docs/API reference for the meaning of the params. 
			_brfManager.setFaceDetectionVars(5.0, 1.0, 14.0, 0.06, 6, false);
			_brfManager.setFaceDetectionROI(
					_faceDetectionRoi.x, _faceDetectionRoi.y, 
					_faceDetectionRoi.width, _faceDetectionRoi.height);
			
			// Face detection only? Yeah, only Face Detection...
			_brfManager.mode = BRFMode.FACE_DETECTION;

			super.onReadyBRF(event);
		}
		
		/**
		 * We don't need to overwrite the updateInput and updateBRF, but we
		 * need to draw the results for BRFMode.FACE_DETECTION.
		 */
		override public function updateGUI() : void {
			
			_draw.clear();
			// Get the current BRFState.
			var state : String = _brfManager.state;

			// Draw BRFs region of interest, that got analysed:
			DrawingUtils.drawRect(_draw, _brfRoi, false, 1.0, 0xacfeff, 1.0);
	
			if(state == BRFState.FACE_DETECTION) {
				// Draw the face detection roi.
				DrawingUtils.drawRect(_draw, _faceDetectionRoi, false, 1.0, 0xffff00, 1.0);
				
				// Draw all found face regions:
				DrawingUtils.drawRects(_draw, _brfManager.lastDetectedFaces);
				
				// And draw the one result, that got calculated from all the lastDetectedFaces.
				var rect : Rectangle = _brfManager.lastDetectedFace;
				if(rect != null && rect.width != 0) {
					DrawingUtils.drawRect(_draw, rect, false, 3.0, 0xff7900, 1.0);
				}
			}
		}
	}
}