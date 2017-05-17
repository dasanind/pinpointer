package com.exercise.mycarfinder1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
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
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;



public class SaveLocation extends Activity {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    
    protected LocationManager locationManager;
    protected Button saveLocationButton;
    protected Button takePictureButton;
    private GpsDbAdapter mDbHelper;
    private Long mRowId;
    private EditText mDescriptionText;

    public ArrayList<String> imageDescDetails = new ArrayList<String>();
    public boolean save_location;
    public String imagePath ="";

    private EditText mDateText;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mDbHelper = new GpsDbAdapter(this);
        mDbHelper.open();
        
        setContentView(R.layout.save_location);
        setTitle(R.string.app_name);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView)((FrameLayout)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setTextSize(15);
        View title = getWindow().findViewById(android.R.id.title);
        View titleBar = (View) title.getParent();
        titleBar.setBackgroundColor(Color.DKGRAY);
        Bundle bun = getIntent().getExtras();
        save_location = bun.getBoolean("save_location");
                
        takePictureButton = (Button) findViewById(R.id.take_pic_button);
        mDescriptionText = (EditText) findViewById(R.id.description_text);
        
        mDateText = (EditText) findViewById(R.id.date);
        mPickDate = (Button) findViewById(R.id.pickDate);
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
        
        saveLocationButton = (Button) findViewById(R.id.save_button);
        Button mainMenuButton = (Button) findViewById(R.id.main_menu_button);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
        
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	boolean take_a_picture = true;
            	Intent intent = new Intent();
            	Bundle bundle= new Bundle();
            	bundle.putBoolean("take_a_picture", take_a_picture);
            	intent.setClass(SaveLocation.this, CameraActivity.class);
            	intent.putExtras(bundle);
            	startActivityForResult(intent,0);
            	
//            	}
            	
            }
    });
        Bundle bundle = getIntent().getExtras();
        imagePath = bundle.getString("imagepath");
        System.out.println("imagePath recieved"+ imagePath);

        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            	String description = mDescriptionText.getText().toString();
            	String date = mDateText.getText().toString();
//            	Log.v("I am here", description);
            	saveCurrentLocation(description, imagePath, date);
            	String save_location_complete = "done";
            	Bundle bundle= new Bundle();
            	Intent intent = new Intent();
            	bundle.putString("save_location_complete", save_location_complete);
            	intent.setClass(SaveLocation.this, MyCarFinder.class);
              	intent.putExtras(bundle);
              	
              	startActivityForResult(intent,0);
            }
    });
        
    
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
	    	
            public void onClick(View view) {
                                                 
            	String save_location_cancelled = "cancel";
            	Bundle bundle= new Bundle();
            	Intent intent = new Intent();
            	bundle.putString("save_location_cancelled", save_location_cancelled);
            	intent.setClass(SaveLocation.this, MyCarFinder.class);
              	intent.putExtras(bundle);
              	
              	startActivityForResult(intent,0);
            }
        });

    }

  protected void saveCurrentLocation(String description, String imagePath, String date) {

      Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

      if (location != null) {
//          String message = String.format(
//                  "Current Location \n Longitude: %1$s \n Latitude: %2$s",
//                  location.getLongitude(), location.getLatitude()
//          );

    	  double latitude_d = location.getLatitude();
    	  double longitude_d = location.getLongitude();
    	  String address = reverseGeocode(latitude_d, longitude_d);
    	  if(address.equals("")){
    		  DecimalFormat df = new DecimalFormat("#.##");
    		  String lat_address = df.format(latitude_d);
    		  String lng_address = df.format(longitude_d);
    		  address=lat_address+"-"+lng_address;
    	  }

          String longitude = ""+location.getLongitude();
          String latitude = ""+location.getLatitude();
//          Log.v("description", description);
//          Log.v("latitude",latitude);
//          Log.v("longitude",longitude);
//          Toast.makeText(MyCarFinder.this, message,
//                  Toast.LENGTH_LONG).show();
          long id1 = mDbHelper.createRecord(latitude, longitude, description, imagePath, date, address);
//          Log.v("inserted new1 latitude", latitude);
//          Log.v("inserted new1 longitude", longitude);
//          Log.v("inserted description", description);
//          Log.v("inserted date", date);
//          Log.v("inserted address", address);

          if (id1 > 0) {
          	mRowId = id1;
//          	Log.v("mRowId inside savecurrentLoacation",""+mRowId);
          }
         
      }

  }
    
  private String reverseGeocode(double latitude_d, double longitude_d){
  	int listSize;
  	int index, index1, length1, length2, length3;
  	String temp, temp1, temp2;
  	String address1="", address2="", address="";
  	//reverse geocoding the location to get the address
//      Geocoder gc = new Geocoder(this, Locale.getDefault());
      Geocoder gc = new Geocoder(this, Locale.US);
     
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
//  		Log.v("addresses",location_address[i]);
  		i++;
  	}
  	length1 = location_address[0].length();
  	index = location_address[0].indexOf('"');
  	temp = location_address[0].substring(index+1, length1);
//  	Log.v("temp",temp);
  	index = temp.indexOf('"');
  	address1 = temp.substring(0,index);
//  	Log.v("address1",address1);
//  	Log.v("address1",address1);
  	length2 = temp.length();                	
  	temp1 = temp.substring(index+1,length2);
//  	Log.v("temp1",temp1);
  	index1 = temp1.indexOf('"');
  	
  	length3 = temp1.length();
  	temp2 = temp1.substring(index1+1,length3);
//  	Log.v("temp2",temp2);
  	index1 = temp2.indexOf('"');
  	address2 = temp2.substring(0,index1);
//  	Log.v("address2",address2);
      } catch (Exception e) {}
      if(!(address1.equals("")) && !(address2.equals(""))) {
    	  address=address1+" "+address2;
      } else {
    	  address="";
      }
//      Log.v("address", address);
      return address;
  }
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
//            Toast.makeText(SaveLocation.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(SaveLocation.this, "Provider status changed",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(SaveLocation.this,
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
//            Toast.makeText(SaveLocation.this,
//                    "Provider enabled by the user. GPS turned on",
//                    Toast.LENGTH_LONG).show();
        }

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //System.out.println("i am here");
    }
    

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
            
}