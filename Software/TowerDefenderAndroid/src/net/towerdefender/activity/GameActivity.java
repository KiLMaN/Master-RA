package net.towerdefender.activity;

import java.io.IOException;

import net.towerdefender.manager.ResourcesManager;
import net.towerdefender.manager.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
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

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class GameActivity extends BaseGameActivity {

	private static GameActivity INSTANCE;
	private static int _LimitFPS = 30;
	private static int _WIDTH = 1280;
	private static int _HEIGHT = 720;

	private Camera camera;
	@SuppressWarnings("unused")
	private ResourcesManager resourcesManager;

	private CameraPreviewSurfaceView mCameraPreviewSurfaceView;

	public GameActivity() {
		INSTANCE = this;
	}

	public static GameActivity getInstance() {
		return INSTANCE;
	}

	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		ResourcesManager.prepareManager(mEngine, this, camera,
				getVertexBufferObjectManager());
		resourcesManager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
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
		return new LimitedFPSEngine(pEngineOptions, _LimitFPS);
	}

	public EngineOptions onCreateEngineOptions() {
		/* Récupération de la taille de l'ecran */
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		_HEIGHT = metrics.heightPixels;
		_WIDTH = metrics.widthPixels;

		toastOnUIThread("Resolution : " + _HEIGHT + "x" + _WIDTH,
				Toast.LENGTH_SHORT);

		camera = new Camera(0, 0, _WIDTH, _HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						_WIDTH, _HEIGHT), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getRenderOptions().setMultiSampling(true);
		return engineOptions;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
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
		/*
		 * // // mFrame.addView(this.mCameraPreviewSurfaceView); FrameLayout
		 * mFrame = new FrameLayout(this);
		 */
		// setContentView(mTestSurfaceView);
		this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		// this.mRenderSurfaceView.setEGLContextClientVersion(2);
		this.mRenderSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		// this.mRenderSurfaceView.setZOrderMediaOverlay(true);
		this.mRenderSurfaceView.setRenderer(this.mEngine, this);

		// Now let's create an OpenGL surface.
		GLSurfaceView glView = new GLSurfaceView(this);
		// To see the camera preview, the OpenGL surface has to be created
		// translucently.
		// See link above.
		glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		// The renderer will be implemented in a separate class, GLView, which
		// I'll show next.
		glView.setRenderer(new GLClearRenderer());
		// Now set this as the main view.

		// Now also create a view which contains the camera preview...
		// TestSurfaceView cameraView = new TestSurfaceView(this);
		// ...and add it, wrapping the full screen size.
		setContentView(mRenderSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(glView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		addContentView(mCameraPreviewSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);
		/*
		 * this.addContentView(this.mCameraPreviewSurfaceView,
		 * BaseGameActivity.createSurfaceViewLayoutParams());
		 * this.addContentView(this.mRenderSurfaceView,
		 * BaseGameActivity.createSurfaceViewLayoutParams());
		 */

		// this.mCameraPreviewSurfaceView.bringToFront();
		// this.mRenderSurfaceView.setZOrderMediaOverlay(true);
		// this.mRenderSurfaceView.bringToFront();
	}

	public CameraPreviewSurfaceView getCameraPreviewSurface() {
		return mCameraPreviewSurfaceView;
	}

}