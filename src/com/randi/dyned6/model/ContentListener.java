package com.randi.dyned6.model;

/**
 * A listener to notify when done content processing.
 * @author Randi Waranugraha
 *
 */
public interface ContentListener {

	/**
	 * Invoked when done content processing.
	 * @param label 
	 */
	public void onFinishTask(String label);
	
	/**
	 * Invoked when error occured during content processing.
	 * @param label
	 */
	public void onErrorTask(String label);

}