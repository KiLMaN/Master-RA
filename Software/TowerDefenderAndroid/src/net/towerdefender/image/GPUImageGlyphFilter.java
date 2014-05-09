package net.towerdefender.image;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

public class GPUImageGlyphFilter extends GPUImageFilterGroup {

	private static float SobelThreshold = 0.5f;

	public GPUImageGlyphFilter() {
		super(createFilters());
	}

	private static List<GPUImageFilter> createFilters() {
		List<GPUImageFilter> filters = new ArrayList<GPUImageFilter>();

		filters.add(new GPUImageGrayscaleFilter());
		filters.add(new GPUImageSobelThresholdFilter(SobelThreshold));

		return filters;
	}

	public void setLineSize(final float size) {
		((GPUImage3x3TextureSamplingFilter) getFilters().get(1))
				.setLineSize(size);
	}
}
