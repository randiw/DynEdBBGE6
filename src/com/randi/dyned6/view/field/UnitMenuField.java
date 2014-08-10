package com.randi.dyned6.view.field;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import com.awan.dyned6.utils.GraphicUtil;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.tools.FontSetting;

public class UnitMenuField extends CustomListField {

	private EducationManager educationManager = EducationManager.getInstance();
	private FontSetting fontSetting = FontSetting.getInstance();

	private static int iconSize = 15;
	private static int counter;
	
	private String title;
	private boolean isActive;
	private boolean isNew;
	private int starCount;
	private Bitmap arrow_inactive;
	private Bitmap arrow_active;
	private Bitmap stars;
	private Bitmap newIcon;
	
	private int drawArea;
	private int starWidth;
	private int myCounter;
	
	public UnitMenuField(String unitId) {
		super(USE_ALL_WIDTH | FOCUSABLE);
		myCounter = counter++;
		title = unitId;

		arrow_active = GraphicUtil.getScaledBitmapImage(EncodedImage.getEncodedImageResource("arrow_question.png"), iconSize - 5, iconSize);
		arrow_inactive = GraphicUtil.getScaledBitmapImage(EncodedImage.getEncodedImageResource("arrow_nonactiv.png"), iconSize - 5, iconSize);

		stars = Bitmap.getBitmapResource("star.png");
		newIcon = Bitmap.getBitmapResource("new.png");
		fontSetting.setPoint(Font.PLAIN, 7);
		starWidth = stars.getWidth() * 3;

		init();
	}

	protected void onDisplay() {
		init();
		invalidate();
		super.onDisplay();
	}
	
	protected void onExposed() {
		init();
		invalidate();
		super.onExposed();
	}
		
	private void init(){
		isActive = educationManager.contains(title);
		if(isActive){
			isNew = educationManager.isJustStartUnit(title);
			if(!isNew){
				starCount = educationManager.countStar(title);
			}
		}
	}

	protected void layout(int width, int height) {
		drawArea = width * 2 / 10;
		setExtent(width, fontSetting.getFont().getHeight() + 30);
	}

	protected void paint(Graphics g) {
		g.setFont(fontSetting.getFont());
		if(isActive){
			g.setColor(Color.BLACK);
			g.drawBitmap(10, (getHeight() - arrow_active.getHeight())/2, arrow_active.getWidth(), arrow_active.getHeight(), arrow_active, 0, 0);						
			if(isNew){
				g.drawBitmap(getWidth() - (drawArea - newIcon.getWidth())/2 - newIcon.getWidth(), (getHeight() - newIcon.getHeight())/2, newIcon.getWidth(), newIcon.getHeight(), newIcon, 0, 0);
			} else {
				if(starCount > 0) {
					for (int i = 0; i < starCount; i++) {
						g.drawBitmap(getWidth() - (drawArea - starWidth)/2 - (i + 1) * stars.getWidth(), (getHeight() - stars.getHeight())/2, stars.getWidth(), stars.getHeight(), stars, 0, 0);
					}
				}
			}
		} else {
			g.setColor(Color.GRAY);
			g.drawBitmap(10, (getHeight() - arrow_inactive.getHeight())/2, arrow_inactive.getWidth(), arrow_inactive.getHeight(), arrow_inactive, 0, 0);			
		}
		boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
		if(focus){
			g.setColor(Color.BLUE);
		}
		g.drawText(title, 20 + arrow_active.getWidth(), (getHeight() - g.getFont().getHeight())/2);
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