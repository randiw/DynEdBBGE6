package com.randi.dyned6.tools;

/**
 * A listener to notify progress.
 * @author Randi Waranugraha
 *
 */
public interface ProgressListener {

	/**
	 * Invoked when progress is complete.
	 * @param message Message posted when progress completed.
	 */
	public void onPost(String message);
	
	/**
	 * Invoked to measure progress.
	 * @param currentProgress
	 * @param totalProgress
	 */
	public void onProgress(int currentProgress, int totalProgress);
	
}