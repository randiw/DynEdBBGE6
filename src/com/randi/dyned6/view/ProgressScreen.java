package com.randi.dyned6.view;

import com.randi.dyned6.model.ContentLoader;
import com.randi.dyned6.tools.ProgressListener;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ProgressScreen extends PopupScreen implements ProgressListener{
	
	private ContentLoader contentLoader;
	private LabelField labelField;
	
	public ProgressScreen() {
		super(new VerticalFieldManager(), Field.FOCUSABLE | Field.FIELD_HCENTER);

		contentLoader = new ContentLoader();
		contentLoader.setListener(this);
		
		labelField = new LabelField("0% of 100%");
		add(labelField);
	}

	protected void onDisplay() {
		contentLoader.start();
	}
	
	public boolean onClose() {
		if (contentLoader.isAlive()) {
			try {
				contentLoader.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.onClose();
	}

	public void onPost(String message) {
		synchronized (UiApplication.getEventLock()) {
			labelField.setText(message);
			invalidate();			
		}
	}

	public void onProgress(int currentProgress, int totalProgress) {
		synchronized (UiApplication.getEventLock()) {
			int percentage = currentProgress * 100 / totalProgress;
			labelField.setText(percentage + "% of 100%");
			invalidate();			
		}
	}
}