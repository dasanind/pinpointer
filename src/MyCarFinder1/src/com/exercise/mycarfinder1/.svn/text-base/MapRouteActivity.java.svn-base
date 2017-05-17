package com.exercise.mycarfinder1;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapRouteActivity extends MapActivity implements OnClickListener {

        LinearLayout linearLayout;
        MapView map_View;
        private Road mRoad;
        private MapController mc;
        public ArrayList<String> result = new ArrayList<String>();
        public String imagePath;
        ImageView myImage;
 	    ImageView myImage1;
 	    ViewFlipper flipper;
 	    private Button mRefreshButton;
 	    //**
 	    private Button mSatelliteButton;
// 	    private Button mStreetButton;
 	   //**
 	    private Button backButton;
 	    protected LocationManager locationManager;
 	    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
 	    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
 	   
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.maproute);
                setTitle(R.string.app_name);
                ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
                ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextSize(15);
                View title = getWindow().findViewById(android.R.id.title);
                View titleBar = (View) title.getParent();
                titleBar.setBackgroundColor(Color.DKGRAY);
                Bundle bun = getIntent().getExtras();
//                Log.v("I am here", "inside on create GMI");
        		result = bun.getStringArrayList("locationDetails");
                map_View = (MapView) findViewById(R.id.map_view);
                map_View.setSatellite(false);
                //**
                mSatelliteButton = (Button) findViewById(R.id.satellite_button);
//                mStreetButton = (Button) findViewById(R.id.street_button);
                //**
                mRefreshButton = (Button) findViewById(R.id.refrsh_button);
                backButton = (Button) findViewById(R.id.back_button);
                map_View.setBuiltInZoomControls(true);
                mc = map_View.getController();
                mc.setZoom(150); 
                new Thread() {
                        @Override
                        public void run() {
                             //double fromLat = 49.85, fromLon = 24.016667, toLat = 50.45, toLon = 30.523333;
                            // double fromLat = 59.85, fromLon = 24.016667, toLat = 50.45, toLon = 30.523333;
                        	//double fromLat = 40.28, fromLon = -116.88354, toLat = 40.19, toLon = -115.839843;  
                        	 int size = result.size();
                         	String[] records = new String[size];
                         	Iterator it = result.iterator();
                         	int i = 0;
                         	while(it.hasNext()){
                         		Object element = it.next();
                         		records[i]=""+element;
                         		Log.v("records[i]", records[i]);
                         		records[i]=records[i].replace("[", "").replace("]", "");
                         		
                         		i++;
                         	}
//                         	Log.v("Array index", ""+i);
//                         	Log.v("lat1", records[0]);
//                             Log.v("long1", records[1]);
//                             Log.v("lat2", records[2]);
//                             Log.v("long2", records[3]);
//                             Log.v("imagepath", records[4]);
                             imagePath = records[4];
//                        	double fromLat = 30.39, fromLon = -97.753691, toLat = Double.parseDouble(records[0]), toLon = Double.parseDouble(records[1]);    
                             double fromLat = Double.parseDouble(records[0]), fromLon = Double.parseDouble(records[1]);
                             double toLat = Double.parseDouble(records[2]), toLon = Double.parseDouble(records[3]); 
                             String url = RoadProvider
                                                .getUrl(fromLat, fromLon, toLat, toLon);
                             Log.v("url",url);
                                InputStream is = getConnection(url);
                                mRoad = RoadProvider.getRoute(is);
//                                Log.v("mRoad.mName",mRoad.mName);
//                                Log.v("mRoad.mDescription",mRoad.mDescription);
                                mHandler.sendEmptyMessage(0);
                        }
                }.start();
                
                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                myImage = (ImageView) findViewById(R.id.icon);
                myImage.setImageBitmap(myBitmap);
//                

        		myImage.setOnClickListener(this);
        		
        		//*
        		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 
                        MINIMUM_TIME_BETWEEN_UPDATES, 
                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                        new MyLocationListener()
                );
                
                
        		mRefreshButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	//**
//                    	mapView.setSatellite(false);
                    	//**
                    	ArrayList<String> locationDetails = new ArrayList<String>();
                    	locationDetails = sendCurrentLocation();
//                    	Log.v("locationdetails", ""+locationDetails);
                    	int size = locationDetails.size();
