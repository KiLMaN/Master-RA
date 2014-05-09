package net.towerdefender.activity;

import java.io.ObjectInputStream;

import net.towerdefender.R;
import rajawali.Object3D;
import rajawali.SerializedObject3D;
import rajawali.animation.Animation.RepeatMode;
import rajawali.animation.RotateOnAxisAnimation;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.math.vector.Vector3.Axis;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.view.animation.LinearInterpolator;

public class ARRajawaliRender extends RajawaliRenderer {

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

			Object3D monkey = new Object3D(serializedMonkey);
			Material material = new Material();
			material.enableLighting(true);
			material.setDiffuseMethod(new DiffuseMethod.Lambert());
			monkey.setMaterial(material);
			monkey.setColor(0xffff8C00);
			monkey.setScale(2);
			getCurrentScene().addChild(monkey);

			RotateOnAxisAnimation anim = new RotateOnAxisAnimation(Axis.Y, 360);
			anim.setDurationMilliseconds(6000);
			anim.setRepeatMode(RepeatMode.INFINITE);
			anim.setInterpolator(new LinearInterpolator());
			anim.setTransformable3D(monkey);
			getCurrentScene().registerAnimation(anim);
			anim.play();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// -- set the background color to be transparent
		// you need to have called setGLBackgroundTransparent(true); in
		// the activity
		// for this to work.
		getCurrentScene().setBackgroundColor(0);

	}

}
