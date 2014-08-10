package com.randi.dyned6.view.manager;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.view.field.CustomEditField;

public class TextInput extends Manager {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private CustomEditField customEditField;
	
	public TextInput(String initText) {
		super(USE_ALL_WIDTH);
		fontSetting.setPoint(Font.PLAIN, 7);

		XYEdges edges = new XYEdges(14, 6, 14, 6);
		if(Variables.smallScreen()){
			edges.set(10, 6, 10, 6);
		}
		
		customEditField = new CustomEditField(FIELD_LEFT | BasicEditField.NO_NEWLINE | BasicEditField.NO_SWITCHING_INPUT);
		customEditField.setInitialValue(initText);
		customEditField.setFont(fontSetting.getFont());
		customEditField.setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1), new XYEdges(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY), Border.STYLE_SOLID));
		customEditField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		customEditField.setPadding(edges);
		add(customEditField);
	}
	
	protected void sublayout(int width, int height) {
		layoutChild(customEditField, width, height);
		setPositionChild(customEditField, 0, 0);
		setExtent(width, customEditField.getHeight());
	}
	
	public String getValue(){
		return customEditField.getText().trim();
	}
}