package com.randi.dyned6.view.field;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public abstract class CustomListField extends Field {

	public CustomListField(long style) {
		super(style);
	}
	
	abstract protected void layout(int width, int height);

	abstract protected void paint(Graphics graphics);

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