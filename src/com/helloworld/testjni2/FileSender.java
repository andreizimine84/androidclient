package com.helloworld.testjni2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

public class FileSender {
	Context globalContext;
	public boolean connectException = false;
	
	public void main(Context context) throws IOException {
		globalContext = context;
		loadFile(globalContext.getFilesDir());
		if(connectException == false)
			deleteRecursive(globalContext.getFilesDir());
	}

	public void deleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);
		fileOrDirectory.delete();
	}

	public void loadFile(File root) throws IOException {
		try {
			for (File child : root.listFiles()) {
				FileInputStream fis = null;
				String inputString;
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if (root.isDirectory()) {
					if (child.exists()) {
						String fileName = child.getName();
						String fileNameParts[] = fileName.substring(0, fileName.length()-4).split("_");
						String sha1 = fileNameParts[3];
						fis = globalContext.openFileInput(child.getName().toString());
						BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));
						try {
							while ((inputString = inputReader.readLine()) != null) {
								baos.write(inputString.getBytes());
							}
							connectAndSendHttp(baos, sha1);
						} catch (IOException e) {
							Log.e("tag", e.getMessage());
						} finally {
							if (baos != null) {
								baos.flush();
								baos.close();
							}
							if (fis != null) {
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
// ConnectException behövs inte för att den läser alltid
	public void connectAndSendHttp(ByteArrayOutputStream baos, String sha1) {
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
			conn.setRequestProperty("X-checksum", sha1);
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			OutputStream output = conn.getOutputStream();
			output.write(baos.toByteArray());
			output.close();
			conn.getInputStream();
		}
		catch (ConnectException e) {
			connectException = true;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
