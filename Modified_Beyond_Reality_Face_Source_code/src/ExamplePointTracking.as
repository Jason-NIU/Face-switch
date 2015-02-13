package {
	import com.tastenkunst.as3.brf.nxt.BRFMode;
	import com.tastenkunst.as3.brf.nxt.BRFState;
	import com.tastenkunst.as3.brf.nxt.utils.DrawingUtils;

	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;

	/**
	 * Uses super class ExampleBase to init BRF, Camera and GUI.
	 * 
	 * Sets tracking mode BRFMode.POINT_TRACKING and its params.
	 * Does not update the faceShape properties,
	 * nor does it update lastDetectedFace.
	 * It's just pure point tracking.
	 * 
	 * (And please, don't hide the BRF logo. If you need a 
	 * version without logo, just email us. Thanks!)
	 * 
	 * @author Marcel Klammer, Tastenkunst GmbH, 2014
	 */
	public class ExamplePointTracking extends ExampleBase {
		
		// Fill this vector when a click occured to update
		// the pointsToTrack in BRF.
		public var _pointsToAdd : Vector.<Point>;
		public var _lastNumPoints : int = 0;

		/**
		 * We use the Rectangles that are preselected in ExampleBase.
		 */
		public function ExamplePointTracking(
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
		
		override public function initGUI() : void {
			super.initGUI();
			
			_clickArea.buttonMode = true;
			_clickArea.useHandCursor = true;
			_clickArea.mouseChildren = false;
		}
		
		/**
		 * When BRF is ready, we can set its params and BRFMode.
		 * 
		 * In this example we want to do point tracking, 
		 * so we set tracking mode to BRFMode.POINT_TRACKING.
		 */
		override public function onReadyBRF(event : Event) : void {

			_pointsToAdd = new Vector.<Point>();
			_lastNumPoints = -1;			
			
			// The following settings are the default settings.
			_brfManager.setPointTrackingVars(21, 4, 25, 0.0006);
			// true means:  Remove points before tracking if they are not valid.
			// false means: Handle point removale on your own.
			_brfManager.checkPointsValidBeforeTracking = true;
			
			//Point tracking? Point tracking!
			_brfManager.mode = BRFMode.POINT_TRACKING;
			
			super.onReadyBRF(event);

			_clickArea.addEventListener(MouseEvent.CLICK, onClickedVideo);
		}
		
		/**
		 *  Right before BRF tracks the points again, we want to add new points.
		 */
		override public function updateBRF() : void {
			if(_pointsToAdd.length > 0) {
				_brfManager.addPointsToTrack(_pointsToAdd);
				_pointsToAdd.length = 0;
			}
			
			super.updateBRF();
		}
		
		/**
		 * Now draw the results for BRFMode.POINT_TRACKING.
		 */
		override public function updateGUI() : void {
			
			_draw.clear();
			
			// Get the points and their states (valid or invalid)
			var points : Vector.<Point> = _brfManager.pointsToTrack;
			var pointStates : Vector.<Boolean> = _brfManager.pointStates;
			
			// Draw BRFs region of interest, that got analysed:
			DrawingUtils.drawRect(_draw, _brfRoi, false, 2, 0xacfeff, 1.0);
			
			if(_brfManager.state == BRFState.POINT_TRACKING) {
				var i : int = 0;
				var l : int = points.length;
				
				// draw points by state: yellow valid, red invalid
				while(i < l) {
					if(pointStates[i]) {
						DrawingUtils.drawPoint(_draw, points[i], 2);
					} else {
						DrawingUtils.drawPoint(_draw, points[i], 2, false, 0xff0000, 1.0);
					}
					++i;
				}
				
				// or just draw points tracked
//				DrawingUtils.drawPoints(_draw, points, 2, false);
			
				if(points.length != _lastNumPoints) {
					_lastNumPoints = points.length;
					trace("Number of points: " + _lastNumPoints);
				}
			}
		}
		
		/**
		 *  After a click occured we want to add points to track.
		 */
		public function onClickedVideo(event : MouseEvent) : void {
			var x : int = event.localX;
			var y : int = event.localY;
			
			// Add 1 point:
			//_pointsToAdd.push(new Point(x, y));
	
			//Add 100 points
			var w : Number = 60.0;
			var step : Number = 6.0;
			var xStart : Number = x - w * 0.5;//event.localX - w * 0.5;
			var xEnd : Number = x + w * 0.5; //event.localX + w * 0.5;
			var yStart : Number = y - w * 0.5; //event.localY - w * 0.5;
			var yEnd : Number = y + w * 0.5;//event.localY + w * 0.5;
			var dy : Number = yStart;
			var dx : Number = xStart;
	
			for(; dy < yEnd; dy += step) {
				for(dx = xStart; dx < xEnd; dx += step) {
					_pointsToAdd.push(new Point(dx, dy));
				}
			}
		}
	}
}
