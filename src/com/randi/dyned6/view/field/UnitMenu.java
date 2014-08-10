package com.randi.dyned6.view.field;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;

import com.awan.dyned6.utils.GraphicUtil;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.tools.FontSetting;

public class UnitMenu extends Manager {

	private EducationManager educationManager = EducationManager.getInstance();
	private FontSetting fontSetting = FontSetting.getInstance();
	
	private static int iconSize = 15;
	private static int counter;

	private String label;
	private LabelField labelField;

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

	public UnitMenu(String label) {
		super(USE_ALL_WIDTH);
		myCounter = counter++;
		this.label = label;
		
		int point = 7;
//		if(Variables.smallScreen()){
//			point = 7;
//		}
		fontSetting.setPoint(Font.PLAIN, point);		
		labelField = new LabelField(label, FOCUSABLE | USE_ALL_WIDTH){
			protected void paint(Graphics g) {
				if(isActive){
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.GRAY);
				}
				boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
				if(focus){
					g.setColor(Color.BLUE);
				}
				super.paint(g);
			};
			
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
		};
		labelField.setFont(fontSetting.getFont());
		add(labelField);

		arrow_active = GraphicUtil.getScaledBitmapImage(EncodedImage.getEncodedImageResource("arrow_question.png"), iconSize - 5, iconSize);
		arrow_inactive = GraphicUtil.getScaledBitmapImage(EncodedImage.getEncodedImageResource("arrow_nonactiv.png"), iconSize - 5, iconSize);

		stars = Bitmap.getBitmapResource("star.png");
		newIcon = Bitmap.getBitmapResource("new.png");
		starWidth = stars.getWidth() * 3;

		init();

		setPadding(5, 10, 5, 10);
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
		isActive = educationManager.contains(label);
		if(isActive){
			isNew = educationManager.isJustStartUnit(label);
			if(!isNew){
				starCount = educationManager.countStar(label);
			}
		}
	}

	protected void sublayout(int width, int height) {
		drawArea = width * 2 / 10;
		if(drawArea < starWidth){
			drawArea = starWidth;
		}
		if(drawArea < newIcon.getWidth()){
			drawArea = newIcon.getWidth();
		}
		
		layoutChild(labelField, width - (drawArea + arrow_active.getWidth()), height);
		setPositionChild(labelField, arrow_active.getWidth() + 10, 5);
		setExtent(width, labelField.getHeight() + 10);
	}

	protected void paint(Graphics g) {
		if(isActive){
			g.drawBitmap(0, (getHeight() - 5 - arrow_active.getHeight())/2, arrow_active.getWidth(), arrow_active.getHeight(), arrow_active, 0, 0);
			if(isNew){
				g.drawBitmap(getWidth() - drawArea - 10 + (drawArea - newIcon.getWidth())/2, (getHeight() - 5 - newIcon.getHeight())/2, newIcon.getWidth(), newIcon.getHeight(), newIcon, 0, 0);
			} else {
				if(starCount > 0) {
					for (int i = 0; i < starCount; i++) {
						g.drawBitmap(getWidth() - drawArea - 10 + (drawArea - starWidth) / 2 + (i * stars.getWidth()), (getHeight() - 5 - stars.getHeight())/2, stars.getWidth(), stars.getHeight(), stars, 0, 0);
					}
				}
			}
		} else {
			g.drawBitmap(0, (getHeight() - 5 - arrow_inactive.getHeight())/2, arrow_inactive.getWidth(), arrow_inactive.getHeight(), arrow_inactive, 0, 0);
		}
		super.paint(g);
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
	
	public void setChangeListener(FieldChangeListener listener) {
		labelField.setChangeListener(listener);
	}
}