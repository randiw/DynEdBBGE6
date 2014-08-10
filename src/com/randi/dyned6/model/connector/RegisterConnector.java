package com.randi.dyned6.model.connector;

import java.io.IOException;
import java.io.InputStream;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.ui.UiApplication;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.randi.dyned6.MainApp;
import com.randi.dyned6.model.Resources;
import com.randi.dyned6.tools.Stream;
import com.randi.dyned6.view.LoginScreen;

/**
 * Open connection for Register
 * @author Randi Waranugraha
 *
 */
public class RegisterConnector extends UploadConnector {

	/**
	 * Creates a RegisterConnector object with postData.
	 * @param postData Post data; parameter and value pair.
	 */
	public RegisterConnector(URLEncodedPostData postData) {
		super(Resources.API_NEW_REGISTER, postData);
	}

	public void onReceiveResponseEvent(InputStream is) {
		synchronized (UiApplication.getEventLock()) {
			try {
				String jsonString = Stream.asString(is);
				System.out.println("[RegisterConnector] jsonString:[" + jsonString + "]");
				JSONObject object = new JSONObject(jsonString);
				String message = "null";
				if(object != null){
					String status = object.getString("status");
					if("true".equals(status)){
						message = object.getString("message");
					} else {
						message = object.getString("error");
					}
					System.out.println("[RegisterConnector] status " + status + " " + message);
				}
				
				MainApp.popAllScreen();
				LoginScreen loginScreen = new LoginScreen();
				loginScreen.alert(message);
				UiApplication.getUiApplication().pushScreen(loginScreen);					
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public void onErrorOccurEvent(Exception e) {
		synchronized (UiApplication.getEventLock()) {
			MainApp.popAllScreen();
			LoginScreen loginScreen = new LoginScreen();
			loginScreen.alert(e.getMessage());
			UiApplication.getUiApplication().pushScreen(loginScreen);					
		}
	}
}