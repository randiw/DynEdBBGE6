package com.randi.dyned6.view.manager;

import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;

import com.awan.dyned6.general.Constans;
import com.awan.dyned6.general.GeneralFunction;
import com.randi.dyned6.MainApp;
import com.randi.dyned6.model.GrammarManager;
import com.randi.dyned6.model.LessonItem;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.connector.StatisticConnector;
import com.randi.dyned6.model.persistable.AvatarManager;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.model.persistable.NotificationManager;
import com.randi.dyned6.model.persistable.SessionManager;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.view.AvatarScreen;
import com.randi.dyned6.view.HomeMenuScreen;
import com.randi.dyned6.view.field.CustomImageButtonField;
import com.randi.dyned6.view.field.GreyLabelField;

public class GrammarReviewManager extends ReviewManager {
	
	private CustomImageButtonField menuButton;
	
	private GrammarManager grammarManager = GrammarManager.getInstance();
	private EducationManager educationManager = EducationManager.getInstance();
	private NotificationManager notificationManager = NotificationManager.getInstance();
		
	private int unitIndex;
	private int lessonIndex;
	private String unitId;
	private String lessonId;
	
	public GrammarReviewManager(Vector vector) {
		super(vector);
		unitIndex = grammarManager.getUnitIndex();
		lessonIndex = grammarManager.getLessonIndex();
		unitId = educationManager.getUnitId(unitIndex);
		lessonId = Integer.toString(lessonIndex);
		
		educationManager.setGrammarPoint(unitId, lessonId, getCount(), getCorrectCount());
		
		FontSetting fontSetting = FontSetting.getInstance();
		fontSetting.setPoint(Font.PLAIN, 1);
		
		LabelField labelField = new LabelField("", FOCUSABLE);
		labelField.setFont(fontSetting.getFont());
		add(labelField);
		
		if(isDone()){
			if(Variables.bigTouchScreen()){
				GreyLabelField greyMenu = new GreyLabelField("Back to Home");
				greyMenu.setMargin(10, 30, 0, 30);
				greyMenu.setChangeListener(this);
				add(greyMenu);
			} else {
				menuButton = new CustomImageButtonField("menu.png", "menu_btn.png", FIELD_HCENTER);
				menuButton.setChangeListener(this);
				add(menuButton);				
			}

			educationManager.setLessonDone(unitId, lessonId);

			//opening next lesson
			int lessonCount = Constans.subCategory[unitIndex].length;
			if(unitIndex != 0){
				if(lessonIndex < lessonCount - 1){
					int nextLesson = lessonIndex + 1;
					String nextLessonId = Integer.toString(nextLesson);
					LessonItem lessonItem = educationManager.getLesson(unitId, nextLessonId);
					if(nextLesson == 3){ //unlock bonus lesson
						lessonItem.unlock();
					} else { //unlock normal lesson
						notificationManager.startCountNextLesson(unitId, nextLessonId, lessonItem);	
					}
				}
			}

			//opening next unit
			if(educationManager.countStar(educationManager.getUnitId(grammarManager.getUnitIndex())) >= 3) {
				int nextIndex = unitIndex + 1;
				if(nextIndex < Constans.mainScreenList.length){
					String nextUnitId = educationManager.getUnitId(nextIndex);
					if(!educationManager.contains(nextUnitId)){
						notificationManager.startCountNextUnit(nextUnitId);						
					}
				}
			}
		}

		if(Variables.bigTouchScreen()){
			GreyLabelField greyTry = new GreyLabelField("Try Again");
			greyTry.setMargin(10, 30, 0, 30);
			greyTry.setChangeListener(this);
			add(greyTry);
		} else {
			XYEdges edges = new XYEdges(10, 0, 0, 0);
			if(Variables.smallScreen()){
				edges.set(5, 0, 0, 0);
			}
			CustomImageButtonField tryButton = new CustomImageButtonField("tryagain.png", "tryagain_btn.png", FIELD_HCENTER);
			tryButton.setPadding(edges);
			tryButton.setChangeListener(this);
			add(tryButton);			
		}
	}
	
	protected void onDisplay() {
		if(isDone()){
			System.out.println("isDone");
			AvatarManager avatarManager = AvatarManager.getInstance();
			int unitIndex = grammarManager.getUnitIndex();
			int lessonIndex = grammarManager.getLessonIndex();
			if(avatarManager.checkPlay(unitIndex, lessonIndex, AvatarManager.AFTER)){
				String fileLocation = avatarManager.getFileLocation(unitIndex, lessonIndex);
				System.out.println("filelocation: " + fileLocation);
				if(fileLocation.startsWith("file:///")){
					if(GeneralFunction.checkIsFileExists(fileLocation)){
						UiApplication.getUiApplication().pushScreen(new AvatarScreen(fileLocation, unitIndex, lessonIndex));
					}
				} else {
					UiApplication.getUiApplication().pushScreen(new AvatarScreen(fileLocation, unitIndex, lessonIndex));										
				}
			}
		}
		statistic();
		super.onDisplay();
	}
	
	private void statistic(){
		SessionManager sessionManager = SessionManager.getInstance();
		
		URLEncodedPostData postData = new URLEncodedPostData(null, false);
		postData.append("app_key", sessionManager.getApp_key());
		postData.append("conversation", Variables.CONVERSATION_CODE);
		postData.append("unit", "U" + Integer.toString(unitIndex + 1));
		postData.append("lesson", getLessonCode(lessonId));
		postData.append("status", "completed");
		
		LessonItem item = educationManager.getLesson(unitId, lessonId);
		postData.append("unlocked_time", Long.toString(item.getUnlocked()));
		postData.append("opened_time", Long.toString(item.getFirstOpen()));
		postData.append("completed_time", Long.toString(item.getCompleted()));
		postData.append("listening_total", Integer.toString(item.getListeningTime()));
		postData.append("grammar_correct", Integer.toString(item.getGrammarCorrect()));
		postData.append("grammar_attempted", Integer.toString(item.getGrammarAttempt()));
		postData.append("content_attempted", Integer.toString(item.getContentAttempt()));
		postData.append("content_correct", Integer.toString(item.getContentCorrect()));

		StatisticConnector connector = new StatisticConnector(postData);
		connector.start();
	}
	
	private String getLessonCode(String lessonId){
		String code = "LBNS";
		if("0".equals(lessonId)){
			code = "LA";
		}
		if("1".equals(lessonId)){
			code = "LB";
		}
		if("2".equals(lessonId)){
			code = "LC";
		}
		return code;
	}
	
	public void fieldChanged(Field field, int context) {
		if(field instanceof CustomImageButtonField){
			synchronized (UiApplication.getEventLock()) {
				if(field == menuButton){
					if(notificationManager.isCounting()){
						Dialog.alert("Please wait to unlock next lesson");
					}
					MainApp.popAllScreen();
					UiApplication.getUiApplication().pushScreen(new HomeMenuScreen());
				} else {
					grammarManager.tryagain();
					grammarManager.nextManager();
				}
			}
		}
		if(field instanceof GreyLabelField){
			synchronized (UiApplication.getEventLock()) {
				GreyLabelField greyButton = (GreyLabelField) field;
				String label = greyButton.getLabel();
				if("Try Again".equals(label)){
					grammarManager.tryagain();
					grammarManager.nextManager();
				} else {
					if(notificationManager.isCounting()){
						Dialog.alert("Your next lesson will be available in 12 hours");
					}
					MainApp.popAllScreen();
					UiApplication.getUiApplication().pushScreen(new HomeMenuScreen());
				}
			}
		}
	}
}