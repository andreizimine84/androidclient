package com.helloworld.testjni2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;

public class FileSender {

	String dirName = "tempDirectory";
	byte[] buffer = null;
	InputStream is = null;
	static Context globalContext;
	
	public static void main(Intent intent, Context context)
	{
		globalContext = context;
		connectAndSendHttp(intent);
	}
	
	public static void connectAndSendHttp(Intent intent){
		try {
			if(TempFolder.connectRWException == true){
				URL url;
		    	url = new URL("http://10.0.2.2:8080");
				
				String charset = "UTF-8";

				HttpURLConnection conn;
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Accept-Charset", charset);
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
				OutputStream output = conn.getOutputStream();

				output = completeLoadFile(output);
				System.out.println("logcat" + output.toString());
				output.close();
				conn.getInputStream();
				
				if(TempFolder.connectException == true)
				{
					OfflineHelpSender.main(intent, globalContext.getApplicationContext());
					TempFolder.connectException = false;
				}
				
				TempFolder.connectRWException = false;
			}

		}	catch (ConnectException e) {
			TempFolder.connectException = true;
    		e.printStackTrace();
    	}  catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static OutputStream completeLoadFile(OutputStream output) throws FileNotFoundException, IOException{
    	
    	if(TempFolder.mAddBackNumber == 1){
    		output.write(loadFile("output_temp" + TempFolder.mAddBackNumber + ".txt").toByteArray());
    		new File(globalContext.getFilesDir(),"output_temp" + TempFolder.mAddBackNumber+ ".txt").delete();
    	}
    	else{
    		TempFolder.mAddBackNumber--;
    		output.write(loadFile("output_temp" + TempFolder.mAddBackNumber + ".txt").toByteArray());
    		new File(globalContext.getFilesDir(),"output_temp" + TempFolder.mAddBackNumber+ ".txt").delete();
    	}
    	return output;
	}

	public static ByteArrayOutputStream loadFile(String inputFile) throws FileNotFoundException {
		String inputString;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	BufferedReader inputReader = null;
    	System.out.println("numberFile" + inputFile);
    	inputReader = new BufferedReader(new InputStreamReader(globalContext.openFileInput(inputFile)));
    	try{
	    	StringBuffer stringBuffer = new StringBuffer(); 
		    
		    while ((inputString = inputReader.readLine()) != null) {
		        stringBuffer.append(inputString);
		        baos.write(inputString.getBytes()); 
		    }
		    baos.flush(); 
	    }
        catch (IOException e) {
        	Log.e("tag", e.getMessage());
        }
	    return baos;
	}

	public ByteArrayOutputStream getStream (InputStream is) throws IOException
	{
		
		int size = is.available();
		byte[] query = new byte[size];
		
		int nRead;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {

			while ((nRead = is.read(query, 0, query.length)) != -1)
			{
				  baos.write(query, 0, nRead);
			}
				baos.flush();
		 }
		 catch (IOException e) {
			e.printStackTrace();
		 }
		
		return baos;
	}

	public List<RunningAppProcessInfo> getSystemService()
	{
		ActivityManager manager = (ActivityManager) globalContext.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningAppProcessInfo> android = manager.getRunningAppProcesses();
   
		return android;
	}

	public ByteArrayOutputStream getInfoStream()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Iterator<RunningAppProcessInfo> iterator = getSystemService().iterator();
		
		ActivityManager manager = (ActivityManager) globalContext.getSystemService(Context.ACTIVITY_SERVICE);
		
		try {
			
			while (iterator.hasNext())
			{
				RunningAppProcessInfo version = iterator.next();
				
				int numb = 0;
				
				Debug.MemoryInfo[] memoryInfos = manager.getProcessMemoryInfo(new int[]{version.pid});
				System.out.println(memoryInfos[numb].getTotalPss());
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
