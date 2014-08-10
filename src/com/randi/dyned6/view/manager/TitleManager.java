package com.randi.dyned6.view.manager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;

import com.randi.dyned6.tools.FontSetting;

public class TitleManager extends Manager {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private Bitmap left_header;
	private Bitmap mid_header;
	private Bitmap right_header;
	
	private LabelField titleField;
	
	public TitleManager(String title){
		super(USE_ALL_WIDTH);
		left_header = Bitmap.getBitmapResource("left_header.png");
		mid_header = Bitmap.getBitmapResource("mid_header.png");
		right_header = Bitmap.getBitmapResource("right_header.png");
		
		titleField = new LabelField(title, LabelField.RIGHT | FOCUSABLE){
			protected void paint(Graphics g) {
				g.setColor(Color.GREEN);
				super.paint(g);
			};
			
			protected void drawFocus(Graphics graphics, boolean on) {
			};
		};
		fontSetting.setPoint(Font.PLAIN, 7);
		titleField.setFont(fontSetting.getFont());
		add(titleField);
	}
	
	public void setText(String title){
		titleField.setText(title);
		invalidate();
	}

	protected void sublayout(int width, int height) {
		layoutChild(titleField, width - 20, height);
		setPositionChild(titleField, (width - titleField.getWidth()) / 2, (mid_header.getHeight() - 11 - titleField.getHeight())/2);
		setExtent(width, mid_header.getHeight());
	}
	
	protected void paint(Graphics g) {
		int sideLength = (getWidth() - mid_header.getWidth())/2;
		int modX = sideLength / 9 + 1;
		for(int i = 0; i < modX; i++){
			g.drawBitmap(i * left_header.getWidth(), 0, left_header.getWidth(), left_header.getHeight(), left_header, 0, 0);
			g.drawBitmap(getWidth() - ((i+1) * right_header.getWidth()), 0, right_header.getWidth(), right_header.getHeight(), right_header, 0, 0);
		}
		
		g.drawBitmap((getWidth() - mid_header.getWidth())/2, 0, mid_header.getWidth(), mid_header.getHeight(), mid_header, 0, 0);
		super.paint(g);
	}
}