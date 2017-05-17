package com.exercise.mycarfinder1;

import java.io.IOException;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewGroup;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.Toast;


public class EnterLocation extends Activity {
//	public class EnterLocation extends ListActivity {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
//    private static final int EMAIL_ID = Menu.FIRST;
//    private static final int DELETE_ID = Menu.FIRST + 1;
//    private static final String LOG_TAG = "EmailLauncherActivity";
    
    protected LocationManager locationManager;
    
   
    protected Button saveAddressButton;


    private GpsDbAdapter mDbHelper;
    private Long mRowId;
    private EditText mAddressText;
//    private Cursor mLocationCursor;
    public boolean enter_location;
    private EditText mDescriptionText;
    //added
    //**
//  private TextView mDateText;
    private EditText mDateText;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
  //**
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
      
        mDbHelper = new GpsDbAdapter(this);
        mDbHelper.open();
                
        setContentView(R.layout.enter_location);
        setTitle(R.string.app_name);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextSize(15);
        View title = getWindow().findViewById(android.R.id.title);
        View titleBar = (View) title.getParent();
        titleBar.setBackgroundColor(Color.DKGRAY);
        Bundle bun = getIntent().getExtras();
        enter_location = bun.getBoolean("enter_location");
        mAddressText = (EditText) findViewById(R.id.address_text);
        mDescriptionText = (EditText) findViewById(R.id.desc_text);
      //**
//      mDateText = (TextView) findViewById(R.id.date);
      mDateText = (EditText) findViewById(R.id.date_add);
      mPickDate = (Button) findViewById(R.id.pickDate_add);
      // add a click listener to the button
      mPickDate.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              showDialog(DATE_DIALOG_ID);
          }
      });

      // get the current date
      final Calendar c = Calendar.getInstance();
      mYear = c.get(Calendar.YEAR);
      mMonth = c.get(Calendar.MONTH);
      mDay = c.get(Calendar.DAY_OF_MONTH);

      // display the current date (this method is below)
      updateDisplay();
      //**
        saveAddressButton = (Button) findViewById(R.id.save_address_button);
        Button mainMenuButton = (Button) findViewById(R.id.main_menu_button_add); 
        Log.v("I am here", "inside on create");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );

                
        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Log.v("I am here", "inside save");
            	String address = null;
            	address = mAddressText.getText().toString();
            	String description = mDescriptionText.getText().toString();
            	System.out.println("address is "+address);
            	String date = mDateText.getText().toString();
//            	Log.v("I am here", address);
            	if(!(address.equals(""))) {
            	saveCurrentLocation(address, description, date);
            	}
            	String save_address_complete = "done";
            	Bundle bundle= new Bundle();
            	Intent intent = new Intent();
            	bundle.putString("save_address_complete", save_address_complete);
            	intent.setClass(EnterLocation.this, MyCarFinder.class);
              	intent.putExtras(bundle);
              	
              	startActivityForResult(intent,0);
            	
            }
    });
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
	    	
            public void onClick(View view) {
                                                 
            	String save_address_cancelled = "cancel";
            	Bundle bundle= new Bundle();
            	Intent intent = new Intent();
            	bundle.putString("save_address_cancelled", save_address_cancelled);
            	intent.setClass(EnterLocation.this, MyCarFinder.class);
              	intent.putExtras(bundle);
              	
              	startActivityForResult(intent,0);
            }
        });
        
    }


    
    
    protected void saveCurrentLocation(String address, String description, String date) {

    	Log.v("I am ","inside saveCurrentLocation EnterLocation class");
    	Geocoder fwdGeocoder = new Geocoder(this, Locale.US);
//    	Geocoder fwdGeocoder = new Geocoder(this, Locale.getDefault());
    	List<Address> locations = null;
    	double n =0.0;
    	double e =0.0;
    	double s =0.0;
    	double w=0.0;
    	Log.v("I am after gc","inside saveCurrentLocation EnterLocation class");
    	Log.v("I am after gc",address);
    	try {
    		Log.v("I am inside","try");
    	locations = fwdGeocoder.getFromLocationName(address, 1, n, e, s, w);
    	if(locations!=null){
        	Log.v("locations",""+locations);
    	
    	int listSize =  locations.size();
    	int length1, length2, length3;
    	int index, index1;
    	String temp, temp1, temp2;
        String[] location_address = new String[listSize];
        Iterator it = locations.iterator();
    	int i = 0;
    	while(it.hasNext()){
    		Object element = it.next();
    		location_address[i]=""+element;
    		Log.v("addresses",location_address[i]);
    		i++;
    	}
    	length1 = location_address[0].length();
    	index = location_address[0].indexOf(',');
		temp = location_address[0].substring(index+1, length1);
    	for(int j =0; j<12; j++){
    		index = temp.indexOf(',');
    		length1 = temp.length();
    		temp = temp.substring(index+1, length1);
    	}
    	Log.v("temp",temp);
    	index = temp.indexOf(',');
    	String lat_address = temp.substring(0,index);
    	Log.v("lat_address",lat_address);
    	length2 = temp.length();                	
    	temp1 = temp.substring(index+1,length2);
    	Log.v("temp1",temp1);
    	index1 = temp1.indexOf(',');
    	length3 = temp1.length();
    	temp2 = temp1.substring(index1+1,length3);
    	Log.v("temp2",temp2);
    	index1 = temp2.indexOf(',');
    	String lng_address = temp2.substring(0,index1);
    	Log.v("lng_address",lng_address);
    	
    	index = lat_address.indexOf('=');
    	length1 = lat_address.length();
    	String latitude = lat_address.substring(index+1, length1);
    	Log.v("latitude",latitude);
    	index = lng_address.indexOf('=');
    	length1 = lng_address.length();
    	String longitude = lng_address.substring(index+1, length1);
    	Log.v("longitude",longitude);
    	Log.v("description", description);
    	String imagePath="";
    	long id1 = mDbHelper.createRecord(latitude, longitude, description, imagePath, date, address);
        Log.v("inserted latitude", latitude);
        Log.v("inserted longitude", longitude);
        Log.v("inserted description", description);
        Log.v("inserted date", date);
        Log.v("inserted address", address);
        if (id1 > 0) {
        	mRowId = id1;
        	Log.v("mRowId inside savecurrentLoacation",""+mRowId);
        }
    	}
//    	else {
//    		Log.v("locations","isnull");
//    	}
    	} catch (IOException i) {Log.v("inside exception",""+i);}

            
            
//        }

    }
    
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
//            Toast.makeText(EnterLocation.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(EnterLocation.this, "Provider status changed",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(EnterLocation.this,
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
//            Toast.makeText(EnterLocation.this,
//                    "Provider enabled by the user. GPS turned on",
//                    Toast.LENGTH_LONG).show();
        }

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
    }
    
    //**
    // updates the date in the TextView
    private void updateDisplay() {
        mDateText.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" "));
    }
    
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
            
            @Override
            protected Dialog onCreateDialog(int id) {
                switch (id) {
                case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                                mDateSetListener,
                                mYear, mMonth, mDay);
                }
                return null;
            }
            //**
}