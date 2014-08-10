package com.randi.dyned6.view.field;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.randi.dyned6.tools.FontSetting;

public class ChoiceButton extends CustomListField {
	
	private int fieldWidth;
	private String label;
	
	private FontSetting fontSetting = FontSetting.getInstance();
	
	public ChoiceButton(String label) {
		super(FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER);
		this.label = label;
		fieldWidth = Display.getWidth() / 2 - 60;
		fontSetting.setPoint(Font.PLAIN, 8);
		setBackground(BackgroundFactory.createLinearGradientBackground(Color.LIGHTGRAY, Color.LIGHTGRAY, Color.GRAY, Color.GRAY));
	}

	protected void layout(int width, int height) {
		setExtent(fieldWidth, fontSetting.getFont().getHeight() + 6);
	}

	protected void paint(Graphics g) {
		g.setFont(fontSetting.getFont());
		g.setColor(Color.WHITE);
		g.drawText(label, (getWidth() - fontSetting.getFont().getAdvance(label))/2, (getHeight() - fontSetting.getFont().getHeight())/2);
	}
	
	protected void onFocus(int direction) {
		setBackground(BackgroundFactory.createLinearGradientBackground(Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREEN, Color.GREEN));
		setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1), new XYEdges(0x6fb628, 0x6fb628, 0x6fb628, 0x6fb628), Border.STYLE_SOLID));
		super.onFocus(direction);
	}
	
	protected void onUnfocus() {
		setBackground(BackgroundFactory.createLinearGradientBackground(Color.LIGHTGRAY, Color.LIGHTGRAY, Color.GRAY, Color.GRAY));
		setBorder(null);
		super.onUnfocus();
	}
}