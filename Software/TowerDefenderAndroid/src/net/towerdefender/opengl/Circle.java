package net.towerdefender.opengl;

public class Circle {

	public static float[] generateCircleVertices(float x, float y,
			float radius, int count) {
		float[] t = new float[count * 3];

		for (int i = 0; i < count * 3;) {
			double angle = ((2 * Math.PI) / count) * i;
			t[i++] = (float) (radius * Math.cos(angle) + x);
			t[i++] = (float) (radius * Math.sin(angle) + x);
			t[i++] = 0.0f;
		}
		return t;
	}
}
