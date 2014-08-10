package com.randi.dyned6.model;

import java.io.InputStream;

import com.awan.dyned6.general.GeneralFunction;
import com.awan.dyned6.utils.Utils;
import com.randi.dyned6.tools.InternetConnection;
import com.randi.dyned6.tools.InternetConnectionListener;
import com.randi.dyned6.tools.SaveFileConnection;
import com.randi.dyned6.tools.SaveFileConnectionListener;
import com.randi.dyned6.tools.ThreadAttendees;
import com.randi.dyned6.tools.ThreadManager;

/**
 * A thread class that manage the sequence of downloading files and save it to device internal memories.
 * @author Randi Waranugraha
 *
 */
public class DownloadAndSaveFile extends Thread implements ThreadAttendees, InternetConnectionListener, SaveFileConnectionListener {
	
	private String fileLocation;
	private String downloadUrl;
	private String threadKey;
	private String label;
	
	private InternetConnection internetConnection;
	private ContentListener contentListener;
	
	private ThreadManager threadManager = ThreadManager.getInstance();

	/**
	 * Creates new object to download with file location and download url.
	 * @param fileLocation target file location
	 * @param downloadUrl target download url
	 */
	public DownloadAndSaveFile(String fileLocation, String downloadUrl) {
		this.fileLocation = fileLocation;
		this.downloadUrl = downloadUrl;

		internetConnection = new InternetConnection(this);		
	}

	/**
	 * Sets the ContentListener to this object.
	 * @param contentListener
	 */
	public void setContentListener(ContentListener contentListener) {
		this.contentListener = contentListener;
	}
	
	/**
	 * Sets the label string for this object.
	 * @param label 
	 */
	public void setLabel(String label){
		this.label = label;
	}
	
	public void run() {
		internetConnection.accessURL(downloadUrl);
		super.run();
	}

	public void start(String key) {
		threadKey = key;
		if(!GeneralFunction.checkIsFileExists(fileLocation)){			
			start();
		} else {
			onDone();
		}
	}

	public void onReceiveResponseEvent(InputStream is) {
		String stringFile = Utils.parseInputStreamToString(is);
		byte[] data = stringFile.getBytes();

		SaveFileConnection saveFileConnection = new SaveFileConnection(this);
		saveFileConnection.saveFile(fileLocation, data);
	}

	public void onErrorOccurEvent(Exception e) {
		System.out.println("error occur" + e.getMessage());
		if(threadKey != null){
			threadManager.notify(threadKey);			
		}
		if(contentListener != null){
			contentListener.onErrorTask(label);
		}
		if(isAlive()){
			interrupt();
		}		
	}

	public void onCancelEvent() {
		// TODO Auto-generated method stub
		System.out.println("cancel");				
	}

	public void onStartEvent(long length, String type) {
		// TODO Auto-generated method stub
		System.out.println("onStart");						
	}

	public void onNotFound() {
		System.out.println("onNotFound");								
		if(threadKey != null){
			threadManager.notify(threadKey);			
		}
		if(contentListener != null){
			contentListener.onErrorTask(label);
		}
		if(isAlive()){
			interrupt();
		}		
	}

	public void onRedirectEvent(String url) {
		// TODO Auto-generated method stub
		System.out.println("redirect " + url);										
	}

	public void onDone() {
		System.out.println("onDone");
		if(threadKey != null){
			threadManager.notify(threadKey);			
		}
		if(contentListener != null){
			contentListener.onFinishTask(label);
		}
		if(isAlive()){
			interrupt();
		}		
	}

	public void onError(String message) {
		// TODO Auto-generated method stub
		System.out.println("onError " + message);										
	}
}