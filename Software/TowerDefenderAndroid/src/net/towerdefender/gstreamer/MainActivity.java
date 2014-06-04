package net.towerdefender.gstreamer;

import net.towerdefender.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gstreamer.GStreamer;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	private native void nativeInit(); // Initialize native code, build pipeline,

	private native void changeIpConnexion(int a, int b, int c, int d); // Update
																		// Ip
	// Connexion

	private native void nativeFinalize(); // Destroy pipeline and shutdown
											// native code

	private native void nativePlay(); // Set pipeline to PLAYING

	private native void nativePause(); // Set pipeline to PAUSED

	private static native boolean nativeClassInit(); // Initialize native class:
														// cache Method IDs for
														// callbacks

	private native void nativeSurfaceInit(Object surface);

	private native void nativeSurfaceFinalize();

	private long native_custom_data; // Native code will use this to keep
										// private data
	private boolean is_playing_desired = true; // Whether the user asked to go
												// to PLAYING

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize GStreamer and warn if it fails
		try {
			GStreamer.init(this);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		setContentView(R.layout.activity_main);

		/*
		 * if (savedInstanceState == null) {
		 * getSupportFragmentManager().beginTransaction() .add(R.id.container,
		 * new PlaceholderFragment()).commit(); }
		 */

		SurfaceView sv = (SurfaceView) this.findViewById(R.id.surface_video);
		SurfaceHolder sh = sv.getHolder();
		sh.addCallback(this);

		nativeInit();

		changeIpConnexion(10, 1, 1, 116);
		nativePause();
		nativePlay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	// Called from native code. Native code calls this once it has created its
	// pipeline and
	// the main loop is running, so it is ready to accept commands.
	private void onGStreamerInitialized() {
		Log.i("GStreamer", "Gst initialized. Restoring state, playing:"
				+ is_playing_desired);
		// Restore previous playing state

		// changeIpConnexion(10, 1, 1, 190);
		// nativePause();
		nativePlay();

		// changeIpConnexion(10, 1, 1, 160);
		// nativePause();
		// nativePlay();
		// nativeFinalize();
		// nativeClassInit();
		// nativeInit();
		// is_playing_desired = true;
		// if (is_playing_desired) {
		// nativePlay();
		// } else {
		// nativePause();
		// }

		// Re-enable buttons, now that GStreamer is initialized
		final Activity activity = this;
		runOnUiThread(new Runnable() {
			public void run() {
				// activity.findViewById(R.id.button_play).setEnabled(true);
				// activity.findViewById(R.id.button_stop).setEnabled(true);
			}
		});
	}

	static {
		System.loadLibrary("gstreamer_android");
		System.loadLibrary("towerdefender_gstreamer");
		nativeClassInit();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Log.d("GStreamer", "Surface changed to format " + format + " width "
		// + width + " height " + height);
		nativeSurfaceInit(holder.getSurface());
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// Log.d("GStreamer", "Surface created: " + holder.getSurface());
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Log.d("GStreamer", "Surface destroyed");
		nativeSurfaceFinalize();
	}

	// Called from native code. This sets the content of the TextView from the
	// UI thread.
	private void setMessage(final String message) {
		final TextView tv = (TextView) this.findViewById(R.id.textview_message);
		runOnUiThread(new Runnable() {
			public void run() {
				tv.setText(message);
			}
		});
	}
}
