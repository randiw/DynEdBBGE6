package com.randi.dyned6.tools;

import java.io.InputStream;

/**
 * A listener to notify InternetConnection status.
 * @author Randi Waranugraha
 *
 */
public interface InternetConnectionListener {

	/**
	 * Invoked when connection is a success and receive response.
	 * @param is InputStream.
	 */
	public void onReceiveResponseEvent(InputStream is);

	/**
	 * Invoked when error occurerd.
	 * @param e Exception thrown.
	 */
	public void onErrorOccurEvent(Exception e);

	/**
	 * Invoked when connection is canceled.
	 */
	public void onCancelEvent();

	/**
	 * Invoked before the connection.
	 * @param length
	 * @param type
	 */
	public void onStartEvent(long length, String type);

	/**
	 * Invoked when Not Found response.
	 */
	public void onNotFound();

	/**
	 * Invoked when connection redirected to the specified url. 
	 * @param url
	 */
	public void onRedirectEvent(String url);

}