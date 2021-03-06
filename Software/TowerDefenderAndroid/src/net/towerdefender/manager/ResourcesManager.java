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
	public Font Sanford_White;

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
	private BuildableBitmapTextureAtlas towerTextureAtlas;
	public ITextureRegion buttonOptionTablet_region;
	public ITextureRegion buttonOptionTower_region;
	private BuildableBitmapTextureAtlas tabletTextureAtlas;
	private BuildableBitmapTextureAtlas transparentTextureAtlas;
	public ITextureRegion transparent_region;

	/* Tools */
	public ITextureRegion buttonOptionBack_region;
	private BuildableBitmapTextureAtlas buttonsOptionBackTextureAtlas;
	public ITextureRegion buttonPlus_region;
	public ITextureRegion buttonPlusGris_region;
	private BuildableBitmapTextureAtlas buttonsPlusTextureAtlas,
			buttonsPlusGrisTextureAtlas;
	public ITextureRegion buttonUnlock_region;
	private BuildableBitmapTextureAtlas buttonsUnlockTextureAtlas;
	public ITextureRegion buttonOptionTowerOpt_region;
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

		buttonsOptionBackTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 250, 308, TextureOptions.BILINEAR);
		buttonsPlusTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		buttonsPlusGrisTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		buttonsUnlockTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);

		buttonOptionBack_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(buttonsOptionBackTextureAtlas, activity,
						"back.png");

		buttonPlus_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(buttonsPlusTextureAtlas, activity, "plus.png");
		buttonPlusGris_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(buttonsPlusGrisTextureAtlas, activity,
						"plus_gris.png");

		buttonUnlock_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(buttonsUnlockTextureAtlas, activity,
						"unlock.png");

		try {
			this.buttonsOptionBackTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.buttonsOptionBackTextureAtlas.load();

			this.buttonsPlusTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.buttonsPlusTextureAtlas.load();

			this.buttonsPlusGrisTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.buttonsPlusGrisTextureAtlas.load();

			this.buttonsUnlockTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.buttonsUnlockTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD5", e.toString());
		}

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/tools/");

		stoneTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 768,
				TextureOptions.BILINEAR);

		buttonOptionTowerOpt_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(stoneTextureAtlas, activity,
						"texturePierre.jpg");

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
		.setAssetBasePath("gfx/TowerDefender/game/");

transparentTextureAtlas = new BuildableBitmapTextureAtlas(
		activity.getTextureManager(), 1, 1, TextureOptions.NEAREST);

transparent_region = BitmapTextureAtlasTextureRegionFactory
		.createFromAsset(transparentTextureAtlas, activity, "cache.png");

try {
	this.transparentTextureAtlas
			.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					0, 1, 0));
	this.transparentTextureAtlas.load();
} catch (final TextureAtlasBuilderException e) {
	Log.e("TD2", e.toString());
}



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
		// towerButtons Configuration
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/game/");

		towerTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 120, 120, TextureOptions.BILINEAR);

		buttonOptionTower_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(towerTextureAtlas, activity, "tower.png");

		// buttonOptionTower2_region = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(towerTextureAtlas, activity, "tour.png");
		//
		// buttonOptionTower3_region = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(towerTextureAtlas, activity, "tour.png");

		try {
			this.towerTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.towerTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD6 je crois", e.toString());
		}

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/TowerDefender/game/");

		tabletTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1196, 781,
				TextureOptions.BILINEAR);

		buttonOptionTablet_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(tabletTextureAtlas, activity, "tablet.png");

		try {
			this.tabletTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.tabletTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("TD7 je crois", e.toString());
		}

	}

	private void loadGameFonts() {

	}

	private void loadGameAudio() {

	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
		menuTextureAtlas=null;
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
				activity.getAssets(), "Sanford.ttf", 30, true, Color.BLACK, 2,
				Color.BLACK, false);
		Sanford.load();

		Sanford_White = FontFactory.createStrokeFromAsset(
				activity.getFontManager(), mainFontTextureSanford,
				activity.getAssets(), "Sanford.ttf", 40, true, Color.WHITE, 2,
				Color.WHITE, false);
		Sanford_White.load();

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