package net.towerdefender;

import net.towerdefender.activity.GameActivity;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 00:06:23 - 11.07.2010 BaseAugmentedRealityGameActivity
 */
public class TowerDefender extends GameActivity {
	/*
	 * // =========================================================== //
	 * Constants // ===========================================================
	 * 
	 * private static final int CAMERA_WIDTH = 1280; private static final int
	 * CAMERA_HEIGHT = 720;
	 * 
	 * // =========================================================== // Fields
	 * // ===========================================================
	 * 
	 * private Camera mCamera;
	 * 
	 * private BitmapTextureAtlas mBitmapTextureAtlas; private ITextureRegion
	 * mFaceTextureRegion;
	 * 
	 * private BitmapTextureAtlas mOnScreenControlTexture; private
	 * ITextureRegion mOnScreenControlBaseTextureRegion; private ITextureRegion
	 * mOnScreenControlKnobTextureRegion;
	 * 
	 * // =========================================================== //
	 * Constructors //
	 * ===========================================================
	 * 
	 * // =========================================================== // Getter
	 * & Setter // ===========================================================
	 * 
	 * // =========================================================== // Methods
	 * for/from SuperClass/Interfaces //
	 * ===========================================================
	 * 
	 * @Override public EngineOptions onCreateEngineOptions() {
	 * 
	 * this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT); HUD hud =
	 * new HUD(); mCamera.setHUD(hud);
	 * 
	 * EngineOptions engineOption = new EngineOptions(true,
	 * ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
	 * CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	 * 
	 * engineOption.setWakeLockOptions(WakeLockOptions.SCREEN_ON); return
	 * engineOption; }
	 * 
	 * @Override public void onCreateResources( OnCreateResourcesCallback
	 * pOnCreateResourcesCallback) {
	 * BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	 * 
	 * this.mBitmapTextureAtlas = new BitmapTextureAtlas(
	 * this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
	 * this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
	 * .createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
	 * this.mBitmapTextureAtlas.load();
	 * 
	 * this.mOnScreenControlTexture = new BitmapTextureAtlas(
	 * this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
	 * this.mOnScreenControlBaseTextureRegion =
	 * BitmapTextureAtlasTextureRegionFactory
	 * .createFromAsset(this.mOnScreenControlTexture, this,
	 * "onscreen_control_base.png", 0, 0);
	 * this.mOnScreenControlKnobTextureRegion =
	 * BitmapTextureAtlasTextureRegionFactory
	 * .createFromAsset(this.mOnScreenControlTexture, this,
	 * "onscreen_control_knob.png", 128, 0);
	 * this.mOnScreenControlTexture.load();
	 * 
	 * pOnCreateResourcesCallback.onCreateResourcesFinished();
	 * 
	 * }
	 * 
	 * @Override public void onCreateScene(OnCreateSceneCallback
	 * pOnCreateSceneCallback) { this.mEngine.registerUpdateHandler(new
	 * FPSLogger());
	 * 
	 * final Scene scene = new Scene(); // scene.setBackgroundEnabled(false); //
	 * scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f)); // final
	 * float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion // .getWidth()) /
	 * 2; // final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion //
	 * .getHeight()) / 2; // final Sprite face = new Sprite(centerX, centerY, //
	 * this.mFaceTextureRegion, this.getVertexBufferObjectManager()); // final
	 * PhysicsHandler physicsHandler = new PhysicsHandler(face); //
	 * face.registerUpdateHandler(physicsHandler);
	 * 
	 * // scene.attachChild(face);
	 * 
	 * // scene.setChildScene(analogOnScreenControl);
	 * pOnCreateSceneCallback.onCreateSceneFinished(scene); // return scene;
	 * 
	 * }
	 * 
	 * // =========================================================== // Methods
	 * // ===========================================================
	 * 
	 * @Override public void onPopulateScene(Scene pScene,
	 * OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
	 * 
	 * final AnalogOnScreenControl analogOnScreenControl = new
	 * AnalogOnScreenControl( 0, CAMERA_HEIGHT -
	 * this.mOnScreenControlBaseTextureRegion.getHeight() - 1, this.mCamera,
	 * this.mOnScreenControlBaseTextureRegion,
	 * this.mOnScreenControlKnobTextureRegion, 0.1f, 200,
	 * this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener()
	 * {
	 * 
	 * @Override public void onControlChange( final BaseOnScreenControl
	 * pBaseOnScreenControl, final float pValueX, final float pValueY) { //
	 * physicsHandler // .setVelocity(pValueX * 100, pValueY * 100); // scene. }
	 * 
	 * @Override public void onControlClick( final AnalogOnScreenControl
	 * pAnalogOnScreenControl) {
	 * 
	 * face.registerEntityModifier(new SequenceEntityModifier( new
	 * ScaleModifier(0.25f, 1, 1.5f), new ScaleModifier(0.25f, 1.5f, 1)));
	 * 
	 * } }); analogOnScreenControl.getControlBase().setBlendFunction(
	 * GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	 * analogOnScreenControl.getControlBase().setAlpha(0.5f);
	 * analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
	 * analogOnScreenControl.getControlBase().setScale(1.0f);
	 * analogOnScreenControl.getControlKnob().setScale(1.0f);
	 * analogOnScreenControl.refreshControlKnobPosition();
	 * pScene.setChildScene(analogOnScreenControl);
	 * 
	 * pOnPopulateSceneCallback.onPopulateSceneFinished();
	 * 
	 * }
	 * 
	 * // =========================================================== // Inner
	 * and Anonymous Classes //
	 * ===========================================================
	 */

}
