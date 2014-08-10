package com.randi.dyned6.model.persistable;

import com.randi.dyned6.model.LessonItem;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.DateTimeUtilities;
import net.rim.device.api.util.Persistable;

public class NotificationManager implements Persistable {

	private static final long KEY = 0xd1377c20c18b079fL; //com.randi.dyned6.model.persistable.NotificationManager
	
	private static NotificationManager instance = null;
	private static PersistentObject notificationObject;
	
	private long nextUpdate;
	private LessonItem nextLesson;
	private String unitId;
	private String nextLessonId;
	private String nextUnitId;
	
	private boolean counting;
	private boolean notified;
	private boolean newItem;
	
	private NotificationManager() {
	}
	
	public static NotificationManager getInstance() {
		if(instance == null){
			notificationObject = PersistentStore.getPersistentObject(KEY);
			if(notificationObject.getContents() == null){
				instance = new NotificationManager();
				synchronized (notificationObject) {
					notificationObject.setContents(instance);
				}
			} else {
				instance = (NotificationManager) notificationObject.getContents();
			}
		}
		return instance;
	}
	
	public static void clear(){
		notificationObject.setContents(null);
		notificationObject.commit();
		instance = null;
	}

	public void startCountNextLesson(String unitId, String nextLessonId, LessonItem lessonItem){
		System.out.println("[NotificationManager] starting count next lesson");
		this.unitId = unitId;
		this.nextLessonId = nextLessonId;
		nextLesson = lessonItem;
		startCounting();
	}
	
	public void startCountNextUnit(String nextUnitId){
		System.out.println("[NotificationManager] starting count next unit");
		this.nextUnitId = nextUnitId;
		startCounting();
	}

	private void startCounting(){
		nextUpdate = System.currentTimeMillis() + (12 * DateTimeUtilities.ONEHOUR);
//		nextUpdate = System.currentTimeMillis() + (1 * DateTimeUtilities.ONEMINUTE);
		counting = true;
		notified = false;
		newItem = false;

		notificationObject.commit();

		System.out.println("[NotificationManager] now is " + System.currentTimeMillis());
		System.out.println("[NotificationManager] next update is " + nextUpdate);
	}
	
	public void setCounting(boolean counting) {
		this.counting = counting;
		notificationObject.commit();
	}
	
	public void setNotified(boolean notified) {
		this.notified = notified;
		notificationObject.commit();
	}
		
	public void setNewItem(boolean newItem) {
		this.newItem = newItem;
		notificationObject.commit();
	}
	
	public long getNextUpdate() {
		return nextUpdate;
	}
	
	public LessonItem getNextLesson() {
		return nextLesson;
	}
	
	public String getUnitId() {
		return unitId;
	}
	
	public String getNextLessonId() {
		return nextLessonId;
	}
	
	public String getNextUnitId() {
		return nextUnitId;
	}
	
	public boolean isCounting() {
		return counting;
	}
	
	public boolean isNotified() {
		return notified;
	}
		
	public boolean isNewItem() {
		return newItem;
	}
}