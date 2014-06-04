package net.towerdefender.scenes;

import java.util.ArrayList;

import gameplay.Pweapon;
import gameplay.Tower;
import gameplay.Weapon;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.manager.SceneManager;
import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
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


public class ToolsScene extends BaseScene {

	private HUD gameHUD;
	private Sprite pictureBack;
	private Text scoreText;
	private ArrayList<Text> towerTexts;

	private Text nameWeaponText;
	private Text statusWeaponText;
	private Text rangeWeaponText;
	private Text damageWeaponText;

	private ArrayList<Sprite> listSpriteShown;
	private ArrayList<Tower> listConnected;
	private Rectangle pansTools;

	public ToolsScene() {
		super();
	}

	@Override
	public void createScene() {
		listSpriteShown = new ArrayList<Sprite>();
		towerTexts = new ArrayList<Text>();
		listConnected = GameActivity.getInstance().getGame().getListTowers();
		createBackground();
		createButtons();
		createTabs();
	}

	private void createTabs() {
		int i = 2;
		for (Tower tower : listConnected) {
			Sprite pictureTower = new SpriteTower(0, 0,
					resourcesManager.buttonOptionTower_region, vbom, tower);

			Text towerText = new Text(0, 0, resourcesManager.Coolvetica,
					" Tour: 0123456789",
					new TextOptions(HorizontalAlign.CENTER), vbom);
			towerText.setHeight(100);
			towerText.setWidth(250);
			towerText.setText(" Tour n°0");
			towerText.setText(" Tour n°" + (tower.getIdTower() + 1));
			
			pictureTower.setPosition(i * 300, 100);
			pictureTower.setHeight(80);
			pictureTower.setWidth(200);
			pictureTower.attachChild(towerText);
			gameHUD.registerTouchArea(pictureTower);
			gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
			gameHUD.attachChild(pictureTower);

			towerTexts.add(towerText);
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

		//pictureTower.detachSelf();
		//pictureTower.dispose();

		pictureBack.detachSelf();
		pictureBack.dispose();

		scoreText.detachSelf();
		scoreText.dispose();

		//towerText.detachSelf();
		//towerText.dispose();

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
		scoreText.setPosition(camera.getWidth() / 2, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);

		camera.setHUD(gameHUD);
	}

	private class SpriteTower extends Sprite {

		protected Tower currentTower;

		public SpriteTower(float pX, float pY, ITextureRegion pTextureRegion,
				VertexBufferObjectManager vbom, Tower tower) {
			super(pX, pY, pTextureRegion, vbom);
			currentTower = tower;
		}

		public Tower getTower() {
			return currentTower;
		}

		@Override
		protected void preDraw(GLState pGLState, Camera pCamera) {
			super.preDraw(pGLState, pCamera);
			pGLState.enableDither();
		}

		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
				final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				int j=1;
				if (pansTools != null) {
					pansTools.detachSelf();
					pansTools.dispose();
					nameWeaponText.detachSelf();
					nameWeaponText.dispose();
					statusWeaponText.detachSelf();
					statusWeaponText.dispose();
					rangeWeaponText.detachSelf();
					rangeWeaponText.dispose();
					damageWeaponText.detachSelf();
					damageWeaponText.dispose();
				}
				pansTools = new Rectangle(0, 220, camera.getWidth(),
						camera.getHeight() - 220, vbom);
				pansTools.setColor(1, 1, 1);
				gameHUD.attachChild(pansTools);

				ArrayList<Pweapon> weapons = currentTower.getWeapons();
				for (Pweapon weapon : weapons) {

					Weapon w = weapon.getWeapon();
					nameWeaponText = new Text(0, 0, resourcesManager.Sanford,
							"Nom: 0123456789", new TextOptions(HorizontalAlign.LEFT),
							vbom);
					nameWeaponText.setPosition(50, (50+(j*220)));
					nameWeaponText.setText("Nom: "+ w.getNameWeapon());
					gameHUD.attachChild(nameWeaponText);
					
					statusWeaponText = new Text(0, 0, resourcesManager.Sanford,
							"Nom: 0123456789", new TextOptions(HorizontalAlign.LEFT),
							vbom);
					statusWeaponText.setPosition((camera.getWidth()/3),(50+(j*220)));
							if( w.isLocked()){
								statusWeaponText.setText("bloqué");
							}else
							{
								statusWeaponText.setText("débloqué");
							}										
					gameHUD.attachChild(statusWeaponText);
					
					rangeWeaponText = new Text(0, 0, resourcesManager.Sanford,
							"Portee: 0123456789", new TextOptions(HorizontalAlign.LEFT),
							vbom);
					rangeWeaponText.setPosition((3*camera.getWidth()/5), (75+(j*220)));
					rangeWeaponText.setText("Portee: "+ w.getRange());
					gameHUD.attachChild(rangeWeaponText);
					
					damageWeaponText = new Text(0, 0, resourcesManager.Sanford,
							"Dommages: 0123456789", new TextOptions(HorizontalAlign.LEFT),
							vbom);
					damageWeaponText.setPosition((3*camera.getWidth()/5), (125+(j*220)));
					damageWeaponText.setText("Dommages: "+ w.getNumberDamage());
					gameHUD.attachChild(damageWeaponText);
					
					j++;
				}
			}
			return true;
		}

	}
}
