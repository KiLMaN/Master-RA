package net.towerdefender.activity;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.image.ARObject;
import net.towerdefender.image.MarkerVisibilityListener;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.primitives.Cube;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.util.Log;

public class ARRajawaliRender extends RajawaliRenderer implements
		MarkerVisibilityListener {

	// private Object3D monkey;
	private Cube[] cube = new Cube[4];
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

		Material material = new Material();
		material.enableLighting(false);
		Material material1 = new Material();
		material.enableLighting(false);
		Material material2 = new Material();
		material.enableLighting(false);
		Material material3 = new Material();
		material.enableLighting(false);

		cube[0] = new Cube(0.1f);
		cube[0].setPosition(2, 5, 0);
		cube[0].setMaterial(material);
		cube[0].setColor(0xFF0000);

		cube[1] = new Cube(0.1f);
		cube[1].setPosition(2, 6, 0);
		cube[1].setMaterial(material1);
		cube[1].setColor(0x00FF00);

		cube[2] = new Cube(0.1f);
		cube[2].setPosition(3, 5, 0);
		cube[2].setMaterial(material2);
		cube[2].setColor(0x0000FF);

		cube[3] = new Cube(0.1f);
		cube[3].setPosition(3, 6, 0);
		cube[3].setMaterial(material3);
		cube[3].setColor(0xFFFF00);

		/*
		 * try { ObjectInputStream ois = new ObjectInputStream(mContext
		 * .getResources().openRawResource(R.raw.monkey_ser));
		 * SerializedObject3D serializedMonkey = (SerializedObject3D) ois
		 * .readObject(); ois.close();
		 * 
		 * monkey = new Object3D(serializedMonkey); Material material = new
		 * Material(); material.enableLighting(true);
		 * material.setDiffuseMethod(new DiffuseMethod.Lambert());
		 * monkey.setMaterial(material); monkey.setColor(0xffff8C00);
		 * monkey.setScale(1);
		 * 
		 * getCurrentScene().addChild(monkey);
		 * 
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */

		/*
		 * Stack<Vector3> points = new Stack<Vector3>(); points.add(new
		 * Vector3(2, 0, 0)); points.add(posA); points.add(new Vector3(0, 2,
		 * 0)); points.add(new Vector3(0, 0, 0)); points.add(new Vector3(2, 0,
		 * 0));
		 * 
		 * line = new Line3D(points, 1, 0xffffff00);
		 * 
		 * Material material = new Material(); material.setColor(0xffffff00);
		 * line.setMaterial(material); getCurrentScene().addChild(line);
		 */
		getCurrentScene().addChild(cube[0]);
		getCurrentScene().addChild(cube[1]);
		getCurrentScene().addChild(cube[2]);
		getCurrentScene().addChild(cube[3]);
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

	public void changePositionTest(float x, float y, float z, Cube cube) {
		float wCenter = /* this.mViewportWidth */720 / 2;
		float hCenter = /* this.mViewportWidth */480 / 2;
		float maxW = 10;
		float maxH = 6;

		float newx = ((x - wCenter) / wCenter) * maxW;
		float newy = ((y - hCenter) / hCenter) * maxH;
		cube.setPosition(newx, -newy, 0);
		// line.reload();
		// Log.w("Drawing", "Reload");
		// monkey.setPosition(newx, -newy, z);
	}

	@Override
	public void makerVisibilityChanged(boolean visible) {
		bMarkerVisible = visible;
		if (bMarkerVisible) {
			Log.w("Drawing", "Start Render");
			cube[0].setVisible(true);
			cube[1].setVisible(true);
			cube[2].setVisible(true);
			cube[3].setVisible(true);
			startRendering();

		} else {
			Log.w("Drawing", "Stop Render");
			cube[0].setVisible(false);
			cube[1].setVisible(false);
			cube[2].setVisible(false);
			cube[3].setVisible(false);
			stopRendering();
		}
	}

	@Override
	protected void onRender(final double deltaTime) {
		Vector<ARObject> objects = GameActivity.getInstance().markerInfo
				.getObjects();
		for (ARObject object : objects) {
			if (object.isVisible()) {
				double[] Xs = object.getXs();
				double[] Ys = object.getYs();
				changePositionTest((float) Xs[0], (float) Ys[0], 0, cube[0]);
				changePositionTest((float) Xs[1], (float) Ys[1], 0, cube[1]);
				changePositionTest((float) Xs[2], (float) Ys[2], 0, cube[2]);
				changePositionTest((float) Xs[3], (float) Ys[3], 0, cube[3]);
			}
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
}
