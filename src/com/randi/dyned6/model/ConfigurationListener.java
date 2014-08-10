package com.randi.dyned6.model;

/**
 * A listener to notify when ConfigurationFactory done parsing.
 * @author Randi Waranugraha
 *
 */
public interface ConfigurationListener {

	/**
	 * Invoked when error occured during parsing.
	 * @param message Error message
	 */
	public void onError(String message);

	/**
	 * Invoked when done parsing.
	 */
	public void onDone();	

}