package com.randi.dyned6.view;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.randi.dyned6.MainApp;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.view.manager.LoginManager;

public class LoginScreen extends AbstractScreen {
	
	private boolean alert;
	private String message;
		
	public LoginScreen() {
		super(USE_ALL_WIDTH | USE_ALL_HEIGHT);
		VerticalFieldManager vfManager = new VerticalFieldManager(USE_ALL_WIDTH);
		XYEdges edges = new XYEdges(20, 20, 20, 20);
		if(Variables.smallScreen()){
			edges.set(10, 10, 0, 10);
		}
		vfManager.setPadding(edges);
		vfManager.add(new LoginManager());
		add(vfManager);
	}

	public void alert(String message){
		alert = true;
		if(message == null || message.length() == 0){
			message = "Please check your internet connection and try again.";
		}
		this.message = message;
	}
	
	protected void onUiEngineAttached(boolean attached) {
		super.onUiEngineAttached(attached);
		if(attached){
			if(alert){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						Dialog.alert(message);
						alert = false;
					}
				});
			}
		}
	}
	
	public void setBackground() {
		Background bg = BackgroundFactory.createBitmapBackground(Bitmap.getBitmapResource("bg_small.png"), Background.POSITION_X_LEFT, Background.POSITION_Y_TOP, Background.REPEAT_BOTH);
		getMainManager().setBackground(bg);
	}

	public void fieldChanged(Field field, int context) {
	}
	
	protected void actionKeyEscape() {
		MainApp.exitApplication();
	}
}