package com.randi.dyned6.model.persistable;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

import org.json.me.JSONObject;

import com.awan.dyned6.general.Constans;
import com.randi.dyned6.model.LessonItem;

/**
 * Keep record for Education progress.
 * @author Randi Waranugraha
 *
 */
public class EducationManager implements Persistable {

	private static final long KEY = 0xfd7dcba16d3956ccL; // com.randi.dyned6.model.persistable.EducationManager
	private static final String CURRENT_VERSION = "1";

	private static PersistentObject educationObject;	
	private static EducationManager instance = null;

	private Hashtable units;
	private String APPS_VERSION = "0";
	
	private EducationManager() {
		units = new Hashtable();
	}

	/**
	 * Retrieves the system's EducationManager instance.
	 * @return Instance of EducationManager.
	 */
	public static EducationManager getInstance() {
		if (instance == null) {
			educationObject = PersistentStore.getPersistentObject(KEY);
			if (educationObject.getContents() == null) {
				System.out.println("new object");
				instance = new EducationManager();
				synchronized (educationObject) {
					educationObject.setContents(instance);
				}
			} else {
				System.out.println("object exist");
				instance = (EducationManager) educationObject.getContents();
			}
		}
		instance.doUpdate();
		return instance;
	}

	private void doUpdate(){
		if((APPS_VERSION == null || APPS_VERSION.length() == 0) || APPS_VERSION != CURRENT_VERSION){
			init();
			APPS_VERSION = CURRENT_VERSION;
		}
	}

	private void init(){
		String unitId = getUnitId(0);
		startUnit(unitId);
		for(int i = 0; i <= 3; i++){
			LessonItem lessonItem = getLesson(unitId, Integer.toString(i));
			lessonItem.unlock();
			commitLesson(unitId, Integer.toString(i), lessonItem);
		}
	}
	
	/**
	 * Get a copy of the system EducationManager instance.
	 * @return A new copy instance of EducationManager.
	 */
	public static EducationManager backup(){
		EducationManager educationManager = new EducationManager();
		if(instance != null){
			educationManager.units = instance.units;			
		}
		return educationManager;
	}

	/**
	 * Set the system EducationManager instance.
	 * @return educationManager
	 */
	public static void set(EducationManager educationManager){
		instance = educationManager;
		educationObject = PersistentStore.getPersistentObject(KEY);
		synchronized (educationObject) {
			educationObject.setContents(instance);
		}
	}
	
	/**
	 * Set the indicated Unit to unlock.
	 * @param unitId Id of Unit.
	 */
	public void startUnit(String unitId) {
		if (!contains(unitId)) {
			Hashtable unit = new Hashtable();
			unit.put("justStart", "1");
			units.put(unitId, unit);
			educationObject.commit();
		}
	}

	/**
	 * Set the indicated Unit just started.
	 * @param Id of Unit.
	 * @return True if just started.
	 */
	public boolean justStartedUnit(String unitId) {
		Hashtable unit = (Hashtable) units.get(unitId);
		String justStart = (String) unit.get("justStart");
		if ("1".equals(justStart)) {
			unit.put("justStart", "0");
			units.put(unitId, unit);
			educationObject.commit();
			return true;
		}
		return false;
	}

