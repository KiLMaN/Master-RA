package net.towerdefender.activity;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.towerdefender.R;
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

import rajawali.Object3D;
import rajawali.SerializedObject3D;
import rajawali.animation.Animation.RepeatMode;
import rajawali.animation.RotateOnAxisAnimation;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.math.vector.Vector3.Axis;
import rajawali.renderer.RajawaliRenderer;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import gstreamer.GStreamerSurfaceView;


public class GameActivity extends BaseGameActivity /*implements SurfaceHolder.Callback*/ {

	private static GameActivity INSTANCE;
	private static int _LimitFPS = 30;
	private static int _WIDTH = 1280;
	private static int _HEIGHT = 720;

	private Camera camera;
	private RajawaliRenderer ARrenderer;
	@SuppressWarnings("unused")
	private ResourcesManager resourcesManager;

	private CameraPreviewSurfaceView mCameraPreviewSurfaceView;
	private GLSurfaceView mAndarRAView;
	
	// Gstreamer
	private GStreamerSurfaceView mGstreamerView;
	private native void nativeInit();     // Initialize native code, build pipeline, etc
    private native void nativeFinalize(); // Destroy pipeline and shutdown native code
    private native void nativePlay();     // Set pipeline to PLAYING
    private native void nativePause();    // Set pipeline to PAUSED
    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks
    private native void nativeSurfaceInit(Object surface);
    private native void nativeSurfaceFinalize();
    private long native_custom_data;      // Native code will use this to keep private data

    private boolean is_playing_desired = true;   // Whether the user asked to go to PLAYING
	    

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

		// startARTest();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, _LimitFPS);
	}

	public EngineOptions onCreateEngineOptions() {
		/* Recuperation de la taille de l'ecran */
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
		
		//this.mGstreamerView = new GStreamerSurfaceView(this);
		
		
		this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);

		this.mRenderSurfaceView = new RenderSurfaceView(this);
		this.mRenderSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.mRenderSurfaceView.setEGLContextClientVersion(2);
		this.mRenderSurfaceView.setRenderer(this.mEngine, this);
		

		ARrenderer = new RajawaliRenderer(this) {
			@Override
			protected void initScene() {
				super.initScene();

				DirectionalLight light = new DirectionalLight(0, 0, -1);
				light.setPower(1);

				getCurrentScene().addLight(light);
				getCurrentCamera().setPosition(0, 0, 16);

				try {
					ObjectInputStream ois = new ObjectInputStream(mContext
							.getResources().openRawResource(R.raw.monkey_ser));
					SerializedObject3D serializedMonkey = (SerializedObject3D) ois
							.readObject();
					ois.close();

					Object3D monkey = new Object3D(serializedMonkey);
					Material material = new Material();
					material.enableLighting(true);
					material.setDiffuseMethod(new DiffuseMethod.Lambert());
					monkey.setMaterial(material);
					monkey.setColor(0xffff8C00);
					monkey.setScale(2);
					getCurrentScene().addChild(monkey);

					RotateOnAxisAnimation anim = new RotateOnAxisAnimation(
							Axis.Y, 360);
					anim.setDurationMilliseconds(6000);
					anim.setRepeatMode(RepeatMode.INFINITE);
					anim.setInterpolator(new LinearInterpolator());
					anim.setTransformable3D(monkey);
					getCurrentScene().registerAnimation(anim);
					anim.play();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// -- set the background color to be transparent
				// you need to have called setGLBackgroundTransparent(true); in
				// the activity
				// for this to work.
				getCurrentScene().setBackgroundColor(0);

			}
		};
		mAndarRAView = new GLSurfaceView(this);
		ARrenderer.setFrameRate(60);
		ARrenderer.setSurfaceView(mAndarRAView);

		mAndarRAView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mAndarRAView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mAndarRAView.setEGLContextClientVersion(2);
		mAndarRAView.setRenderer(ARrenderer);

		// mAndarRAView.getHolder().addCallback(this);

		// Now let's create an OpenGL surface.
		// GLSurfaceView glView = new GLSurfaceView(this);
		// To see the camera preview, the OpenGL surface has to be created
		// translucently.
		// See link above.
		// glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		// glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		// The renderer will be implemented in a separate class, GLView, which
		// I'll show next.
		// glView.setRenderer(new GLClearRenderer());
		// Now set this as the main view.

		// Now also create a view which contains the camera preview...
		// TestSurfaceView cameraView = new TestSurfaceView(this);
		// ...and add it, wrapping the full screen size.

		setContentView(mRenderSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mAndarRAView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		addContentView(mCameraPreviewSurfaceView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//addContentView(this.mGstreamerView, new LayoutParams(
		//		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	}

	public CameraPreviewSurfaceView getCameraPreviewSurface() {
		return mCameraPreviewSurfaceView;
	}

	/*
	// Gstreamer
	private void onGStreamerInitialized () {
        Log.i ("GStreamer", "Gst initialized" );
        // Restore previous playing state
        nativePlay();
	}
    static {
        System.loadLibrary("gstreamer_android");
        System.loadLibrary("TowerDefender");
        nativeClassInit();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        Log.d("GStreamer", "Surface changed to format " + format + " width "
                + width + " height " + height);
        nativeSurfaceInit (holder.getSurface());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface created: " + holder.getSurface());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface destroyed");
        nativeSurfaceFinalize ();
    }
    */
}
