package com.randi.dyned6.view;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.randi.dyned6.view.manager.TitleManager;

public class BasicScreen extends AbstractScreen {
	
	private TitleManager titleManager;
	
	public BasicScreen(String title) {
		super(USE_ALL_WIDTH);
		titleManager = new TitleManager(title);
		add(titleManager);
	}

	public void setBackground() {
		Background bg = BackgroundFactory.createBitmapBackground(Bitmap.getBitmapResource("bg_small.png"), Background.POSITION_X_LEFT, Background.POSITION_Y_TOP, Background.REPEAT_BOTH);
		getMainManager().setBackground(bg);
	}

	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub

	}
	
	public void setTitle(String title){
		titleManager.setText(title);
		invalidate();
	}
}