package net.towerdefender.scenes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.manager.ResourcesManager;
import net.towerdefender.manager.SceneManager;
import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.ByteArrayInputStreamOpener;
import org.andengine.util.color.Color;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.util.Log;

public class GameScene extends BaseScene implements PreviewCallback,
		ErrorCallback {

	private HUD gameHUD;
	private Text scoreText;
	private Text fpsText;
	private int score = 0;

	public Camera mCamera;
	public SurfaceTexture mSurfaceTextureCamera;
	public BitmapTexture mTexture;
	public TextureRegion mTextureRegion;
	private Sprite mSpriteVideo;
	private ByteArrayInputStreamOpener byteArrayInput;
	private byte[] dataCamera = new byte[0];

	@Override
	public void createScene() {
		createBackground();
		createHUD();
		createController();

		/*
		 * mSurfaceTextureCamera = new SurfaceTexture(1); this.mCamera =
		 * Camera.open(); final Camera.Parameters parameters =
		 * this.mCamera.getParameters(); parameters.setPreviewSize(768, 480);
		 * parameters
		 * .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		 * mCamera.setParameters(parameters); mCamera.setErrorCallback(this);
		 * mCamera.setPreviewCallback(this); mCamera.startPreview(); showToast(
		 * "Preview Vidéo Started Resolution :" +
		 * parameters.getPreviewSize().width + "x" +
		 * parameters.getPreviewSize().height, true);
		 */
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {

	}

	private void createBackground() {
		Background background = new Background(Color.RED) {
			public void onDraw(GLState pGLState,
					org.andengine.engine.camera.Camera pCamera) {
				GLES20.glClearColor(0, 0, 0, 0);
				GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
				GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT
						| GL10.GL_DEPTH_BUFFER_BIT);
			};
		};
		setBackground(background);

	}

	private void createHUD() {
		gameHUD = new HUD();

		// CREATE SCORE TEXT
		scoreText = new Text(20, 420, resourcesManager.Coolvetiva,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setPosition(0, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		fpsText = new Text(20, 420, resourcesManager.Coolvetiva, "FPS : ",
				new TextOptions(HorizontalAlign.LEFT), vbom);
		fpsText.setPosition(0, 100);
		fpsText.setText("FPS : NA");
		gameHUD.attachChild(fpsText);

		camera.setHUD(gameHUD);
	}

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

	private void createController() {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
				0,
				camera.getHeight()
						- ResourcesManager.getInstance().onScreenControlBase
								.getHeight(), camera,
				ResourcesManager.getInstance().onScreenControlBase,
				ResourcesManager.getInstance().onScreenControlKnob, 0.1f, 200,
				vbom, new IAnalogOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {

					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl) {
						addToScore(1);
						GameActivity.getInstance().getCameraPreviewSurface()
								.autoFocusCamera();
					}
				});
		analogOnScreenControl.getControlBase().setBlendFunction(
				GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(1f);
		analogOnScreenControl.getControlKnob().setScale(1f);
		analogOnScreenControl.getBackground().setColor(0, 0, 0, 0);
		analogOnScreenControl.refreshControlKnobPosition();

		this.setChildScene(analogOnScreenControl);
	}

	private int imageCounter = 0;
	private long lastMillisecond = 0;

	@SuppressWarnings("unused")
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		imageCounter++;
		long current = System.currentTimeMillis();
		if (current - lastMillisecond >= 1000) {
			/*
			 * showToast("FPS : " + (imageCounter / ((current - lastMillisecond)
			 * / 100)), false);
			 */
			fpsText.setText("FPS : " + imageCounter);
			lastMillisecond = current;
			imageCounter = 0;
		}
		if (false) {
			Size previewSize = camera.getParameters().getPreviewSize();
			YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21,
					previewSize.width, previewSize.height, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width,
					previewSize.height), 100, baos);
			byte[] jdata = baos.toByteArray();
			if (data != null) {
				dataCamera = jdata;
				/*
				 * if (mSpriteVideo == null) createVideo();
				 * 
				 * mTexture.load(); this.mTextureRegion = TextureRegionFactory
				 * .extractFromTexture(this.mTexture);
				 * 
				 * this.mTextureRegion.updateTexture(mTexture);
				 * 
				 * mSpriteVideo.setTextureRegion(mTextureRegion);
				 */

				byteArrayInput = new ByteArrayInputStreamOpener(dataCamera);
				/*
				 * if (mTexture != null) mTexture.unload();
				 */
				try {
					this.mTexture = new BitmapTexture(
							ResourcesManager.getInstance().activity
									.getTextureManager(),
							byteArrayInput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.mTexture.load();

				/*
				 * if (mSpriteVideo != null) { mSpriteVideo.detachSelf();
				 * mSpriteVideo.dispose(); mSpriteVideo = null; }
				 */

				if (mSpriteVideo == null) {
					this.mTextureRegion = TextureRegionFactory
							.extractFromTexture(this.mTexture);
					mSpriteVideo = new Sprite(0, 0, mTextureRegion, vbom) {

						@Override
						protected void preDraw(GLState pGLState,
								org.andengine.engine.camera.Camera pCamera) {
							super.preDraw(pGLState, pCamera);
							pGLState.enableDither();
						}

					};

					mSpriteVideo.setScale(1f);
					mSpriteVideo.setPosition(this.camera.getCenterX()
							- (mSpriteVideo.getHeight() / 2),
							this.camera.getCenterY()
									- (mSpriteVideo.getWidth() / 2));

					attachChild(mSpriteVideo);
				} else {
					mTextureRegion.updateTexture(mTexture);
					mSpriteVideo.setTextureRegion(mTextureRegion);
				}

				/*
				 * ByteArrayInputStreamOpener byteArrayInput = new
				 * ByteArrayInputStreamOpener( data);
				 * 
				 * try { this.mTexture = new BitmapTexture(
				 * ResourcesManager.getInstance().activity .getTextureManager(),
				 * byteArrayInput); } catch (IOException e) {
				 * e.printStackTrace(); } this.mTexture.load();
				 * 
				 * this.mTextureRegion = TextureRegionFactory
				 * .extractFromTexture(this.mTexture);
				 */

			}
		}

	}

	@Override
	public void onError(int error, Camera camera) {
		Log.e("CAM", "Error Callback");
	}

}
