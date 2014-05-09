package net.towerdefender.manager;

import net.towerdefender.activity.GameActivity;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;
import android.util.Log;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Font Coolvetica;

	public Engine engine;
	public GameActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	/* Splash */
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	/* Menu */
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	private BuildableBitmapTextureAtlas menuTextureAtlas;

	/* Game */
	public ITextureRegion onScreenControlBase;
	public ITextureRegion onScreenControlKnob;
	private BuildableBitmapTextureAtlas onScreenControlTextureAtlas;
	public ITextureRegion buttonOptionSettings_region;
	private BuildableBitmapTextureAtlas gameTextureAtlas;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/menu/");

		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity, "options.png");

		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD", e.toString());
		}
	}

	private void loadMenuAudio() {

	}

	private void loadGameGraphics() {

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/");

		onScreenControlTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);

		onScreenControlBase = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(onScreenControlTextureAtlas, activity,
						"onscreen_control_base.png");
		onScreenControlKnob = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(onScreenControlTextureAtlas, activity,
						"onscreen_control_knob.png");

		try {
			this.onScreenControlTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.onScreenControlTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD", e.toString());
		}

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/game/");

		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);

		buttonOptionSettings_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "settings.png");

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD2", e.toString());
		}

	}

	private void loadGameFonts() {

	}

	private void loadGameAudio() {

	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		Coolvetica = FontFactory.createStrokeFromAsset(
				activity.getFontManager(), mainFontTexture,
				activity.getAssets(), "coolvetica.ttf", 50, true, Color.WHITE,
				2, Color.WHITE, true);
		Coolvetica.load();
	}

	public void unloadGameTextures() {
		// TODO (Since we did not create any textures for game scene yet)
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, GameActivity activity,
			Camera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
}