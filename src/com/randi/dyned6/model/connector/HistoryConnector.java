package com.randi.dyned6.model.connector;

import java.io.IOException;
import java.io.InputStream;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.ui.UiApplication;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.randi.dyned6.MainApp;
import com.randi.dyned6.model.LessonItem;
import com.randi.dyned6.model.Resources;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.model.persistable.NotificationManager;
import com.randi.dyned6.tools.Stream;
import com.randi.dyned6.view.HomeMenuScreen;

public class HistoryConnector extends UploadConnector {

	private EducationManager educationManager = EducationManager.getInstance();
	private NotificationManager notificationManager = NotificationManager.getInstance();
	
	public HistoryConnector(URLEncodedPostData postData) {
		super(Resources.API_NEW_HISTORY, postData);
	}

	public void onReceiveResponseEvent(InputStream is) {
		System.out.println("[HistoryConnector] onReceiveResponseEvent");
		String jsonString;
		try {
			jsonString = Stream.asString(is);
			System.out.println("[HistoryConnector] JSON: " + jsonString);
			JSONObject object = new JSONObject(jsonString);
			if(object != null){
				String status = object.getString("status");
				if("true".equals(status)){
					JSONObject data = object.getJSONObject("data");
					JSONArray records = data.getJSONArray("records");
					System.out.println("[HistoryConnector] JSON records: " + records.length());
					for(int i = 0; i < records.length(); i++){
						String unitId = educationManager.getUnitId(i);
						educationManager.startUnit(unitId);
						educationManager.justStartedUnit(unitId);
			
						JSONObject unit = records.getJSONObject(i);
						JSONArray lessons = unit.getJSONArray("lessons");
						System.out.println("[HistoryConnector] unit " + unitId + " lessons: " + records.length());
						for(int j = 0; j < lessons.length(); j++){
							String lessonId = Integer.toString(j);
							JSONObject lesson = lessons.getJSONObject(i);
							int content_attempted = getIntAfterNotNull(lesson, "content_attempted");
							int content_correct = getIntAfterNotNull(lesson, "content_correct");
							int grammar_attempted = getIntAfterNotNull(lesson, "grammar_attempted");
							int grammar_correct = getIntAfterNotNull(lesson, "grammar_correct");
							int listening_total = getIntAfterNotNull(lesson, "listening_total");
							
							LessonItem lessonItem = educationManager.getLesson(unitId, lessonId);
							lessonItem.unlock();
							lessonItem.completed();
							
							lessonItem.addListeningTime(listening_total);
							lessonItem.addContentAttempt(content_attempted);
							lessonItem.addContentCorrect(content_correct);
							lessonItem.addGrammarAttempt(grammar_attempted);
							lessonItem.addGrammarCorrect(grammar_correct);
							
							educationManager.commitLesson(unitId, lessonId, lessonItem);
							
							if((i > 0 && i == records.length() - 1) && !notificationManager.isCounting()){
								if(j == lessons.length() - 1){
									String nextUnitId = educationManager.getUnitId(i + 1);
									notificationManager.startCountNextUnit(nextUnitId);
								} else {
									int nextLesson = j + 1;
									String nextLessonId = Integer.toString(nextLesson);
									LessonItem nextLessonItem = educationManager.getLesson(unitId, nextLessonId);
									notificationManager.startCountNextLesson(unitId, nextLessonId, nextLessonItem);
								}
							}
						}
					}
					
					synchronized (UiApplication.getEventLock()) {
						MainApp.popAllScreen();
						UiApplication.getUiApplication().pushScreen(new HomeMenuScreen());						
					}
				} else {
					String message = object.getString("error");
					System.out.println("message error: " + message);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getIntAfterNotNull(JSONObject json, String label){
		try {
			int x = json.isNull(label) ? 0 : json.getInt(label);
			return x;
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}
}