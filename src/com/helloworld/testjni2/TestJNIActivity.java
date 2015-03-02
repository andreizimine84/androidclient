package com.helloworld.testjni2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestJNIActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	Button button1;
	
	static {
    	System.loadLibrary("TestJNI2");
    }
	    
	private BroadcastReceiver mStartReceiver;
	private BroadcastReceiver mBreakReceiver;
	private IntentFilter mStartFilter;
	private IntentFilter mBreakFilter;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	//System.out.println(String.format("connectToHost returned: %d", connectToHostJNI("http://10.0.2.2:8080")));
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mStartFilter = new IntentFilter();
		mStartFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mBreakFilter = new IntentFilter();
		mBreakFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		
		mStartReceiver = new BroadcastReceiver() {
			  public void onReceive(Context context, Intent intent) {
					  Intent intent1 = new Intent(context, SelectAndShareService.class);
					  context.startService(intent1); 
			  } 
		};

		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = registerReceiver(null, ifilter);
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
		                     status == BatteryManager.BATTERY_STATUS_FULL;
		
		int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		
		System.out.println("battery" + status + isCharging + chargePlug + usbCharge + acCharge);
		
		LocalBroadcastManager.getInstance(this).registerReceiver(mBreakReceiver2, new IntentFilter("byteArray"));

		mBreakReceiver = new BroadcastReceiver() {
			  public void onReceive(Context context, Intent intent) {
					  Intent intent1 = new Intent(context, FileService.class);
					  context.startService(intent1);
			  } 
		};	

    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(this);

    	String text = stringFromJNICPP();
    	
    	Log.d("VIVZ", text);
    	//btnCount.setActivated(true);
    	//button1.setVisibility(0);
    }
    
	public BroadcastReceiver mBreakReceiver2 = new BroadcastReceiver() {
		@Override
	    public void onReceive(Context context, Intent intent) {
			TempFolder.mAddBackNumber = TempFolder.mAddNumber;				
			Intent intent2 = new Intent(context, FileService.class);
			intent2.putExtra("image", intent.getStringExtra("folder"));
			intent2.putExtra("image", intent.getStringExtra("numberFile"));
			TempFolder.connectRWException = true;
			context.startService(intent2);
	    }
	};
	
    public void showLog(){
    	Log.d("log", "clicked");
    }

    public static native String stringFromJNICPP();
    public static native void connectToHostJNICPP(String port, String addr, String data);
    
    String data = "http://10.0.2.2:8080";
    String addr = "10.0.2.2";
    String port = "8080";
    
    public void onClick(View v){
    	Log.d("VIVZ", "Knappen fungerar");

    	try{
        	connectToHostJNICPP(port, addr, data);
    	}
    	catch(UnsatisfiedLinkError e){
    		e.printStackTrace();
    	}
    	catch(RuntimeException e){
    		e.printStackTrace();
    	}    
    	
    	Log.d("VIVZ", "Knappen fungerar 2");
    }
    
	 protected void onStart() {
		  super.onStart();  
	 }
	
	/* @Override
	 public void onResume() {
	   super.onResume();
	   registerReceiver(mStartReceiver, mStartFilter);
	   registerReceiver(mBreakReceiver, mBreakFilter);
	 }*/

	public void enableStartBroadcastReceiver(){

		 ComponentName receiver = new ComponentName(this, MyStartReceiver.class);

		 PackageManager pm = this.getPackageManager();

		 pm.setComponentEnabledSetting(receiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	 }
	
	 public void disableStartBroadcastReceiver(){

		 ComponentName receiver = new ComponentName(this, MyStartReceiver.class);

		 PackageManager pm = this.getPackageManager();

		 pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

	 } 

		public boolean isNetworkAvailable() {
		    ConnectivityManager cm = (ConnectivityManager) 
		      getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        return true;
		    }
		    return false;
		}

	 /*
	 @Override
	 protected void onPause() {
	
	   super.onPause();
	   unregisterReceiver(mStartReceiver);
	   unregisterReceiver(mBreakReceiver);
	
	 } */
	 
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();   
    }
}