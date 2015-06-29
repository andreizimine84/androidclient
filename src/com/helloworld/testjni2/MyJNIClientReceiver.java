package com.helloworld.testjni2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyJNIClientReceiver extends BroadcastReceiver{
	  public void onReceive(Context context, Intent intent) {
		  if(isOrderedBroadcast()){
			  return;
		  }
		  else{
			  Intent intent1 = new Intent(context, JNIClient.class);
			  context.startService(intent1);
		  }
	  } 
}
