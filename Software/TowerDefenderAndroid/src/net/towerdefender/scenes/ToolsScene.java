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
	private Sprite pictureTower;
	private Text scoreText;
	private Text towerText;
	private int numberTower=1;
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
		int i=2;
		for(Tower tower : listConnected){
			pictureTower = new Sprite(0, 0,
					resourcesManager.buttonOptionTower_region, vbom) {
				@Override
				protected void preDraw(GLState pGLState, Camera pCamera) {
					super.preDraw(pGLState, pCamera);
					pGLState.enableDither();
				}

				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					//if(pSceneTouchEvent.isActionUp())
						// SceneManager.getInstance().loadToolsScene(engine);
					return true;
				}

			};
			
			towerText = new Text(0, 0, resourcesManager.Coolvetica,
					" Tour: 0123456789", new TextOptions(HorizontalAlign.CENTER),
					vbom);
			towerText.setHeight(100);
			towerText.setWidth(250);
			towerText.setText(" Tour n°0");
			numberTower ++;
			towerText.setText(" Tour n°" + (tower.getIdTower()+1));


			pictureTower.setPosition(i*300, 200);
			pictureTower.setHeight(80);
			pictureTower.setWidth(200);
			pictureTower.attachChild(towerText);
			gameHUD.registerTouchArea(pictureTower);
			gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
			gameHUD.attachChild(pictureTower);
			
			listSpriteShown.add(pictureTower);
			i++;
		}
		
		
		
		
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadGameScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		
		pictureTower.detachSelf();
		pictureTower.dispose();
		
		pictureBack.detachSelf();
		pictureBack.dispose();

		scoreText.detachSelf();
		scoreText.dispose();

		towerText.detachSelf();
		towerText.dispose();

		gameHUD.detachSelf();
		gameHUD.dispose();

		this.detachSelf();
		this.dispose();
	}

	private void createBackground() {
		Background background = new Background(Color.TRANSPARENT);
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

}
