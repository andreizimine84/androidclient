package com.helloworld.testjni2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

public class JNISender {
	
	static Context globalContext;
	final String addr = "10.0.2.2";
	final String port = "8080";
	static int maxDataLength = 3000;
	static boolean check = false;
	static boolean connectCppException = false;
	
	static {
    	System.loadLibrary("TestJNI2");
    }
	
	public static native boolean connectToHostJNICPP(String port, String addr, String data, int maxDataLength);
	// Egen class för TempFolder
	// TempFolder initieras hur?
	@SuppressWarnings("static-access")
	public static void main(Intent intent, Context context){
		globalContext = context; 
		boolean check = false;
		try {
			System.out.println("Cpp 1");
			TempFolder.connectRWCppException = true;
			System.out.println("check" + check);
			check = connectCppAndSend();
			System.out.println("check" + check);
			if(check == true){
				if(connectCppException == true)
				{
					System.out.println("check 777");
					OfflineHelpSender.main(globalContext.getApplicationContext());
					connectCppException = false;
				}
			}
			System.out.println("Cpp 2");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@SuppressWarnings("static-access")
	public static boolean connectCppAndSend() throws IOException, SecurityException, RemoteException, ClassCastException, NullPointerException{
		boolean check = false;
		if(TempFolder.connectRWCppException == true){
			System.out.println("Cpp 4");
			final String addr = "10.0.2.2";
			final String port = "8080";	
			check = connectToHostJNICPP(port, addr, completeLoadFile().toString(), maxDataLength);
			System.out.println("check777" + check + connectCppException);
			TempFolder.connectRWCppException = false;
		}
		return check;
	}
	
	public static ByteArrayOutputStream completeLoadFile() throws FileNotFoundException, IOException{   	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(TempFolder.mAddBackNumber == 1){
    		baos.write(loadFile("output_temp" + TempFolder.mAddBackNumber + ".txt").toByteArray());
    		new File(globalContext.getFilesDir(),"output_temp" + TempFolder.mAddBackNumber + ".txt").delete();
    	}
    	else{
    		TempFolder.mAddBackNumber--;
    		baos.write(loadFile("output_temp" + TempFolder.mAddBackNumber + ".txt").toByteArray());
    		new File(globalContext.getFilesDir(),"output_temp" + TempFolder.mAddBackNumber + ".txt").delete();
    	}
    	return baos;
	}

	public static ByteArrayOutputStream loadFile(String inputFile) throws FileNotFoundException {
		String inputString;
		ByteArrayOutputStream baos = new ByteArrayOutputStream(maxDataLength);
    	BufferedReader inputReader = null;
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
}
