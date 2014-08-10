package com.randi.dyned6.tools;

/**
 * A listener to notify SaveFileConnection status.
 * @author Randi Waranugraha
 *
 */
public interface SaveFileConnectionListener {

	/**
	 * Invoked when SaveFileConnection is completed.
	 */
	public void onDone();

	/**
	 * Invoked when error occured.
	 * @param message
	 */
	public void onError(String message);
	
}