	/**
	 * Determine if the indicated Unit just start.
	 * @param unitId Id of Unit.
	 * @return True if the indicated Unit just started.
	 */
	public boolean isJustStartUnit(String unitId) {
		if (contains(unitId)) {
			Hashtable unit = (Hashtable) units.get(unitId);
			String justStart = (String) unit.get("justStart");
			if ("1".equals(justStart)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if this instance contains Unit.
	 * @param unitId Id of Unit.
	 * @return True if this instance contains Unit.
	 */
	public boolean contains(String unitId) {
		return units.containsKey(unitId);
	}

	/**
	 * Get LessonItem with Unit and Lesson id.
	 * @param unitId Id of Unit.
	 * @param lessonId Id of Lesson.
	 * @return LessonItem
	 */
	public LessonItem getLesson(String unitId, String lessonId){
		Hashtable unit = null;
		if(units.containsKey(unitId)){
			unit = (Hashtable) units.get(unitId);			
		} else {
			unit = new Hashtable();
		}

		LessonItem lessonItem = null;
		if(unit.containsKey(lessonId)){
			lessonItem = (LessonItem) unit.get(lessonId);
		} else {
			lessonItem = new LessonItem(lessonId);
		}
		return lessonItem;
	}
	
	/**
	 * Update and store LessonItem to this instance.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 * @param lessonItem LessonItem.
	 */
	public void commitLesson(String unitId, String lessonId, LessonItem lessonItem){
		Hashtable unit = null;
		if(units.containsKey(unitId)){
			unit = (Hashtable) units.get(unitId);			
		} else {
			unit = new Hashtable();
		}

		unit.put(lessonId, lessonItem);
		units.put(unitId, unit);
		educationObject.commit();
	}
	
	/**
	 * Unlocks lesson.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 */
	public void unlockLesson(String unitId, String lessonId) {
		LessonItem lessonItem = getLesson(unitId, lessonId);
		lessonItem.unlock();
		
		commitLesson(unitId, lessonId, lessonItem);
	}
	
	/**
	 * Set Lesson done.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 */
	public void setLessonDone(String unitId, String lessonId) {
		LessonItem lessonItem = getLesson(unitId, lessonId);
		lessonItem.completed();
		
		commitLesson(unitId, lessonId, lessonItem);
	}
	
	/**
	 * Set Lesson open.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 */
	public void openLesson(String unitId, String lessonId) {
		LessonItem lessonItem = getLesson(unitId, lessonId);
		if(lessonItem.isFirstOpen()){
			lessonItem.firstOpen();
			commitLesson(unitId, lessonId, lessonItem);
		}
	}
	
	/**
	 * Adds the amount listening time for the indicated Lesson.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 * @param time Amount of listening time.
	 */
	public void addLessonListeningTime(String unitId, String lessonId, int time){
		LessonItem lessonItem = getLesson(unitId, lessonId);
		lessonItem.addListeningTime(time);
		commitLesson(unitId, lessonId, lessonItem);
	}
	
	/**
	 * Adds the attempted and correct answer of Comprehension for the indicated Lesson.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 * @param attempt Amount of attempt answers.
	 * @param correct Amount of correct answers.
	 */
	public void setComprehensionPoint(String unitId, String lessonId, int attempt, int correct){
		LessonItem lessonItem = getLesson(unitId, lessonId);
		lessonItem.addContentAttempt(attempt);
		lessonItem.addContentCorrect(correct);
		commitLesson(unitId, lessonId, lessonItem);
	}
	
	/**
	 * Adds the attempted and correct answer of Grammar for the indicated Lesson.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 * @param attempt Amount of attempt answers.
	 * @param correct Amount of correct answers.
	 */
	public void setGrammarPoint(String unitId, String lessonId, int attempt, int correct){
		LessonItem lessonItem = getLesson(unitId, lessonId);
		lessonItem.addGrammarAttempt(attempt);
		lessonItem.addGrammarCorrect(correct);
		commitLesson(unitId, lessonId, lessonItem);		
	}
	
	/**
	 * Get a JSONObject of the indicated LessonItem.
	 * @param unitId String id of Unit.
	 * @param lessonId String id of Lesson.
	 * @return JSONObject.
	 */
	public JSONObject getLessonJSON(String unitId, String lessonId){
		LessonItem lessonItem = getLesson(unitId, lessonId);
		return lessonItem.toJSON();
	}
	
	/**
	 * Get string id of Unit from Unit index.
	 * @param unitId Index of Unit.
	 * @return String id of Unit.
	 */
	public String getUnitId(int unitId){
		return Constans.mainScreenList[unitId];
	}
	
	/**
	 * Count completed lesson in this unit.
	 * @param unitId Index of Unit.
	 * @return Completed lesson.
	 */
	public int countStar(String unitId) {
		int count = 0;
		Hashtable unit = (Hashtable) units.get(unitId);
		Enumeration unitKeys = unit.keys();
		while (unitKeys.hasMoreElements()) {
			String key = (String) unitKeys.nextElement();
			if(!"justStart".equals(key)){
				LessonItem lessonItem = (LessonItem) unit.get(key);
				if (lessonItem.hasCompleted()) {
					count++;
				}
			}
		}
		if(count > 3){
			count = 3;
		}
		return count;
	}

	/**
	 * Clear this system's EducationManager instance.
	 */
	public static void clear() {
		educationObject.setContents(null);
		educationObject.commit();
		instance = null;
	}
}