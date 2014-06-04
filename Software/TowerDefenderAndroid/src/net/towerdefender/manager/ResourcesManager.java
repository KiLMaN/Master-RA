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
	public Font Sanford;

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
	public ITextureRegion buttonOptionPlay_region;
	private BuildableBitmapTextureAtlas playTextureAtlas;
	public ITextureRegion buttonOptionPause_region;
	private BuildableBitmapTextureAtlas pauseTextureAtlas;

	
	/* Tools*/
	public ITextureRegion buttonOptionBack_region;
	private BuildableBitmapTextureAtlas backTextureAtlas;
	public ITextureRegion buttonOptionTower_region;
	private BuildableBitmapTextureAtlas stoneTextureAtlas;
	
	
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

	public void loadToolsResources() {
		loadToolsGraphics();
		loadToolsFonts();
		loadToolsAudio();
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

	private void loadToolsGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/tools/");

		backTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 250, 308, TextureOptions.BILINEAR);

		buttonOptionBack_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backTextureAtlas, activity, "back.png");

		try {
			this.backTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.backTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD5", e.toString());
		}

		
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/tools/");
		
		stoneTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 768, TextureOptions.BILINEAR);

		buttonOptionTower_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(stoneTextureAtlas, activity, "texturePierre.jpg");

		try {
			this.stoneTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.stoneTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD6", e.toString());
		}
		

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
				activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);

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

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/game/");

		playTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);

		buttonOptionPlay_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(playTextureAtlas, activity, "play.png");

		try {
			this.playTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.playTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD3", e.toString());
		}

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/game/");

		pauseTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 800, 800, TextureOptions.BILINEAR);

		buttonOptionPause_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pauseTextureAtlas, activity, "pause.png");

		try {
			this.pauseTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.pauseTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD4", e.toString());
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
				activity.getAssets(), "coolvetica.ttf", 50, true, Color.BLACK,
				2, Color.WHITE, true);
		Coolvetica.load();
		
		final ITexture mainFontTextureSanford = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		Sanford = FontFactory.createStrokeFromAsset(
				
				activity.getFontManager(), mainFontTextureSanford,
				activity.getAssets(), "Sanford.ttf", 50, true, Color.BLACK,
				2, Color.BLACK, false);
		Sanford.load();
	}

	public void unloadGameTextures() {
		// TODO (Since we did not create any textures for game scene yet)
	}

	private void loadToolsFonts() {

	}

	private void loadToolsAudio() {

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