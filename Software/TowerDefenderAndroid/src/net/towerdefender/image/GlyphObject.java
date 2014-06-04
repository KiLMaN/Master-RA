package net.towerdefender.image;

import org.andengine.util.math.MathUtils;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.primitives.Cube;
import rajawali.renderer.RajawaliRenderer;

/**
 * An example of an AR object being drawn on a marker.
 * 
 * @author tobi
 * 
 */
public class GlyphObject extends ARObject {

	Object3D _object;
	Material material;
	int _color;

	public GlyphObject(String name, String patternName, double markerWidth,
			double[] markerCenter) {
		super(name, patternName, markerWidth, markerCenter);
		_color = 0x0000FF;
	}

	public GlyphObject(String name, String patternName, double markerWidth,
			double[] markerCenter, int color) {
		this(name, patternName, markerWidth, markerCenter);
		_color = color;
	}

	@Override
	public void init(RajawaliRenderer renderer) {
		material = new Material();
		material.enableLighting(false);
		_object = new Cube(1f);
		_object.setMaterial(material);
		_object.setColor(_color);

		renderer.getCurrentScene().addChild(_object);
		this.initialized = true;

	}

	@Override
	public void render(RajawaliRenderer renderer) {
		if (!this.isInitialised()) {
			this.init(renderer);
		}
		if (this.isVisible()) {

			float wCenter = /* this.mViewportWidth */720 / 2;
			float hCenter = /* this.mViewportWidth */480 / 2;
			float maxW = 11.5f;
			float maxH = 6.5f;

			float x = MathUtils.arrayAverage(this.getXs());
			float y = MathUtils.arrayAverage(this.getYs());
			float newx = ((x - wCenter) / wCenter) * maxW;
			float newy = ((y - hCenter) / hCenter) * maxH;
			// line.reload();

			_object.setPosition(newx, -newy, 0);
			_object.setVisible(true);
			this.setHidden(false);
		} else
			_object.setVisible(false);

	}

	@Override
	public void hide() {
		this._object.setVisible(false);
		super.hide();
	}

}
