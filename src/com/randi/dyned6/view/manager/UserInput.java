package com.randi.dyned6.view.manager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.tools.ImageUtils;
import com.randi.dyned6.view.field.CustomEditField;

public class UserInput extends Manager {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private BitmapField userImage;
	private CustomEditField userField;
		
	public UserInput() {
		super(USE_ALL_WIDTH);
		
		userField = new CustomEditField(FIELD_LEFT | BasicEditField.NO_NEWLINE | BasicEditField.NO_SWITCHING_INPUT);
		fontSetting.setPoint(Font.PLAIN, 7);
		userField.setFont(fontSetting.getFont());
		userField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		add(userField);

		Bitmap icon = Bitmap.getBitmapResource("icon_user.png");
		if(Variables.smallScreen()){
			int newWidth = icon.getWidth() * 320 / 480;
			int newHeight = icon.getHeight() * 240 / 360;
			icon = ImageUtils.resizeBitmap(icon, newWidth, newHeight);
		}
		userImage = new BitmapField(icon);
		userImage.setBackground(BackgroundFactory.createSolidBackground(0xf7f7f7));
		userImage.setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1), new XYEdges(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY), Border.STYLE_SOLID));
		add(userImage);
	}
	
	public void setDefaultText(String defaultText) {
		userField.setInitialValue(defaultText);
	}
		
	protected void sublayout(int width, int height) {
		layoutChild(userImage, width, height);
		layoutChild(userField, width - (userImage.getWidth()+7), height);
		
		setPositionChild(userField, 6, (userImage.getHeight() - userField.getHeight())/2);
		setPositionChild(userImage, width - userImage.getWidth(), 0);
		
		setExtent(width, userImage.getHeight());
	}
	
	protected void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, userField.getWidth() + 8, userImage.getHeight());
		
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, userField.getWidth() + 8, userImage.getHeight());
		
		super.paint(g);
	}
	
	public String getValue(){
		return userField.getText().trim();
	}	
}