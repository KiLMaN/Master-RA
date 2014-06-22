package net.towerdefender.activity;

import gameplay.Game;
import gameplay.Player;
import gameplay.Tower;
import gameplay.XMLParser;
import gameplay.XMLParserWave;
import gameplay.XMLParserWeapon;

import java.io.IOException;

import net.towerdefender.FileReaderAndroid;
import net.towerdefender.UdpClient;
import net.towerdefender.gstreamer.GStreamerSurfaceView;
import net.towerdefender.image.ARObject;
import net.towerdefender.image.ARToolkit;
import net.towerdefender.image.CameraPreviewHandler;
import net.towerdefender.image.GlyphObject;
import net.towerdefender.image.IO;
import net.towerdefender.manager.ResourcesManager;
import net.towerdefender.manager.SceneManager;
import net.towerdefender.opengl.LessonOneRenderer;

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
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.gstreamer.GStreamer;

public class GameActivity extends BaseGameActivity implements
		SurfaceHolder.Callback {
	/* Jeux */
	private Game mGame;

	/* Communication */
	private UdpClient mUdpClient;
	private Tower mCurrentlyControlledTower = null;

	/* Used to keep only one instance */
	private static GameActivity INSTANCE;

	/* Engine params */
	private static int _LimitFPS = 30;
	private static int _WIDTH = 1280;
	private static int _HEIGHT = 720;

	/* Engine Camera and resource manager (for AndEngine) */
	private Camera mEngineCamera;
	private ResourcesManager resourcesManager;

	/* Augmented Reality Renderer and Surface */
	//public ARRajawaliRender mARRajawaliRender;
	private GLSurfaceView mRAView;

	/* Marker detector */
	ARToolkit markerInfo;

	/* Surface for the Camera and the CameraPreviewHandler */
	private CameraPreviewSurfaceView mCameraPreviewSurfaceView;
	private CameraPreviewHandler mCameraPreview;

	/* Gstreamer Surface */
	private GStreamerSurfaceView mGstreamerView = null;

	public native void nativeInit(); // Initialize native code, build pipeline,

	public native void changeIpConnexion(int a, int b, int c, int d, int stop); // Update
																				// Ip
																				// Connexion

	public native void nativeFinalize(); // Destroy pipeline and shutdown native
											// code

	public native void nativePlay(); // Set pipeline to PLAYING

	public native void nativePause(); // Set pipeline to PAUSED

	public static native boolean nativeClassInit(); // Initialize native class:
													// cache Method IDs for
													// callbacks

	private native void nativeSurfaceInit(Object surface);

	private native void nativeSurfaceFinalize();

	private long native_custom_data; // Native code will use this to keep
										// private data
	private boolean is_playing_desired = true; // Whether the user asked to go

	public GameActivity() {
		super();
		INSTANCE = this;
		this.mGame = new Game();
		this.mCurrentlyControlledTower = null;
		this.mUdpClient = new UdpClient(this);
	}

	public void initGame() {
		XMLParser parserWeapon = new XMLParser("weapons.xml");
		parserWeapon.loadFile(new FileReaderAndroid(this));
		Node rootWeapon = parserWeapon.getRoot();
		mGame.setDefaultWeapons(XMLParserWeapon.parseXMLWeapon(rootWeapon));

		// System.out.println("Loading Enemies Waves");
		XMLParser parserWaves = new XMLParser("waves.xml");
		parserWaves.loadFile(new FileReaderAndroid(this));
		Node rootWave = parserWaves.getRoot();
		mGame.setWaves(XMLParserWave.parseXMLWaves(rootWave));

		this.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {

				mGame.setCurrentPlayer(new Player(1, "player n°1", 100, 0));

				mGame.addPoints(999);

				mGame.setPaused(true);

				mGame.setTowers(getUdpClient().getTowers());
				mGame.assignWeapons();

			}
		});

	}

	public Game getGame() {
		return mGame;
	}

	public static GameActivity getInstance() {
		return INSTANCE;
	}

	public Tower getCurrentlyControlledTower() {
		return mCurrentlyControlledTower;
	}

	public void setCurrentlyControlledTower(Tower _tower) {
		if (_tower != null) {
			if (mCurrentlyControlledTower != null) {
				this.mCurrentlyControlledTower.disconnect();
				this.mCurrentlyControlledTower.stopCommunication();
				this.mCurrentlyControlledTower.setControledByPlayer(false);
			}
			this.mCurrentlyControlledTower = _tower;
			this.mCurrentlyControlledTower.setControledByPlayer(true);
			this.mCurrentlyControlledTower.startCommunication();
			this.mCurrentlyControlledTower.connect();
			int[] ip = mCurrentlyControlledTower.getIpNumbers();
			updateIp(ip[0], ip[1], ip[2], ip[3]);

			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// mCameraPreviewSurfaceView
					// .setVisibility(SurfaceView.INVISIBLE);
					mGstreamerView.getHolder().setFormat(PixelFormat.OPAQUE);
				}
			});

		} else {
			if (mCurrentlyControlledTower != null) {
				this.mCurrentlyControlledTower.disconnect();
				this.mCurrentlyControlledTower.stopCommunication();
				this.mCurrentlyControlledTower.setControledByPlayer(false);
				nativePause();
				this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// mCameraPreviewSurfaceView
						// .setVisibility(SurfaceView.VISIBLE);
						mGstreamerView.getHolder().setFormat(
								PixelFormat.TRANSLUCENT);
					}
				});

			}
			this.mCurrentlyControlledTower = null;
		}

	}

	public UdpClient getUdpClient() {
		return this.mUdpClient;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize GStreamer and warn if it fails
		try {
			GStreamer.init(this);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		SurfaceView sv = (SurfaceView) this.mGstreamerView;
		SurfaceHolder sh = sv.getHolder();
		sh.addCallback(this);

		nativeInit();
		nativePause();
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
		return new Engine(pEngineOptions);
	}

	public EngineOptions onCreateEngineOptions() {
		/* Recuperation de la taille de l'ecran */
		// DisplayMetrics metrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);
		/*
		 * Desactivation de la taille automatique en fonction de l'ecran, comme
		 * ça on a la meme chose entre les tablettes et les téléphones
		 */
		// _HEIGHT = metrics.heightPixels;
		// _WIDTH = metrics.widthPixels;

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

		this.mGstreamerView = new GStreamerSurfaceView(this);
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

		//this.mARRajawaliRender = new ARRajawaliRender(this);
		LessonOneRenderer mRenderera = new LessonOneRenderer();
		mRAView = new GLSurfaceView(this);

		mRAView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mRAView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mRAView.setEGLContextClientVersion(2);
		mRAView.setRenderer(mRenderera);

		markerInfo = new ARToolkit(getResources(), getFilesDir());
		markerInfo.addVisibilityListener(mRenderera);
		mCameraPreview = new CameraPreviewHandler(mRAView, getResources(),
				markerInfo);
		try {
			IO.transferFilesToPrivateFS(getFilesDir(), getResources());
		} catch (IOException e) {
			e.printStackTrace();
			// throw new AndARRuntimeException(e.getMessage());
		}

		setContentView(mRenderSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mRAView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// addContentView(mGLSurfaceView, new LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(this.mGstreamerView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mCameraPreviewSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		/* Once the layout is ok, load the Game */
		initGame();

	}

	public void setHardwareCamera(android.hardware.Camera cam) {

		cam.setPreviewCallback(mCameraPreview);
		mCameraPreview.init(cam);

		ARObject obj;/*
						* = new GlyphObject("test", "barcode.patt", 80.0, new
						* double[] { 0, 0 }); markerInfo.registerARObject(obj);
						*/

		obj = new GlyphObject("objectif", "marker6_objectif.patt", 100.0,
				new double[] { 0, 0 }, 0x00FFFF);
		markerInfo.registerARObject(obj);

		obj = new GlyphObject("start", "marker7_start.patt", 100.0,
				new double[] { 0, 0 }, 0xFFFFFF);
		markerInfo.registerARObject(obj);

		obj = new GlyphObject("tour1", "marker1_tour.patt", 100.0,
				new double[] { 0, 0 }, 0xFFFF00);
		markerInfo.registerARObject(obj);

		obj = new GlyphObject("tour2", "marker2_tour.patt", 100.0,
				new double[] { 0, 0 }, 0xFFFF00);
		markerInfo.registerARObject(obj);

		obj = new GlyphObject("tour3", "marker3_tour.patt", 100.0,
				new double[] { 0, 0 }, 0xFFFF00);
		markerInfo.registerARObject(obj);

		obj = new GlyphObject("tour4", "marker4_tour.patt", 100.0,
				new double[] { 0, 0 }, 0xFFFF00);
		markerInfo.registerARObject(obj);

		obj = new GlyphObject("tour5", "marker5_tour.patt", 100.0,
				new double[] { 0, 0 }, 0xFFFF00);
		markerInfo.registerARObject(obj);

	}

	public ARToolkit getArtoolkit() {
		return markerInfo;
	}

	public CameraPreviewSurfaceView getCameraPreviewSurface() {
		return mCameraPreviewSurfaceView;
	}

	// Gstreamer

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		nativeSurfaceInit(holder.getSurface());
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		nativeSurfaceFinalize();
	}

	// Called from native code. This sets the content of the TextView from the
	// UI thread.
	private void setMessage(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d("Gstreamer Message", message);
			}
		});
	}

	private void onGStreamerInitialized() {
		nativePlay();
	}

	public void updateIp(int a, int b, int c, int d) {

		changeIpConnexion(a, b, c, d, 1);
		nativePlay();
	}

	static {
		System.loadLibrary("gstreamer_android");
		System.loadLibrary("towerdefender_gstreamer");
		nativeClassInit();
	}

}
