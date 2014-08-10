package com.randi.dyned6.view.field;

import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.tools.ImageUtils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

public class LessonListField extends CustomListField {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private static int counter;
	
	private Bitmap image;
	private Bitmap normalArrow;
	private Bitmap focusArrow;
	private Bitmap lock;
	private String title;
	private boolean locked = true;
	
	private int myCounter;

	public LessonListField(String title, String imageFile) {
		super(USE_ALL_WIDTH | FOCUSABLE);
		myCounter = counter++;
		this.title = title;
		image = Bitmap.getBitmapResource(imageFile);
		int newWidth = Display.getWidth() / 2;
		if(Variables.smallScreen()){
			newWidth = Display.getWidth() * 4 / 10;
		}
		if(image.getWidth() > newWidth){
			int newHeight = newWidth * image.getHeight() / image.getWidth();
			image = ImageUtils.resizeBitmap(image, newWidth, newHeight);
		}
		normalArrow = Bitmap.getBitmapResource("next_inactive_btn.png");
		focusArrow = Bitmap.getBitmapResource("next_active_btn.png");
		lock = Bitmap.getBitmapResource("small_lock.png");
		fontSetting.setPoint(Font.PLAIN, 7);
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	protected void layout(int width, int height) {
		setExtent(width, image.getHeight() + 10);
	}

	protected void paint(Graphics g) {
		g.setFont(fontSetting.getFont());
		boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
		int textColor = Color.BLACK;
		if(focus){
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, 5, getHeight());			
		}
		if(locked){
			textColor = Color.GRAY;
			g.drawBitmap(getWidth() - 5 - lock.getWidth(), (getHeight() - lock.getHeight())/2, lock.getWidth(), lock.getHeight(), lock, 0, 0);
		} else {
			if(focus){
				g.drawBitmap(getWidth() - 5 - focusArrow.getWidth(), (getHeight() - focusArrow.getHeight())/2, focusArrow.getWidth(), focusArrow.getHeight(), focusArrow, 0, 0);
			} else {
				g.drawBitmap(getWidth() - 5 - normalArrow.getWidth(), (getHeight() - normalArrow.getHeight())/2, normalArrow.getWidth(), normalArrow.getHeight(), normalArrow, 0, 0);
			}
		}
		g.drawBitmap(25, (getHeight() - image.getHeight())/2, image.getWidth(), image.getHeight(), image, 0, 0);
		g.setColor(textColor);
		g.drawText(title, 5 + 20 + image.getWidth() + 20, (getHeight() - g.getFont().getHeight())/2);			
	}

	protected void paintBackground(Graphics g) {
		super.paintBackground(g);
		if(myCounter % 2 == 0){
			g.setColor(Color.WHITE);			
		} else {
			g.setColor(0xf8f8f8);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0xe4e3e1);
		g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
	}
}