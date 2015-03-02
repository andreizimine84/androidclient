package com.helloworld.testjni2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Debug;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
//import android.app.IActivityManager;

public class SelectAndShare extends Application{
	FileOutputStream fos = null;
	ByteArrayOutputStream baos = null;
	InputStream is = null;
	int bytesAvailable = 0;
	byte[] buffer = null;
	static Context globalContext;
	
	public static void main(Intent intent, Context context) throws SecurityException, RemoteException, ClassCastException, NullPointerException
	{
		globalContext = context;
		TempFolder.setFolder(globalContext.getFilesDir().getPath());
		//TempFolder.setFile(completeWriteTempFile(getInfoStream()));
		TempFolder.setFile(completeWriteTempFile(getInfoStream()));
		processCheck();
		Intent in1 = new Intent("byteArray");
		sendLocationBroadcast(in1);
	}
	
	public static String completeWriteTempFile(ByteArrayOutputStream baos) 
	{
		
		FileOutputStream outputStream = null;
	    try
	    {
	    	TempFolder.mAddNumber += 1;
    		if(TempFolder.mAddNumber != 0) 	
    			outputStream = globalContext.openFileOutput("output_temp" + TempFolder.mAddNumber + ".txt", Context.MODE_PRIVATE);
    		else
    			throw new EmptyStackException();
    	
	    	baos.writeTo(outputStream);
	    	
	    	return "output_temp" + TempFolder.mAddNumber + ".txt";
	    }
    
		catch(IOException e)
		{
			System.err.println("Caught IOException: " + e.getMessage());
		}
	    
	    return null;
	}
	
	private static void sendLocationBroadcast(Intent intent)
	{
		intent.putExtra("numberFile", TempFolder.getFile());
		intent.putExtra("folder", TempFolder.getFolder());
	    LocalBroadcastManager.getInstance(globalContext).sendBroadcast(intent);
	}

	public static List<RunningAppProcessInfo> getRunningAppProcessInfo()
	{
		ActivityManager manager = (ActivityManager) globalContext.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningAppProcessInfo> android = manager.getRunningAppProcesses();
	
		return android;
	}

	public static List<RunningServiceInfo> getRunningServiceInfo()
	{
		ActivityManager manager = (ActivityManager) globalContext.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningServiceInfo> android = manager.getRunningServices(manager.getRunningAppProcesses().size());
		
		return android;
	}
	
	public static List<Sensor> getSensorInfo()
	{
		SensorManager manager;
		manager = (SensorManager) globalContext.getSystemService(SENSOR_SERVICE);	
		List<Sensor> android = manager.getSensorList(Sensor.TYPE_ALL);
		return android;
	}
	
	// Batteri info from applikationer Batteri statistiken hur mycket str�m f�rbrukat under en viss period app stat
	// ConnectionTracker interface data skickat och tagit emot 
	
