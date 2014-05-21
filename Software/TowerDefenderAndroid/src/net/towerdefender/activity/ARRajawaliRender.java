package net.towerdefender.activity;

import java.io.ObjectInputStream;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.R;
import net.towerdefender.image.ARObject;
import net.towerdefender.image.MarkerVisibilityListener;
import rajawali.Object3D;
import rajawali.SerializedObject3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.util.Log;

public class ARRajawaliRender extends RajawaliRenderer implements
		MarkerVisibilityListener {

	private Object3D monkey;

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

		try {
			ObjectInputStream ois = new ObjectInputStream(mContext
					.getResources().openRawResource(R.raw.monkey_ser));
			SerializedObject3D serializedMonkey = (SerializedObject3D) ois
					.readObject();
			ois.close();

			monkey = new Object3D(serializedMonkey);
			Material material = new Material();
			material.enableLighting(true);
			material.setDiffuseMethod(new DiffuseMethod.Lambert());
			monkey.setMaterial(material);
			monkey.setColor(0xffff8C00);
			monkey.setScale(1);

			getCurrentScene().addChild(monkey);

			/*
			 * RotateOnAxisAnimation anim = new RotateOnAxisAnimation(Axis.Y,
			 * 360); anim.setDurationMilliseconds(6000);
			 * anim.setRepeatMode(RepeatMode.INFINITE); anim.setInterpolator(new
			 * LinearInterpolator()); anim.setTransformable3D(monkey);
			 * getCurrentScene().registerAnimation(anim); anim.play();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public void changePositionTest(float x, float y, float z) {
		float wCenter = this.mViewportWidth / 2;
		float hCenter = this.mViewportWidth / 2;
		float maxW = 10;
		float maxH = 6;

		float newx = ((x - wCenter) / wCenter) * maxW;
		float newy = ((y - hCenter) / hCenter) * maxH;
		monkey.setPosition(newx, -newy, z);
	}

	@Override
	public void makerVisibilityChanged(boolean visible) {
		bMarkerVisible = visible;
		if (bMarkerVisible) {
			Log.w("Drawing", "Start Render");
			startRendering();
		} else {
			Log.w("Drawing", "Stop Render");
			stopRendering();
		}
	}

	void lineSeg(double x1, double y1, double x2, double y2, ARGL_CONTEXT_SETTINGS_REF contextSettings, ARParam cparam, double zoom)
	{
		int enable;
	    float   ox, oy;
	    double  xx1, yy1, xx2, yy2;
		
		if (!contextSettings) return;
		arglDistortionCompensationGet(contextSettings, &enable);
	    if (arglDrawModeGet(contextSettings) == AR_DRAW_BY_TEXTURE_MAPPING && enable) {
	        xx1 = x1;  yy1 = y1;
	        xx2 = x2;  yy2 = y2;
	    } else {
	        arParamIdeal2Observ(cparam.dist_factor, x1, y1, &xx1, &yy1);
	        arParamIdeal2Observ(cparam.dist_factor, x2, y2, &xx2, &yy2);
	    }
		
	    xx1 *= zoom; yy1 *= zoom;
	    xx2 *= zoom; yy2 *= zoom;
		
		ox = 0;
		oy = cparam.ysize - 1;
		glBegin(GL_LINES);
		glVertex2f(ox + xx1, oy - yy1);
		glVertex2f(ox + xx2, oy - yy2);
		glEnd();
	    glFlush();
	}

	@Override
	protected void onRender(final double deltaTime) {
		if (bMarkerVisible) {
			Vector<ARObject> ArObjs = GameActivity.getInstance().markerInfo
					.getObjects();
			ARObject ArObj = ArObjs.get(0);
			changePositionTest(ArObj.getX(), ArObj.getY(), 0);
			/*
			 * Log.w("Drawing", "Drawing @ x :" + ArObj.getX() + " y : " +
			 * ArObj.getY());
			 */
			super.onRender(deltaTime);
		}
	}
}
