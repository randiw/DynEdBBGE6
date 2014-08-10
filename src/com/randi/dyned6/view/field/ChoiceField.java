package com.randi.dyned6.view.field;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;

public class ChoiceField extends LabelField {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	public ChoiceField(String label) {
		super(label, USE_ALL_WIDTH | FOCUSABLE | LabelField.HCENTER | LabelField.VCENTER);
		
		int point = 8;
		if(Variables.bigTouchScreen()){
			point = 9;
		}
		fontSetting.setPoint(Font.PLAIN, point);
		setFont(fontSetting.getFont());
		setBackground(BackgroundFactory.createLinearGradientBackground(0xe0dedf, 0xe0dedf, 0xccccca, 0xccccca));
	}

	protected void paint(Graphics g) {
		g.setColor(0x595959);
		super.paint(g);
	}

	protected void paintBackground(Graphics g) {
		super.paintBackground(g);
		boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
		if(!focus){
			g.setColor(Color.WHITE);
			g.drawLine(5, 2, getWidth() - 5, 2);
		}
	}

	protected void drawFocus(Graphics g, boolean on) {
		boolean oldDrawStyleFocus = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
		try {
			if (on) {
				g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, true);
			}
			paintBackground(g);
			paint(g);
		} finally {
			g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus);
		}
	}

	protected void onFocus(int direction) {
		setBackground(BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.WHITE, 0xe1e1e1, 0xe1e1e1));
		setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1), new XYEdges(0x6fb628, 0x6fb628, 0x6fb628, 0x6fb628), Border.STYLE_SOLID));
		super.onFocus(direction);
	}

	protected void onUnfocus() {
		setBackground(BackgroundFactory.createLinearGradientBackground(0xe0dedf, 0xe0dedf, 0xccccca, 0xccccca));
		setBorder(null);
		super.onUnfocus();
	}	

	public void clickButton() {
		fieldChangeNotify(0);
	}

	protected boolean invokeAction(int action) {
		switch (action) {
		case ACTION_INVOKE: {
			clickButton();
			return true;
		}
		}
		return super.invokeAction(action);
	}

	protected boolean keyChar(char character, int status, int time) {
		if (character == Characters.ENTER) {
			clickButton();
			return true;
		}
		return super.keyChar(character, status, time);
	}

	protected boolean navigationClick(int status, int time) {
		clickButton();
		return true;
	}

	protected boolean trackwheelClick(int status, int time) {
		clickButton();
		return true;
	}
	
	public void setDirty(boolean dirty) {
	}

	public void setMuddy(boolean muddy) {
	}
}