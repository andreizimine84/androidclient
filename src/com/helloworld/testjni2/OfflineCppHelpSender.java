package com.helloworld.testjni2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OfflineCppHelpSender{
	
	static Context globalContext;
	private ByteArrayOutputStream baos = null;
	static TempFolder globalTempFolder;
	static int maxDataLength = 3000;
	
	static {
    	System.loadLibrary("JNIClient2");
    }

	public static native void connectToHostJNICPP(String port, String addr, String data, int maxDataLength);
		
	public static void main(Context context) throws IOException
	{
		globalContext = context;
		loadFile(globalContext.getFilesDir());
		deleteRecursive(globalContext.getFilesDir());
	}

	public static void deleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            deleteRecursive(child);
	    fileOrDirectory.delete();
	}
	
	@SuppressWarnings("static-access")
	public static void loadFile(File root) throws IOException{
    	final String addr = "10.0.2.2";
    	final String port = "8080";
		try {
			for (File child : root.listFiles()){
				System.out.println("Cpp3 Off");
				FileInputStream fis;
				String inputString;
				final ByteArrayOutputStream baos = new ByteArrayOutputStream(maxDataLength);
				if(root.isDirectory()){
					if(child.exists()){
						fis = globalContext.openFileInput(child.getName().toString());
						BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));
						try{
							StringBuffer stringBuffer = new StringBuffer(maxDataLength); 		
							while ((inputString = inputReader.readLine()) != null) {
								stringBuffer.append(inputString);
								baos.write(inputString.getBytes());
								inputString = null;
							}	
							connectToHostJNICPP(port, addr, baos.toString(), maxDataLength);		
							baos.flush();
						}
					    catch (IOException e) {
					    	Log.e("tag", e.getMessage());
					    } 
						finally{
					         if(baos!=null){
					        	 baos.flush();
					        	 baos.close();
					         } 
					         if(fis!=null){
					        	 //fis.reset();
					        	 fis.close();
					        	 fis = null;
					         }     	 
					    }	
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
}
