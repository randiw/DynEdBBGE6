package com.randi.dyned6.view.field;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class GreyLabelField extends CustomListField {

	private FontSetting fontSetting = FontSetting.getInstance();
	private String label;

	public GreyLabelField(String label) {
		super(USE_ALL_WIDTH | FOCUSABLE);
		this.label = label;
		fontSetting.setPoint(Font.PLAIN, 8);
		setBackground(BackgroundFactory.createLinearGradientBackground(Color.LIGHTGRAY, Color.LIGHTGRAY, Color.GRAY, Color.GRAY));
		setBorder(BorderFactory.createRoundedBorder(new XYEdges(2, 2, 2, 2), Color.GRAY, Border.STYLE_SOLID));
	}

	protected void layout(int width, int height) {
		int heightMod = 20;
		if(Variables.smallScreen()){
			heightMod = 10;
		}
		setExtent(width, fontSetting.getFont().getHeight() + heightMod);
	}

	protected void paint(Graphics g) {
		g.setFont(fontSetting.getFont());
		g.setColor(Color.WHITE);
		g.drawText(label, (getWidth() - g.getFont().getAdvance(label))/2, (getHeight() - g.getFont().getHeight())/2);
	}

	protected void onFocus(int direction) {
		setBackground(BackgroundFactory.createLinearGradientBackground(0x6cb72b, 0x6cb72b, 0xaada52, 0xaada52 ));
		setBorder(BorderFactory.createRoundedBorder(new XYEdges(2, 2, 2, 2), 0x6cb72b, Border.STYLE_SOLID));
		super.onFocus(direction);
	}

	protected void onUnfocus() {
		setBackground(BackgroundFactory.createLinearGradientBackground(Color.LIGHTGRAY, Color.LIGHTGRAY, Color.GRAY, Color.GRAY));
		setBorder(BorderFactory.createRoundedBorder(new XYEdges(2, 2, 2, 2), Color.GRAY, Border.STYLE_SOLID));
		super.onUnfocus();
	}

	public String getLabel(){
		return label;
	}
}