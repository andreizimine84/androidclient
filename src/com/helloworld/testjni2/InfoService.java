package com.helloworld.testjni2;

import java.util.Arrays;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

public class InfoService extends IntentService{
	
	public InfoService() {
		super("InfoService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {		
		// TODO Auto-generated method stub
		try {
			InfoSender.main(intent, this.getApplicationContext());
			Log.d("VIVZ", "Knappen fungerar 4");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
