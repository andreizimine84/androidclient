package com.helloworld.testjni2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyInfoStartReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		  Intent intent3 = new Intent(context, InfoService.class);
		  context.startService(intent3);
	}
}
