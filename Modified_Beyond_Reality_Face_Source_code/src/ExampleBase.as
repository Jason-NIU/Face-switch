package  {
	import com.tastenkunst.as3.brf.nxt.BRFManager;
	import com.tastenkunst.as3.brf.nxt.utils.BitmapDataStats;

	import flash.display.BitmapData;
	import flash.display.Graphics;
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageQuality;
	import flash.display.StageScaleMode;
	import flash.events.Event;
	import flash.geom.Matrix;
	import flash.geom.Rectangle;
	import flash.media.Camera;
	import flash.media.Video;

	/**
	 * This is a simple base class for all BRF trackings modes.
	 * It handles most things for you and offers you presets for 
	 * camera and analysis resolutions (see the constructor).
	 * 
	 * v3.0.10: Restructered this base class to add a better
	 * handling for single image analysis.
	 * 
	 * (And please, don't hide the BRF logo. If you need a 
	 * version without logo, just email us. Thanks!)
	 * 
	 * "roi" means "region of interest" - a Rectangle area to work in.
	 * 
	 * @author Marcel Klammer, Tastenkunst GmbH, 2014
	 */
	public class ExampleBase extends Sprite {
		
		// We work with rectangles to configurate
		// all visible and analysis stuff. 
		// See the constructor for details.
		public var _cameraResolution : Rectangle;
		public var _brfResolution : Rectangle;
		public var _brfRoi : Rectangle;
		public var _faceDetectionRoi : Rectangle;
		public var _screenRect : Rectangle;
		public var _maskContainer : Boolean;
		public var _webcamInput : Boolean;
		
		// view and brf matrices to draw inputs the right way
		public var _videoToBRFMatrix : Matrix;
		public var _videoToScreenMatrix : Matrix;
		
		// We add all visible content to this container.
		public var _container : Sprite;
		// And mask it, if needed.
		public var _containerMask : Sprite;
		
		// Camera, Video and mirrored? 
		// We don't need a video for single image handling though.
		public var _camera : Camera;
		public var _video : Video;
		public var _mirrored : Boolean;
		public var _cameraRotation : Number;
		
		// Drawing is done on _drawSprite, it will be scaled
		// to match the BRF BitmapData size/results.
		public var _drawSprite : Sprite;
		public var _draw : Graphics;
		// Same with this click area. We need the same
		// coordinate system that BRF works in, 
		// so need to place and scale it accordingly.
		public var _clickArea : Sprite;
		
		// That's the BitmapData BRF analyses.
		public var _brfBmd : BitmapData;
		// The all mighty BRFManager!
		// See the API and Examples for implementation details.
		public var _brfManager : BRFManager;
		
		// Stats with a minimal performance hit.
		public var _stats : BitmapDataStats;
		
		/**
		 * You can of course put in your own custom resolution rectangles here.
		 * Or you just use one of the following presets for a quick start.
		 * 
		 * (And please, don't hide the BRF logo. If you need a 
		 * version without logo, just email us. Thanks!)
		 */
		public function ExampleBase(
				// That's width and height for the _camera.setMode and _video or _image.
				cameraResolution : Rectangle = null, 
				// That's the size of the BitmapData BRF will work on.
				brfResolution : Rectangle = null,
				// Let's limit the area of detailed face analysis
				// to achieve better performance.
				brfRoi : Rectangle = null,
				// The detailed analysis should start only if the users face
				// is centered in the webcam image. Let's limit the initial face detection roi.
				faceDetectionRoi : Rectangle = null,
				// Set the position and size of the visible webcam video/image here.
				screenRect : Rectangle = null, 
				// You can also mask the video/image container to limit it exactly to 
				// screenRect size and position.
				maskContainer : Boolean = true,
				// Webcams should usually be mirrored, image inputs should not though.
				// Also we don't need to add the video, if an image is used.
				webcamInput : Boolean = true) {
				
			// BRF is very CPU expensive at higher than 640x480 resolutions. 
			// BRFs BitmapData size/region of interest should definitly NOT exceed 800x600. 
			// You can of course choose a higher camera resolution
			// and draw to a smaller BRF BitmapData, then scale the results back up accordingly. 
			// This is what this fancy base class does for you.
			//	
			// Here are some presets for a quick start:
			
			// 240p camera resolution + 320x240 BRF roi + 320x240 face detection roi + doubled screenRect size to scale the video up
//			_cameraResolution	= cameraResolution	|| new Rectangle(   0,   0,  320, 240),	// Camera resolution
//			_brfResolution		= brfResolution		|| new Rectangle(   0,   0,  320, 240),	// BRF BitmapData size
//			_brfRoi				= brfRoi			|| new Rectangle(   0,   0,  320, 240),	// BRF region of interest within BRF BitmapData size
//			_faceDetectionRoi	= faceDetectionRoi	|| new Rectangle(   0,   0,  320, 240),	// BRF face detection region of interest within BRF BitmapData size
//			_screenRect			= screenRect		|| new Rectangle(   0,   0,  640, 480),	// Shown video screen rectangle
			
			// 480p camera resolution + 480x400 BRF roi + 320x320 centered face detection roi + 480p screenRect
			_cameraResolution	= cameraResolution	|| new Rectangle(   0,   0,  640, 480),	// Camera resolution
			_brfResolution		= brfResolution		|| new Rectangle(   0,   0,  640, 480),	// BRF BitmapData size
			_brfRoi				= brfRoi			|| new Rectangle(  80,  40,  480, 400),	// BRF region of interest within BRF BitmapData size
			_faceDetectionRoi	= faceDetectionRoi	|| new Rectangle( 160,  80,  320, 320),	// BRF face detection region of interest within BRF BitmapData size
			_screenRect			= screenRect		|| new Rectangle(   0,   0,  640, 480),	// Shown video screen rectangle
			
			// 720p camera resolution + 520x400 BRF roi + 320x320 face detection roi + 720p screenRect
//			_cameraResolution	= cameraResolution	|| new Rectangle(   0,   0, 1280, 720),	// Camera resolution
//			_brfResolution		= brfResolution		|| new Rectangle(   0,   0,  640, 480),	// BRF BitmapData size
//			_brfRoi				= brfRoi			|| new Rectangle(  60,  40,  520, 400),	// BRF region of interest within BRF BitmapData size
//			_faceDetectionRoi	= faceDetectionRoi	|| new Rectangle( 160,  80,  320, 320),	// BRF face detection region of interest within BRF BitmapData size
//			_screenRect			= screenRect		|| new Rectangle(   0,   0, 1280, 720),	// Shown video screen rectangle

			// 720p camera resolution + 640x480 BRF roi + 320x320 face detection roi + 720p screenRect
//			_cameraResolution	= cameraResolution	|| new Rectangle(   0,   0, 1280, 720),	// Camera resolution
//			_brfResolution		= brfResolution		|| new Rectangle(   0,   0,  640, 480),	// BRF BitmapData size
//			_brfRoi				= brfRoi			|| new Rectangle(   0,   0,  640, 480),	// BRF region of interest within BRF BitmapData size
//			_faceDetectionRoi	= faceDetectionRoi	|| new Rectangle( 160,  80,  320, 320),	// BRF face detection region of interest within BRF BitmapData size
//			_screenRect			= screenRect		|| new Rectangle(   0,   0, 1280, 720),	// Shown video screen rectangle
			
			_maskContainer		= maskContainer;	// Mask the video and draw container to match the screen rect width and height.
			_mirrored			= webcamInput;		// Mirror webcam, but don't mirror images.
			_webcamInput		= webcamInput;
			_cameraRotation		= 0.0;
			
			if(stage == null) {
				addEventListener(Event.ADDED_TO_STAGE, onAddedToStage);
			} else {
				stage.align = StageAlign.TOP_LEFT;
				stage.scaleMode = StageScaleMode.NO_SCALE;
				stage.quality = StageQuality.HIGH;
				// Who needs more than that anyway? 
				// (Cameras most likely can't do more either.)
				stage.frameRate = 30;
				
				onAddedToStage();
			}
		}
		
		/**
		 * BRF needs a stage reference. That's why we wait until the stage is available.
		 */
		public function onAddedToStage(event : Event = null) : void {
			removeEventListener(Event.ADDED_TO_STAGE, onAddedToStage);
			init();
		}
		
		/**
		 * Init all parts of this example class for webcam usage.
		 * Overwrite this method if you want to analyse still images (remove initCamera then).
		 */
		public function init() : void {
			initGUI();
			initBRF();
			initCamera();
		}
		
		/**
		 * Init all GUI elements.
		 */
		public function initGUI() : void {
			_container = new Sprite();
			_containerMask = new Sprite();
			_clickArea = new Sprite();
			_drawSprite = new Sprite();
			_draw = _drawSprite.graphics;
			
			_video = new Video();
			_video.smoothing = true;
			
			_stats = new BitmapDataStats();
			
			_videoToBRFMatrix = new Matrix();
			_videoToScreenMatrix = new Matrix();
			
			_webcamInput && _container.addChild(_video);
			_container.addChild(_drawSprite);
			_container.addChild(_stats);
			_container.addChild(_clickArea);
			
			addChild(_container);
			
			updateMatrices();
		}
		
		/**
		 * Init BRF once. Reuse the _brfManager instance, don't create a new one.
		 * 
		 * If you want to pause BRF, just call _brfManager.reset() and 
		 * don't call _brfManager.update() anymore.
		 */
		public function initBRF() : void {
			if(_brfManager == null) {
				_brfBmd = new BitmapData(_brfResolution.width, _brfResolution.height, false, 0x444444);
				_brfManager = new BRFManager(_brfBmd, _brfRoi, stage);
				_brfManager.addEventListener("ready", onReadyBRF);
			}
		}
		
		/**
		 * BRF is setup to do face tracking by default. 
		 * Overwrite this method to setup BRF params for a specific
		 * tracking mode. 
		 * See the other example subclasses for
		 * implementation details.
		 */
		public function onReadyBRF(event : Event) : void {
			addEventListener(Event.ENTER_FRAME, update);
		}
		
		/**
		 * Init the webcam.
		 */
		public function initCamera() : Boolean {
			_camera = Camera.getCamera();
			
			if(_camera != null) {
				updateCameraResolution(_cameraResolution.width, _cameraResolution.height);
				
				_video.attachCamera(_camera);
			}
			
			return _camera != null;
		}
		
		/**
		 * This method updates the input (webcam video or image),
		 * updates BRF and finally updates the GUI as well.
		 * Overwrite the other 3 methods (updateInput, updateBRF, updateGUI),
		 * if you want to change a behavior.
		 */
		public function update(event : Event = null) : void {
			updateInput();
			updateBRF();
			updateGUI();
		}
		
		/**
		 * BRF needs a fresh input (new webcam image or a still image),
		 * so do the BitmapData preparation here.
		 */
		public function updateInput() : void {
			_brfBmd.draw(_video, _videoToBRFMatrix);
		}
		
		/**
		 * Then we let BRF do its magic. There is the _brfManager.update() method
		 * as well as _brfManager.updateByEyes() for still images. default is webcam handling.
		 */
		public function updateBRF() : void {
			_brfManager.update(_brfBmd);
		}
		
		/**
		 * And after BRF got updated we get the current BRFState 
		 * and draw the updated results (eg. points, lastDetectedFace(s) or faceShape vertices etc.).
		 * 
		 * This is BRFMode specific, so overwrite this method in subclasses.
		 */ 
		public function updateGUI() : void {
			// Draw the results.
		}
		
		/**
		 * Update the input size. This might be done once in initCamera or if you want
		 * to use single images as input, this will be called once per image.
		 */
		public function updateCameraResolution(width : int, height : int, resizeScreenResolution : Boolean = false) : void {
			_cameraResolution.width = width;
			_cameraResolution.height = height;
			
			if(resizeScreenResolution) {
				_screenRect.width = width;
				_screenRect.height = height;
			}
			
			var cw : int = _cameraResolution.width;
			var ch : int = _cameraResolution.height;
			
			if(cw < ch) {
				cw = _cameraResolution.height;
				ch = _cameraResolution.width;
			}
			
			if(_camera != null) {
				_camera.setMode(cw, ch, 30);
			
				var index : int = -1;
				
				if(_video != null) {
					if(_container.contains(_video)) {
						index = _container.getChildIndex(_video);
						_container.removeChild(_video);
					}
					_video.attachCamera(null);
				}
				
				_video = new Video(cw, ch);
				_video.smoothing = true;
				
				if(index >= 0) {
					_container.addChildAt(_video, index);
				}
			}
			
			updateMatrices();
		}
		
		/**
		 * If the input size (_cameraResolution) changes, we need 
		 * to update the matrices and the whole GUI.
		 */
		public function updateMatrices() : void {
			
			// update visible content
			
			var screenRatio : Number = _screenRect.width / _screenRect.height;
			var videoRatio : Number = _cameraResolution.width / _cameraResolution.height;
			var zoom : Number = 1.0;
			
			if(screenRatio <= videoRatio) {
				zoom = _cameraResolution.height / _screenRect.height;
			} else {
				zoom = _cameraResolution.width / _screenRect.width;				
			}
				
			var videoToScreenScale : Number = 1.0 / zoom;
			
			_videoToScreenMatrix.a = videoToScreenScale;
			_videoToScreenMatrix.b = 0.0;
			_videoToScreenMatrix.c = 0.0;
			_videoToScreenMatrix.d = videoToScreenScale;
			_videoToScreenMatrix.tx = videoToScreenScale * (_screenRect.width  * zoom - _cameraResolution.width)  * 0.5;
			_videoToScreenMatrix.ty = videoToScreenScale * (_screenRect.height * zoom - _cameraResolution.height) * 0.5;
			
			if(_cameraRotation != 0) {
				if(_cameraRotation == 90) {
					_videoToScreenMatrix.rotate(_cameraRotation * Math.PI / 180);
					_videoToScreenMatrix.translate(_screenRect.width, 0);
				}
				if(_cameraRotation == -90) {
					_videoToScreenMatrix.rotate(_cameraRotation * Math.PI / 180);
					_videoToScreenMatrix.translate(0, _screenRect.height);
				}
			}
			
			if(_mirrored) {
				_videoToScreenMatrix.scale(-1.0, 1.0);
				_videoToScreenMatrix.translate(_screenRect.width, 0.0);
			}
			
			_video.transform.matrix = _videoToScreenMatrix.clone();
			_video.smoothing = true;
			
			// update brf BitmapData filling matrix
			
			var brfRatio : Number = _brfResolution.width / _brfResolution.height;
			
			if(brfRatio <= videoRatio) {
				zoom = _cameraResolution.height / _brfResolution.height;
			} else {
				zoom = _cameraResolution.width / _brfResolution.width;				
			}
			
			var videoToBRFScale : Number = 1 / zoom;
			
			_videoToBRFMatrix.a = videoToBRFScale;
			_videoToBRFMatrix.b = 0.0;
			_videoToBRFMatrix.c = 0.0;
			_videoToBRFMatrix.d = videoToBRFScale;
			_videoToBRFMatrix.tx = videoToBRFScale * (_brfResolution.width  * zoom - _cameraResolution.width)  * 0.5;
			_videoToBRFMatrix.ty = videoToBRFScale * (_brfResolution.height * zoom - _cameraResolution.height) * 0.5;
				
			if(_cameraRotation != 0) {
				if(_cameraRotation == 90) {
					_videoToBRFMatrix.rotate(_cameraRotation * Math.PI / 180);
					_videoToBRFMatrix.translate(_brfResolution.width, 0);
				}
				if(_cameraRotation == -90) {
					_videoToBRFMatrix.rotate(_cameraRotation * Math.PI / 180);
					_videoToBRFMatrix.translate(0, _brfResolution.height);
				}
			}
			
			if(_mirrored) {
				_videoToBRFMatrix.scale(-1.0, 1.0);
				_videoToBRFMatrix.translate(_brfResolution.width, 0.0);
			}
			
			// update the GUI
			
			_container.x = _screenRect.x;
			_container.y = _screenRect.y;
			
			_drawSprite.scaleX = _videoToScreenMatrix.d / _videoToBRFMatrix.d;
			_drawSprite.scaleY = _drawSprite.scaleX;
			_drawSprite.x = (_screenRect.width  - _brfResolution.width  * _drawSprite.scaleX) * 0.5;
			_drawSprite.y = (_screenRect.height - _brfResolution.height * _drawSprite.scaleY) * 0.5;
			
			_stats.x = _screenRect.x + _screenRect.width - _stats.WIDTH;
			
			// To get local X and Y value, we need am area, that is the same size
			// as the BitmapData, but streched to the screenRect.
			_clickArea.graphics.clear();
			_clickArea.graphics.beginFill(0xffffff, 0.01);
			_clickArea.graphics.drawRect(0, 0, _brfResolution.width, _brfResolution.height);
			_clickArea.graphics.endFill();
			_clickArea.x = _drawSprite.x;
			_clickArea.y = _drawSprite.y;
			_clickArea.scaleX = _drawSprite.scaleX;
			_clickArea.scaleY = _drawSprite.scaleY;
			
			// Hide screenRect overlapping content
			_containerMask.graphics.clear();
			_containerMask.graphics.beginFill(0xffffff, 0.01);
			_containerMask.graphics.drawRect(0, 0, _screenRect.width, _screenRect.height);
			_containerMask.graphics.endFill();
			_containerMask.x = _screenRect.x;
			_containerMask.y = _screenRect.y;
			
			if(_maskContainer) {
				_container.mask = _containerMask;
			}
		}
		
		/**
		 * Sets whether the input should be mirrored or not.
		 */
		public function set mirrored(mirrored : Boolean) : void {
			if(_mirrored != mirrored) {
				_mirrored = mirrored;
				updateMatrices();
			}
		}

		/**
		 * If we have a setter, we should also add a getter.
		 */
		public function get mirrored() : Boolean {
			return _mirrored;
		}
	}
}