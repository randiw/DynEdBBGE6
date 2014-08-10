package com.randi.dyned6.view.manager;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.connector.HistoryConnector;
import com.randi.dyned6.model.connector.LoginConnector;
import com.randi.dyned6.model.persistable.AvatarManager;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.model.persistable.LoginHistoryManager;
import com.randi.dyned6.model.persistable.SessionManager;
import com.randi.dyned6.view.LoadingScreen;
import com.randi.dyned6.view.RegisterScreen;
import com.randi.dyned6.view.field.GreyLabelField;

public class LoginManager extends BoxManager {

	private LoginTitleManager loginTitleManager;
	private UserInput userInput;
	private PassLockInput passInput;
	
	public LoginManager() {
		VerticalFieldManager vfManager = new VerticalFieldManager(USE_ALL_WIDTH | VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
		vfManager.setPadding(new XYEdges(0, 0, 10, 0));
		add(vfManager);
		
		XYEdges titleEdges = new XYEdges(20, 20, 15, 20);
		XYEdges checkEdges = new XYEdges(0, 20, 5, 20);
		XYEdges buttonEdges = new XYEdges(0, 20, 0, 20);
		
		if(Variables.smallScreen()){
			titleEdges.set(12, 15, 8, 15);
			checkEdges.set(0, 15, 5, 15);
			buttonEdges.set(0, 15, 0, 15);
		}
		
		loginTitleManager = new LoginTitleManager();
		loginTitleManager.setPadding(titleEdges);
		vfManager.add(loginTitleManager);

		userInput = new UserInput();
		userInput.setDefaultText(Variables.LOGIN_NAME);
		userInput.setPadding(checkEdges);
		vfManager.add(userInput);

		passInput = new PassLockInput();
		passInput.setPadding(checkEdges);
		vfManager.add(passInput);

		GreyLabelField signinButton = new GreyLabelField("Sign In");
		signinButton.setMargin(buttonEdges);
		signinButton.setChangeListener(this);
		vfManager.add(signinButton);

		GreyLabelField signupButton = new GreyLabelField("Sign Up"); 
		signupButton.setMargin(buttonEdges);
		signupButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				UiApplication.getUiApplication().pushScreen(new RegisterScreen());
			}
		});
		vfManager.add(signupButton);
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof GreyLabelField) {
			String username = userInput.getValue();
			String password = passInput.getValue();
			
			LoginHistoryManager historyManager = LoginHistoryManager.getInstance();
			if(historyManager.isUser(username)){
				if(historyManager.checkLogin(username, password)){
					AvatarManager.set(historyManager.restoreAvatar(username));
					EducationManager.set(historyManager.restoreEducation(username));
					SessionManager.set(historyManager.restoreSession(username));
					
					SessionManager sessionManager = SessionManager.getInstance();
					URLEncodedPostData postData = new URLEncodedPostData(null, false);
					postData.append("conversation", Variables.CONVERSATION_CODE);
					postData.append("app_key", sessionManager.getApp_key());

					HistoryConnector historyConnector = new HistoryConnector(postData);
					historyConnector.start();
//					UiApplication.getUiApplication().pushScreen(new LoadingScreen(historyConnector));
					UiApplication.getUiApplication().pushScreen(new LoadingScreen(historyConnector, "Fetching history.."));
				} else {
					Dialog.alert("Please check your password");
				}
			} else {
				URLEncodedPostData postData = new URLEncodedPostData(null, false);
				postData.append("email", username);
				postData.append("password", password);
				
				LoginConnector loginConnector = new LoginConnector(postData);
				loginConnector.setUsername(username);
				loginConnector.setPassword(password);
				loginConnector.start();
//				UiApplication.getUiApplication().pushScreen(new LoadingScreen(loginConnector));
				UiApplication.getUiApplication().pushScreen(new LoadingScreen(loginConnector, "Logging in.."));
			}
		}
	}
}