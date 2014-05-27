package net.towerdefender.scenes;

import java.util.ArrayList;

import gameplay.Position;
import gameplay.Tower;

import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.manager.ResourcesManager;
import net.towerdefender.manager.SceneManager;
import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

public class ToolsScene extends BaseScene{

	private HUD gameHUD;
	private Sprite pictureBack;
	private Sprite pictureSettings;
	private Text scoreText;
	private int score = 0;
	private ArrayList <Sprite> listSpriteShown;
	private ArrayList <Tower> listConnected;
	
	public ToolsScene() {
		super();

		
	}

	@Override
	public void createScene() {
		listSpriteShown= new ArrayList<Sprite>();
		listConnected = new ArrayList<Tower>();
		listConnected.add(new Tower(0, new Position(0,0)));
		listConnected.add(new Tower(1, new Position(1,0)));
		listConnected.add(new Tower(2, new Position(2,0)));
		createBackground();
		createButtons();
		createTabs();
	}

	private void createTabs() {
		int i=3;
		for(Tower tower : listConnected){
			pictureSettings = new Sprite(0, 0,
					resourcesManager.buttonOptionSettings_region, vbom) {
				@Override
				protected void preDraw(GLState pGLState, Camera pCamera) {
					super.preDraw(pGLState, pCamera);
					pGLState.enableDither();
				}

				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionUp())
						SceneManager.getInstance().loadToolsScene(engine);
					return true;
				}

			};
			pictureSettings.setPosition(i*300, 200);
			pictureSettings.setHeight(100);
			pictureSettings.setWidth(100);
			gameHUD.registerTouchArea(pictureSettings);
			gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
			gameHUD.attachChild(pictureSettings);
			i++;
		}
		
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
		
		pictureBack.detachSelf();
		pictureBack.dispose();

		scoreText.detachSelf();
		scoreText.dispose();


		gameHUD.detachSelf();
		gameHUD.dispose();

		this.detachSelf();
		this.dispose();
	}

	private void createBackground() {
		Background background = new Background(Color.WHITE);
		setBackground(background);
	}

	private void createButtons() {
		gameHUD = new HUD();

		// CREATE SPRITE SETTINGS
		pictureBack = new Sprite(0, 0,
				resourcesManager.buttonOptionBack_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			}

		};
		
		pictureBack.setHeight(100);
		pictureBack.setWidth(100);
		gameHUD.registerTouchArea(pictureBack);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(pictureBack);

		// CREATE SCORE TEXT
		scoreText = new Text(0, 0, resourcesManager.Coolvetica,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setPosition(camera.getWidth()/2, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		
		camera.setHUD(gameHUD);
	}

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}


}
