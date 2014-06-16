package net.towerdefender.scenes;

import gameplay.Pweapon;
import gameplay.Tower;
import gameplay.UpgradeType;
import gameplay.Weapon;

import java.util.ArrayList;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.manager.SceneManager;
import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
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

	// private ArrayList<Text> towerTexts;

	// private ArrayList<Text> listStatusWeaponText;
	// private ArrayList<Text> listRangeWeaponText;
	private ArrayList<ButtonTabTower> listButtonsTabs;
	private ArrayList<ContentTabTower> listContentTabs;

	private ArrayList<Tower> listConnected;

	private Rectangle pansTools;

	public ToolsScene() {
		super();
	}

	@Override
	public void createScene() {
		listConnected = GameActivity.getInstance().getGame().getListTowers();
		listButtonsTabs = new ArrayList<ButtonTabTower>();
		listContentTabs = new ArrayList<ContentTabTower>();
		// towerTexts = new ArrayList<Text>();

		createBackground();
		createButtons();
		createTabs();
		updateTabs();
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadGameScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_OPTION;
	}

	@Override
	public void disposeScene() {
	}

	public void updateTabs() {
		scoreText.setText("Score: "
				+ GameActivity.getInstance().getGame().getPoints());

		for (ContentTabTower t : listContentTabs) {
			t.updateContent();

		}
	}

	private void createBackground() {
		Background background = new Background(Color.BLACK);
		setBackground(background);
	}

	public void showTab(ContentTabTower tab) {
		for (ContentTabTower t : listContentTabs) {
			if (tab == t)
				t.show();
			else
				t.hide();
		}
	}

	private Rectangle _fond = null;

	private void createTabs() {
		int i = 0;

		_fond = new Rectangle(0, 220, camera.getWidth(),
				camera.getHeight() - 220, vbom);
		_fond.setColor(1, 1, 1);
		gameHUD.attachChild(_fond);

		for (Tower tower : listConnected) {

			ContentTabTower contentTab = new ContentTabTower(tower);
			ButtonTabTower buttonTab = new ButtonTabTower(0, 0,
					resourcesManager.buttonOptionTowerOpt_region, vbom, tower,
					contentTab);

			contentTab.hide();

			buttonTab.setPosition(i * 300, 100);
			buttonTab.setHeight(80);
			buttonTab.setWidth(200);
			// buttonTab.attachChild(towerText);
			if (!gameHUD.getTouchAreas().contains(buttonTab))
				gameHUD.registerTouchArea(buttonTab);
			gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
			gameHUD.attachChild(buttonTab);

			_fond.attachChild(contentTab);

			listButtonsTabs.add(buttonTab);
			listContentTabs.add(contentTab);
			// towerTexts.add(towerText);
			// listSpriteShown.add(pictureTower);
			i++;
		}

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
		if (!gameHUD.getTouchAreas().contains(pictureBack))
			gameHUD.registerTouchArea(pictureBack);
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.attachChild(pictureBack);

		// CREATE SCORE TEXT
		scoreText = new Text(0, 0, resourcesManager.Coolvetica,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setPosition(camera.getWidth() / 2, 0);

		gameHUD.attachChild(scoreText);

		camera.setHUD(gameHUD);
	}

	public class ButtonTabTower extends Sprite {

		private ContentTabTower _tabContent;
		private Tower _tower;
		private Text towerText;

		public ButtonTabTower(float pX, float pY,
				ITextureRegion pTextureRegion, VertexBufferObjectManager vbom,
				Tower tower, ContentTabTower tabContent) {
			super(pX, pY, pTextureRegion, vbom);
			_tabContent = tabContent;
			_tower = tower;

			towerText = new Text(0, 0, resourcesManager.Coolvetica,
					" Tour n° 99", new TextOptions(HorizontalAlign.CENTER),
					vbom);
			towerText.setHeight(100);
			towerText.setWidth(250);
			towerText.setText(" Tour n° " + (_tower.getIdTower()));
			this.attachChild(towerText);

		}

		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
				final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				showTab(_tabContent); // Will show the tab and hide the others
			}
			return true;
		}

		@Override
		public boolean detachSelf() {
			if (towerText != null) {
				towerText.detachSelf();
				if (!towerText.isDisposed())
					towerText.dispose();
				towerText = null;
			}
			return super.detachSelf();
		}
	}

	public class ContentTabTower extends Entity {

		private Tower _tower;

		private Text idText = null;
		private ArrayList<WeaponEntity> listWeapons = null;

		public ContentTabTower(Tower tower) {
			this._tower = tower;

			listWeapons = new ArrayList<WeaponEntity>();

			/* Numéro de la tour */
			idText = new Text(0, 0, resourcesManager.Sanford, "Tour N° 99",
					new TextOptions(HorizontalAlign.LEFT), vbom);

			idText.setPosition(10, 0);

			this.attachChild(idText);
			ArrayList<Pweapon> weapons = _tower.getWeapons();

			int _spaceV = 120;
			int j = 0;
			for (Pweapon weapon : weapons) {

				WeaponEntity weaponEntity = new WeaponEntity(_tower, weapon,
						this);
				weaponEntity.setPosition(0, 60 + (j * _spaceV));
				listWeapons.add(weaponEntity);
				this.attachChild(weaponEntity);
				j++;
			}
		}

		public void hide() {
			idText.setVisible(false);
			for (WeaponEntity entity : listWeapons) {
				entity.setVisible(false);
			}
		}

		public void show() {
			idText.setVisible(true);
			for (WeaponEntity entity : listWeapons) {
				entity.setVisible(true);
			}
		}

		public void updateContent() {
			idText.setText("Tour N° " + _tower.getIdTower());
			for (WeaponEntity entity : listWeapons) {
				entity.update();
			}
		}

	}

	/* Cette classe représente chacune des lignes des armes d'une tour */
	private class WeaponEntity extends Entity {
		private Pweapon _weapon = null;
		private Tower _tower;
		private Text nameText = null;
		private Text statusText = null;
		private Text damageText = null;
		private Text rangeText = null;
		private Text unlockCostText = null;

		private ContentTabTower tab;
		private Sprite unlockSprite = null;
		SpriteUpgrade upgradePower = null;
		SpriteUpgrade upgradeRange = null;

		public WeaponEntity(Tower tower, Pweapon pweapon, ContentTabTower tab) {
			this._weapon = pweapon;
			this._tower = tower;
			this.tab = tab;
			buildEntity();
			update();
		}

		public void update() {

			damageText.setText("Dommages : " + _weapon.getDamage() + " / "
					+ _weapon.getWeapon().getMaxDamage());

			rangeText.setText("Portee : " + (int) _weapon.getRange() + " / "
					+ (int) _weapon.getWeapon().getMaxRange());

			if (_weapon.isLocked()) {
				statusText.setText("Bloquée");
				unlockCostText.setText(_weapon.getWeapon().getCostWeapon()
						+ " pts");

				unlockSprite.setVisible(true);
				unlockCostText.setVisible(true);

				if (!gameHUD.getTouchAreas().contains(unlockSprite))
					gameHUD.registerTouchArea(unlockSprite);

			} else {
				statusText.setText("Débloquée");
				gameHUD.unregisterTouchArea(unlockSprite);
				unlockSprite.setVisible(false);
				unlockCostText.setVisible(false);
			}

			if (upgradePower != null)
				upgradePower.update();

			if (upgradeRange != null)
				upgradeRange.update();

		}

		@Override
		public void setVisible(boolean pVisible) {
			// Log.w("debug", "SetVisible WeaponEntity : " + pVisible);
			super.setVisible(pVisible);
			upgradeRange.setVisible(pVisible);
			upgradePower.setVisible(pVisible);
			gameHUD.unregisterTouchArea(unlockSprite);
			if (pVisible) {
				if (unlockSprite != null)
					if (!gameHUD.getTouchAreas().contains(unlockSprite))
						gameHUD.registerTouchArea(unlockSprite);
			}

		}

		@Override
		public boolean detachSelf() {
			if (nameText != null) {
				nameText.detachSelf();
				nameText.dispose();
				nameText = null;
			}
			if (statusText != null) {
				statusText.detachSelf();
				statusText.dispose();
				statusText = null;

			}

			if (damageText != null) {
				damageText.detachSelf();
				damageText.dispose();
				damageText = null;
			}
			if (rangeText != null) {
				rangeText.detachSelf();
				rangeText.dispose();
				rangeText = null;
			}

			if (unlockSprite != null) {
				gameHUD.unregisterTouchArea(unlockSprite);
				unlockSprite.detachSelf();
				unlockSprite.dispose();
				unlockSprite = null;
			}
			if (unlockCostText != null) {
				unlockCostText.detachSelf();
				unlockCostText.dispose();
				unlockCostText = null;
			}

			return super.detachSelf();

		}

		private void buildEntity() {

			Weapon w = _weapon.getWeapon();

			/* Nom de l'arme */
			nameText = new Text(0, 0, resourcesManager.Sanford,
					"Nom: 0123456789012345", new TextOptions(
							HorizontalAlign.LEFT), vbom);

			int stringSize = (w.getNameWeapon().length() < 16) ? w
					.getNameWeapon().length() : 10;

			nameText.setText("Nom: "
					+ w.getNameWeapon().substring(0, stringSize));

			nameText.setPosition(20, 0);
			this.attachChild(nameText);

			/* Status de l'arme */
			statusText = new Text(0, 0, resourcesManager.Sanford, "statutArme",
					new TextOptions(HorizontalAlign.LEFT), vbom);

			statusText.setPosition(3 * (camera.getWidth() / 10), 0);
			this.attachChild(statusText);

			/* Portée */
			rangeText = new Text(0, 0, resourcesManager.Sanford,
					"Portee: 012345678900", new TextOptions(
							HorizontalAlign.LEFT), vbom);
			rangeText.setPosition(camera.getWidth() / 2, 0);
			this.attachChild(rangeText);

			upgradeRange = new SpriteUpgrade(_tower, _weapon,
					UpgradeType.UPGRADE_RANGE, tab);
			upgradeRange.setPosition((4 * camera.getWidth() / 5), 0);
			this.attachChild(upgradeRange);

			/* Dégats */
			damageText = new Text(0, 0, resourcesManager.Sanford,
					"Dommages: 012345678900", new TextOptions(
							HorizontalAlign.LEFT), vbom);
			damageText.setPosition(camera.getWidth() / 2, 40);
			// damageText.setText("Dommages: " + w.getNumberDamage());
			this.attachChild(damageText);

			upgradePower = new SpriteUpgrade(_tower, _weapon,
					UpgradeType.UPGRADE_POWER, tab);
			upgradePower.setPosition((4 * camera.getWidth() / 5), 40);
			this.attachChild(upgradePower);

			unlockSprite = new Sprite(0, 0,
					resourcesManager.buttonUnlock_region, vbom) {
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX,
						final float pTouchAreaLocalY) {

					if (pSceneTouchEvent.isActionUp()) {
						_tower.unlockWeapon(_weapon.getWeapon());
						updateTabs();
						showTab(tab);

					}
					return true;
				}
			};
			unlockSprite.setPosition(3 * (camera.getWidth() / 10), 40);
			unlockSprite.setHeight(35);
			unlockSprite.setWidth(35);
			unlockSprite.setVisible(false);
			this.attachChild(unlockSprite);

			unlockCostText = new Text(0, 0, resourcesManager.Sanford,
					"9999 pts", new TextOptions(HorizontalAlign.LEFT), vbom);
			unlockCostText.setPosition((3 * (camera.getWidth() / 10)) + 55, 40);
			this.attachChild(unlockCostText);
		}
	}

	private class SpriteUpgrade extends Entity {

		private UpgradeType _typeUpgrade;
		private Pweapon _weapon;
		private Tower _tower;

		private Sprite plusButton = null;
		private Text costUpgrade = null;
		private ContentTabTower _tab = null;

		public SpriteUpgrade(Tower tower, Pweapon weapon,
				final UpgradeType typeUpgrade, ContentTabTower tab) {
			this._weapon = weapon;
			this._typeUpgrade = typeUpgrade;
			this._tower = tower;
			this._tab = tab;

			costUpgrade = new Text(0, 0, resourcesManager.Sanford, "9999 pts",
					new TextOptions(HorizontalAlign.LEFT), vbom);
			costUpgrade.setPosition(55, 0);
			this.attachChild(costUpgrade);

			plusButton = new Sprite(0, 0, resourcesManager.buttonPlus_region,
					vbom) {
				@Override
				protected void preDraw(GLState pGLState, Camera pCamera) {
					super.preDraw(pGLState, pCamera);
					pGLState.enableDither();
				}

				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX,
						final float pTouchAreaLocalY) {

					if (pSceneTouchEvent.isActionUp()) {
						_tower.upgradeWeapon(_weapon.getWeapon(), _typeUpgrade);
						updateTabs();
						showTab(_tab);
					}
					return true;
				}
			};

			plusButton.setPosition(0, 0);
			plusButton.setHeight(35);
			plusButton.setWidth(35);
			this.attachChild(plusButton);

		}

		@Override
		public void setVisible(boolean pVisible) {
			// Log.w("debug", "SetVisible SpriteUpgrade : " + pVisible);

			gameHUD.unregisterTouchArea(plusButton);
			super.setVisible(pVisible);
			if (pVisible && !_weapon.isLocked()
					&& _weapon.canUpgrade(_typeUpgrade)) {
				if (!gameHUD.getTouchAreas().contains(plusButton))
					gameHUD.registerTouchArea(plusButton);
			}

		}

		@Override
		public boolean detachSelf() {

			if (costUpgrade != null) {
				costUpgrade.detachSelf();
				costUpgrade.dispose();

			}
			if (plusButton != null) {
				gameHUD.unregisterTouchArea(plusButton);
				plusButton.detachSelf();
				plusButton.dispose();

			}

			return super.detachSelf();
		}

		public void update() {
			/*
			 * if (plusButton != null) {
			 * gameHUD.unregisterTouchArea(plusButton); plusButton.detachSelf();
			 * if (!plusButton.isDisposed()) plusButton.dispose(); plusButton =
			 * null; }
			 */
			gameHUD.unregisterTouchArea(plusButton);
			if (!_weapon.isLocked() && _weapon.canUpgrade(_typeUpgrade)) {

				costUpgrade.setText(_weapon.getCostUpgrade(_typeUpgrade)
						+ " pts");

				plusButton.setTextureRegion(resourcesManager.buttonPlus_region);

				if (!gameHUD.getTouchAreas().contains(plusButton))
					gameHUD.registerTouchArea(plusButton);

			} else {
				costUpgrade.setText("");
				plusButton
						.setTextureRegion(resourcesManager.buttonPlusGris_region);

			}
		}
	}

}
