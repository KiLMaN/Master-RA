package activity;

import ra.Renderer;

import com.example.ra.R;

import log.Log;
import communication.Communication;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.widget.TextView;
import android.widget.VideoView;

import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;
import android.content.pm.ActivityInfo;

public class MainActivity extends Activity {
	Communication com;
	VideoView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 // When working with the camera, it's useful to stick to one orientation.
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        
		// Initialise log
		initLog();
		
		// Initialise communication
	//	initCom();
		
		// Init render
		//Renderer renderer = new Renderer();
		
		
		 videoView = (VideoView)this.findViewById(R.id.videoView);
	        
	        
	      //Set the path of Video or URI
		 MediaController mediaController = new MediaController(this);
	     mediaController.setAnchorView(videoView);
	     mediaController.setMediaPlayer(videoView);
		 videoView.setMediaController(mediaController);
		 
	     videoView.setVideoURI(Uri.parse("rtsp://@192.168.1.20:8554/"));
	        //

	      //Set the focus
	       videoView.requestFocus();
	      videoView.start();
	      
	        
	      
	    //    v.requestFocus();
	        //v.start();
		 
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initLog(){
		// Log initialisation
		Log.log = (TextView)findViewById(R.id.log);
		Log.log.setText(""); // Clear the log contener
	}
	
	private void initCom(){
		// Communiction initialisation
		com = new Communication();
		com.run();
	}
	
}
