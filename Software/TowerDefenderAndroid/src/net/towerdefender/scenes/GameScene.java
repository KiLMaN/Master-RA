package net.towerdefender.scenes;

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
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

public class GameScene extends BaseScene {

	private HUD gameHUD;
	private Text scoreText;
	private Text fpsText;
	private int score = 0;

	@Override
	public void createScene() {
		createBackground();
		createHUD();
		createController();

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
				GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
						| GL10.GL_DEPTH_BUFFER_BIT);
				GLES20.glClearColor(0, 0, 0, 0);

			};
		};
		setBackground(background);
	}

	private void createHUD() {
		gameHUD = new HUD();

		// CREATE SCORE TEXT
		scoreText = new Text(0, 0, resourcesManager.Coolvetiva,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setPosition(0, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		fpsText = new Text(0, 0, resourcesManager.Coolvetiva, "FPS : ",
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
						// Log.i("OnScreenControll", "Position :x " + pValueX
						// + "y" + pValueY);
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
		analogOnScreenControl.getControlBase().setScale(3f);
		analogOnScreenControl.getControlKnob().setScale(3f);
		analogOnScreenControl.getBackground().setColor(0, 0, 0, 0);
		analogOnScreenControl.refreshControlKnobPosition();

		this.setChildScene(analogOnScreenControl);
	}
}
