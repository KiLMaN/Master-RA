package com.example.ra;
 
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
 
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;
import android.content.pm.ActivityInfo;
 
public class RtspActivity extends Activity {
	VideoView videoView;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView)this.findViewById(R.id.videoView);
        
        
      //Set the path of Video or URI
       // videoView.setVideoURI(Uri.parse("rtsp://tv.hindiworldtv.com:1935/live/getpnj"));
        //

      //Set the focus
       // videoView.requestFocus();
        
      //  v.setMediaController( new MediaController( this ) );
    //    v.requestFocus();
        //v.start();
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rtsp, menu);
        return true;
    }
}