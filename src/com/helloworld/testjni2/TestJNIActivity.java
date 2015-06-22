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
	private BroadcastReceiver mJNIClientReceiver;
	
	private IntentFilter mStartFilter;
	private IntentFilter mBreakFilter;
	private IntentFilter mJNIClientFilter;
	private static boolean JNI = true;
	
	int maxDataLength = 300;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mStartFilter = new IntentFilter();
		mBreakFilter = new IntentFilter();
		mJNIClientFilter = new IntentFilter();
		if(!JNI){
			mStartFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
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
		}
		else if(JNI){	
			mStartFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mJNIClientFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mStartReceiver = new BroadcastReceiver() {
				  public void onReceive(Context context, Intent intent) {
						  Intent intent1 = new Intent(context, SelectAndShareService.class);
						  context.startService(intent1); 
				  } 
			};
			
			LocalBroadcastManager.getInstance(this).registerReceiver(mBreakReceiver3, new IntentFilter("byteArray2"));
			
			mJNIClientReceiver = new BroadcastReceiver() {
				  public void onReceive(Context context, Intent intent) {
						  Intent intent1 = new Intent(context, JNIClient.class);
						  context.startService(intent1); 
				  } 
			};
		}
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
	
	public BroadcastReceiver mBreakReceiver3 = new BroadcastReceiver() {
		@Override
	    public void onReceive(Context context, Intent intent) {
			TempFolder.mAddBackNumber = TempFolder.mAddNumber;				
			Intent intent2 = new Intent(context, JNIClient.class);
			intent2.putExtra("image", intent.getStringExtra("folder"));
			intent2.putExtra("image", intent.getStringExtra("numberFile"));
			context.startService(intent2);
	    }
	};
	
    public void onClick(View v){
    }
        
	 protected void onStart() {
		 super.onStart(); 
	 }
	
	 @Override
	 public void onResume() {
	   super.onResume();
	   registerReceiver(mStartReceiver, mStartFilter);
	   registerReceiver(mBreakReceiver, mBreakFilter);
	   registerReceiver(mJNIClientReceiver, mJNIClientFilter);
	 }

	 @Override
	 protected void onPause() {
	   super.onPause();
	   unregisterReceiver(mStartReceiver);
	   unregisterReceiver(mBreakReceiver);
	   unregisterReceiver(mJNIClientReceiver);
	 } 
	 
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();   
    }
}