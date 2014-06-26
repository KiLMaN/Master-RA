package net.towerdefender.scenes;

import gameplay.Tower;

import java.util.ArrayList;

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
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import comon.GameTickUpdateListener;

public class GameScene extends BaseScene implements GameTickUpdateListener {

	private HUD gameHUD;
	private Sprite pictureSettings;
	private Sprite picturePlay;
	private Sprite picturePause;
	private Sprite[] pictureTower;
	private Sprite pictureTablet;
	private Text scoreText;
	private Text lifeText;
	private int lastUpdate = 0;

	private boolean displayList = false;
	private AnalogOnScreenControl analogOnScreenControl;

	public void updateScore() {
		scoreText.setText(GameActivity.getInstance().getGame().getPoints()
				+ " pts");

	}

	public void updateLife() {
		lifeText.setText(" Vies : "
				+ GameActivity.getInstance().getGame().getCurrentPlayer()
						.getLifesPlayer());

	}

	@Override
	public void createScene() {
		createBackground();
		createController();
		createHUD();

		GameActivity.getInstance().getGame().addTickListener(this);

		// currentControlTower = new Tower(new Position());
		// currentControlTower.setIp("192.168.1.7");
		// currentControlTower.startCommunication();

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

		if (picturePause != null) {
			picturePause.detachSelf();
			picturePause.dispose();
		}
		disposeTowersButtons();
		disposeTabletButton();
		if (picturePlay != null) {
			picturePlay.detachSelf();
			picturePlay.dispose();
		}

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
		Background background = new Background(Color.TRANSPARENT);
		setBackground(background);
		setBackgroundEnabled(false);
	}

	private boolean playButtonOn = false;

	private void switchPlayPauseButton(boolean bPlay) {
		if (bPlay) {
			playButtonOn = true;
			picturePlay
					.setTextureRegion(resourcesManager.buttonOptionPause_region);
		} else {
			playButtonOn = false;
			picturePlay
					.setTextureRegion(resourcesManager.buttonOptionPlay_region);
		}

	}

