package com.helloworld.testjni2;

import android.content.Context;

/**
 * 
 * @author andreizimine
 * Anv�nds f�r att l�sa av flaggor f�r t. ex systemfel eller andra variabelelement som klasser beh�ver f�r att 
 * f� ett direkt v�rde. Alternativt anv�ndning inf�r framtida utveckling �r Intent,s som g�r samma arbete.
 *
 */
public class TempFolder
{	
	static String folderUrl;
	static String nameOfFile;
	static int mAddNumber = 0;
	static int mAddBackNumber = 0;
	static int mCppMaxDataLength = 0;
	static boolean connectException = false;
	static boolean connectCppException = false;
	static boolean connectRWException = false;
	static boolean connectRWCppException = false;
	static boolean connectTemp = false;
	private static TempFolder instance = null;
	
	private native void accessFields();
	
	static {
		System.loadLibrary("JNIClient");
	}
	
	public static void main() {
	 	//TempFolder tf = new TempFolder();
	    //TempFolder.connectCppException = true;
	    //tf.accessFields();
	}

    public static int getAddNumber(){
    	return mAddNumber;
    }
    
    public static int getCppMaxDataLength(){
    	return mCppMaxDataLength;
    }
    
    public static TempFolder getInstance() {
        if (instance == null) {
            instance = new TempFolder();
        }
        return instance;
    }

	public static void setRWConnectException(boolean value){
		connectRWCppException = value;
	}
	
	public static boolean getRWConnectException(){
		return connectRWCppException;
	}
    
	public static void setCppConnectException(boolean value){
		connectCppException = value;
	}
	
	public static boolean getCppConnectException(){
		return connectCppException;
	}
	
	public static void setRWCppConnectException(boolean value){
		connectRWCppException = value;
	}
	
	public static boolean getRWCppConnectException(){
		return connectRWCppException;
	}
	
    public static void setAddBackNumber(int addBackNumber){
    	mAddBackNumber = addBackNumber;
    }
    
    public static int getAddBackNumber(){
    	return mAddBackNumber;
    }
    
    public static void setAddNumber(int addNumber){
    	mAddNumber = addNumber;
    }
    
	public static void setConnectException(boolean value){
		connectException = value;
	}
	
	public static boolean getConnectException(){
		return connectException;
	}

	public static String getFolder(){
		return folderUrl;
	}
	public static void setFolder(String newUrl){
		folderUrl = newUrl;
	}
	public static String getFile(){
		return nameOfFile;
	}
	
	public static void setFile(String newFile){
		nameOfFile = newFile;
	}
	
	public static void setCppMaxDataLength(int cppMaxDataLength){
		mCppMaxDataLength = cppMaxDataLength;
	}
}
