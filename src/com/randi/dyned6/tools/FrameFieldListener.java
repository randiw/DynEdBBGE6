package com.randi.dyned6.tools;

import net.rim.device.api.ui.Manager;

/**
 * A listener to notify that the registered Frame needs to be changed.
 * @author Randi Waranugraha
 *
 */
public interface FrameFieldListener {

	/**
	 * Invoked when needs to change new Frame.
	 * @param manager FrameManager.
	 */
	public void onFrameChanged(Manager manager);
	public void changeTitle(String title);
}