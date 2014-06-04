package net.towerdefender.activity;

import gameplay.Game;
import gameplay.Player;
import gameplay.XMLParser;
import gameplay.XMLParserTower;
import gameplay.XMLParserWave;
import gameplay.XMLParserWeapon;

import java.io.IOException;

import net.towerdefender.FileReaderAndroid;
import net.towerdefender.manager.ResourcesManager;
import net.towerdefender.manager.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.w3c.dom.Node;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class GameActivity extends BaseGameActivity /*
													 * implements
													 * SurfaceHolder.Callback
													 */{
	private Game mGame;
	private static GameActivity INSTANCE;

	@SuppressWarnings("unused")
	private static int _LimitFPS = 60;
	private static int _WIDTH = 1280;
	private static int _HEIGHT = 720;

	private Camera mEngineCamera;
	private ARRajawaliRender mARRajawaliRender;
	@SuppressWarnings("unused")
	private ResourcesManager resourcesManager;

	// public GPUImage mGPUImage;

	private CameraPreviewSurfaceView mCameraPreviewSurfaceView;
	private GLSurfaceView mRAView;

	// Gstreamer
	/*
	 * private GStreamerSurfaceView mGstreamerView;
	 * 
	 * private native void nativeInit(); // Initialize native code, build
	 * pipeline, // etc
	 * 
	 * private native void nativeFinalize(); // Destroy pipeline and shutdown //
	 * native code
	 * 
	 * private native void nativePlay(); // Set pipeline to PLAYING
	 * 
	 * private native void nativePause(); // Set pipeline to PAUSED
	 * 
	 * private static native boolean nativeClassInit(); // Initialize native
	 * class: // cache Method IDs for // callbacks
	 * 
	 * private native void nativeSurfaceInit(Object surface);
	 * 
	 * private native void nativeSurfaceFinalize();
	 * 
	 * private long native_custom_data; // Native code will use this to keep //
	 * private data
	 * 
	 * private boolean is_playing_desired = true; // Whether the user asked to
	 * go // to PLAYING
	 */

	public void initGame() {
		XMLParser parserWeapon = new XMLParser("weapons.xml");
		parserWeapon.loadFile(new FileReaderAndroid(this));
		Node rootWeapon = parserWeapon.getRoot();
		mGame.setDefaultWeapons(XMLParserWeapon.parseXMLWeapon(rootWeapon));

		System.out.println("Loading Tower Positions");
		XMLParser parserTower = new XMLParser("towers.xml");
		parserTower.loadFile(new FileReaderAndroid(this));
		Node rootTower = parserTower.getRoot();
		mGame.setTowers(XMLParserTower.parseXMLTowers(rootTower));

		mGame.assignWeapons();

		System.out.println("Loading Enemies Waves");
		XMLParser parserWaves = new XMLParser("waves.xml");
		parserWaves.loadFile(new FileReaderAndroid(this));
		Node rootWave = parserWaves.getRoot();
		mGame.setWaves(XMLParserWave.parseXMLWaves(rootWave));

		mGame.setCurrentPlayer(new Player(1, "player n°1", 100, 0, 0));
	}

	public GameActivity() {
		INSTANCE = this;
		this.mGame = new Game();

	}

	public Game getGame() {
		return mGame;
	}

	public static GameActivity getInstance() {
		return INSTANCE;
	}

	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		ResourcesManager.prepareManager(mEngine, this, mEngineCamera,
				getVertexBufferObjectManager());
		resourcesManager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		// Timer from SplashScreen to Menu
		mEngine.registerUpdateHandler(new TimerHandler(1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						SceneManager.getInstance().createMenuScene();
					}
				}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// We are using a limited FPS engine
		// return new LimitedFPSEngine(pEngineOptions, _LimitFPS);
		return new Engine(pEngineOptions);
	}

	public EngineOptions onCreateEngineOptions() {
		/* Recuperation de la taille de l'ecran */
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		_HEIGHT = metrics.heightPixels;
		_WIDTH = metrics.widthPixels;

		// Debig affichage
		toastOnUIThread("Resolution : " + _HEIGHT + "x" + _WIDTH,
				Toast.LENGTH_SHORT);

		// On créer une caméra de la taille de la fenetre de jeux
		mEngineCamera = new Camera(0, 0, _WIDTH, _HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						_WIDTH, _HEIGHT), this.mEngineCamera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getRenderOptions().setMultiSampling(true);
		return engineOptions;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Si la touche est : RETOUR : Dispatch à la scene courante
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		return false;
	}

	public void gameToast(final String msg, final int toastLenght) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(GameActivity.this, msg, toastLenght).show();
			}
		});
	}

	@Override
	protected void onSetContentView() {

		// this.mGstreamerView = new GStreamerSurfaceView(this);

		this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);

		/*
		 * GLSurfaceView mGLSurfaceView = new GLSurfaceView(this);
		 * mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		 * mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		 * mGLSurfaceView.setEGLContextClientVersion(2); //mGPUImage = new
		 * GPUImage(this); //mGPUImage.setGLSurfaceView(mGLSurfaceView);
		 */

		this.mRenderSurfaceView = new RenderSurfaceView(this);
		this.mRenderSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.mRenderSurfaceView.setEGLContextClientVersion(2);
		this.mRenderSurfaceView.setRenderer(this.mEngine, this);

		this.mARRajawaliRender = new ARRajawaliRender(this);

		mRAView = new GLSurfaceView(this);

		mRAView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mRAView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mRAView.setEGLContextClientVersion(2);
		mRAView.setRenderer(mARRajawaliRender);

		setContentView(mRenderSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mRAView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// addContentView(mGLSurfaceView, new LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mCameraPreviewSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// addContentView(this.mGstreamerView, new LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		initGame();
	}

	public void setHardwareCamera(android.hardware.Camera cam) {

		/*
		 * mGPUImage .setUpCamera( cam, TowerDefender.CameraSelection ==
		 * CameraInfo.CAMERA_FACING_FRONT ? 180 : 0,
		 * TowerDefender.CameraSelection == CameraInfo.CAMERA_FACING_FRONT,
		 * false);
		 */
		// mGPUImage.setUpCamera(cam);
		// mGPUImage.setFilter(new GPUImageSobelEdgeDetection());
	}

	public CameraPreviewSurfaceView getCameraPreviewSurface() {
		return mCameraPreviewSurfaceView;
	}

	// Gstreamer
	/*
	 * private void onGStreamerInitialized() { Log.i("GStreamer", // Restore
	 * previous playing state
	 */

	static {
		// System.loadLibrary("gstreamer_android");
		// System.loadLibrary("TowerDefender");
		// nativeClassInit();
	}

	/*
	 * public void surfaceChanged(SurfaceHolder holder, int format, int width,
	 * int height) { Log.d("GStreamer", "Surface changed to format " + format +
	 * " width " + width + " height " + height);
	 * nativeSurfaceInit(holder.getSurface()); }
	 * 
	 * public void surfaceCreated(SurfaceHolder holder) { Log.d("GStreamer",
	 * "Surface created: " + holder.getSurface()); }
	 * 
	 * public void surfaceDestroyed(SurfaceHolder holder) { Log.d("GStreamer",
	 * "Surface destroyed"); nativeSurfaceFinalize(); }
	 */
}
