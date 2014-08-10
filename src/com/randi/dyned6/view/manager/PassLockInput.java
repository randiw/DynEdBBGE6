package com.randi.dyned6.view.manager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.tools.ImageUtils;
import com.randi.dyned6.view.field.CustomPasswordField;

public class PassLockInput extends Manager {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private BitmapField passImage;
	private CustomPasswordField passField;
	
	public PassLockInput() {
		super(USE_ALL_WIDTH);
		
		passField = new CustomPasswordField("", "Password", 30, FIELD_LEFT | PasswordEditField.NO_NEWLINE);
		fontSetting.setPoint(Font.PLAIN, 7);
		passField.setFont(fontSetting.getFont());
		passField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		add(passField);
		
		Bitmap icon = Bitmap.getBitmapResource("icon_lock.png");
		if(Variables.smallScreen()){
			int newWidth = icon.getWidth() * 320 / 480;
			int newHeight = icon.getHeight() * 240 / 360;
			icon = ImageUtils.resizeBitmap(icon, newWidth, newHeight);
		}
		passImage = new BitmapField(icon);
		passImage.setBackground(BackgroundFactory.createSolidBackground(0xf7f7f7));
		passImage.setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1), new XYEdges(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY), Border.STYLE_SOLID));
		add(passImage);
	}
	
	protected void sublayout(int width, int height) {
		layoutChild(passImage, width, height);
		layoutChild(passField, width - (passImage.getWidth()+7), height);
		
		setPositionChild(passField, 6, (passImage.getHeight() - passField.getHeight())/2);
		setPositionChild(passImage, width - passImage.getWidth(), 0);
		
		setExtent(width, passImage.getHeight());
	}

	protected void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, passField.getWidth() + 8, passImage.getHeight());
		
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, passField.getWidth() + 8, passImage.getHeight());
	
		super.paint(g);
	}
	
	public String getValue(){
		return passField.getText().trim();
	}
}