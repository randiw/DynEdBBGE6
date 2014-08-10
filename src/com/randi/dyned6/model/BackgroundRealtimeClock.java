package com.randi.dyned6.model;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.RealtimeClockListener;

import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.model.persistable.NotificationManager;

public class BackgroundRealtimeClock implements RealtimeClockListener {
	
	NotificationManager notificationManager = NotificationManager.getInstance();
	
	public void clockUpdated() {
		if(notificationManager.isCounting() && !notificationManager.isNotified()){
			if(System.currentTimeMillis() > notificationManager.getNextUpdate()){
				notifyIcon();
			} 		
		}
	}

	private void notifyIcon() {
		//start next lesson
		EducationManager educationManager = EducationManager.getInstance();
		if(notificationManager.getNextLesson() != null){
			String unitId = notificationManager.getUnitId();
			String lessonId = notificationManager.getNextLessonId();
			LessonItem lessonItem = notificationManager.getNextLesson();
			lessonItem.unlock();
			educationManager.commitLesson(unitId, lessonId, lessonItem);
		}
		
		//start next unit
		if(notificationManager.getNextUnitId() != null && notificationManager.getNextUnitId().length() > 0){
			educationManager.startUnit(notificationManager.getNextUnitId());
		}

		notificationManager.setCounting(false);
		notificationManager.setNotified(true);
		notificationManager.setNewItem(true);

		//set icon notify here
		EncodedImage icon_indicator = EncodedImage.getEncodedImageResource("icon_indicator_notification.png");
		ApplicationIcon applicationIcon = new ApplicationIcon(icon_indicator);
		Bitmap homeIcon = Bitmap.getBitmapResource("icon_notification.png");
		
		try {
			ApplicationIndicatorRegistry indicatorRegistry = ApplicationIndicatorRegistry.getInstance();
			indicatorRegistry.register(applicationIcon, true, true);
			
			HomeScreen.updateIcon(homeIcon, 0);
		} catch (Exception e) {
		}
	}
}