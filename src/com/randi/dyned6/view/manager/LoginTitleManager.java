package com.randi.dyned6.view.manager;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;

public class LoginTitleManager extends Manager {
	
	private FontSetting fontSetting = FontSetting.getInstance();
	
//	private BitmapField logoField;
	private LabelField welcomeField;
	private LabelField pleaseField;

	public LoginTitleManager() {
		super(0);
//		Bitmap logo = Bitmap.getBitmapResource("logo_dyned.png");
//		if(Variables.smallScreen()){
//			int newWidth = logo.getWidth() * 320 / 480;
//			int newHeight = logo.getHeight() * 240 / 360;
//			logo = ImageUtils.resizeBitmap(logo, newWidth, newHeight);
//		}
		int welcomePoint = 8;
		int pleasePoint = 5;
		if(Variables.bigTouchScreen()){
			welcomePoint = 9;
			pleasePoint = 6;
		}
//		logoField = new BitmapField(logo);			
//		add(logoField);
		
		welcomeField = new LabelField("Welcome") {
			protected void paint(Graphics g) {
				g.setColor(Color.BLACK);
				super.paint(g);
			};
		};
		fontSetting.setPoint(Font.PLAIN, welcomePoint);
		welcomeField.setFont(fontSetting.getFont());
		add(welcomeField);
		
		pleaseField = new LabelField("Please set up your login-ID and password") {
			protected void paint(Graphics g) {
				g.setColor(Color.GRAY);
				super.paint(g);
			};
		};
		fontSetting.setPoint(Font.PLAIN, pleasePoint);
		pleaseField.setFont(fontSetting.getFont());
		add(pleaseField);
	}

	protected void sublayout(int width, int height) {
//		layoutChild(logoField, width, height);
//		layoutChild(welcomeField, width - (logoField.getWidth() + 10), height);
//		layoutChild(pleaseField, width - (logoField.getWidth() + 10), height - welcomeField.getHeight());
//		
//		setPositionChild(logoField, 0, 0);
//		setPositionChild(welcomeField, logoField.getWidth() + 10, 0);
//		setPositionChild(pleaseField, logoField.getWidth() + 10, welcomeField.getHeight());
		
		layoutChild(welcomeField, width - 10, height);
		layoutChild(pleaseField, width - 10, height - welcomeField.getHeight());

		setPositionChild(welcomeField, 10, 0);
		setPositionChild(pleaseField, 10, welcomeField.getHeight());

		int rightWidth = welcomeField.getWidth() > pleaseField.getWidth() ? welcomeField.getWidth() : pleaseField.getWidth();

//		int betterHeight = logoField.getHeight() > (welcomeField.getHeight() + pleaseField.getHeight()) ? logoField.getHeight() : (welcomeField.getHeight() + pleaseField.getHeight());
		int betterHeight = welcomeField.getHeight() + pleaseField.getHeight();

//		setExtent(logoField.getWidth() + rightWidth + 10, betterHeight);
		setExtent(rightWidth + 10, betterHeight);
	}
}