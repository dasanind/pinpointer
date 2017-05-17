package com.exercise.mycarfinder1;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {
  private static final String TAG = "CameraDemo";
  Preview preview; // <1>
  Button buttonClick; // <2>
  String imageName;
  String imagePath;
  Button back;
  //added 11/24/2011
  private Long mRowId;
  private GpsDbAdapter mDbHelper;
  private boolean take_picture;
//  private ArrayList<String> descImageDetails = new ArrayList<String>();
//added 11/24/2011
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
  //added
    mDbHelper = new GpsDbAdapter(this);
    mDbHelper.open();
    //added
    
    setContentView(R.layout.cameraactivity);
    setTitle(R.string.app_name);
    ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
    ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextColor(Color.BLACK);
    ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextSize(15);
    View title = getWindow().findViewById(android.R.id.title);
    View titleBar = (View) title.getParent();
    titleBar.setBackgroundColor(Color.DKGRAY);

    preview = new Preview(this); // <3>
    ((FrameLayout) findViewById(R.id.preview)).addView(preview); // <4>

    buttonClick = (Button) findViewById(R.id.buttonClick);
    back = (Button) findViewById(R.id.back);
    buttonClick.setOnClickListener(new OnClickListener() {
      public void onClick(View v) { // <5>
        preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        
      }
    });
  //added 11/24/2011
    Bundle bundle = getIntent().getExtras();
//    mRowId = bundle.getLong("rowId");
//    Log.v("mRowid inside cameractivity",""+mRowId);
    take_picture = bundle.getBoolean("take_a_picture");
//    descImageDetails = bundle.getStringArrayList("recordDetails");
//    Log.v("descImageDetails inside cameractivity",""+descImageDetails);
    
  //added 11/24/2011
    Log.d(TAG, "onCreate'd");
    
    back.setOnClickListener(new View.OnClickListener() {
    	
        public void onClick(View view) {
                                             
            Intent mIntent = new Intent();           
            setResult(RESULT_OK, mIntent);
            finish();
        	
//        	Bundle bundle= new Bundle();
//        	Intent intent = new Intent();
//        	bundle.putString("imagepath", imagePath);
////        	Log.v("imagepath", imagePath);
////        	 Log.v("descImageDetails inside cameractivity",""+descImageDetails);
////        	bundle.putStringArrayList("descImageDetails", descImageDetails);
//        	intent.setClass(CameraActivity.this, SaveLocation.class);
//        	intent.putExtras(bundle);
//        	startActivityForResult(intent,0);
        }
    });
//    back.setOnClickListener(new OnClickListener() {
//        public void onClick(View v) { // <5>
//        	Bundle bundle= new Bundle();
//        	Intent intent = new Intent();
//        	bundle.putString("imagepath", imageName);
//        	Log.v("imagepath", imageName);
//        	intent.setClass(CameraActivity.this, ImageDisplay.class);
//          	intent.putExtras(bundle);
//          	startActivityForResult(intent,0);
//        }
//      });
  }

  // Called when shutter is opened
  ShutterCallback shutterCallback = new ShutterCallback() { // <6>
    public void onShutter() {
      Log.d(TAG, "onShutter'd");
    }
  };

  // Handles data for raw picture
  PictureCallback rawCallback = new PictureCallback() { // <7>
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.d(TAG, "onPictureTaken - raw");
    }
  };

  // Handles data for jpeg picture
  PictureCallback jpegCallback = new PictureCallback() { // <8>
    public void onPictureTaken(byte[] data, Camera camera) {
//      FileOutputStream outStream = null;
//      try {
//        // Write to SD Card
//        outStream = new FileOutputStream(String.format("/sdcard/%d.jpg",
//            System.currentTimeMillis())); // <9>
//        outStream.write(data);
//        outStream.close();
//        Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);

//        File sdImageMainDirectory = new File("/sdcard/myImages");
    	String extStorageDirectory;
    	extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    	Log.v("extStorageDirectory", extStorageDirectory);
		FileOutputStream fileOutputStream = null;
	    imageName = String.format(extStorageDirectory+"/%d.jpg",
	            System.currentTimeMillis());
		int quality=50;
		try {

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 5;
			
			Bitmap myImage = BitmapFactory.decodeByteArray(data, 0,
					data.length,options);

			
			fileOutputStream = new FileOutputStream(imageName);
							
  
			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			bos.close();
//			if(mRowId != null){
//		    	Log.v("before","updatefield");
//		     updateField();
//		    }
			if(take_picture == true) {
				imagePath = imageName.toString();
				System.out.println("imagePath inside on picture taken"+ imagePath);
				//**
		        Bundle bundle= new Bundle();
		    	Intent intent = new Intent();
		    	bundle.putString("imagepath", imagePath);
		    	System.out.println("imagePath being returned "+ imagePath);
//		    	 Log.v("descImageDetails inside cameractivity",""+descImageDetails);
//		    	bundle.putStringArrayList("descImageDetails", descImageDetails);
		    	intent.setClass(CameraActivity.this, SaveLocation.class);
		    	intent.putExtras(bundle);
		    	startActivityForResult(intent,0);
		        //**
			}
//			imagePath = imageName.toString();
//			descImageDetails.add(imagePath);
//			 Log.v("descImageDetails inside cameractivity",""+descImageDetails);
      } catch (FileNotFoundException e) { // <10>
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
      }
      Log.d(TAG, "onPictureTaken - jpeg");
    }
  };

//  private void updateField() {
//      String imagePath = imageName.toString();
//    
//      Log.v("inside","updatefiled");
//      if (mRowId != null) {
//    	  Cursor record = mDbHelper.fetchRecord(mRowId);
//          startManagingCursor(record);
//    	  Log.v("before","update"); 
//          mDbHelper.updateRecord(mRowId, imagePath);
//          Log.v("Record updated with ",imagePath);
//      }
//  }
}
