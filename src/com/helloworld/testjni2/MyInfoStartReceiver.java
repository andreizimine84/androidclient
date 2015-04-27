package com.helloworld.testjni2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyInfoStartReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		  Log.d("VIVZ", "Knappen fungerar 3");
		  Intent intent3 = new Intent(context, InfoService.class);
		  context.startService(intent3);	  
	}
}
