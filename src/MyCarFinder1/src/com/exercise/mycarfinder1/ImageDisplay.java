package com.exercise.mycarfinder1;

import java.io.File;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;

public class ImageDisplay extends Activity {// implements OnTouchListener{
	
	public String result = new String();

	   ImageView myImage;
	   ImageView myImage1;
	   ViewFlipper flipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.image_display);
		setTitle(R.string.app_name);
	    ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
	    ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
	    ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextSize(15);
	    View title = getWindow().findViewById(android.R.id.title);
	    View titleBar = (View) title.getParent();
	    titleBar.setBackgroundColor(Color.DKGRAY);
		       
        Bundle bun = getIntent().getExtras();
		result = bun.getString("enlargeImage");
		Log.v("result", result);   
		
		Bitmap myBitmap = BitmapFactory.decodeFile(result);
		myImage = (ImageView) findViewById(R.id.icon1);
		
		myImage.setImageBitmap(myBitmap);

	
		
		myImage.setOnClickListener(new View.OnClickListener() {
	    	
	        public void onClick(View view) {
	                                             
	            Intent mIntent = new Intent();           
	            setResult(RESULT_OK, mIntent);
	            finish();
	        }
	    });

	}
}
		
