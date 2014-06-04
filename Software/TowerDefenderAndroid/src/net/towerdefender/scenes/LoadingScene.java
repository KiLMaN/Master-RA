package net.towerdefender.scenes;

import net.towerdefender.manager.SceneManager.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

public class LoadingScene extends BaseScene {
	@Override
	public void createScene() {
		setBackground(new Background(Color.WHITE));
		Text LoadingText = new Text(0, 0, resourcesManager.Coolvetica, "Loading...",
				vbom);
		LoadingText.setPosition(camera.getCenterX()
				- (LoadingText.getWidth() / 2), camera.getCenterY()
				- (LoadingText.getHeight() / 2));
		attachChild(LoadingText);
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {

	}
}