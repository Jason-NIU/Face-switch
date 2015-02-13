package {
	import com.tastenkunst.as3.brf.nxt.BRFFaceShape;
	import com.tastenkunst.as3.brf.nxt.BRFMode;
	import com.tastenkunst.as3.brf.nxt.BRFState;
	import com.tastenkunst.as3.brf.nxt.utils.DrawingUtils;
	import flash.events.Event;
	import flash.geom.Rectangle;
	import Generate_connection;

    
	
	/**
	 * Uses super class ExampleBase to init BRF, Camera and GUI.
	 * 
	 * Sets tracking mode BRFMode.FACE_TRACKING and its params.
	 * Does not update the candide properties (see onReadyBRF).
	 * 
	 * (And please, don't hide the BRF logo. If you need a 
	 * version without logo, just email us. Thanks!)
	 * 
	 * @author Marcel Klammer, Tastenkunst GmbH, 2014
	 */
	
	public class Sendingfacialdata extends ExampleBase {
		private var count:int;
		
		/**
		 * We use the Rectangles that are preselected in ExampleBase.
		 */
		//initialise network connection
		public var socket_initial:Generate_connection= new Generate_connection();
				
		public function Sendingfacialdata(
				cameraResolution : Rectangle = null,
				brfResolution : Rectangle = null,
				brfRoi : Rectangle = null,
				faceDetectionRoi : Rectangle = null,
				screenRect : Rectangle = null,
				maskContainer : Boolean = true,
				webcamInput : Boolean = true) {
			
			super(cameraResolution, brfResolution, brfRoi, 
				faceDetectionRoi, screenRect, maskContainer, webcamInput);
			
			socket_initial.sock.connect("Localhost",8080);
			//socket_initial.configureListeners();
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
			_brfManager.setFaceDetectionROI(
					_faceDetectionRoi.x, _faceDetectionRoi.y, 
					_faceDetectionRoi.width, _faceDetectionRoi.height);
			_brfManager.setFaceTrackingVars(80, 500, 1);
			
			// If you don't need 3d engine support or don't want to use
			// the candide vertices, you can turn that feature off, 
			// which saves CPU cycles.
			_brfManager.candideEnabled = false;
			_brfManager.candideActionUnitsEnabled = false;
			
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
			DrawingUtils.drawRect(_draw, _brfRoi, false, 1.0, 0xacfeff, 1.0);
	
			if(state == BRFState.FACE_DETECTION) {
				// Last update was face detection only, 
				// draw the face detection roi and lastDetectedFace:
				DrawingUtils.drawRect(_draw, _faceDetectionRoi);//, false, 1, 0xffff00, 1);
				
				var rect : Rectangle = _brfManager.lastDetectedFace;
				if(rect != null && rect.width != 0) {
					DrawingUtils.drawRect(_draw, rect, false, 1.0, 0xff7900, 1.0);
				}
			} else if(state == BRFState.FACE_TRACKING_START || state == BRFState.FACE_TRACKING) {
				// The found face rectangle got analysed in detail
				// draw the faceShape and its bounds:
				//DrawingUtils.drawTriangles(_draw, faceShape.faceShapeVertices, faceShape.faceShapeTriangles);
				DrawingUtils.drawTrianglesAsPoints(_draw, faceShape.faceShapeVertices);
				//trace(faceShape.faceShapeVertices);
				count++;
				//check the connection and send the data
				if(socket_initial.sock.connected){
				socket_initial.sendRequest(faceShape.faceShapeVertices.toString());
				}else trace("Disconnected");
				DrawingUtils.drawRect(_draw, faceShape.bounds);
			}
		}
	}
}