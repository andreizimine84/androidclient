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

public class OfflineHelpSender {
	static Context globalContext;
	
	public static void main(Intent intent, Context context)
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

	public static ByteArrayOutputStream loadFile(File root){
		String inputString;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			for (File child : root.listFiles()){
				if(root.isDirectory()){
					if(child.exists()){
						FileInputStream fis;
						fis = globalContext.openFileInput(child.getName().toString());
						BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));
						try{
							StringBuffer stringBuffer = new StringBuffer(); 		
							while ((inputString = inputReader.readLine()) != null) {
								stringBuffer.append(inputString);
								baos.write(inputString.getBytes()); 
							}
							baos.flush();

							connectAndSendHttp(baos);
						}
					    catch (IOException e) {
					    	Log.e("tag", e.getMessage());
					    }
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return baos;
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
