package com.randi.dyned6.view;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.randi.dyned6.view.field.AnimatedGIFField;

public class LoadingScreen extends PopupScreen {

	private Thread thread = null;

	public LoadingScreen(Thread thread) {
		super(new VerticalFieldManager(), Field.FOCUSABLE | Field.FIELD_HCENTER);
		this.thread = thread;
		add(new AnimatedGIFField((GIFEncodedImage) GIFEncodedImage.getEncodedImageResource("loading.gif")));
	}

	public LoadingScreen(Thread thread, String loadingText) {
		this(thread);
		add(new LabelField(loadingText, FIELD_HCENTER));
	}
	
	public boolean keyChar(char key, int status, int time) {
		if (key == Characters.ESCAPE) {
			try {
				thread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
			close();
		}
		return super.keyChar(key, status, time);
	}

	public boolean onClose() {
		if (thread.isAlive()) {
			try {
				thread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.onClose();
	}	
}