package com.randi.dyned6.view.field;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class CustomImageButtonField extends Field {

	private Bitmap buttonNormal;
	private Bitmap buttonFocus;
	
	public CustomImageButtonField(String imageNormal, String imageFocus) {
		super(FOCUSABLE);
		buttonNormal = Bitmap.getBitmapResource(imageNormal);
		buttonFocus = Bitmap.getBitmapResource(imageFocus);
	}

	public CustomImageButtonField(String imageNormal, String imageFocus, long style) {
		super(style | FOCUSABLE);
		buttonNormal = Bitmap.getBitmapResource(imageNormal);
		buttonFocus = Bitmap.getBitmapResource(imageFocus);
	}

	public int getPreferredWidth() {
		return buttonNormal.getWidth();
	}
	
	public int getPreferredHeight() {
		return buttonNormal.getHeight();
	}
	
	protected void layout(int width, int height) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	protected void paint(Graphics g) {
		boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
		if(focus){
			g.drawBitmap(0, 0, buttonFocus.getWidth(), buttonFocus.getHeight(), buttonFocus, 0, 0);
		} else {
			g.drawBitmap(0, 0, buttonNormal.getWidth(), buttonNormal.getHeight(), buttonNormal, 0, 0);
		}
	}

	protected void paintBackground(Graphics g) {
//		boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
//		if(focus){
//			g.setColor(Color.BLUE);
//			g.fillRect(0, 0, getWidth(), getHeight());
//		}
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