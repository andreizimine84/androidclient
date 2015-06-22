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

public class OfflineHelpSender{
	static Context globalContext;
	private ByteArrayOutputStream baos = null;
	static int maxDataLength = 3000;
	private static boolean JNI = true;
	
	static {
    	System.loadLibrary("TestJNI2");
    }
	
	public static void main(Context context) throws IOException
	{
		globalContext = context;
		if(!JNI){
			loadFile(globalContext.getFilesDir());
			deleteRecursive(globalContext.getFilesDir());
		}
		else if(JNI){
			OfflineCppHelpSender.main(globalContext);
		}
	}

	public static void deleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            deleteRecursive(child);

	    fileOrDirectory.delete();
	}

	public static void loadFile(File root) throws IOException{
		try {
			for (File child : root.listFiles()){
				FileInputStream fis = null;
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
							connectAndSendHttp(baos);
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
						/*catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static void connectAndSendHttp(ByteArrayOutputStream baos){
		try {
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
	    	output.write(baos.toByteArray());
	    	output.close();
	    	conn.getInputStream();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