//                    	Log.v("locationdetails size", ""+size);
                    	if(size != 0) {
                    	
                    	int size_result = result.size();
                    	String[] records_r = new String[size_result];
                    	Iterator ite = result.iterator();
                     	int j = 0;
                     	while(ite.hasNext()){
                     		Object element_r = ite.next();
                     		records_r[j]=""+element_r;
                     		//records_r[j]=records_r[j].replace("[", "").replace("]", "");
                     		
                     		j++;
                     	}
//                     	  Log.v("Array index", ""+j);
//                     	  Log.v("lat1", records_r[0]);
//                        Log.v("long1", records_r[1]);
//                        Log.v("lat2", records_r[2]);
//                        Log.v("long2", records_r[3]);
//                        Log.v("imagepath", records_r[4]);
                        
                        String savedImagePath = records_r[4];
                        String savedlatitude=records_r[2];
                        String savedlongitude=records_r[3];
                        
                        locationDetails.add(savedlatitude);
                    	locationDetails.add(savedlongitude);
                    	locationDetails.add(savedImagePath);
                    	Bundle bundle= new Bundle();
                    	Intent intent = new Intent(); 
                    	bundle.putStringArrayList("locationDetails", locationDetails);
                    	intent.setClass(MapRouteActivity.this, MapRouteActivity.class);
                      	intent.putExtras(bundle);
                      	
                      	startActivityForResult(intent,0);
                    	}
                    	
                    }
            });
        		
        	backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    	Log.v("I am here", "inside back");
                    	
                    	String map_view_complete = "done";
                    	Bundle bundle= new Bundle();
                    	Intent intent = new Intent();
                    	bundle.putString("map_view_complete", map_view_complete);
//                    	bundle.putStringArrayList("currentLocationDetails", currentLocationDetails);
//                    	Log.v("After bundle", ""+currentLocationDetails);
                    	intent.setClass(MapRouteActivity.this, MyCarFinder.class);
                      	intent.putExtras(bundle);
                      	
                      	startActivityForResult(intent,0);
                    }
            });
        	
        	//**
        	mSatelliteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	ArrayList<String> satelliteLocationDetails = new ArrayList<String>();
                	satelliteLocationDetails=result;
                	Bundle bundle= new Bundle();
                	Intent intent = new Intent();
                	bundle.putStringArrayList("locationDetails", satelliteLocationDetails);
//                	bundle.putStringArrayList("currentLocationDetails", currentLocationDetails);
//                	Log.v("After bundle", ""+currentLocationDetails);
                	intent.setClass(MapRouteActivity.this, SatelliteRouteActivity.class);
                  	intent.putExtras(bundle);
                  	
                  	startActivityForResult(intent,0);
                }
        });
        	
//        	mStreetButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                	ArrayList<String> streetLocationDetails = new ArrayList<String>();
//                	streetLocationDetails=result;
//                	Bundle bundle= new Bundle();
//                	Intent intent = new Intent();
//                	bundle.putStringArrayList("locationDetails", streetLocationDetails);
////                	bundle.putStringArrayList("currentLocationDetails", currentLocationDetails);
////                	Log.v("After bundle", ""+currentLocationDetails);
//                	intent.setClass(MapRouteActivity.this, StreetRouteActivity.class);
//                  	intent.putExtras(bundle);
//                  	
//                  	startActivityForResult(intent,0);
//                }
//        });
        	
        }
        
        @Override
		public void onClick(View v) {
		// TODO Auto-generated method stub
//		if (v == myImage1) {
//			flipper.showNext();
//		}
//		if (v == myImage) {
//			flipper.showPrevious();
//		}
        	Bundle bundle= new Bundle();
        	Intent intent = new Intent();
        	
        	bundle.putString("enlargeImage", imagePath);
        	intent.setClass(MapRouteActivity.this, ImageDisplay.class);
          	intent.putExtras(bundle);
          	
          	startActivityForResult(intent,0);
	}

        Handler mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                        TextView textView = (TextView) findViewById(R.id.description);
                        textView.setText(mRoad.mName + " " + mRoad.mDescription);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                        myImage = (ImageView) findViewById(R.id.icon);
                        myImage.setImageBitmap(myBitmap);
                        MapOverlay mapOverlay = new MapOverlay(mRoad, map_View);
                        List<Overlay> listOfOverlays = map_View.getOverlays();
                        listOfOverlays.clear();
                        listOfOverlays.add(mapOverlay);
                        map_View.invalidate();
                };
        };

        private InputStream getConnection(String url) {
                InputStream is = null;
                try {
                        URLConnection conn = new URL(url).openConnection();
                        is = conn.getInputStream();
                } catch (MalformedURLException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return is;
        }

        @Override
        protected boolean isRouteDisplayed() {
                return false;
        }
        
        private class MyLocationListener implements LocationListener {

            public void onLocationChanged(Location location) {
//                String message = String.format(
//                        "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                        location.getLongitude(), location.getLatitude()
//                );
//                Toast.makeText(MyCarFinder.this, message, Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String s, int i, Bundle b) {
//                Toast.makeText(MapRouteActivity.this, "Provider status changed",
//                        Toast.LENGTH_LONG).show();
            }

            public void onProviderDisabled(String s) {
//                Toast.makeText(MapRouteActivity.this,
//                        "Provider disabled by the user. GPS turned off",
//                        Toast.LENGTH_LONG).show();
            }

            public void onProviderEnabled(String s) {
//                Toast.makeText(MapRouteActivity.this,
//                        "Provider enabled by the user. GPS turned on",
//                        Toast.LENGTH_LONG).show();
            }

        }
        
        protected ArrayList<String> sendCurrentLocation() {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ArrayList<String> currentLocationDetails = new ArrayList<String>();
            if (location != null) {
//                String message = String.format(
//                        "Current Location \n Longitude: %1$s \n Latitude: %2$s",
//                        location.getLongitude(), location.getLatitude()
//                );
                String currentLatitude = ""+location.getLatitude();
                String currentLongitude = ""+location.getLongitude();
                
//                Log.v("latitude",currentLatitude);
//                Log.v("longitude",currentLongitude);
//                Toast.makeText(MyCarFinder.this, message,
//                        Toast.LENGTH_LONG).show();
                currentLocationDetails.add(currentLatitude);
                currentLocationDetails.add(currentLongitude);
                
            }
            return currentLocationDetails;
        }
        
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);
            //System.out.println("i am here");
        }
}

