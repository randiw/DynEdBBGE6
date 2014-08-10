package com.randi.dyned6.model;

import java.util.Random;

import net.rim.device.api.system.Display;

/**
 * Configuration variables.
 * @author Randi Waranugraha
 *
 */
public class Variables {

	public static final String CONVERSATION_CODE = "GE6";
	public static final String APP_NAME = "General English 6";
	public static final String APP_ID = "100006";

	public static final String SPLASH_NAME = "General English / 6";
	public static final String SPLASH_TITLE = "CONVERSATIONS";
	public static final String SPLASH_SUB = "mobile.dyned.com";
	
	public static final String LOGIN_NAME = "E-mail";

	public static final String API_KEY = "286e88a65df98fd67352a448071cfc8b";

	private static Random generator = new Random();
	
	public static int generateRandom(int min, int max){
		int v = generator.nextInt();
		int randomNumber = min + Math.abs(v) % (max - min);
		return randomNumber;
	}

	/**
	 * Determine if mobile device is small screen (width: 320 and height: 240) usually BlackBerry Gemini size.
	 * @return True if small screen.
	 */
	public static boolean smallScreen(){
		if(Display.getWidth() == 320 && Display.getHeight() == 240){
			return true;
		}
		return false;
	}
	
	/**
	 * Determine if mobile device is big and touchscreen (width: 480 and height: 800) usually BlackBerry Monza size.
	 * @return True if big and touchscreen.
	 */
	public static boolean bigTouchScreen(){
		if(Display.getWidth() == 480 && Display.getHeight() == 800){
			return true;
		}
		return false;
	}
}