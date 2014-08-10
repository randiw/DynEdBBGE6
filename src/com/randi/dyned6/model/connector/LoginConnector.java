package com.randi.dyned6.model.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.randi.dyned6.model.Resources;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.persistable.LoginHistoryManager;
import com.randi.dyned6.model.persistable.SessionManager;
import com.randi.dyned6.tools.Stream;
import com.randi.dyned6.view.LoadingScreen;

/**
 * Open connection for Login.
 * @author Randi Waranugraha
 *
 */
public class LoginConnector extends UploadConnector {

	private String errorMessage;
	private String username;
	private String password;
	
	/**
	 * Creates a LoginConnector object with postData.
	 * @param postData Post data; parameter and value pair.
	 */
	public LoginConnector(URLEncodedPostData postData) {
		super(Resources.API_NEW_LOGIN, postData);
		internetConnection.addHeader("X-APP-VERSION", ApplicationDescriptor.currentApplicationDescriptor().getVersion());
		internetConnection.addHeader("X-APP-NAME", Variables.APP_NAME);
		internetConnection.addHeader("X-DEVICE-MODEL", DeviceInfo.getDeviceName());
		internetConnection.addHeader("X-DEVICE-OS", DeviceInfo.getSoftwareVersion());
	}
	
	/**
	 * Sets username being used for login.
	 * @param username Username string.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Sets password being used for login.
	 * @param password Password string.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void onReceiveResponseEvent(InputStream is) {
		synchronized (UiApplication.getEventLock()) {
			try {
				String jsonString = Stream.asString(is);
				System.out.println("[LoginConnector] jsonString: " + jsonString);
				JSONObject object = new JSONObject(jsonString);
				String status = object.getString("status");
				String message = null;
				if("true".equals(status)){
					message = object.getString("message");
					
					JSONObject data = object.getJSONObject("data");
					
					String app_key = data.getString("app_key");
					String role = data.getString("role");
					
					JSONObject profile = data.getJSONObject("profile");
					Hashtable tableProfile = new Hashtable();
					Enumeration keys = profile.keys();
					while(keys.hasMoreElements()){
						String key = (String)keys.nextElement();
						String value = null;
						if("id".equals(key)){
							value = Integer.toString(profile.getInt(key));
						} else {
							value = (String)profile.get(key);								
						}
						tableProfile.put(key, value);
					}
										
					LoginHistoryManager historyManager = LoginHistoryManager.getInstance();
					if(!historyManager.isUser(username)){
						historyManager.addNewUser(username, password);
					} 
					
					SessionManager session = SessionManager.getInstance();
					session.setApp_key(app_key);
					session.setRole(role);
					session.setId((String)tableProfile.get("id"));
					session.setUsername(username);
					session.setTablePofile(tableProfile);

					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
					URLEncodedPostData postData = new URLEncodedPostData(null, false);
					postData.append("conversation", Variables.CONVERSATION_CODE);
					postData.append("app_key", session.getApp_key());

					HistoryConnector historyConnector = new HistoryConnector(postData);
					historyConnector.start();
//					UiApplication.getUiApplication().pushScreen(new LoadingScreen(historyConnector));
					UiApplication.getUiApplication().pushScreen(new LoadingScreen(historyConnector, "Fetching history.."));
				} else {
					message = object.getString("error");
					errorMessage = defaultError(message);
					pushError(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				pushError(e.getMessage());
			} catch (JSONException e) {
				e.printStackTrace();
				pushError(e.getMessage());
			}
		}
	}

	public void onErrorOccurEvent(Exception e) {
		errorMessage = defaultError(e.getMessage());
		synchronized (UiApplication.getEventLock()) {
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					System.out.println("errorMessage: " + errorMessage);
					Dialog.alert(errorMessage);
				}
			});
		}
	}

	private String defaultError(String errorMessage){
		if(errorMessage == null || errorMessage.length() == 0){
			errorMessage = "Please check your internet connection and try again.";
		}
		return errorMessage;
	}

	private void pushError(final String message){
		UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				Dialog.alert(message);
			}
		});
	}
}