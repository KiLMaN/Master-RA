package net.towerdefender.scenes;

import net.towerdefender.manager.SceneManager;
import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

public class CreditScene extends BaseScene implements IOnMenuItemClickListener {

	private Text credits;
	private Sprite background;

	@Override
	public void createScene() {
		createBackground();

		credits = new Text(
				0,
				0,
				resourcesManager.Sanford_White,
				"Bienvenue sur le jeu Tower Defender \nCe jeu à été développé par les Master 1 ISIM SIC\ndans le cadre du projet de synthèse de la formation\nPour plus d'informations sur le développement \net l'application rendez-vous sur le site www.projetra.squadfree.net \n Bon jeu",
				new TextOptions(HorizontalAlign.CENTER), vbom);
		credits.setPosition(camera.getCenterX()
				- (credits.getWidthScaled() / 2), camera.getCenterY()
				- (credits.getHeightScaled() / 2) + 140);
		attachChild(credits);
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	private void createBackground() {
		background = new Sprite(0, 0, resourcesManager.menu_background_region,
				vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp())
					SceneManager.getInstance().loadMenuSceneFromCredit(engine);
				return true;
			}
		};

		background.setHeight(camera.getHeight());
		background.setWidth(camera.getWidth());
		this.registerTouchArea(background);
		attachChild(background);

	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		this.unregisterTouchArea(background);
		background.detachSelf();
		background.dispose();

		credits.detachSelf();
		credits.dispose();

		this.detachSelf();
		this.dispose();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}
}
