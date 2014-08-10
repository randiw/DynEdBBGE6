package com.randi.dyned6.view.manager;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.view.field.CustomPasswordField;

public class PassInput extends Manager {
	
	private FontSetting fontSetting = FontSetting.getInstance();
	
	private CustomPasswordField passField;
	
	public PassInput() {
		super(USE_ALL_WIDTH);
	
		XYEdges edges = new XYEdges(14, 6, 14, 6);
		if(Variables.smallScreen()){
			edges.set(10, 6, 10, 6);
		}

		passField = new CustomPasswordField("", "Password", 30, FIELD_LEFT | PasswordEditField.NO_NEWLINE);
		fontSetting.setPoint(Font.PLAIN, 7);
		passField.setFont(fontSetting.getFont());
		passField.setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1), new XYEdges(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY), Border.STYLE_SOLID));
		passField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		passField.setPadding(edges);
		add(passField);
	}

	protected void sublayout(int width, int height) {
		layoutChild(passField, width, height);
		setPositionChild(passField, 0, 0);
		setExtent(width, passField.getHeight());
	}

	public String getValue(){
		return passField.getText();
	}
}