package com.exercise.mycarfinder1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.Toast;


//public class MyCarFinder extends Activity {
	public class MyCarFinder extends ListActivity {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    private static final int EMAIL_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final String LOG_TAG = "EmailLauncherActivity";
    
    protected LocationManager locationManager;
    protected Button saveLocationButton;
    protected Button enterLocationButton;
    private GpsDbAdapter mDbHelper;
    private Long mRowId;
    private Cursor mLocationCursor;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        mDbHelper = new GpsDbAdapter(this);
        mDbHelper.open();
               
        setContentView(R.layout.mycarfinder);
        setTitle(R.string.app_name);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextSize(15);
        View title = getWindow().findViewById(android.R.id.title);
        View titleBar = (View) title.getParent();
        titleBar.setBackgroundColor(Color.DKGRAY);
        //titleBar.setMinimumHeight(1000);
        
        saveLocationButton = (Button) findViewById(R.id.save_location_button);
        enterLocationButton = (Button) findViewById(R.id.enter_location_button);
                    
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );        
       
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.location_header, lv, false);
        lv.addHeaderView(header, null, false);
        fillData();
        registerForContextMenu(getListView());
                      
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	boolean save_location = true;
            	Bundle bundle= new Bundle();
            	Intent intent = new Intent();
            	bundle.putBoolean("save_location", save_location);

            	intent.setClass(MyCarFinder.this, SaveLocation.class);
              	intent.putExtras(bundle);
              	
              	startActivityForResult(intent,0);

            }
    });
        
        
       enterLocationButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           	boolean enter_location = true;
           	Bundle bundle= new Bundle();
           	Intent intent = new Intent();
           	bundle.putBoolean("enter_location", enter_location);
           	intent.setClass(MyCarFinder.this, EnterLocation.class);
             	intent.putExtras(bundle);
             	
             	startActivityForResult(intent,0);
           	
           }
   });
               
        

    }
 
    private void fillData() {
        // Get all of the rows from the database and create the item list
        
        mLocationCursor = mDbHelper.fetchAllRecords();
        startManagingCursor(mLocationCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{GpsDbAdapter.KEY_DATE, GpsDbAdapter.KEY_DESCRIPTION, GpsDbAdapter.KEY_ADDRESS};
        // and an array of the fields we want to bind those fields to (in this case just text1)

        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter positions = 
            new SimpleCursorAdapter(this, R.layout.location_row, mLocationCursor, from, to);
        setListAdapter(positions);
        positions.notifyDataSetChanged();
    }
    

    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        menu.add(0, EMAIL_ID, 0, R.string.menu_email);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteRecord(info.id);
                fillData();
                return true;
            case EMAIL_ID:
                AdapterContextMenuInfo info_location = (AdapterContextMenuInfo) item.getMenuInfo();
                
                	long rowId = info_location.id;
                	String latitude;
                	String longitude;
                	String imagePath;
                	int listSize;
                	int index, index1, length1, length2, length3;
                	String temp, temp1, temp2;
                	String address1, address2;
                	
                	//fetch the latitude and longitude from the database
                	Cursor location = mDbHelper.fetchRecord(rowId);
                    startManagingCursor(location);
                    latitude =location.getString(
                    		location.getColumnIndexOrThrow(GpsDbAdapter.KEY_LATITUDE));
                    longitude = location.getString(
                    		location.getColumnIndexOrThrow(GpsDbAdapter.KEY_LONGITUDE));
                    imagePath = location.getString(
                    		location.getColumnIndexOrThrow(GpsDbAdapter.KEY_IMAGEPATH));
                    
                    //reverse geocoding the location to get the address
                    double latitude_d = Double.parseDouble(latitude);
                    double longitude_d = Double.parseDouble(longitude);
                    Geocoder gc = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                    addresses = gc.getFromLocation(latitude_d, longitude_d, 1);
                    } catch (IOException e) {}
                    
                    //retrieving the address from the list
                    try {
                    listSize =  addresses.size();
                    String[] location_address = new String[listSize];
                    Iterator it = addresses.iterator();
                	int i = 0;
                	while(it.hasNext()){
                		Object element = it.next();
                		location_address[i]=""+element;
//                		Log.v("addresses",location_address[i]);
                		i++;
                	}
                	length1 = location_address[0].length();
                	index = location_address[0].indexOf('"');
                	temp = location_address[0].substring(index+1, length1);
//                	Log.v("temp",temp);
                	index = temp.indexOf('"');
                	address1 = temp.substring(0,index);
//                	Log.v("address1",address1);
//                	Log.v("address1",address1);
                	length2 = temp.length();                	
                	temp1 = temp.substring(index+1,length2);
//                	Log.v("temp1",temp1);
                	index1 = temp1.indexOf('"');
                	
                	length3 = temp1.length();
                	temp2 = temp1.substring(index1+1,length3);
//                	Log.v("temp2",temp2);
                	index1 = temp2.indexOf('"');
                	address2 = temp2.substring(0,index1);
//                	Log.v("address2",address2);
                	
                	//sending email with the address
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    emailIntent.setType("plain/text");
                    emailIntent.setType("image/jpg");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Message from PinPointer");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Address:\n"+address1+"\n"+address2+"\n"+"\n"+"Latitude: "+latitude+"\n"
                    +"Longitude: "+longitude+"\n"+"\n"+"\n"+"Sent from PinPointer App");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+imagePath));
                    startActivity(emailIntent);
                } catch (Exception e) {
//                    Log.e(LOG_TAG, "sendSimpleEmail() failed to start activity.", e);
//                    Toast.makeText(this, "No handler", Toast.LENGTH_LONG).show();
                } 

                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Cursor c = mLocationCursor;
    	ArrayList<String> locationDetails = new ArrayList<String>();
    	locationDetails = sendCurrentLocation();
