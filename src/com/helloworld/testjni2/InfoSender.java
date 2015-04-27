package com.helloworld.testjni2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

public class InfoSender {
	
	public static int size = 0;
	public static int maxDataLength = 1000;
	public static StringBuilder data = new StringBuilder(maxDataLength);
	public static String addr = "10.0.2.2";
	public static String port = "8080";
	public static int lastSize = 0;
	public static int maxLastSize = 0;
	public static String[] selectAndShare = new String[6];
	public static Context globalContext;
    //static List<String> selectAndShare = new ArrayList<String>();
	
	static {
    	System.loadLibrary("TestJNI2");
    }
    
    public static native void connectToHostJNICPP(String port, String addr, String data);
	
	public static void main(Intent intent, Context context) throws SecurityException, RemoteException, ClassCastException, NullPointerException
	{
		
		globalContext = context;
		System.out.println("Knappen fungerar 5");
		Log.d("VIVZ", "Knappen fungerar 5.1");

		try {
			SelectAndShare.main(intent, globalContext);
		} catch (SecurityException e) {
			// TODO Auto-generated catch blockK
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
		sendData();

		maxLastSize = 0;

		System.out.println("jjj all" + data.length() + selectAndShare.length);
	}
	
	public static void sendData(){
		for(int x = 0; x <= maxLastSize; x = x + 1){
			if(x < maxLastSize){
				dataSizeAndSend(selectAndShare[x].toString());
			}		
			else if(x == maxLastSize){
				connectToHostJNICPP(port, addr, data.toString());
				data.delete(0, data.length());
				data.setLength(0);
				Arrays.fill(selectAndShare, "");
				selectAndShare = new String[0];
				break;
			}
		}
	}
	
	public static void infoCollector(){
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
	
	public static void dataSizeAndSend(String str)
	{
    		data.append(str);
    		data.append("\n");
    }
}
