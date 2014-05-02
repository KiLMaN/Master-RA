package net.towerdefender.activity;

import java.io.IOException;
import java.util.List;

import org.andengine.util.debug.Debug;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:38:21 - 24.05.2010
 */
public class CameraPreviewSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SurfaceHolder mSurfaceHolder;
	private Camera mCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	@SuppressWarnings("deprecation")
	public CameraPreviewSurfaceView(final Context pContext) {
		super(pContext);

		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void surfaceCreated(final SurfaceHolder pSurfaceHolder) {
		this.mCamera = Camera.open();

		try {
			this.mCamera.setPreviewDisplay(pSurfaceHolder);
		} catch (IOException e) {
			Debug.e("Error in Camera.setPreviewDisplay", e);
		}
	}

	public void surfaceDestroyed(final SurfaceHolder pSurfaceHolder) {
		this.mCamera.stopPreview();
		this.mCamera.release();
		this.mCamera = null;
	}

	public void surfaceChanged(final SurfaceHolder pSurfaceHolder,
			final int pPixelFormat, final int pWidth, final int pHeight) {
		final Camera.Parameters parameters = this.mCamera.getParameters();
		parameters.setPreviewSize(pWidth, pHeight);
		List<String> focusModes = parameters.getSupportedFocusModes();
		if (focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
			parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		else if (focusModes.contains(Parameters.FOCUS_MODE_AUTO))
			parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);

		Toast.makeText(
				GameActivity.getInstance(),
				"AutoFocus : " + parameters.getFocusMode() + " Lenght :"
						+ parameters.getFocalLength(), Toast.LENGTH_LONG)
				.show();
		this.mCamera.setParameters(parameters);
		this.mCamera.startPreview();

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void autoFocusCamera() {
		mCamera.autoFocus(null);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}