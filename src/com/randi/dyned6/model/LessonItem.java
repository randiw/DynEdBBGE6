package com.randi.dyned6.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

/**
 * A class that represent object of Lesson.
 * @author Randi Waranugraha
 *
 */
public class LessonItem implements Persistable {

	private String id;

	private long unlock;
	private long firstOpen;
	private long completed;
	
	private int listeningTime;
	
	private int grammarAttempt;
	private int grammarCorrect;
	
	private int contentAttempt;
	private int contentCorrect;
	
	/**
	 * Creates new LessonItem object with id.
	 * @param id
	 */
	public LessonItem(String id) {
		this.id = id;
	}

	/**
	 * Gets the id of this LessonItem object.
	 * @return id string
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Unlock this LessonItem object.
	 */
	public void unlock(){
		unlock = System.currentTimeMillis();
	}

	/**
	 * Determines if this lesson has been unlocked.
	 * @return True if this object has been unlocked; otherwise, false.
	 */
	public boolean isUnlock() {
		return unlock > 0;
	}

	/**
	 * Determines if this lesson is currently open for the first time.
	 * @return True if this is first time open.
	 */
	public boolean isFirstOpen(){
		return firstOpen == 0;
	}
	
	/**
	 * Set to indicate that this lesson is currently being open for the first time.
	 */
	public void firstOpen() {
		firstOpen = System.currentTimeMillis();
	}
	
	/**
	 * Set to indicate that this lesson has been completed.
	 */
	public void completed(){
		completed = System.currentTimeMillis();
	}
	
	/**
	 * Gets the timestamp when this lesson has been unlocked.
	 * @return Unlocked timestamp
	 */
	public long getUnlocked() {
		return unlock;
	}
	
	/**
	 * Gets the timestamp when this lesson was open for the first time.
	 * @return First open timestamp
	 */
	public long getFirstOpen() {
		return firstOpen;
	}
	
	/**
	 * Gets the timestamp when this lesson was completed.
	 * @return Completed timestamp
	 */
	public long getCompleted() {
		return completed;
	}

	/**
	 * Determines if this lesson has been completed.
	 * @return True if this lesson has been completed.
	 */
	public boolean hasCompleted(){
		return completed != 0;
	}
	
	/**
	 * Adds the amount time for listening.
	 * @param time Amount time (in seconds)
	 */
	public void addListeningTime(int time){
		listeningTime = listeningTime + time;
	}

	/**
	 * Gets the amount time for listening.
	 * @return Amount time (in seconds)
	 */
	public int getListeningTime() {
		return listeningTime;
	}
	
	/**
	 * Adds the amount of attempt for grammar questions.
	 * @param attempt Amount of attempt.
	 */
	public void addGrammarAttempt(int attempt){
		grammarAttempt = grammarAttempt + attempt;
	}
	
	/**
	 * Adds the amount of correct answer for grammar questions.
	 * @param correct Amount of correct answers.
	 */
	public void addGrammarCorrect(int correct){
		grammarCorrect = grammarCorrect + correct;
	}
	
	/**
	 * Adds the amount of attempt for comprehension questions.
	 * @param attempt Amount of attempt.
	 */
	public void addContentAttempt(int attempt){
		contentAttempt = contentAttempt + attempt;
	}
	
	/**
	 * Adds the amount of correct answer for comprehension questions.
	 * @param correct Amount of correct answers.
	 */
	public void addContentCorrect(int correct){
		contentCorrect = correct;
	}
	
	/**
	 * Gets the amount of attempt for grammar questions.
	 * @return Amount of attempt.
	 */
	public int getGrammarAttempt() {
		return grammarAttempt;
	}
	
	/**
	 * Gets the amount of correct answer for grammar questions.
	 * @return Amount of correct answers.
	 */
	public int getGrammarCorrect() {
		return grammarCorrect;
	}
	
	/**
	 * Gets the amount of attempt for comprehension questions.
	 * @return Amount of attempt.
	 */
	public int getContentAttempt() {
		return contentAttempt;
	}
	
	/**
	 * Gets the amount of correct answer for comprehension questions.
	 * @return Amount of correct answers.
	 */
	public int getContentCorrect() {
		return contentCorrect;
	}
	
	/**
	 * Make a JSONObject of this LessonItem object.
	 * @return JSONObject of this LessonItem object.
	 */
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("active", "1");
			json.put("unlocked", unlock);
			json.put("first_open", firstOpen);
			json.put("completed", completed);
			json.put("status", "3");
			json.put("listening_time", listeningTime);
			json.put("grammar_attempted", grammarAttempt);
			json.put("grammar_correct", grammarCorrect);
			json.put("content_attempted", contentAttempt);
			json.put("content_correct", contentCorrect);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * Make a JSON text of this LessonItem object.
	 * @return JSON text.
	 */
	public String toJSONString(){
		return toJSON().toString();
	}
}