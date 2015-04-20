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

public class InfoService extends IntentService{
	int size = 0;
	int maxDataLength = 3000;
	StringBuilder data = new StringBuilder(maxDataLength);
    String addr = "10.0.2.2";
    String port = "8080";
	int lastSize = 0;
	int maxLastSize = 0;
    String[] selectAndShare = new String[5];
    
	public InfoService() {
		super("InfoService");
	}

	static {
    	System.loadLibrary("TestJNI2");
    }
    
    public static native void connectToHostJNICPP(String port, String addr, String data);
	
	@Override
	protected void onHandleIntent(Intent intent) {		
		// TODO Auto-generated method stub
		try {
			SelectAndShare.main(intent, this.getApplicationContext());
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
		maxLastSize = 5;
		infoCollector();
		for(int x = lastSize; x < maxLastSize; x = x + 1){
			size = dataSizeAndSend(selectAndShare[x], false);
			if(size >= maxDataLength){
				size = dataSizeAndSend(selectAndShare[x], true);
				lastSize = x;
			}
			else if(x == 4){
				size = dataSizeAndSend(selectAndShare[x], true);
			}
		}
		maxLastSize = 0;
		Arrays.fill(selectAndShare, null);
		selectAndShare = new String[0];
		//scheduleNextUpdate();
	}

	public void infoCollector(){
		try {
			selectAndShare[0] = SelectAndShare.getInfoStream().toString();
			selectAndShare[1] = SelectAndShare.getNetworkInfoStream().toString();
			selectAndShare[2] = SelectAndShare.getRunningTaksInfoStream().toString();
			selectAndShare[3] = SelectAndShare.getSensorInfoStream().toString();
			selectAndShare[4] = SelectAndShare.getTimeInfoStream().toString();
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
	
    public int dataSizeAndSend(String str, boolean send){
    	if(!send){
    		data.append(str);
    		data.append("\n");
    		size = data.length();
    	}
    	else if(send){
    		connectToHostJNICPP(port, addr, data.toString());
    		data.setLength(0);
    		size = data.length();
    		System.out.println("testar");
    	}
    	return size;
    }
    
	public void scheduleNextUpdate()
	{
		Intent intent = new Intent(this, this.getClass());
		PendingIntent pendingIntent =
				PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		long currentTimeMillis = System.currentTimeMillis();
		long nextUpdateTimeMillis = currentTimeMillis + 1 * DateUtils.MINUTE_IN_MILLIS;
		Time nextUpdateTime = new Time();
		nextUpdateTime.set(nextUpdateTimeMillis);

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
	}
}
