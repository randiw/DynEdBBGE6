package com.randi.dyned6.model.connector;

import java.io.InputStream;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import com.randi.dyned6.tools.InternetConnection;
import com.randi.dyned6.tools.InternetConnectionListener;

/**
 * Abstract thread class to open httpconnection using POST method.
 * @author Randi Waranugraha
 *
 */
public abstract class UploadConnector extends Thread implements Runnable, InternetConnectionListener {

	private URLEncodedPostData postData;
	protected InternetConnection internetConnection;
	private String url;
	
	/**
	 * Creates new UploadConnector object with url and post data.
	 * @param url Target url.
	 * @param postData Post data; parameter and value pair.
	 */
	public UploadConnector(String url, URLEncodedPostData postData) {
		this.url = url;
		this.postData = postData;
		internetConnection = new InternetConnection(this);
	}
	
	public void run() {
		internetConnection.uploadParameter(url, postData);
	}
	
	abstract public void onReceiveResponseEvent(InputStream is);

	public void onErrorOccurEvent(Exception e) {
		// TODO Auto-generated method stub
		System.out.println("onErrorOccurEvent");
	}

	public void onCancelEvent() {
		// TODO Auto-generated method stub
		System.out.println("onCancelEvent");
	}

	public void onStartEvent(long length, String type) {
		// TODO Auto-generated method stub
		System.out.println("onStartEvent");
	}

	public void onNotFound() {
		// TODO Auto-generated method stub
		System.out.println("onNotFound");
	}

	public void onRedirectEvent(String url) {
		// TODO Auto-generated method stub
		System.out.println("onRedirectEvent");
	}
}