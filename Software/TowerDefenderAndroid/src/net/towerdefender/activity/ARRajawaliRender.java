package net.towerdefender.activity;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.image.ARObject;
import net.towerdefender.image.MarkerDetectedListener;
import rajawali.lights.DirectionalLight;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;

public class ARRajawaliRender extends RajawaliRenderer implements
		MarkerDetectedListener {

	// private Object3D monkey;
	// private Cube[] cube = new Cube[4];
	private boolean bMarkerVisible = false;

	public ARRajawaliRender(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initScene() {
		super.initScene();

		DirectionalLight light = new DirectionalLight(0, 0, -1);
		light.setPower(1);

		getCurrentScene().addLight(light);
		getCurrentCamera().setPosition(0, 0, 16);
		// -- set the background color to be transparent
		// you need to have called setGLBackgroundTransparent(true); in
		// the activity
		// for this to work.
		getCurrentScene().setBackgroundColor(0);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		super.onSurfaceChanged(gl, width, height);
		GameActivity.getInstance().markerInfo.setScreenSize(width, height);
	}

	@Override
	protected void onRender(final double deltaTime) {
		Vector<ARObject> objects = GameActivity.getInstance().markerInfo
				.getObjects();
		for (ARObject object : objects) {
			if (object.isVisible())
				object.render(this);
			else if (!object.isHidden())
				object.hide();
		}
		render(deltaTime);
	}

	/*
	 * void lineSeg(double x1, double y1, double x2, double y2,
	 * ARGL_CONTEXT_SETTINGS_REF contextSettings, ARParam cparam, double zoom) {
	 * int enable; float ox, oy; double xx1, yy1, xx2, yy2;
	 * 
	 * if (!contextSettings) return;
	 * arglDistortionCompensationGet(contextSettings, &enable); if
	 * (arglDrawModeGet(contextSettings) == AR_DRAW_BY_TEXTURE_MAPPING &&
	 * enable) { xx1 = x1; yy1 = y1; xx2 = x2; yy2 = y2; } else {
	 * arParamIdeal2Observ(cparam.dist_factor, x1, y1, &xx1, &yy1);
	 * arParamIdeal2Observ(cparam.dist_factor, x2, y2, &xx2, &yy2); }
	 * 
	 * xx1 *= zoom; yy1 *= zoom; xx2 *= zoom; yy2 *= zoom;
	 * 
	 * ox = 0; oy = cparam.ysize - 1; glBegin(GL_LINES); glVertex2f(ox + xx1, oy
	 * - yy1); glVertex2f(ox + xx2, oy - yy2); glEnd(); glFlush(); }
	 */

	@Override
	public void makerDetected(boolean visible) {
		bMarkerVisible = visible;
		if (bMarkerVisible) {

			startRendering();

		} else {

			stopRendering();
		}

	}

	@Override
	public void makerUpdated() {
		// TODO Auto-generated method stub

	}
}