	public static List< ActivityManager.RunningTaskInfo > getRunningTaskInfo() throws RemoteException
	{
		//List<Pair<AppHistoryEntry,ActivityInfo>> activityInfos = new ArrayList<Pair<AppHistoryEntry,ActivityInfo>>();
		
		//List<AppHistoryEntry> appHistories;
		
		//PackageManager manager;
		//IActivityManager iam = null;
		//iam.enterSafeMode();
		//iam.activityPaused(arg0);
		//iam.asBinder();
		//iam.getDeviceConfigurationInfo();
		//iam.getRunningExternalApplications();
		//iam.getRunningAppProcesses();
		//AActivityInfo activityInfo;
		//ApplicationInfo component = activityInfo.applicationInfo;
	    ActivityManager am = (ActivityManager) globalContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List< ActivityManager.RunningTaskInfo > android = am.getRunningTasks(10);
		return android;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("static-access")
	public static boolean getNetworkInfo()
	{
		ConnectivityManager manager = (ConnectivityManager)globalContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		//NetworkInfo[] android = manager.getAllNetworkInfo();
		manager.isActiveNetworkMetered();
		
		boolean android = manager.isNetworkTypeValid(manager.TYPE_WIFI);
		return android;
	}

	public static ByteArrayOutputStream getRunningTaksInfoStream() throws RemoteException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Iterator< ActivityManager.RunningTaskInfo > iterator = getRunningTaskInfo().iterator();
		StringBuilder data = new StringBuilder();
		try 
	    {
	    	while (iterator.hasNext())
			{
	    		ActivityManager.RunningTaskInfo version = iterator.next();
	    		data.append(version.topActivity.getClassName() + "/n");
			}
				
		    baos.write(data.toString().getBytes());
			baos.flush();
		} 
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return baos;
	}

	
	public static ByteArrayOutputStream getSensorInfoStream()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    try 
	    {
			List<Sensor> iterator = getSensorInfo();
			StringBuilder data = new StringBuilder();
		    for(Sensor sensor: iterator){
		    	data.append(sensor.getName() + "\n");
		    	data.append(sensor.getVendor() + "\n");
		    	data.append(sensor.getVersion() + "\n");
		    }
			
		    baos.write(data.toString().getBytes());
			baos.flush();
		} 
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return baos;
	}

	public static void processCheck(){
		try
	    {
	        Process mLogcatProc = null;
	        BufferedReader reader = null;
	        mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});

	        reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

	        String line;
	        final StringBuilder log = new StringBuilder();
	        String separator = System.getProperty("line.separator"); 

	        while ((line = reader.readLine()) != null)
	        {
	            log.append(line);
	            log.append(separator);
	        }
	        String w = log.toString();
	        System.out.println("log proc" + w);
	    }
	    catch (Exception e) 
	    {
	       
	    }
	}

	public static ByteArrayOutputStream getTimeInfoStream()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Iterator<RunningServiceInfo> iterator = getRunningServiceInfo().iterator();
		String s;
		Format formatter;
		try {
			
			while (iterator.hasNext())
			{
				RunningServiceInfo version = iterator.next();
				int count = version.crashCount;
			    long startTime = version.activeSince;
			    long endTime = version.lastActivityTime;
			    long estimatedTime = System.nanoTime() - startTime;
			    long estimatedEndTime = System.nanoTime() - endTime;
			    long finEnd = TimeUnit.SECONDS.convert(estimatedEndTime, TimeUnit.NANOSECONDS);
			    long fin = TimeUnit.SECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS);
			    formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
			    s = formatter.format(System.currentTimeMillis() - fin);
			    baos.write(version.process.getBytes());
			    baos.write(s.getBytes());
			    //System.out.println("log" + s);
			    s = formatter.format(System.currentTimeMillis() - finEnd);
			    baos.write(s.getBytes());
				//System.out.println("log End" + s);
			}

			baos.flush();

		 }
		 catch (IOException e) {
			e.printStackTrace();
		 }
		
		return baos;
	}
	
	public static ByteArrayOutputStream getNetworkInfoStream()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try 
		{	
			boolean version = getNetworkInfo();
			String append = "WIFI" + " " + version;
			baos.write(append.getBytes());
			baos.flush();
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return baos;
	}

	@SuppressLint("ServiceCast")
	public static ByteArrayOutputStream getInfoStream() throws SecurityException, RemoteException, ClassCastException, NullPointerException
	{
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Iterator<RunningAppProcessInfo> iterator = getRunningAppProcessInfo().iterator();
        //IBinder b = ServiceManager.getService(WIFI_SERVICE);
        //IWifiManager service = IWifiManager.Stub.asInterface(b);
		ActivityManager manager = (ActivityManager) globalContext.getSystemService(Context.ACTIVITY_SERVICE);
        //List<WifiConfiguration> android = service.getConfiguredNetworks();
		//IWifiManager wifi = null;
		//wifi.getConnectionInfo();
		//WifiManager wf;
		
		//List<WifiConfiguration> android = wifi.getConfiguredNetworks();
		

		

		//manager.getRunningAppProcesses();

		try {
			
			while (iterator.hasNext())
			{
				RunningAppProcessInfo version = iterator.next();
				
				int numb = 0;
				Debug.MemoryInfo[] memoryInfos = manager.getProcessMemoryInfo(new int[]{version.pid});
				String str = version.processName + memoryInfos[numb].getTotalPss() + "\n";
				baos.write(str.getBytes());

				numb++;
				iterator.remove();
			}

				baos.flush();

		 }
		 catch (IOException e) {
			e.printStackTrace();
		 }
		
		return baos;
	}

}
