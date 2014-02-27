package com.example.test3;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

public class CameraActivity extends Activity {
    private Preview mPreview;
    private Camera mCamera;

    // Test GIT


    class Preview extends SurfaceView implements SurfaceHolder.Callback {
        SurfaceHolder mHolder;

        Preview(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mCamera!=null){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

            // You need to choose the most appropriate previewSize for your app
            Camera.Size previewSize = previewSizes.get(2);// .... select one of previewSizes here

            parameters.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCamera=Camera.open();
        mCamera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mPreview = new Preview(this);
        setContentView(mPreview);
    }


    protected void finishActivity(){
        System.out.println("CameraAcivity finished");
        mPreview.invalidate();
        this.finish();
    }
}