//    	Log.v("locationdetails", ""+locationDetails);
//    	c.moveToPosition(position);
    	int size = locationDetails.size();
//    	Log.v("locationdetails size", ""+size);
    	if(size != 0) {
    	String id1 =""+id;
//    	Log.v("Inside on ListItemClick id1", id1);
    	String savedlatitude = c.getString(c.getColumnIndexOrThrow(GpsDbAdapter.KEY_LATITUDE));
//    	Log.v("Inside on ListItemClick lat", savedlatitude);
    	String savedLongitude = c.getString(c.getColumnIndexOrThrow(GpsDbAdapter.KEY_LONGITUDE));
//    	Log.v("Inside on ListItemClick lng", savedLongitude);
    	String savedDescription = c.getString(c.getColumnIndexOrThrow(GpsDbAdapter.KEY_DESCRIPTION));
//    	Log.v("Inside on ListItemClick des", savedDescription);
    	String savedImagePath = c.getString(c.getColumnIndexOrThrow(GpsDbAdapter.KEY_IMAGEPATH));
    	//**
    	String savedAddress = c.getString(c.getColumnIndexOrThrow(GpsDbAdapter.KEY_ADDRESS));
    	if(savedAddress.equals(" ")){
    		System.out.println("null string");
    	}
    	Log.v("Inside on ListItemClick des savedAddress", savedAddress);
    	locationDetails.add(savedlatitude);
    	locationDetails.add(savedLongitude);
    	locationDetails.add(savedImagePath);
    	showCurrentLocation();
    	
    	Bundle bundle= new Bundle();
    	Intent intent = new Intent();
    	bundle.putStringArrayList("locationDetails", locationDetails);
    	intent.setClass(MyCarFinder.this, MapRouteActivity.class);
      	intent.putExtras(bundle);
      	
      	startActivityForResult(intent,0);
    	}
    }
    
    protected ArrayList<String> sendCurrentLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ArrayList<String> currentLocationDetails = new ArrayList<String>();
        if (location != null) {
//            String message = String.format(
//                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
            String currentLatitude = ""+location.getLatitude();
            String currentLongitude = ""+location.getLongitude();
            
//            Log.v("latitude",currentLatitude);
//            Log.v("longitude",currentLongitude);
//            Toast.makeText(MyCarFinder.this, message,
//                    Toast.LENGTH_LONG).show();
            currentLocationDetails.add(currentLatitude);
            currentLocationDetails.add(currentLongitude);
            
        }
        return currentLocationDetails;
    }
  //added 11/19/2011
    
    protected void showCurrentLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
//            String message = String.format(
//                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
            String longitude = ""+location.getLongitude();
            String latitude = ""+location.getLatitude();
//            Log.v("latitude",latitude);
//            Log.v("longitude",longitude);
//            Toast.makeText(MyCarFinder.this, message,
//                    Toast.LENGTH_LONG).show();
        }

    }
    

    
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
//            Toast.makeText(MyCarFinder.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(MyCarFinder.this, "Provider status changed",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(MyCarFinder.this,
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
//            Toast.makeText(MyCarFinder.this,
//                    "Provider enabled by the user. GPS turned on",
//                    Toast.LENGTH_LONG).show();
        }

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //System.out.println("i am here");
    }
}