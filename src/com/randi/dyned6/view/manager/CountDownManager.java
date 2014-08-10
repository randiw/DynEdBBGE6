package com.randi.dyned6.view.manager;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.util.DateTimeUtilities;

import com.randi.dyned6.model.ContentListener;
import com.randi.dyned6.model.LessonItem;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.model.persistable.NotificationManager;
import com.randi.dyned6.tools.FontSetting;

public class CountDownManager extends Manager {
	
	private LabelField desc;
	private LabelField time;
	
	private Timer timer;
	
	private long delta;
	
	private ContentListener listener;
	
	private NotificationManager notificationManager = NotificationManager.getInstance();
	private FontSetting fontSetting = FontSetting.getInstance();
	
	public CountDownManager() {
		super(USE_ALL_WIDTH);
		setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));

		delta = notificationManager.getNextUpdate() - System.currentTimeMillis();
		
		desc = new LabelField("Time remaining until next lesson:"){
			protected void paint(Graphics g) {
				g.setColor(Color.WHITE);
				super.paint(g);
			};
		};
		fontSetting.setPoint(Font.PLAIN, 4);
		desc.setFont(fontSetting.getFont());
		add(desc);
		
		time = new LabelField(timeFormat(delta)){
			protected void paint(Graphics g) {
				g.setColor(Color.RED);
				super.paint(g);
			};
		};
		time.setFont(fontSetting.getFont());
		add(time);
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new CountDown(), 1000, 1000);
	}

	public void setListener(ContentListener listener) {
		this.listener = listener;
	}
	
	protected void sublayout(int width, int height) {
		if(time != null){
			layoutChild(desc, width, height);
			layoutChild(time, width - desc.getWidth(), height);
			setPositionChild(desc, 0, 0);
			setPositionChild(time, width - time.getWidth(), 0);
		} else {
			layoutChild(desc, width, height);
			setPositionChild(desc, (width - desc.getWidth()) / 2, 0);
		}
		setExtent(width, desc.getHeight());
	}

	private String zeroFill(int time){
		if(time > 9){
			return Integer.toString(time);
		} else {
			return "0" + time;
		}
	}

	private String timeFormat(long time){
		int hour = (int) (time / DateTimeUtilities.ONEHOUR);

		time = time % DateTimeUtilities.ONEHOUR;
		int minute = (int) (time / DateTimeUtilities.ONEMINUTE);
		
		time = time % DateTimeUtilities.ONEMINUTE;
		int seconds = (int) (time / DateTimeUtilities.ONESECOND);
		
		return zeroFill(hour) + ":" + zeroFill(minute) + ":" + zeroFill(seconds);
	}
	
	private class CountDown extends TimerTask {
		public void run() {
			UiApplication.getUiApplication().invokeLater(new Runnable() {				
				public void run() {
					if(delta > 1000){
						delta = delta - 1000;
						time.setText(timeFormat(delta));
					} else {
						time.setText(timeFormat(0));
						timer.cancel();
						
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
						
						Dialog.alert("New lesson is available");
						listener.onFinishTask("done");
					}
				}
			});
		}
	}
}