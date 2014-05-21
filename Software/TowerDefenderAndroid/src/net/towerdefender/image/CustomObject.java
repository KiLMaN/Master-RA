package net.towerdefender.image;

/**
 * An example of an AR object being drawn on a marker.
 * 
 * @author tobi
 * 
 */
public class CustomObject extends ARObject {

	public CustomObject(String name, String patternName, double markerWidth,
			double[] markerCenter) {
		super(name, patternName, markerWidth, markerCenter);
		/*
		 * float mat_ambientf[] = { 0f, 1.0f, 0f, 1.0f }; float mat_flashf[] = {
		 * 0f, 1.0f, 0f, 1.0f }; float mat_diffusef[] = { 0f, 1.0f, 0f, 1.0f };
		 * float mat_flash_shinyf[] = { 50.0f };
		 * 
		 * mat_ambient = GraphicsUtil.makeFloatBuffer(mat_ambientf); mat_flash =
		 * GraphicsUtil.makeFloatBuffer(mat_flashf); mat_flash_shiny =
		 * GraphicsUtil.makeFloatBuffer(mat_flash_shinyf); mat_diffuse =
		 * GraphicsUtil.makeFloatBuffer(mat_diffusef);/
		 * 
		 * }
		 * 
		 * public CustomObject(String name, String patternName, double
		 * markerWidth, double[] markerCenter, float[] customColor) {
		 * /*super(name, patternName, markerWidth, markerCenter); float
		 * mat_flash_shinyf[] = { 50.0f };
		 * 
		 * mat_ambient = GraphicsUtil.makeFloatBuffer(customColor); mat_flash =
		 * GraphicsUtil.makeFloatBuffer(customColor); mat_flash_shiny =
		 * GraphicsUtil.makeFloatBuffer(mat_flash_shinyf); mat_diffuse =
		 * GraphicsUtil.makeFloatBuffer(customColor);
		 */

	}

	/*
	 * private SimpleBox box = new SimpleBox(); private FloatBuffer mat_flash;
	 * private FloatBuffer mat_ambient; private FloatBuffer mat_flash_shiny;
	 * private FloatBuffer mat_diffuse;
	 */

	/**
	 * Everything drawn here will be drawn directly onto the marker, as the
	 * corresponding translation matrix will already be applied.
	 */
	/*
	 * @Override public final void draw() { super.draw();
	 * 
	 * }
	 */

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
}
