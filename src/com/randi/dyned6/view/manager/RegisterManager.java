package com.randi.dyned6.view.manager;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.randi.dyned6.model.connector.RegisterConnector;
import com.randi.dyned6.tools.StringHelpers;
import com.randi.dyned6.view.LoadingScreen;
import com.randi.dyned6.view.field.GreenLabelField;

public class RegisterManager extends VerticalFieldManager implements FieldChangeListener {
			
	private TextInput firstnameEdit;
	private TextInput lastnameEdit;
	private UserInput userInput;
	private PassLockInput passInput;
	private PassInput confirmPassInput;
	
	public RegisterManager() {
		super(USE_ALL_WIDTH | VERTICAL_SCROLL | VERTICAL_SCROLLBAR);

		XYEdges boxEdges = new XYEdges(0, 20, 5, 20);
		XYEdges buttonEdges = new XYEdges(0, 20, 0, 20);

		userInput = new UserInput();
		userInput.setDefaultText("E-mail as username");
		userInput.setPadding(boxEdges);
		add(userInput);
			
		firstnameEdit = new TextInput("First name");
		firstnameEdit.setPadding(boxEdges);
		add(firstnameEdit);

		lastnameEdit = new TextInput("Last name");
		lastnameEdit.setPadding(boxEdges);
		add(lastnameEdit);

		passInput = new PassLockInput();
		passInput.setPadding(boxEdges);
		add(passInput);

		confirmPassInput = new PassInput();
		confirmPassInput.setPadding(boxEdges);
		add(confirmPassInput);

		GreenLabelField greenButton = new GreenLabelField("Submit");
		greenButton.setMargin(buttonEdges);
		greenButton.setChangeListener(this);
		add(greenButton);
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof GreenLabelField) {
			String pass = passInput.getValue();
			String confirm = confirmPassInput.getValue();
			String username = userInput.getValue().trim();
			if(validateUsername(username) && validatePassword(pass, confirm)){
				URLEncodedPostData postData = new URLEncodedPostData(null, false);
				String name = firstnameEdit.getValue().trim() + " " + lastnameEdit.getValue().trim(); 
				postData.append("name", name);
				postData.append("email", username);
				postData.append("password", pass);

				RegisterConnector registerConnector = new RegisterConnector(postData);
				registerConnector.start();
				UiApplication.getUiApplication().pushScreen(new LoadingScreen(registerConnector));
			}
		}
	}
	
	private boolean validatePassword(String pass, String confirm){
		if(!pass.equals(confirm)){
			Dialog.alert("Please confirm your password");		
			return true;
		}
		return true;
	}
	
	private boolean validateUsername(String username){
		if(username == null || !(username.trim().length() > 0)){
			Dialog.alert("Please fill your username");
			return false;
		}
		if(!StringHelpers.contains(username, '@')){
			Dialog.alert("Email as username");
			return false;
		}
		return true;
	}
}