class MapOverlay extends com.google.android.maps.Overlay {
        Road mRoad;
        ArrayList<GeoPoint> mPoints;

        public MapOverlay(Road road, MapView mv) {
                mRoad = road;
                if (road.mRoute.length > 0) {
                        mPoints = new ArrayList<GeoPoint>();
                        for (int i = 0; i < road.mRoute.length; i++) {
                                mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000),
                                                (int) (road.mRoute[i][0] * 1000000)));
//                                Log.v("road.mRoute[i][1]",i+""+road.mRoute[i][1]);
//                                Log.v("road.mRoute[i][0]",i+""+road.mRoute[i][1]);
                        }
                        int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(
                                        mPoints.size() - 1).getLatitudeE6() - mPoints.get(0)
                                        .getLatitudeE6()) / 2);
                        int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(
                                        mPoints.size() - 1).getLongitudeE6() - mPoints.get(0)
                                        .getLongitudeE6()) / 2);
                        GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);
//                        Log.v("moveToLat",""+moveToLat);
//                        Log.v("moveToLong",""+moveToLong);
                        MapController mapController = mv.getController();
                        mapController.animateTo(moveTo);
                        mapController.setZoom(18);
                }
        }

        @Override
        public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
                super.draw(canvas, mv, shadow);
                drawPath(mv, canvas);
                return true;
        }

        public void drawPath(MapView mv, Canvas canvas) {
                int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
//                System.out.println("mPoints.size()"+mPoints.size());
                for (int i = 0; i < mPoints.size(); i++) {
                        Point point = new Point();
                        mv.getProjection().toPixels(mPoints.get(i), point);
                        x2 = point.x;
                        y2 = point.y;
                        if (i > 0) {
                                canvas.drawLine(x1, y1, x2, y2, paint);
                        }
                        x1 = x2;
                        y1 = y2;
                }
                //added 11/22/2011
//                Log.v("mPoints.size()",""+mPoints.size());
                int x = mPoints.size();
                Point p = new Point();
                int markerRadius = 5;
                Paint startPaint = new Paint();
                startPaint.setColor(Color.BLUE);
                Paint endPaint = new Paint();
                endPaint.setColor(Color.RED);
                Paint backPaint = new Paint();
                backPaint.setARGB(200, 200, 200, 200);
                backPaint.setAntiAlias(true);
                //marker for start point
                mv.getProjection().toPixels(mPoints.get(0), p);
                RectF startOval = new RectF(p.x-markerRadius,
                        p.y-markerRadius,
                        p.x+markerRadius,
                        p.y+markerRadius);
                canvas.drawOval(startOval, startPaint);
                float textWidth = startPaint.measureText("Start");
                float textHeight = startPaint.getTextSize();
                RectF textRect = new RectF(p.x+markerRadius, p.y-textHeight,
                                           p.x+markerRadius+8+textWidth, p.y+4);
                canvas.drawRoundRect(textRect, 3, 3, backPaint);
                canvas.drawText("Start", p.x+markerRadius+4, p.y, startPaint);
//                Log.v("mPoints.get(0)",""+p.x);
//                Log.v("mPoints.get(0)",""+p.y);
                //marker for end point
                mv.getProjection().toPixels(mPoints.get(x-1), p);
                RectF endOval = new RectF(p.x-markerRadius,
                        p.y-markerRadius,
                        p.x+markerRadius,
                        p.y+markerRadius);
                canvas.drawOval(endOval, endPaint);
                float textWidth1 = endPaint.measureText("End");
                float textHeight1 = endPaint.getTextSize();
                RectF textRect1 = new RectF(p.x+markerRadius, p.y-textHeight1,
                                           p.x+markerRadius+8+textWidth1, p.y+4);
                canvas.drawRoundRect(textRect1, 3, 3, backPaint);
                canvas.drawText("End", p.x+markerRadius+4, p.y, endPaint);
//                Log.v("mPoints.get(i)",""+p.x);
//                Log.v("mPoints.get(i)",""+p.y);
        }
}
