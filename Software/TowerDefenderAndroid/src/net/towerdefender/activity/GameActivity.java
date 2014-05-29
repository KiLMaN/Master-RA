package net.towerdefender.activity;

import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import net.towerdefender.R;
import net.towerdefender.TowerDefender;
import net.towerdefender.gstreamer.GStreamerSurfaceView;
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

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera.CameraInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gstreamer.GStreamer;

public class GameActivity extends BaseGameActivity implements
		SurfaceHolder.Callback {

	private static GameActivity INSTANCE;

	@SuppressWarnings("unused")
	private static int _LimitFPS = 60;
	private static int _WIDTH = 1280;
	private static int _HEIGHT = 720;

	private Camera mEngineCamera;
	private ARRajawaliRender mARRajawaliRender;
	@SuppressWarnings("unused")
	private ResourcesManager resourcesManager;

	public GPUImage mGPUImage;

	private CameraPreviewSurfaceView mCameraPreviewSurfaceView;
	private GLSurfaceView mRAView;

	// Gstreamer
	private GStreamerSurfaceView mGstreamerView = null;

	private native void nativeInit(); // Initialize native code, build pipeline,

	public native void changeIpConnexion(int a, int b, int c, int d); // Update
																		// Ip
	// Connexion

	private native void nativeFinalize(); // Destroy pipeline and shutdown
											// native code

	public native void nativePlay(); // Set pipeline to PLAYING

	public native void nativePause(); // Set pipeline to PAUSED

	private static native boolean nativeClassInit(); // Initialize native class:
														// cache Method IDs for
														// callbacks

	private native void nativeSurfaceInit(Object surface);

	private native void nativeSurfaceFinalize();

	private long native_custom_data; // Native code will use this to keep
										// private data
	private boolean is_playing_desired = true; // Whether the user asked to go

	// to PLAYING

	public void a(int a, int b, int c, int d) {
		changeIpConnexion(a, b, c, d);
		nativePause();
		nativePlay();
	}

	public GameActivity() {
		INSTANCE = this;
	}

	public static GameActivity getInstance() {
		return INSTANCE;
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

		// setContentView(R.layout.activity_main);

		/*
		 * if (savedInstanceState == null) {
		 * getSupportFragmentManager().beginTransaction() .add(R.id.container,
		 * new PlaceholderFragment()).commit(); }
		 */

		SurfaceView sv = (SurfaceView) this.mGstreamerView;
		SurfaceHolder sh = sv.getHolder();
		sh.addCallback(this);

		nativeInit();
		a(10, 1, 1, 116);
		// changeIpConnexion(10, 1, 1, 116);
		// nativePause();
		// nativePlay();
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

		this.mGstreamerView = new GStreamerSurfaceView(this);

		this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);

		GLSurfaceView mGLSurfaceView = new GLSurfaceView(this);
		mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mGLSurfaceView.setEGLContextClientVersion(2);
		mGPUImage = new GPUImage(this);
		mGPUImage.setGLSurfaceView(mGLSurfaceView);

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

		addContentView(this.mGstreamerView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mCameraPreviewSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	}

	public void setHardwareCamera(android.hardware.Camera cam) {

		mGPUImage
				.setUpCamera(
						cam,
						TowerDefender.CameraSelection == CameraInfo.CAMERA_FACING_FRONT ? 180
								: 0,
						TowerDefender.CameraSelection == CameraInfo.CAMERA_FACING_FRONT,
						false);

		// mGPUImage.setUpCamera(cam);
		mGPUImage.setFilter(new GPUImageSobelEdgeDetection());
	}

	public CameraPreviewSurfaceView getCameraPreviewSurface() {
		return mCameraPreviewSurfaceView;
	}

	// Gstreamer

	static {
		System.loadLibrary("gstreamer_android");
		System.loadLibrary("towerdefender_gstreamer");
		nativeClassInit();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Log.d("GStreamer", "Surface changed to format " + format + " width "
		// + width + " height " + height);
		nativeSurfaceInit(holder.getSurface());
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// Log.d("GStreamer", "Surface created: " + holder.getSurface());
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Log.d("GStreamer", "Surface destroyed");
		nativeSurfaceFinalize();
	}

	// Called from native code. This sets the content of the TextView from the
	// UI thread.
	private void setMessage(final String message) {
		final TextView tv = (TextView) this.findViewById(R.id.textview_message);
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d("GSTTT", message);
				// tv.setText(message);
			}
		});
	}

	private void onGStreamerInitialized() {
		Log.i("GStreamer", "Gst initialized. Restoring state, playing:"
				+ is_playing_desired);
		// Restore previous playing state

		// changeIpConnexion(10, 1, 1, 190);
		// nativePause();
		nativePlay();

		// changeIpConnexion(10, 1, 1, 160);
		// nativePause();
		// nativePlay();
		// nativeFinalize();
		// nativeClassInit();
		// nativeInit();
		// is_playing_desired = true;
		// if (is_playing_desired) {
		// nativePlay();
		// } else {
		// nativePause();
		// }

		// Re-enable buttons, now that GStreamer is initialized
		final Activity activity = this;
		runOnUiThread(new Runnable() {
			public void run() {
				// activity.findViewById(R.id.button_play).setEnabled(true);
				// activity.findViewById(R.id.button_stop).setEnabled(true);
			}
		});
	}

}