	private void picturePlayPausePressed() {
		if (!playButtonOn) {
			GameActivity.getInstance().getGame().setPlaying(true);
			GameActivity.getInstance().getGame().setPaused(false);
			switchPlayPauseButton(true);
		}
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
				if (pSceneTouchEvent.isActionUp())
					SceneManager.getInstance().loadToolsScene(engine);
				return true;
			}

		};
		pictureSettings.setHeight(100);
		pictureSettings.setWidth(100);
		gameHUD.registerTouchArea(pictureSettings);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(pictureSettings);
		// CREATE SPRITE PAUSE

		picturePlay = new Sprite(0, 0,
				resourcesManager.buttonOptionPlay_region, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					picturePlayPausePressed();
				}
				return true;
			}
		};

		picturePlay.setPosition(camera.getWidth() - 100, 0);
		picturePlay.setHeight(100);
		picturePlay.setWidth(100);
		switchPlayPauseButton(false);

		gameHUD.registerTouchArea(picturePlay);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(picturePlay);

		// CREATE SCORE TEXT
		scoreText = new Text(0, 0, resourcesManager.Coolvetica,
				"Score: 0123456789 pts", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setPosition(0, 100);
		// scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		updateScore();

		// CREATE LIFE TEXT
		lifeText = new Text(0, 0, resourcesManager.Coolvetica,
				"Vies : 987654321", new TextOptions(HorizontalAlign.LEFT), vbom);
		lifeText.setPosition(0, 200);
		lifeText.setText("Vie : 0");
		updateLife();
		gameHUD.attachChild(lifeText);

		// createTowersButtons(GameActivity.getInstance().getGame().getTowers()
		// .size(), 100, 100);
		createTabletButton(100, 100);
		if (analogOnScreenControl != null)
			disposeAnalogOnScreenControl();

		camera.setHUD(gameHUD);
	}

	private void createController() {
		analogOnScreenControl = new AnalogOnScreenControl(0,
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

						if (lastUpdate == 0) {
							if (GameActivity.getInstance()
									.getCurrentlyControlledTower() != null) {
								if (pValueX < -0.1f || pValueX > 0.1f) {
									GameActivity.getInstance()
											.getCurrentlyControlledTower()
											.moveH((int) (pValueX * 10));
								}

								if (pValueY < -0.1f || pValueY > 0.1f) {
									GameActivity.getInstance()
											.getCurrentlyControlledTower()
											.moveVOffset((int) (pValueY * 10));
								}
							}
						} else {
							lastUpdate--;
						}
					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl) {
						GameActivity.getInstance().getCameraPreviewSurface()
								.autoFocusCamera();

						// GameActivity.getInstance().updateIp(192, 168, 1, 88);

						// if (currentControlTower != null) {
						// if (!currentControlTower.isConnected())
						// currentControlTower.connect();
						// else {
						//
						// }
						// }
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

	private void createTowersButtons(ArrayList<Tower> towers,
			final int widthButtons, int heightButtons) {
		int numberButtons = towers.size();
		pictureTower = new Sprite[numberButtons];

		for (int i = 0; i <= numberButtons - 1; i++) {
			pictureTower[i] = new IconTower(0, 0,
					resourcesManager.buttonOptionTower_region, vbom,
					GameActivity.getInstance().getGame().getTowers().get(i));
			pictureTower[i].setPosition(camera.getWidth() - (i + 2)
					* widthButtons - (int) (0.75 * widthButtons),
					camera.getHeight() - heightButtons);
			pictureTower[i].setHeight(heightButtons);
			pictureTower[i].setWidth(widthButtons);
			Text towerText = new Text(40, 25, resourcesManager.Coolvetica,
					Integer.toString(towers.get(i).getIdTower()),
					new TextOptions(HorizontalAlign.CENTER), vbom);
			pictureTower[i].attachChild(towerText);
			gameHUD.registerTouchArea(pictureTower[i]);
			gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
			gameHUD.attachChild(pictureTower[i]);

		}

	}

	private void disposeTowersButtons() {
		if (pictureTower != null) {
			for (int i = 0; i < pictureTower.length; i++) {
				if (pictureTower[i] != null) {
					gameHUD.unregisterTouchArea(pictureTower[i]);
					pictureTower[i].detachSelf();
					if (!pictureTower[i].isDisposed())
						pictureTower[i].dispose();
					pictureTower[i] = null;
				}
			}
		}
	}

	private void disposeTowersButtonExcept(int j) {
		for (int i = 0; i < pictureTower.length; i++) {
			if (i == j) {
				continue;
			}
			gameHUD.unregisterTouchArea(pictureTower[i]);
			pictureTower[i].detachSelf();
			pictureTower[i].dispose();
			pictureTower[i] = null;
		}
	}

	private void createTabletButton(int widthButton, int heightButton) {
		pictureTablet = new Sprite(0, 0,
				resourcesManager.buttonOptionTablet_region, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionUp()) {
					if (displayList) {

						disposeTowersButtons();
						displayList = false;

						if (analogOnScreenControl != null)
							disposeAnalogOnScreenControl();

						connectToTablet();
					} else {

						createTowersButtons(GameActivity.getInstance()
								.getGame().getTowers(), 100, 100);
						displayList = true;

					}
				}
				return true;
			}
		};

		pictureTablet.setPosition(
				camera.getWidth() - (int) (widthButton * 1.5),
				camera.getHeight() - heightButton);
		pictureTablet.setHeight(heightButton);
		pictureTablet.setWidth((int) (widthButton * 1.5));

		gameHUD.registerTouchArea(pictureTablet);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(pictureTablet);
	}

	private void disposeTabletButton() {
		if (pictureTablet != null) {
			gameHUD.unregisterTouchArea(pictureTablet);
			pictureTablet.detachSelf();
			pictureTablet.dispose();
			pictureTablet = null;
		}
	}

	private void disposeAnalogOnScreenControl() {
		analogOnScreenControl.setVisible(false);
		analogOnScreenControl.clearTouchAreas();
		analogOnScreenControl.clearChildScene();
		analogOnScreenControl.clearEntityModifiers();
		analogOnScreenControl.detachSelf();
		analogOnScreenControl.dispose();
		analogOnScreenControl = null;
	}

	private void connectToTower(Tower _tower) {
		GameActivity.getInstance().setCurrentlyControlledTower(_tower);
	}

	private void connectToTablet() {
		GameActivity.getInstance().setCurrentlyControlledTower(null);
	}

	public class IconTower extends Sprite {

		private Tower _tower = null;

		public IconTower(int i, int j, ITextureRegion buttonOptionTower_region,
				VertexBufferObjectManager vbom, Tower tower) {
			super(i, j, buttonOptionTower_region, vbom);
			this._tower = tower;
		}

		@Override
		protected void preDraw(GLState pGLState, Camera pCamera) {
			super.preDraw(pGLState, pCamera);
			pGLState.enableDither();
		}

		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,

		final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

			if (pSceneTouchEvent.isActionUp()) {
				if (displayList) {
					this.setPosition(camera.getWidth() - this.getWidth(),
							camera.getHeight() - this.getHeight());

					disposeTowersButtonExcept(this.getIndex());

					disposeTabletButton();

					displayList = false;

					if (analogOnScreenControl == null) {
						createController();
					}
					connectToTower(_tower);
				} else {
					gameHUD.unregisterTouchArea(this);
					this.detachSelf();
					this.dispose();

					createTowersButtons(GameActivity.getInstance().getGame()
							.getTowers(), 100, 100);
					createTabletButton(100, 100);
					displayList = true;
				}
			}
			return true;
		}

	}

	@Override
	public void gameTick() {
		switchPlayPauseButton(!GameActivity.getInstance().getGame().isPaused());
		updateScore();
		updateLife();
	}
}
