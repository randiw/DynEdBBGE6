package com.awan.dyned6.general;
import net.rim.device.api.ui.Font;import net.rim.device.api.ui.Ui;

public class Constans {
	/**
	 * Main Screen Title
	 * */
	public static String mainHeaderTitle = "General English 6";
	public static String askToCreateDirectoryOnSdCard = "Application Config not exists, do you want to create itu now?";
	public static String waitingForDownloadWord = "Please wait while apps downloading the audio material...";
	/**
	 * Main Screen List
	 * */
	public static String[] mainScreenList =null;
	/**
	 * Main Question Sub Category
	 * */
	public static String[][] subCategory = null;
	/**
	 * Audio Lesson List
	 * */
	public static String[][] audioList = null; 
	
	/**
	 * Image Lesson List
	 * */
	public static String[][] imageList = null;
	
	/**
	 * view script list
	 * */
	public static String[][] scriptList=null;
	
	/**
	 * comprehension list<br/>
	 * String[mainlistitemindex][lessonindex][comprehensionindex]<br/>
	 * {
	 * 	{
	 * 		{type, question, option, option, option, answer},
	 * 		{type, question, option, option, option, answer},
	 * 		{type, question, option, option, option, answer}
	 * 	},
	 * 	{
	 * 		{type, question, option, option, option, answer},
	 * 		{type, question, option, option, option, answer},
	 * 		{type, question, option, option, option, answer}
	 * 	}
	 * }
	 * {type, question, option, option, option, answer}
	 * */
	public static String[][][][] comprehensionItem=null;
	public static String[][][][] grammarItem=null;
	
	public static String compTextOnly = "textonly";
	public static String compAudioAndText = "audioandtext";
	public static String grammarTextOnly = "textonly";
	public static String grammarSentencebuilder = "sentencebuilder";
	public static Font font = Font.getDefault().derive(Font.PLAIN, 9,Ui.UNITS_pt);
    public static Font smallFont = Font.getDefault().derive(Font.PLAIN, 6, Ui.UNITS_pt);
    public static String getNetworkTimeout(){ return "60000"; }
}