package com.helloworld.testjni2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestJNIActivity extends Activity implements OnClickListener {
	Button button1;
		    
	private BroadcastReceiver mStartReceiver;
	private BroadcastReceiver mBreakReceiver;
	//private BroadcastReceiver mInfoStartReceiver;
	private IntentFilter mStartFilter;
	private IntentFilter mBreakFilter;
	//private IntentFilter mInfoStartFilter;
	int maxDataLength = 300;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
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

		LocalBroadcastManager.getInstance(this).registerReceiver(mBreakReceiver2, new IntentFilter("byteArray"));

		mBreakReceiver = new BroadcastReceiver() {
			  public void onReceive(Context context, Intent intent) {
					  Intent intent1 = new Intent(context, FileService.class);
					  context.startService(intent1);
			  } 
		};	

    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(this);
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
	
    public static native void connectToHostJNICPP(String port, String addr, String data);

    public void onClick(View v){
    	Log.d("VIVZ", "Knappen fungerar");
		Context context = this.getApplicationContext();
		Intent intent3 = new Intent(context, InfoService.class);
		context.startService(intent3);
    	Log.d("VIVZ", "Knappen fungerar 2");
    }
        
	 protected void onStart() {
		 super.onStart(); 
	 }
	
	 @Override
	 public void onResume() {
	   super.onResume();
	   registerReceiver(mStartReceiver, mStartFilter);
	   registerReceiver(mBreakReceiver, mBreakFilter);
	 }

	 @Override
	 protected void onPause() {
	   super.onPause();
	   unregisterReceiver(mStartReceiver);
	   unregisterReceiver(mBreakReceiver);
	 } 
	 
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();   
    }
}