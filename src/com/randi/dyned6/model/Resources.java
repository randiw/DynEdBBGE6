package com.randi.dyned6.model;

/**
 * Resource class for API's url and file location.
 * @author Randi Waranugraha
 *
 */
public class Resources {

//	public static final String BASEURL = "http://mdc.pistarlabs.net/0.1.0/index.php";
	public static final String BASEURL = "https://mobile.dyned.com/index.php";
	public static final String API_NEW_REGISTER = BASEURL + "/api/oauth/register";
	public static final String API_NEW_LOGIN = BASEURL + "/api/oauth/login";
	public static final String API_NEW_UPDATE = BASEURL + "/api/conversation/update";
	public static final String API_NEW_HISTORY = BASEURL + "/api/conversation/history";
	
//	public static final String FILES = "http://pistarlabs.net/projects/dyned/apps/files/";
	public static final String FILES = "https://mobile.dyned.com/conv/bb/";
	public static final String FILES_AUDIO = FILES + "audio/level-6/";
	public static final String FILES_AUDIO_DIALOGUES = FILES_AUDIO + "dialogues/";
	public static final String FILES_AUDIO_QUESTIONS = FILES_AUDIO + "questions/";
	public static final String FILES_AVATAR_EN = FILES + "avatar/id/";
	
	public static final String PATH = "file:///SDCard/dyned/";
	public static final String PATH_AUDIO = PATH + "audio/";
	public static final String PATH_AUDIO_LEVEL = PATH_AUDIO + "level-6/";
	public static final String PATH_AUDIO_DIALOGUES = PATH_AUDIO_LEVEL + "dialogues/";
	public static final String PATH_AUDIO_QUESTIONS = PATH_AUDIO_LEVEL + "questions/";
	public static final String PATH_AVATAR = PATH + "avatar/";
	public static final String PATH_AVATAR_EN = PATH_AVATAR + "id/";
}