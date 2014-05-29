package net.towerdefender.scenes;

import gameplay.Tower;

import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.manager.ResourcesManager;
import net.towerdefender.manager.SceneManager;
import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

public class GameScene extends BaseScene {

	private HUD gameHUD;
	private Sprite pictureSettings;
	private Sprite picturePlay;
	private Sprite picturePause;
	private Text scoreText;
	private Text lifeText;
	private int score = 0, lastUpdate = 0;
	private static Tower currentControlTower = null;

	@Override
	public void createScene() {
		createBackground();
		createHUD();
		createController();

		/*
		 * currentControlTower = new Tower(new Position());
		 * currentControlTower.setIp("192.168.1.9");
		 * currentControlTower.startCommunication();
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
		pictureSettings.detachSelf();
		pictureSettings.dispose();

		// TODO: � d�commenter une fois que la condition aura �t� impl�ment�e
		// picturePause.detachSelf();
		// picturePause.dispose();

		picturePlay.detachSelf();
		picturePlay.dispose();

		scoreText.detachSelf();
		scoreText.dispose();

		lifeText.detachSelf();
		lifeText.dispose();

		gameHUD.detachSelf();
		gameHUD.dispose();

		this.detachSelf();
		this.dispose();
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

		// CREATE SPRITE SETTINGS
		pictureSettings = new Sprite(0, 0,
				resourcesManager.buttonOptionSettings_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				addToScore(1);
				return true;
			}

		};
		pictureSettings.setHeight(100);
		pictureSettings.setWidth(100);
		gameHUD.registerTouchArea(pictureSettings);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(pictureSettings);
		// CREATE SPRITE PAUSE

		// TODO: mettre l'affichage en true si le jeu est en route

		/*
		 * picturePause = new Sprite(0, 0,
		 * resourcesManager.buttonOptionPause_region, vbom) {
		 * 
		 * @Override protected void preDraw(GLState pGLState, Camera pCamera) {
		 * super.preDraw(pGLState, pCamera); pGLState.enableDither(); } };
		 * picturePause.setPosition(camera.getWidth() - 100, 0);
		 * picturePause.setHeight(100); picturePause.setWidth(100);
		 * gameHUD.attachChild(picturePause);
		 */
		// TODO: mettre l'affichage en true si le jeu est en pause

		picturePlay = new Sprite(0, 0,
				resourcesManager.buttonOptionPlay_region, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				addToScore(1);
				return true;
			}
		};

		picturePlay.setPosition(camera.getWidth() - 100, 0);
		picturePlay.setHeight(100);
		picturePlay.setWidth(100);

		gameHUD.registerTouchArea(picturePlay);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(picturePlay);

		// CREATE SCORE TEXT
		scoreText = new Text(0, 0, resourcesManager.Coolvetica,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setPosition(0, 100);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);

		// CREATE LIFE TEXT
		lifeText = new Text(0, 0, resourcesManager.Coolvetica,
				"Life : 987654321", new TextOptions(HorizontalAlign.LEFT), vbom);
		lifeText.setPosition(0, 200);
		lifeText.setText("Life : 0");
		gameHUD.attachChild(lifeText);

		camera.setHUD(gameHUD);
	}

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

	public float xtest = 0;
	public float ytest = 0;

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

						xtest += pValueX * 10;
						ytest -= pValueY * 10;
						/*
						 * GameActivity.getInstance().mARRajawaliRender
						 * .changePositionTest(xtest, ytest, 0.0f);
						 */

						/*
						 * Log.w("test", "test value pos : x :" + xtest +
						 * " y : " + ytest);
						 */
						if (lastUpdate == 0) {
							if (currentControlTower != null) {
								currentControlTower.moveH((int) (pValueX * 10));
							}
						} else {
							lastUpdate--;
						}
					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl) {
						addToScore(1);
						GameActivity.getInstance().getCameraPreviewSurface()
								.autoFocusCamera();
						if (currentControlTower != null) {
							if (!currentControlTower.isConnected())
								currentControlTower.connect();
							else {

							}
						}
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
